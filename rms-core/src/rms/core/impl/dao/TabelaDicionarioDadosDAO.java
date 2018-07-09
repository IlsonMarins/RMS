package rms.core.impl.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import rms.dominio.Banco;
import rms.dominio.Cliente;
import rms.dominio.EntidadeDominio;
import rms.dominio.Owner;
import rms.dominio.AgendamentoReorg;
import rms.dominio.Tabela;

public class TabelaDicionarioDadosDAO extends AbstractJdbcDAO {

	public TabelaDicionarioDadosDAO() {
		super("TABELA", "TAB_ID");
	}

	@Override
	public void salvar(EntidadeDominio entidade) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void alterar(EntidadeDominio entidade) throws SQLException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public List<EntidadeDominio> visualizar(EntidadeDominio entidade) throws SQLException {
		return null;
	} // fim-visualizar()

	@Override
	public List<EntidadeDominio> consultar(EntidadeDominio entidade) throws SQLException {
		openConnection();
		PreparedStatement pst = null;
		AgendamentoReorg agendamentoReorg = (AgendamentoReorg)entidade;
		List<EntidadeDominio> entidades = new ArrayList<EntidadeDominio>();
		List<Tabela> tabelas = null;
		// buscar tabelas de uma solicitação específica?
		if (agendamentoReorg.getEntidadeBusca() != null
				&& (agendamentoReorg.getEntidadeBusca().equals("VISUALIZAR")
						|| agendamentoReorg.getEntidadeBusca().equals("DETALHES"))){
			Integer solId = agendamentoReorg.getId();
			Integer ownId = agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).getId();
			try {
				connection.setAutoCommit(false);
				// preencher o objeto das tabelas
				StringBuilder sql = new StringBuilder();
				sql.append("select t.tab_id, t.tab_nome_tabela "
						+ "from solicitacao s, solicitacao_tabela m, tabela t, owner o "
						+ "where s.sol_id=m.stb_sol_id and m.stb_tab_id=t.tab_id "
						+ "and s.sol_id = ?"
						+ "and o.own_id = ?");
				pst = connection.prepareStatement(sql.toString());
				pst.setInt(1, solId);
				pst.setInt(2, ownId);
				ResultSet rs = pst.executeQuery();
				tabelas = new ArrayList<Tabela>();
				while (rs.next()) {
					Tabela tabela = new Tabela();
					tabela.setId(rs.getInt("tab_id"));
					tabela.setNomeTabela(rs.getString("tab_nome_tabela"));
					tabelas.add(tabela);
				}
				agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).setTabelas(tabelas);
				entidades.add(agendamentoReorg);
				rs.close();	
			} catch (SQLException e) {
				try {
					connection.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();			
			}finally{
				try {
					pst.close();
					connection.close();
					return entidades;
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		// buscar tabelas de um owner especifico?
		if (agendamentoReorg.getCliente().getBancos().get(0).getOwners() != null){
			Owner owner = agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0);
			String ownNome = owner.getNomeOwner();
			try {
				connection.setAutoCommit(false);
				// preencher o objeto das tabelas
				StringBuilder sql = new StringBuilder();
				sql.append("SELECT TABLE_NAME, TABLESPACE_NAME, STATUS "
						+ "FROM DBA_TABLES "
						+ "WHERE OWNER = ? ");
				pst = connection.prepareStatement(sql.toString());
				pst.setString(1, ownNome);
			    sql.append("ORDER BY TABLE_NAME ");
				ResultSet rs = pst.executeQuery();
				tabelas = new ArrayList<Tabela>();
				while (rs.next()) {
					Tabela tabela = new Tabela();
					tabela.setNomeTabela(rs.getString("TABLE_NAME"));
					tabelas.add(tabela);
				}
				agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).setTabelas(tabelas);
				entidades.add(agendamentoReorg);
				rs.close();
			} catch (SQLException e) {
				try {
					connection.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();			
			}finally{
				try {
					pst.close();
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
//			try {
//				connection.setAutoCommit(false);
//				// preencher o objeto das tabelas
//				StringBuilder sql = new StringBuilder();
//				sql.append("SELECT t.tab_id, t.tab_nome_tabela "
//						+ "FROM banco b, owner o, tabela t "
//						+ "WHERE b.ban_id=o.own_ban_id AND o.own_id=t.tab_own_id "
//						+ "AND o.own_id = ");
//				sql.append(ownNome);
//				sql.append(" ORDER BY 2");
//				pst = connection.prepareStatement(sql.toString());
//				ResultSet rs = pst.executeQuery();
//				tabelas = new ArrayList<Tabela>();
//				while (rs.next()) {
//					Tabela tabela = new Tabela();
//					tabela.setId(rs.getInt(1));
//					tabela.setNomeTabela(rs.getString(2));
//					tabelas.add(tabela);
//				}
//				agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).setTabelas(tabelas);
//				entidades.add(agendamentoReorg);
//				rs.close();
//			} catch (SQLException e) {
//				try {
//					connection.rollback();
//				} catch (SQLException e1) {
//					e1.printStackTrace();
//				}
//				e.printStackTrace();			
//			}finally{
//				try {
//					pst.close();
//					connection.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
		}/*else if (solicitacao.getId() != null){ // buscar as tabelas de uma solicitação?
			// criar um cliente para a solicitação
			solicitacao.setCliente(new Cliente());
			// criar o banco do cliente para a solicitação
			Banco banco = new Banco();
			List<Banco> bancos = new ArrayList<Banco>();
			bancos.add(banco);
			solicitacao.getCliente().setBancos(bancos);
			// criar o owner do banco para a solicitação
			Owner owner = new Owner();
			List<Owner> owners = new ArrayList<Owner>();
			owners.add(owner);
			solicitacao.getCliente().getBancos().get(0).setOwners(owners);
			// buscar todas as tabelas da solicitacao
			// e preencher no objeto recebido por parâmetro, para ser
			// modificado por referência
			try {
				connection.setAutoCommit(false);
				// preencher o objeto das tabelas
				StringBuilder sql = new StringBuilder();
				sql.append("select t.tab_own_id, t.tab_id, t.tab_nome_tabela "
						+ "from solicitacao s, solicitacao_tabela m, tabela t, owner o, banco b, cliente c "
						+ "where s.sol_id=m.stb_sol_id and m.stb_tab_id=t.tab_id "
						+ "and t.tab_own_id=o.own_id and o.own_ban_id=b.ban_id and b.ban_cli_id=c.cli_id "
						+ "and s.sol_id = ?");
				pst = connection.prepareStatement(sql.toString());
				pst.setInt(1, solicitacao.getId());
				ResultSet rs = pst.executeQuery();
				owner = new Owner();
				tabelas = new ArrayList<Tabela>();
				while (rs.next()) {
					Tabela tabela = new Tabela();
					if (owner.getId() == null || owner.getId() != rs.getInt("tab_own_id")){
						owner = new Owner();
						owner.setId(rs.getInt("tab_own_id"));
					}
					tabela.setOwner(owner);
					tabela.setId(rs.getInt("tab_id"));
					tabela.setNomeTabela(rs.getString("tab_nome_tabela"));
					tabelas.add(tabela);
				}
				solicitacao.getCliente().getBancos().get(0).getOwners().get(0).setTabelas(tabelas);
				entidades.add(solicitacao);
				rs.close();	
			} catch (SQLException e) {
				try {
					connection.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();			
			}finally{
				try {
					pst.close();
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}*/else{ // buscar todas as tabelas cadastradas no sistema
			try {
				connection.setAutoCommit(false);
				// preencher o objeto das tabelas
				StringBuilder sql = new StringBuilder();
				sql.append("select TAB_OWN_ID, TAB_ID, TAB_NOME_TABELA "
						+ "from tabela "
						+ "order by 3");
				pst = connection.prepareStatement(sql.toString());
				ResultSet rs = pst.executeQuery();
				Owner owner = new Owner();
				tabelas = new ArrayList<Tabela>();
				while (rs.next()) {
					Tabela tabela = new Tabela();
					if (owner.getId() == null || owner.getId() != rs.getInt(1)){
						owner = new Owner();
						owner.setId(rs.getInt(1));
					}
					tabela.setOwner(owner);
					tabela.setId(rs.getInt(2));
					tabela.setNomeTabela(rs.getString(3));
					tabelas.add(tabela);
				}
				agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).setTabelas(tabelas);
				entidades.add(agendamentoReorg);
				rs.close();	
			} catch (SQLException e) {
				try {
					connection.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();			
			}finally{
				try {
					pst.close();
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return entidades;
	} // fim-consultar()
}
