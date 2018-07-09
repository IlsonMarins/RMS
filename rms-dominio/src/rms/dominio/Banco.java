package rms.dominio;

import java.util.List;

public class Banco extends EntidadeDominio {
	// atributos privados
	private String nomeBanco;
	private List<Owner> owners;
	private Owner owner;
	private Cliente cliente;
	
	// métodos de acesso
	public String getNomeBanco() {
		return nomeBanco;
	}

	public void setNomeBanco(String nomeBanco) {
		this.nomeBanco = nomeBanco;
	}

	public List<Owner> getOwners() {
		return owners;
	}

	public void setOwners(List<Owner> ownerBanco) {
		this.owners = ownerBanco;
	}

	public Owner getOwner() {
		return owner;
	}

	public void setOwner(Owner owner) {
		this.owner = owner;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

}
