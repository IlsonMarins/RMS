
package rms.core.impl.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rms.dominio.Banco;
import rms.dominio.Cliente;
import rms.dominio.Console;
import rms.dominio.EntidadeDominio;
import rms.dominio.AgendamentoReorg;

public class ConsoleDAO extends AbstractJdbcDAO {

	// construtores
	public ConsoleDAO() {
		super("tb_banco", "ban_id");	
	}
	
	@Override
	public void salvar(EntidadeDominio entidade) {
		openConnection();
		PreparedStatement pst = null;
		Console console = (Console)entidade;
			
		try {
			connection.setAutoCommit(false);
			
			StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO CONSOLE(CON_DT_REGISTRO, CON_MENSAGEM) ");
			sql.append("VALUES (sysdate, ?)");
					
			pst = connection.prepareStatement(sql.toString());
			
			pst.setString(1, console.getMsg());
			
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
			//if(ctrlTransaction){
				try {
					pst.close();
					//if(ctrlTransaction)
						connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			//}
			
		}
	}
	
	@Override
	public void alterar(EntidadeDominio entidade) {
		Console console = (Console)entidade;
		console.setDtCadastro(new Date());
		console.setMsg("Console limpo!");
		
		openConnection();
		PreparedStatement pst = null;
		
		// limpar a tabela de console
		try {
			connection.setAutoCommit(false);
			StringBuilder sql = new StringBuilder();
			sql.append("truncate table console");
			pst = connection.prepareStatement(sql.toString());
			pst.executeUpdate();
			
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
		
		// atualizar a tabela de console
		salvar(console);
	}

	@Override
	public List<EntidadeDominio> consultar(EntidadeDominio entidade) throws SQLException {
		openConnection();
		PreparedStatement pst = null;
		AgendamentoReorg agendamentoReorg = (AgendamentoReorg)entidade;
		List<EntidadeDominio> entidades = new ArrayList<EntidadeDominio>();
		Cliente cliente = null;
		// buscar o banco de uma solicitação específica?
		if (agendamentoReorg.getId() != null){
			Integer solId = agendamentoReorg.getId();
			cliente = agendamentoReorg.getCliente();
			Integer cliId = cliente.getId();
			List<Banco> bancos = new ArrayList<Banco>();
			try {
				connection.setAutoCommit(false);
				// preencher o objeto dos bancos
				StringBuilder sql = new StringBuilder();
				sql.append("select b.ban_id, b.ban_nome_banco "
						+ "from solicitacao s, solicitacao_tabela m, tabela t, "
						+ "owner o, banco b, cliente c "
						+ "where s.sol_id=m.stb_sol_id and stb_tab_id=t.tab_id "
						+ "and t.tab_own_id=o.own_id and o.own_ban_id=b.ban_id and b.ban_cli_id=c.cli_id "
						+ "and s.sol_id = ? "
						+ "and c.cli_id = ?");
				pst = connection.prepareStatement(sql.toString());
				pst.setInt(1, solId);
				pst.setInt(2, cliId);
				ResultSet rs = pst.executeQuery();
				if (rs.next()) {
					Banco banco = new Banco();
					banco.setId(rs.getInt("ban_id"));
					banco.setNomeBanco(rs.getString("ban_nome_banco"));
					bancos.add(banco);
				}
				agendamentoReorg.getCliente().setBancos(bancos);
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
		// buscar os bancos de um cliente especifico?
		else if (agendamentoReorg.getCliente() != null){
			cliente = agendamentoReorg.getCliente();
			Integer cliId = cliente.getId();
			List<Banco> bancos = new ArrayList<Banco>();
			try {
				connection.setAutoCommit(false);
				// preencher o objeto dos bancos
				StringBuilder sql = new StringBuilder();
				sql.append("SELECT b.ban_id, b.ban_nome_banco "
						+ "FROM banco b, cliente c "
						+ "WHERE b.ban_cli_id=c.cli_id "
						+ "AND c.cli_id = ");
				sql.append(cliId);
			    sql.append(" ORDER BY 2");
				pst = connection.prepareStatement(sql.toString());
				ResultSet rs = pst.executeQuery();
				while (rs.next()) {
					Banco b = new Banco();
					b.setId(rs.getInt("ban_id"));
					b.setNomeBanco(rs.getString("ban_nome_banco"));
					bancos.add(b);
				}
				agendamentoReorg.getCliente().setBancos(bancos);
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
		}else{ // buscar todos os bancos cadastrados no sistema
			try {
				connection.setAutoCommit(false);
				// preencher o objeto dos bancos
				StringBuilder sql = new StringBuilder();
				sql.append("select BAN_CLI_ID, BAN_ID, BAN_NOME_BANCO "
						+ "from banco "
						+ "order by 3");
				pst = connection.prepareStatement(sql.toString());
				ResultSet rs = pst.executeQuery();
				ArrayList<Banco> bancos = new ArrayList<Banco>();
				cliente = new Cliente();
				while (rs.next()) {
					Banco banco = new Banco();
					if (cliente.getId() == null || cliente.getId() != rs.getInt("BAN_CLI_ID")){
						cliente = new Cliente();
						cliente.setId(rs.getInt("BAN_CLI_ID"));
					}
					List<Cliente> clientes = new ArrayList<Cliente>();
					clientes.add(cliente);
					agendamentoReorg.setClientes(clientes);
					banco.setCliente(cliente);
					banco.setId(rs.getInt("BAN_ID"));
					banco.setNomeBanco(rs.getString("BAN_NOME_BANCO"));
					bancos.add(banco);
				}
				agendamentoReorg.getCliente().setBancos(bancos);
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
		} //fim-buscar_todos_os_bancos
		return entidades;
	}

	@Override
	public List<EntidadeDominio> visualizar(EntidadeDominio entidade) throws SQLException {
		openConnection();
		PreparedStatement pst = null;
		Console console = (Console)entidade;
		List<EntidadeDominio> entidades = new ArrayList<EntidadeDominio>();
		try {
			connection.setAutoCommit(false);
			StringBuilder sql = new StringBuilder();
			sql.append("select con_dt_registro, con_mensagem "
					+ "from console "
					+ "order by 1 desc");
			pst = connection.prepareStatement(sql.toString());
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Console con = new Console();
				con.setDtCadastro(rs.getTimestamp("con_dt_registro"));
				con.setMsg(rs.getString("con_mensagem"));
				entidades.add(con);
			}
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
		return entidades;
	}

}
