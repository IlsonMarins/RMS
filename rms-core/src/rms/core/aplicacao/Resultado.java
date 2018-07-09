package rms.core.aplicacao;

import java.util.List;

import rms.dominio.EntidadeDominio;


public class Resultado extends EntidadeAplicacao {
	
	// atributos privados
	private String msg;
	private List<EntidadeDominio> entidades;
	
	// métodos de acesso
	public String getMsg() {
		return msg;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public List<EntidadeDominio> getEntidades() {
		return entidades;
	}
	
	public void setEntidades(List<EntidadeDominio> entidades) {
		this.entidades = entidades;
	}
	
} // fim-classe
