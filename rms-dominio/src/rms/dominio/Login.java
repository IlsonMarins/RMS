package rms.dominio;

public class Login extends EntidadeDominio {
	
	// atributos privados
	private Registro registro;
	private Pessoa usuario;
	
	// métodos públicos
	public Registro getRegistro() {
		return registro;
	}
	public void setRegistro(Registro registro) {
		this.registro = registro;
	}
	public Pessoa getUsuario() {
		return usuario;
	}
	public void setUsuario(Pessoa usuario) {
		this.usuario = usuario;
	}
		
}
