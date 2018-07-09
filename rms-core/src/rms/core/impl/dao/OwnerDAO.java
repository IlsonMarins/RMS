package rms.core.impl.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import rms.dominio.Banco;
import rms.dominio.EntidadeDominio;
import rms.dominio.Owner;
import rms.core.util.DriverConexao;
import rms.dominio.AgendamentoReorg;

public class OwnerDAO extends AbstractJdbcDAO {

	public OwnerDAO() {
		super("TABELA", "TAB_ID");
	}

	@Override
	public void salvar(EntidadeDominio entidade) throws SQLException{
		// inserir um novo owner na tabela 'owner' do RMS?
		if (entidade instanceof Owner){
			Owner owner = (Owner)entidade;
			// fazer a inserção de fato
			PreparedStatement pst = null;
			try {
				openConnection();
				connection.setAutoCommit(false);
				StringBuilder sql = new StringBuilder();
				sql.append("INSERT INTO OWNER(OWN_BAN_ID, OWN_ID, OWN_NOME_OWNER) ");
				sql.append("VALUES (?, SEQ_ID_OWNER.NEXTVAL, ?)");
				pst = connection.prepareStatement(sql.toString());
				pst.setInt(1, owner.getBanco().getId());
				pst.setString(2, owner.getNomeOwner());
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
	public List<EntidadeDominio> visualizar(EntidadeDominio entidade) throws SQLException {
		PreparedStatement pst = null;
		List<EntidadeDominio> entidades = null;
		if(entidade instanceof Owner){ // buscar com informações do próprio Owner?
			Owner ownerRecebido = (Owner)entidade;
			List<Owner> owners = null;
			Boolean temIdOwner = null;
			Boolean temNomeOwner = null;
			temIdOwner = ownerRecebido.getId() != null;
			temNomeOwner = ownerRecebido.getNomeOwner() != null;
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT O.OWN_BAN_ID, O.OWN_ID, O.OWN_NOME_OWNER "
					+ "FROM OWNER O INNER JOIN BANCO B "
					+ "ON B.BAN_ID=O.OWN_BAN_ID "); // todos os owners do sistema (que tenha banco, obviamente)
			if (!temNomeOwner && !temIdOwner){ // full table scan?
				
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
					pst.setInt(1, ownerRecebido.getId());
				}else{
					int i = 1;
					if (temNomeOwner){
						pst.setString(i++, ownerRecebido.getNomeOwner());
					}
				}
				ResultSet rs = pst.executeQuery();
				owners = new ArrayList<Owner>();
				if (rs.next()) {
					Banco banco = new Banco();
					ownerRecebido.setId(rs.getInt("OWN_ID"));
					ownerRecebido.setNomeOwner(rs.getString("OWN_NOME_OWNER"));
					banco.setId(rs.getInt("OWN_BAN_ID"));
					ownerRecebido.setBanco(banco);
				}
				rs.close();
				entidades = new ArrayList<EntidadeDominio>();
				entidades.add(ownerRecebido);
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
		if (entidade instanceof AgendamentoReorg){ // buscar com informações de AgendamentoReorg?
			AgendamentoReorg agendamentoReorg = (AgendamentoReorg)entidade;
			List<Owner> owners = null;
			Boolean temBanco = agendamentoReorg.getCliente().getBancos().get(0) != null;
			Boolean temIdBanco = null;
			Boolean temNomeBanco = null;
			if (temBanco){
				temIdBanco = agendamentoReorg.getCliente().getBancos().get(0).getId() != null;
				temNomeBanco = agendamentoReorg.getCliente().getBancos().get(0).getNomeBanco() != null;
			}else{
				// nesse caso, só importa os owners de um banco. Entao sair sem fazer a busca!
				return null;
			}
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT O.OWN_BAN_ID, O.OWN_ID, O.OWN_NOME_OWNER "
					+ "FROM OWNER O INNER JOIN BANCO B "
					+ "ON B.BAN_ID=O.OWN_BAN_ID "); // todos os owners do sistema (que tenha banco, obviamente)
			if (!temNomeBanco && !temIdBanco){ // full table scan?
				// nesse caso, só importa os owners de um banco. Entao sair sem fazer a busca!
				return null;
			}else if (temIdBanco){ // buscar baseado no id do banco?
				sql.append("WHERE B.BAN_ID = ? ");
			}else{ // buscar baseado em outra(s) informação(ções)
				Boolean flgColocaAnd = false;
				sql.append("WHERE ");
				if (temNomeBanco){ // buscar baseado no nome do banco?
					if (flgColocaAnd)
						sql.append("AND ");
					sql.append("B.BAN_NOME_BANCO = ? ");
					flgColocaAnd = true;
				}
			}
			// fazer a busca efetivamente
			try {
				openConnection(DriverConexao.RMS);
				connection.setAutoCommit(false);
				pst = connection.prepareStatement(sql.toString());
				// preencher o objeto dos owners
				if (temIdBanco){
					pst.setInt(1, agendamentoReorg.getCliente().getBancos().get(0).getId());
				}else{
					int i = 1;
					if (temNomeBanco){
						pst.setString(i++, agendamentoReorg.getCliente().getBancos().get(0).getNomeBanco());
					}
				}
				ResultSet rs = pst.executeQuery();
				owners = new ArrayList<Owner>();
				while (rs.next()) {
					Owner owner = new Owner();
					Banco banco = new Banco();
					owner.setId(rs.getInt("OWN_ID"));
					owner.setNomeOwner(rs.getString("OWN_NOME_OWNER"));
					banco.setId(rs.getInt("OWN_BAN_ID"));
					owner.setBanco(banco);
					owners.add(owner);
				}
				rs.close();
				agendamentoReorg.getCliente().getBancos().get(0).setOwners(owners);
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
