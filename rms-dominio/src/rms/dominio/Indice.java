package rms.dominio;

import java.util.Date;

public class Indice extends EntidadeDominio {
	// atributos privados
	private String nomeIndice;
	private Owner ownerIndice;
	private String compressionIndice;
	private String tablespaceIndice;
	private Double iniTransIndice;
	private String loggingIndice;
	private String statusIndice;
	private Date lastAnalyzedIndice;
	private String parallelIndice;
	private String partitionedIndice;
	
	// métodos de acesso
	public String getNomeIndice() {
		return nomeIndice;
	}

	public void setNomeIndice(String nomeIndice) {
		this.nomeIndice = nomeIndice;
	}

	public Owner getOwnerIndice() {
		return ownerIndice;
	}

	public void setOwnerIndice(Owner ownerIndice) {
		this.ownerIndice = ownerIndice;
	}

	public String getCompressionIndice() {
		return compressionIndice;
	}

	public void setCompressionIndice(String compressionIndice) {
		this.compressionIndice = compressionIndice;
	}

	public String getTablespaceIndice() {
		return tablespaceIndice;
	}

	public void setTablespaceIndice(String tablespaceIndice) {
		this.tablespaceIndice = tablespaceIndice;
	}

	public Double getIniTransIndice() {
		return iniTransIndice;
	}

	public void setIniTransIndice(Double iniTransIndice) {
		this.iniTransIndice = iniTransIndice;
	}

	public String getLoggingIndice() {
		return loggingIndice;
	}

	public void setLoggingIndice(String loggingIndice) {
		this.loggingIndice = loggingIndice;
	}

	public String getStatusIndice() {
		return statusIndice;
	}

	public void setStatusIndice(String statusIndice) {
		this.statusIndice = statusIndice;
	}

	public Date getLastAnalyzedIndice() {
		return lastAnalyzedIndice;
	}

	public void setLastAnalyzedIndice(Date lastAnalyzedIndice) {
		this.lastAnalyzedIndice = lastAnalyzedIndice;
	}

	public String getParallelIndice() {
		return parallelIndice;
	}

	public void setParallelIndice(String parallelIndice) {
		this.parallelIndice = parallelIndice;
	}

	public String getPartitionedIndice() {
		return partitionedIndice;
	}

	public void setPartitionedIndice(String partitionedIndice) {
		this.partitionedIndice = partitionedIndice;
	}
	
	
}
