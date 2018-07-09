package rms.dominio;

import java.util.Date;

public class Metadado extends EntidadeDominio {
	// atributos privados
	private Tabela tabela;
	private String parallel;
	private Double iniTrans;
	private String compression;
	private String criterioColuna;
	private Double pctEstatistica;
	private String nomeTablespace;
	private Double tamanho;
	private Date dtUltimaAnalise;
	
	// métodos de acesso
	public Tabela getTabela() {
		return tabela;
	}

	public void setTabela(Tabela tabela) {
		this.tabela = tabela;
	}
	
	public String getParallel() {
		return parallel;
	}
	
	public void setParallel(String parallel) {
		this.parallel = parallel;
	}
	
	public Double getIniTrans() {
		return iniTrans;
	}
	
	public void setIniTrans(Double iniTrans) {
		this.iniTrans = iniTrans;
	}
	
	public String getCompressTable() {
		return compression;
	}
	
	public void setCompressTable(String compressTable) {
		this.compression = compressTable;
	}
	
	public String getCriterioColuna() {
		return criterioColuna;
	}
	public void setCriterioColuna(String criterioColuna) {
		this.criterioColuna = criterioColuna;
	}
	
	public Double getPctEstatistica() {
		return pctEstatistica;
	}
	public void setPctEstatistica(Double pctEstatistica) {
		this.pctEstatistica = pctEstatistica;
	}
	
	public String getNomeTablespace() {
		return nomeTablespace;
	}
	
	public void setNomeTablespace(String nomeTablespace) {
		this.nomeTablespace = nomeTablespace;
	}
	
	public Double getTamanho() {
		return tamanho;
	}
	
	public void setTamanho(Double tamanho) {
		this.tamanho = tamanho;
	}
	
	public Date getLastAnalyzed() {
		return dtUltimaAnalise;
	}
	
	public void setLastAnalyzed(Date lastAnalyzed) {
		this.dtUltimaAnalise = lastAnalyzed;
	}
}
