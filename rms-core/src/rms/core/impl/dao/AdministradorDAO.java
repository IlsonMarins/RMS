
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

public class AdministradorDAO extends AbstractJdbcDAO {
	
	// construtores
	public AdministradorDAO() {
		super("ADMINISTRADOR", "ADM_FUN_ID");	
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
		
		Administrador admin = null;
		if (entidade instanceof Administrador){
			admin = (Administrador)entidade;
		}else{
			Login req = (Login)entidade;
			admin = (Administrador)req.getUsuario();
		}
		
		String query = null;
		
		boolean temNome, temEmail, ativoIsNull, temId, temLogin = false, temRegistro, temSenha = false;
		Character ativo = null;
		
		// testar se o administrador tem os dados e atribuir as respostas aos flags
		temNome = admin.getNome() != null && !admin.getNome().equals("");
		temRegistro = admin.getRegistro() != null;
		if (temRegistro){
			temLogin = admin.getRegistro().getUsuario() != null && !admin.getRegistro().getUsuario().equals("");
			temSenha = admin.getRegistro().getSenha() != null && !admin.getRegistro().getSenha().equals("");
			ativo = admin.getRegistro().getAtivo();
		}
		ativoIsNull = ativo == null;
		temId = admin.getId() != null;
		temEmail = admin.getEmail() != null && !admin.getEmail().equals("");
		
		query = "SELECT R.REG_LOGIN, R.REG_SENHA, R.REG_ATIVO, F.FUN_NOME, F.FUN_CARGO, F.FUN_SETOR, F.FUN_DT_CADASTRO, F.FUN_EMAIL, A.ADM_FUN_ID "
				+ "FROM " + table + " A "
				+ "INNER JOIN FUNCIONARIO F "
				+ "ON A.ADM_FUN_ID=F.FUN_ID "
				+ "INNER JOIN REGISTRO R "
				+ "ON F.FUN_REG_LOGIN=R.REG_LOGIN "; // todos os funcionários administradores
		if (!temLogin && entidade instanceof Login)
			return entidades;
		if (ativoIsNull && !temNome && !temLogin && !temEmail && !temId){ // full table scan?
			
		}else if (temId){ // buscar baseado no id?
			query += "WHERE A." + tableId + " = ?";
		}else{ // buscar baseado em outras informações
			boolean flgColocaAnd = false;
			query += "WHERE ";
			if (temNome){
				if (flgColocaAnd)
					query += " AND ";
				query += "F.FUN_NOME = ?";
				flgColocaAnd = true;
			}
			if (!ativoIsNull){
				if (flgColocaAnd)
					query += " AND ";
				query += "R.REG_ATIVO = ?";
				flgColocaAnd = true;
			}
			if (temLogin){
				if (flgColocaAnd)
					query += " AND ";
				query += "R.REG_LOGIN = ?";
				flgColocaAnd = true;
			}
			if (temSenha){
				if (flgColocaAnd)
					query += " AND ";
				query += "R.REG_SENHA = ?";
				flgColocaAnd = true;
			}
			if (temEmail){
				if (flgColocaAnd)
					query += " AND ";
				query += "F.FUN_EMAIL = ?";
				flgColocaAnd = true;
			}
		}
		try {
			openConnection();
			connection.setAutoCommit(false);
			pst = connection.prepareStatement(query);
			
			if (temId){
				pst.setInt(1, admin.getId());
			}else{
				int i = 1;
				
				if (temNome){
					pst.setString(i++, admin.getNome());
				}
				if (!ativoIsNull){
					pst.setString(i++, admin.getRegistro().getAtivo().toString());
				}
				if (temLogin){
					pst.setString(i++, admin.getRegistro().getUsuario());
				}
				if (temSenha){
					pst.setString(i++, admin.getRegistro().getSenha());
				}
				if (temEmail){
					pst.setString(i++, admin.getEmail());
				}
			}
			
			ResultSet rs = pst.executeQuery();
			Timestamp tst;
			Date dtCadastro;
			
			while (rs.next()){
				Administrador a = new Administrador();
				Registro r = new Registro();
				
				r.setUsuario(rs.getString("REG_LOGIN"));
				r.setSenha(rs.getString("REG_SENHA"));
				r.setAtivo(rs.getString("REG_ATIVO").charAt(0));
				a.setRegistro(r);
				a.setNome(rs.getString("FUN_NOME"));
				a.setEmail(rs.getString("FUN_EMAIL"));
				a.setId(rs.getInt(tableId));
				tst = rs.getTimestamp("FUN_DT_CADASTRO");
				dtCadastro = new Date(tst.getTime());
				a.setDtCadastro(dtCadastro);
				entidades.add(a);
			}
			rs.close();
			/*if (registro.getLogin().equals(registroBanco.getLogin()) && // dados estão corretos?
					registro.getSenha().equals(registroBanco.getSenha())){
				registro.setAtivo(true);
				entidades.add(registro);
				return entidades;
			}
			
			registro.setAtivo(false);
			registro.setLogin(null);
			registro.setSenha(null);
			entidades.add(registro);*/
		}catch (SQLException e) {
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
