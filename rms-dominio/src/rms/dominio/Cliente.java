package rms.dominio;

import java.util.List;

public class Cliente extends EntidadeDominio {
	// atributos privados
	private String nomeCliente;
	private List<Banco> bancos;
	
	// métodos de acesso
	public String getNomeCliente() {
		return nomeCliente;
	}
	public void setNomeCliente(String nome) {
		this.nomeCliente = nome;
	}
	public List<Banco> getBancos() {
		return bancos;
	}
	public void setBancos(List<Banco> bancos) {
		this.bancos = bancos;
	}
	
	
	
	
}
