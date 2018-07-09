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
			return null; // n�o necess�rio retornar "ok" pois o Java trabalha por refer�ncia
		
	} //fim-processar
	
} //fim-classe
