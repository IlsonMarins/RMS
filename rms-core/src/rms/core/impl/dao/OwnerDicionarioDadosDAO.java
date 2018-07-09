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
import rms.core.util.DriverConexao;
import rms.dominio.AgendamentoReorg;

public class OwnerDicionarioDadosDAO extends AbstractJdbcDAO {

	public OwnerDicionarioDadosDAO() {
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
		openConnection(DriverConexao.RMS);
		PreparedStatement pst = null;
		AgendamentoReorg agendamentoReorg = (AgendamentoReorg)entidade;
		List<EntidadeDominio> entidades = new ArrayList<EntidadeDominio>();
		List<Owner> owners = null;
		// buscar os owners uma solicitação específica?
		if (agendamentoReorg.getEntidadeBusca() != null
				&& (agendamentoReorg.getEntidadeBusca().equals("VISUALIZAR")
						|| agendamentoReorg.getEntidadeBusca().equals("DETALHES"))){
			if (agendamentoReorg.getId() != null){ // confirmar se o ID está preenchido
				Integer solId = agendamentoReorg.getId();
				try {
					connection.setAutoCommit(false);
					// preencher o objeto dos owners
					StringBuilder sql = new StringBuilder();
					sql.append("select o.own_ban_id, o.own_id, o.own_nome_owner "
							+ "from solicitacao s, solicitacao_tabela m, tabela t, owner o "
							+ "where s.sol_id=m.stb_sol_id and m.stb_tab_id=t.tab_id and t.tab_own_id=o.own_id "
							+ "and s.sol_id = ?");
					pst = connection.prepareStatement(sql.toString());
					pst.setInt(1, solId);
					ResultSet rs = pst.executeQuery();
					owners = new ArrayList<Owner>();
					if (rs.next()) {
						Owner owner = new Owner();
						owner.setId(rs.getInt("own_id"));
						owner.setNomeOwner(rs.getString("own_nome_owner"));
						owners.add(owner);
					}
					agendamentoReorg.getCliente().getBancos().get(0).setOwners(owners);
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
		}
		// buscar os owners do dicionario de dados?
		if (agendamentoReorg.getCliente().getBancos() != null){
			try {
				connection.setAutoCommit(false);
				// preencher o objeto dos owners
				StringBuilder sql = new StringBuilder();
				sql.append("SELECT USERNAME, ACCOUNT_STATUS, DEFAULT_TABLESPACE, PROFILE "
						+ "FROM dba_users "
						+ "WHERE USERNAME not in( "
						+ "'QS_CB','PERFSTAT','QS_ADM','PM','SH','HR','OE','ODM_MTR','WKPROXY','ANONYMOUS','OWNER','SYS','SYSTEM','SCOTT', "
						+ "'SYSMAN','XDB','DBSNMP','EXFSYS','OLAPSYS','MDSYS','WMSYS','WKSYS','DMSYS','ODM','EXFSYS','CTXSYS','LBACSYS', "
						+ "'ORDPLUGINS','SQLTXPLAIN','OUTLN','TSMSYS','XS$NULL','TOAD','STREAM','SPATIAL_CSW_ADMIN','SPATIAL_WFS_ADMIN','APEX_040000', "
						+ "'SI_INFORMTN_SCHEMA','QS','QS_CBADM','QS_CS','QS_ES','QS_OS','QS_WS','PA_AWR_USER','OWBSYS_AUDIT','OWBSYS','ORDSYS','ORDDATA', "
						+ "'ORACLE_OCM','MGMT_VIEW','MDDATA','FLOWS_FILES','FLASHBACK','AWRUSER','APPQOSSYS','APEX_PUBLIC_USER','APEX_030200','FLOWS_020100') "
						+ "ORDER BY USERNAME ");
				pst = connection.prepareStatement(sql.toString());
				ResultSet rs = pst.executeQuery();
				owners = new ArrayList<Owner>();
				while (rs.next()) {
					Owner owner = new Owner();
					owner.setNomeOwner(rs.getString("USERNAME"));
					owners.add(owner);
				}
				agendamentoReorg.getCliente().getBancos().get(0).setOwners(owners);
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
//				// preencher o objeto dos owners
//				StringBuilder sql = new StringBuilder();
//				sql.append("SELECT o.own_id, o.own_nome_owner "
//						+ "FROM banco b, owner o "
//						+ "WHERE o.own_ban_id=b.ban_id "
//						+ "AND b.ban_id = ");
//				sql.append(banId);
//				sql.append(" ORDER BY 2");
//				pst = connection.prepareStatement(sql.toString());
//				ResultSet rs = pst.executeQuery();
//				owners = new ArrayList<Owner>();
//				while (rs.next()) {
//					Owner owner = new Owner();
//					owner.setId(rs.getInt("own_id"));
//					owner.setNomeOwner(rs.getString("own_nome_owner"));
//					owners.add(owner);
//				}
//				agendamentoReorg.getCliente().getBancos().get(0).setOwners(owners);
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
		}else{ // buscar todos os owners cadastrados no sistema
			// criar os objetos necessários para a navegação até os owners
			Cliente cliente = new Cliente();
			agendamentoReorg.setCliente(cliente);
			Banco banco = new Banco();
			List<Banco> bancos = new ArrayList<Banco>();
			bancos.add(banco);
			agendamentoReorg.getCliente().setBancos(bancos);
			try {
				connection.setAutoCommit(false);
				// preencher o objeto dos bancos
				StringBuilder sql = new StringBuilder();
				sql.append("select OWN_BAN_ID, OWN_ID, OWN_NOME_OWNER "
						+ "from owner "
						+ "order by 3");
				pst = connection.prepareStatement(sql.toString());
				ResultSet rs = pst.executeQuery();
				owners = new ArrayList<Owner>();
				banco = new Banco();
				while (rs.next()) {
					Owner owner = new Owner();
					if (banco.getId() == null || banco.getId() != rs.getInt("OWN_BAN_ID")){
						banco = new Banco();
						banco.setId(rs.getInt("OWN_BAN_ID"));
					}
					owner.setBanco(banco);
					owner.setId(rs.getInt("OWN_ID"));
					owner.setNomeOwner(rs.getString("OWN_NOME_OWNER"));
					owners.add(owner);
				}
				agendamentoReorg.getCliente().getBancos().get(0).setOwners(owners);
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
