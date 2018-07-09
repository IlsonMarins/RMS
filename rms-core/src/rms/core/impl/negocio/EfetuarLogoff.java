package rms.core.impl.negocio;

import rms.core.IStrategy;
import rms.dominio.EntidadeDominio;
import rms.dominio.*;

public class EfetuarLogoff implements IStrategy {

	@Override
	public String processar(EntidadeDominio entidade) {
		
		if (entidade instanceof Login)
			return "Processando logout..."; 
		else
			return null; // não necessário retornar "ok" pois o Java trabalha por referência
		
	} //fim-processar
	
} //fim-classe
