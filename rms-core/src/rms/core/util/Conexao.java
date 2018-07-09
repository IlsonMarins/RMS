
package rms.core.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



public class Conexao {
    
	public static Connection getConnection() throws ClassNotFoundException, SQLException{
        String driver = "oracle.jdbc.OracleDriver";
        String url = "jdbc:oracle:thin:@localhost:1521/XE";
        String user = "RMSUSER";
        String password = "rmsuser";
        Class.forName(driver);
        Connection conn = DriverManager.getConnection(url, user, password);
        return conn;
    }
	
	public static Connection getConnection(String driver, String url, String user, String password) throws ClassNotFoundException, SQLException{
        Class.forName(driver);
        Connection conn = DriverManager.getConnection(url, user, password);
        return conn;
    }
	
	public static Connection getConnection(DriverConexao conexao) throws ClassNotFoundException, SQLException{
		String driver = null;
        String url = null;
        String user = null;
        String password = null;
		switch (conexao){
			case RMS:
				driver = DriverConexao.RMS.getDriver(); 	// acesso ao campo estático através da classe que o suporta
				url = DriverConexao.RMS.getUrl(); 			// acesso ao campo estático através da classe que o suporta
				user = DriverConexao.RMS.getUser();			// acesso ao campo estático através da classe que o suporta
				password = DriverConexao.RMS.getPassword(); // acesso ao campo estático através da classe que o suporta
				break;
			default:
                System.out.println("Erro! " + Conexao.class.getCanonicalName() 
                		+ "\nNecessário passar um enum de driver de conexão válido!");
                break;
		}
        Class.forName(driver);
        Connection conn = DriverManager.getConnection(url, user, password);
        return conn;
    }
	
}
