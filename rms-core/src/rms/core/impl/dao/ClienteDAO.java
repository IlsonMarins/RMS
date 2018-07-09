package rms.core.impl.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rms.dominio.Banco;
import rms.dominio.Cliente;
import rms.dominio.EntidadeDominio;
import rms.dominio.Metadado;
import rms.dominio.Owner;
import rms.core.util.DriverConexao;
import rms.dominio.AgendamentoReorg;
import rms.dominio.Tabela;

public class ClienteDAO extends AbstractJdbcDAO {

	public ClienteDAO() {
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
		// buscar o cliente de uma solicitação específica?
		if (agendamentoReorg.getId() != null
				&& agendamentoReorg.getEntidadeBusca() != null
				&& !agendamentoReorg.getEntidadeBusca().equals("CARREGAR")){
			Integer solId = agendamentoReorg.getId();
			try {
				connection.setAutoCommit(false);
				StringBuilder sql = new StringBuilder();
				sql.append("select CLI_ID, CLI_NOME_CLIENTE "
						+ "from solicitacao s, solicitacao_tabela m, tabela t, owner o, banco b, cliente c "
						+ "where s.sol_id=m.stb_sol_id and m.stb_tab_id=t.tab_id and t.tab_own_id=o.own_id "
						+ "and o.own_ban_id=b.ban_id and b.ban_cli_id=c.cli_id "
						+ "and s.sol_id = ?");
				pst = connection.prepareStatement(sql.toString());
				pst.setInt(1, solId);
				ResultSet rs = pst.executeQuery();
				if (rs.next()) {
					Cliente cliente = new Cliente();
					cliente.setId(rs.getInt(1));
					cliente.setNomeCliente(rs.getString(2));
					agendamentoReorg.setCliente(cliente);
				}
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
		}else{ // buscar todos os clientes cadastrados no sistema
			try {
				connection.setAutoCommit(false);
				StringBuilder sql = new StringBuilder();
				sql.append("select CLI_ID, CLI_NOME_CLIENTE "
						+ "from cliente "
						+ "order by 2");
				pst = connection.prepareStatement(sql.toString());
				ResultSet rs = pst.executeQuery();
				ArrayList<Cliente> clientes = new ArrayList<Cliente>();
				while (rs.next()) {
					Cliente c = new Cliente();
					c.setId(rs.getInt(1));
					c.setNomeCliente(rs.getString(2));
					clientes.add(c);
				}
				agendamentoReorg.setClientes(clientes);
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
