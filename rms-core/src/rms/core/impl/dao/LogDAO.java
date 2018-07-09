
package rms.core.impl.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rms.core.aplicacao.Resultado;
import rms.dominio.Banco;
import rms.dominio.Cliente;
import rms.dominio.Console;
import rms.dominio.EntidadeDominio;
import rms.dominio.Log;
import rms.dominio.AgendamentoReorg;

public class LogDAO extends AbstractJdbcDAO {

	// construtores
	public LogDAO() {
		super("tb_banco", "ban_id");	
	}
	
	@Override
	public void salvar(EntidadeDominio entidade) {
		openConnection();
		PreparedStatement pst = null;
		Banco ban = (Banco)entidade;
		try {
			connection.setAutoCommit(false);
			
			StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO tb_banco(ban_id, ban_nome) ");
			sql.append("VALUES (seq_id.nextval, ?)");
					
			pst = connection.prepareStatement(sql.toString());
			
			pst.setString(1, ban.getNomeBanco());
			
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
	}

	@Override
	public List<EntidadeDominio> consultar(EntidadeDominio entidade) throws SQLException {
		openConnection();
		PreparedStatement pst = null;
		List<EntidadeDominio> entidades = new ArrayList<EntidadeDominio>();
		if (entidade instanceof Log){
			try {
				connection.setAutoCommit(false);
				StringBuilder sql = new StringBuilder();
				sql.append("select log_dt_registro, log_mensagem "
						+ "from logging "
						+ "order by 1 desc");
				pst = connection.prepareStatement(sql.toString());
				ResultSet rs = pst.executeQuery();
				while (rs.next()) {
					Log log = new Log();
					log.setDtCadastro(rs.getTimestamp("log_dt_registro"));
					log.setMsg(rs.getString("log_mensagem"));
					entidades.add(log);
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
		}else{
			System.out.println("Entidade reotornada da Fachada não é uma entidade de Log!");
		}
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
