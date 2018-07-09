package rms.dominio;

import java.util.ArrayList;
import java.util.List;

public class SolicitacaoCarregar extends EntidadeDominio {
	// atributos privados
	private List<Tabela> tabelas = new ArrayList<Tabela>();
	private List<Cliente> clientes = new ArrayList<Cliente>();
	private List<Banco> bancos = new ArrayList<Banco>();
	private List<Owner> owners = new ArrayList<Owner>();
	
	// métodos de acesso
	public List<Tabela> getTabelas() {
		return tabelas;
	}
	public void setTabelas(List<Tabela> tabelas) {
		this.tabelas = tabelas;
	}
	public List<Cliente> getClientes() {
		return clientes;
	}
	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}
	public List<Banco> getBancos() {
		return bancos;
	}
	public void setBancos(List<Banco> bancos) {
		this.bancos = bancos;
	}
	public List<Owner> getOwners() {
		return owners;
	}
	public void setOwners(List<Owner> owners) {
		this.owners = owners;
	}
}
