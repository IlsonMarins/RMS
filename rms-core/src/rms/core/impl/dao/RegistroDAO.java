
package rms.core.impl.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rms.dominio.Administrador;
import rms.dominio.Banco;
import rms.dominio.Console;
import rms.dominio.EntidadeDominio;
import rms.dominio.Registro;
import rms.dominio.Login;

public class RegistroDAO extends AbstractJdbcDAO {
	
	// construtores
	public RegistroDAO() {
		super("REGISTRO", "REG_ID");	
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
		
		PreparedStatement pst = null;
		List<EntidadeDominio> entidades = new ArrayList<EntidadeDominio>(); // retorno do método
		
		Registro registro = null;
		if (entidade instanceof Registro)
			registro = (Registro)entidade;
		else
			return null;
		
		boolean temId, temLogin, temSenha;
		temId = registro.getId() != null;
		temLogin = registro.getUsuario() != null && !registro.getUsuario().equals(""); // o login foi preenchido?
		temSenha = registro.getSenha() != null && !registro.getSenha().equals(""); // a senha foi preenchida?
		
		String query = null;
		
		query = "SELECT * "
				+ "FROM "
				+ table; // consultar os registros
		if (!temId && !temLogin && !temSenha){ // full table scan?
			
		}else if (temId){ // filtrar pelo id?
			query += " WHERE " + tableId + " = ?";
		}else{ // outros filtros
			boolean flgColocaAnd = false;
			query += " WHERE ";
			if (temLogin){ // filtrar pelo login
				if (flgColocaAnd)
					query += " AND ";
				query += "REG_LOGIN = ?";
				flgColocaAnd = true;
			}
			if (temSenha){ // filtrar pela senha
				if (flgColocaAnd)
					query += " AND ";
				query += "REG_SENHA = ?";
				flgColocaAnd = true;
			}
		}
		
		try { // efetuar a consulta
			openConnection();
			connection.setAutoCommit(false);
			pst = connection.prepareStatement(query);
			
			if (temId){ // filtrado pelo id?
				pst.setInt(1, registro.getId());
			}else{ // outro(s) filtros
				int i = 1;
				if (temLogin){
					pst.setString(i++, registro.getUsuario());
				}
				if (temSenha){
					pst.setString(i++, registro.getSenha());
				}
			}
			
			ResultSet rs = pst.executeQuery(); // executa a consulta na base
			
			while (rs.next()){ // itera no resultado da consulta
				Registro r = new Registro();
				int i = 1;
				r.setId(rs.getInt(i++));
				r.setUsuario(rs.getString(i++));
				r.setSenha(rs.getString(i++));
				if (rs.getString(i++).charAt(0) == 'T')
					r.setFlgAtivo(true);
				else
					r.setFlgAtivo(false);
				if (rs.getString(i++).charAt(0) == 'T')
					r.setFlgAdmin(true);
				else
					r.setFlgAdmin(false);
				entidades.add(r);
			}
			
//			// verificar se o retorno foi de apenas um registro,
//			// pois se foi, indica que é um login, entao deve-se
//			// preencher a entidade recebida da fachada para
//			// continuar com as regras de negocio de login
//			if (entidades != null && entidades.size() <= 1){
//				Registro reg = (Registro)entidades.get(0);
//				registro.setDtCadastro(reg.getDtCadastro());
//				registro.setFlgAdmin(reg.getFlgAdmin());
//				registro.setFlgAtivo(reg.getFlgAtivo());
//				registro.setId(reg.getId());
//				registro.setLogin(reg.getLogin());
//				registro.setSenha(reg.getSenha());
//			}
			
			rs.close();
			
		}catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();			
		}finally{
			try {
				connection.commit();
				pst.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return entidades;
	}

	@Override
	public List<EntidadeDominio> visualizar(EntidadeDominio entidade) throws SQLException {
		openConnection();
		PreparedStatement pst = null;
		//Console console = (Console)entidade;
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
