/**
 * 
 */
package rms.core.util;

/**
 * @author ilromape
 *
 */
public enum DriverConexao {
	
	RMS("oracle.jdbc.OracleDriver", "jdbc:oracle:thin:@localhost:1521/XE", "RMSUSER", "rmsuser");
	
	private final String driver;
	private final String url;
	private final String user;
	private final String password;
	
	private DriverConexao(String driver, String url, String user, String password){
		this.driver = driver;
		this.url = url;
		this.user = user;
		this.password = password;
	}
	
	public String getDriver(){
		return this.driver;
	}
	
	public String getUrl(){
		return this.url;
	}
	
	public String getUser(){
		return this.user;
	}
	
	public String getPassword(){
		return this.password;
	}
	
}
