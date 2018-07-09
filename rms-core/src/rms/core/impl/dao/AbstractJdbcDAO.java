
package rms.core.impl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import rms.core.IDAO;
import rms.core.util.Conexao;
import rms.core.util.DriverConexao;
import rms.dominio.EntidadeDominio;

public abstract class AbstractJdbcDAO implements IDAO{
	
	// atributos
	protected Connection connection;
	protected String table;
	protected String tableId;
	protected boolean ctrlTransaction = true;
	
	// construtores
	public AbstractJdbcDAO (Connection connection, String table, String tableId){
		this.table = table;
		this.tableId = tableId;
		this.connection = connection;
	}
	
	protected AbstractJdbcDAO (String table, String tableId){
		this.table = table;
		this.tableId = tableId;
	}
	
	// métodos
	@Override
	public void excluir (EntidadeDominio entidade){		
		openConnection();
		PreparedStatement pst=null;		
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM ");
		sb.append(table);
		sb.append(" WHERE ");
		sb.append(tableId);
		sb.append("=");
		sb.append("?");	
		try {
			connection.setAutoCommit(false);
			pst = connection.prepareStatement(sb.toString());
			pst.setInt(1, entidade.getId());

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
			
			try {
				pst.close();
				if(ctrlTransaction)
					connection.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected void openConnection(){
		try {
			if(connection == null || connection.isClosed())
				connection = Conexao.getConnection();
		} catch (ClassNotFoundException e) {
			System.out.println("\nErro ClassNotFound!\n");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("\nErro SQLException!\n");
			e.printStackTrace();
		}
	}
	
	protected void openConnection(String driver, String url, String user, String password){
		try {
			if(connection == null || connection.isClosed())
				connection = Conexao.getConnection(driver, url, user, password);
		} catch (ClassNotFoundException e) {
			System.out.println("\nErro ClassNotFound!\n");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("\nErro SQLException!\n");
			e.printStackTrace();
		}
	}
	
	protected void openConnection(DriverConexao conexao){
		try {
			if(connection == null || connection.isClosed())
				connection = Conexao.getConnection(conexao);
		} catch (ClassNotFoundException e) {
			System.out.println("\nErro ClassNotFound!\n");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("\nErro SQLException!\n");
			e.printStackTrace();
		}
	}
	
}
