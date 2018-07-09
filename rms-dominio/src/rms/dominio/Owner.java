package rms.dominio;

import java.util.List;

public class Owner extends EntidadeDominio {
	// atributos privados
	private String nomeOwner;
	private List<Tabela> tabelas;
	private Banco banco;
	
	// métodos de acesso
	public String getNomeOwner() {
		return nomeOwner;
	}

	public void setNomeOwner(String nomeOwner) {
		this.nomeOwner = nomeOwner;
	}

	public List<Tabela> getTabelas() {
		return tabelas;
	}

	public void setTabelas(List<Tabela> tabelas) {
		this.tabelas = tabelas;
	}

	public Banco getBanco() {
		return banco;
	}

	public void setBanco(Banco banco) {
		this.banco = banco;
	}
}
