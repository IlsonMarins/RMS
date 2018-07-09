package rms.dominio;

public class Funcionario extends Pessoa {
	
	// atributos privados
	private String cargo;
	private String setor;
	
	// métodos de acesso
	public String getCargo() {
		return cargo;
	}
	public void setCargo(String cargo) {
		this.cargo = cargo;
	}
	public String getSetor() {
		return setor;
	}
	public void setSetor(String setor) {
		this.setor = setor;
	}
	
}
