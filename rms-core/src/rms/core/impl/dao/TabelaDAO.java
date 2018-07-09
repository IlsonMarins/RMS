package rms.core.impl.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import rms.dominio.Banco;
import rms.dominio.EntidadeDominio;
import rms.dominio.Owner;
import rms.dominio.Tabela;
import rms.core.util.DriverConexao;
import rms.dominio.AgendamentoReorg;

public class TabelaDAO extends AbstractJdbcDAO {

	public TabelaDAO() {
		super("TABELA", "TAB_ID");
	}

	@Override
	public void salvar(EntidadeDominio entidade) throws SQLException{
		// inserir uma nova tabela na tabela 'TABELA' do RMS?
		if (entidade instanceof Tabela){
			Tabela tabela = (Tabela)entidade;
			// fazer a inserção de fato
			PreparedStatement pst = null;
			try {
				openConnection();
				connection.setAutoCommit(false);
				StringBuilder sql = new StringBuilder();
				sql.append("INSERT INTO TABELA(TAB_OWN_ID, TAB_ID, TAB_NOME_TABELA) ");
				sql.append("VALUES (?, SEQ_ID_TABELA.NEXTVAL, ?)");
				pst = connection.prepareStatement(sql.toString());
				pst.setInt(1, tabela.getOwner().getId());
				pst.setString(2, tabela.getNomeTabela());
				pst.executeUpdate();
				connection.commit();
			} catch (SQLException e) {
				try {
					connection.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();	
			}finally{
				try{
					pst.close();
					connection.close();
				}catch (SQLException e){
					e.printStackTrace();
				}
			}
		}else{ // entidade não é Owner
			return;
		}
		return;
	} // fim salvar

	@Override
	public void alterar(EntidadeDominio entidade) throws SQLException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public List<EntidadeDominio> visualizar(EntidadeDominio entidade) throws SQLException{
		PreparedStatement pst = null;
		List<EntidadeDominio> entidades = null;
		if (entidade instanceof Tabela){ // fazer busca por uma tabela específica?
			Tabela tabelaRecebida = (Tabela)entidade;
			Boolean temTabela = tabelaRecebida != null;
			Boolean temIdTabela = null;
			Boolean temNomeTabela = null;
			Boolean temOwnerTabela = null;
			Boolean temIdOwner = null;
			if (temTabela){
				temIdTabela = tabelaRecebida.getId() != null;
				temNomeTabela = tabelaRecebida.getNomeTabela() != null;
				temOwnerTabela = tabelaRecebida.getOwner() != null;
				if (temOwnerTabela){
					temIdOwner = tabelaRecebida.getOwner().getId() != null;
				}
			}
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT T.TAB_OWN_ID, T.TAB_ID, T.TAB_NOME_TABELA "
					+ "FROM TABELA T INNER JOIN OWNER O "
					+ "ON O.OWN_ID=T.TAB_OWN_ID "); // todas as tabelas do sistema (que tenha owner, obviamente)
			if (!temNomeTabela && !temIdTabela){ // full table scan?
				
			}else if (temIdTabela){ // buscar baseado no id da tabela?
				sql.append("WHERE T.TAB_ID = ? ");
			}else{ // buscar baseado em outra(s) informação(ções)
				Boolean flgColocaAnd = false;
				sql.append("WHERE ");
				if (temNomeTabela){ // buscar baseado no nome da tabela?
					if (flgColocaAnd)
						sql.append("AND ");
					sql.append("T.TAB_NOME_TABELA = ? ");
					flgColocaAnd = true;
				}
				if (temIdOwner){ // buscar baseado no id do owner da tabela?
					if (flgColocaAnd)
						sql.append("AND ");
					sql.append("T.TAB_OWN_ID = ? ");
					flgColocaAnd = true;
				}
			}
			// fazer a busca efetivamente
			try {
				openConnection(DriverConexao.RMS);
				connection.setAutoCommit(false);
				pst = connection.prepareStatement(sql.toString());
				// preencher o objeto dos owners
				if (temIdTabela){
					pst.setInt(1, tabelaRecebida.getId());
				}else{
					int i = 1;
					if (temNomeTabela){
						pst.setString(i++, tabelaRecebida.getNomeTabela());
					}
					if (temIdOwner){
						pst.setInt(i++, tabelaRecebida.getOwner().getId());
					}
				}
				ResultSet rs = pst.executeQuery();
				entidades = new ArrayList<EntidadeDominio>();
				while (rs.next()) {
					Owner owner = new Owner();
					tabelaRecebida.setId(rs.getInt("TAB_ID"));
					tabelaRecebida.setNomeTabela(rs.getString("TAB_NOME_TABELA"));
					owner.setId(rs.getInt("TAB_OWN_ID"));
					if (temOwnerTabela)
						tabelaRecebida.getOwner().setId(owner.getId());
					else
						tabelaRecebida.setOwner(owner);
					entidades.add(tabelaRecebida);
				}
				rs.close();
			}catch (SQLException e){
				try{
					connection.rollback();
				} catch (SQLException e1){
					e1.printStackTrace();
				}
				e.printStackTrace();			
			}finally{
				try{
					pst.close();
					connection.close();
					return entidades;
				}catch (SQLException e){
					e.printStackTrace();
				}
			}
		}
		return entidades;
	} // fim-visualizar()

	@Override
	public List<EntidadeDominio> consultar(EntidadeDominio entidade) throws SQLException {
		PreparedStatement pst = null;
		List<EntidadeDominio> entidades = null;
		if (entidade instanceof AgendamentoReorg){ // fazer busca com "joins"?
			AgendamentoReorg agendamentoReorg = (AgendamentoReorg)entidade;
			List<Tabela> tabelas = null;
			Boolean temOwner = agendamentoReorg.getCliente().getBancos().get(0).getOwners() != null;
			Boolean temIdOwner = null;
			Boolean temNomeOwner = null;
			if (temOwner){
				temIdOwner = agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).getId() != null;
				temNomeOwner = agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).getNomeOwner() != null;
			}else{
				// nesse caso, só importa as tabelas de um owner. Entao sair sem fazer a busca!
				return null;
			}
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT T.TAB_OWN_ID, T.TAB_ID, T.TAB_NOME_TABELA "
					+ "FROM TABELA T INNER JOIN OWNER O "
					+ "ON O.OWN_ID=T.TAB_OWN_ID "); // todas as tabelas do sistema (que tenha owner, obviamente)
			if (!temNomeOwner && !temIdOwner){ // full table scan?
				// nesse caso, só importa as tabelas de um owner. Entao sair sem fazer a busca!
				return null;
			}else if (temIdOwner){ // buscar baseado no id do owner?
				sql.append("WHERE O.OWN_ID = ? ");
			}else{ // buscar baseado em outra(s) informação(ções)
				Boolean flgColocaAnd = false;
				sql.append("WHERE ");
				if (temNomeOwner){ // buscar baseado no nome do owner?
					if (flgColocaAnd)
						sql.append("AND ");
					sql.append("O.OWN_NOME_OWNER = ? ");
					flgColocaAnd = true;
				}
			}
			// fazer a busca efetivamente
			try {
				openConnection(DriverConexao.RMS);
				connection.setAutoCommit(false);
				pst = connection.prepareStatement(sql.toString());
				// preencher o objeto dos owners
				if (temIdOwner){
					pst.setInt(1, agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).getId());
				}else{
					int i = 1;
					if (temNomeOwner){
						pst.setString(i++, agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).getNomeOwner());
					}
				}
				ResultSet rs = pst.executeQuery();
				tabelas = new ArrayList<Tabela>();
				while (rs.next()) {
					Tabela tabela = new Tabela();
					Owner owner = new Owner();
					tabela.setId(rs.getInt("TAB_ID"));
					tabela.setNomeTabela(rs.getString("TAB_NOME_TABELA"));
					owner.setId(rs.getInt("TAB_OWN_ID"));
					tabela.setOwner(owner);
					tabelas.add(tabela);
				}
				rs.close();
				agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).setTabelas(tabelas);
				entidades = new ArrayList<EntidadeDominio>();
				entidades.add(agendamentoReorg);
			}catch (SQLException e){
				try{
					connection.rollback();
				} catch (SQLException e1){
					e1.printStackTrace();
				}
				e.printStackTrace();			
			}finally{
				try{
					pst.close();
					connection.close();
					return entidades;
				}catch (SQLException e){
					e.printStackTrace();
				}
			}
		}
		return entidades;
	} // fim-consultar()
}
