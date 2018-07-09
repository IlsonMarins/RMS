package rms.dominio;

import java.util.List;

public class Tabela extends EntidadeDominio {
	// atributos privados
	private String nomeTabela;
	private Owner owner;
	private Metadado metadado;
	private List<Indice> indices;

	// métodos de acesso
	public String getNomeTabela() {
		return nomeTabela;
	}
	public void setNomeTabela(String nomeTabela) {
		this.nomeTabela = nomeTabela;
	}
	public Metadado getMetadado() {
		return metadado;
	}
	public void setMetadado(Metadado metadado) {
		this.metadado = metadado;
	}
	public Owner getOwner() {
		return owner;
	}
	public void setOwner(Owner owner) {
		this.owner = owner;
	}
	public List<Indice> getIndices() {
		return indices;
	}
	public void setIndices(List<Indice> indices) {
		this.indices = indices;
	}
	
	

}
