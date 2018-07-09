package rms.dominio;

public class Registro extends EntidadeDominio {
	// atributos privados
	private String usuario;
	private String senha;
	private Boolean flgAtivo;
	private Boolean flgAdmin;
	
	// métodos de acesso
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public Boolean getFlgAtivo() {
		return flgAtivo;
	}
	public void setFlgAtivo(Boolean flgAtivo) {
		this.flgAtivo = flgAtivo;
	}
	public Boolean getFlgAdmin() {
		return flgAdmin;
	}
	public void setFlgAdmin(Boolean flgAdmin) {
		this.flgAdmin = flgAdmin;
	}
}
