
package rms.core.impl.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rms.core.aplicacao.Resultado;
import rms.dominio.Banco;
import rms.dominio.Cliente;
import rms.dominio.Console;
import rms.dominio.EntidadeDominio;
import rms.dominio.Funcionario;
import rms.dominio.Registro;
import rms.dominio.Login;
import rms.dominio.Administrador;
import rms.dominio.AgendamentoReorg;

public class FuncionarioDAO extends AbstractJdbcDAO {

	// construtores
	public FuncionarioDAO() {
		super("FUNCIONARIO", "FUN_ID");	
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
		
		PreparedStatement pst = null; // envio de consulta ao banco
		List<EntidadeDominio> entidades = new ArrayList<EntidadeDominio>(); // retorno do método
		
		Funcionario funcionario = null;
		if (entidade instanceof Funcionario)
			funcionario = (Funcionario)entidade;
		else
			return null;
		
		String query = null; // consulta ao banco
		
		boolean temNome, temEmail, temId, temLogin = false,
				temRegistro, temSenha = false, estaAtivo, isAdmin,
				temCargo, temSetor, temDtCadastro, temRegId = false;
		
		// atribuir as respostas aos flags
		temCargo = funcionario.getCargo() != null;
		temSetor = funcionario.getSetor() != null;
		temEmail = funcionario.getEmail() != null && !funcionario.getEmail().equals("");
		temNome = funcionario.getNome() != null && !funcionario.getNome().equals("");
		temRegistro = funcionario.getRegistro() != null;
		if (temRegistro){
			temRegId = funcionario.getRegistro().getId() != null;
			temLogin = funcionario.getRegistro().getUsuario() != null && !funcionario.getRegistro().getUsuario().equals("");
			temSenha = funcionario.getRegistro().getSenha() != null && !funcionario.getRegistro().getSenha().equals("");
			estaAtivo = funcionario.getRegistro().getFlgAtivo();
			isAdmin = funcionario.getRegistro().getFlgAdmin();
		}
		temId = funcionario.getId() != null;
		temDtCadastro = funcionario.getDtCadastro() != null;
		query = "SELECT * "
				+ "FROM "
				+ table; // consultar os funcionários
		if (temId){ // filtrar pelo id?
			query += " WHERE " + tableId + " = ?";
		}else{ // outros filtros
			boolean flgColocaAnd = false;
			query += " WHERE ";
			if (temRegId){
				if (flgColocaAnd)
					query += " AND ";
				query += "FUN_REG_ID = ?";
				flgColocaAnd = true;
			}
			if (temNome){
				if (flgColocaAnd)
					query += " AND ";
				query += "FUN_NOME = ?";
				flgColocaAnd = true;
			}
			if (temCargo){
				if (flgColocaAnd)
					query += " AND ";
				query += "FUN_CARGO = ?";
				flgColocaAnd = true;
			}
			if (temSetor){
				if (flgColocaAnd)
					query += " AND ";
				query += "FUN_SETOR = ?";
				flgColocaAnd = true;
			}
			if (temDtCadastro){
				if (flgColocaAnd)
					query += " AND ";
				query += "FUN_DT_CADASTRO = ?";
				flgColocaAnd = true;
			}
			if (temEmail){
				if (flgColocaAnd)
					query += " AND ";
				query += "FUN_EMAIL = ?";
				flgColocaAnd = true;
			}
		}
		try { // executar a consulta
			openConnection();
			connection.setAutoCommit(false);
			pst = connection.prepareStatement(query);
			
			if (temId){
				pst.setInt(1, funcionario.getId());
			}else{
				int i = 1;
				if (temRegId){
					pst.setInt(i++, funcionario.getRegistro().getId());
				}
				if (temNome){
					pst.setString(i++, funcionario.getNome());
				}
				if (temCargo){
					pst.setString(i++, funcionario.getCargo());
				}
				if (temSetor){
					pst.setString(i++, funcionario.getSetor());
				}
				if (temDtCadastro){
					Timestamp dtConsulta = new Timestamp(funcionario.getDtCadastro().getTime());
					pst.setTimestamp(i++, dtConsulta);
				}
				if (temEmail){
					pst.setString(i++, funcionario.getEmail());
				}
			}
			
			ResultSet rs = pst.executeQuery();
			
			Timestamp tst;
			Date dtCadastro;
			while (rs.next()){
				Funcionario f = new Funcionario();
				Registro r = new Registro ();
				f.setRegistro(r);
				int i = 1;
				f.setId(rs.getInt(i++));
				f.getRegistro().setId(rs.getInt(i++));
				f.setNome(rs.getString(i++));
				f.setCargo(rs.getString(i++));
				f.setSetor(rs.getString(i++));
				tst = rs.getTimestamp(i++);
				dtCadastro = new Date(tst.getTime());
				f.setDtCadastro(dtCadastro);
				f.setEmail(rs.getString(i++));
				entidades.add(f);
			}
			
//			// validar se apenas um funcionario foi buscado, pois indica login
//			if (entidades != null && entidades.size() <= 1 && temRegistro){
//				Funcionario fun = (Funcionario)entidades.get(0);
//				fun.setRegistro(funcionario.getRegistro());
//				entidades.set(0, fun); // nao sei se isso é necessáro visto que o java trabalha por referencia
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
		
	} // fim consultar

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
