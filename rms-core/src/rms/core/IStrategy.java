package rms.core;

import rms.dominio.EntidadeDominio;


public interface IStrategy 
{

	public String processar(EntidadeDominio entidade);
	
}
