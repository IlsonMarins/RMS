package rms.core.impl.negocio;

import rms.core.IStrategy;
import rms.dominio.EntidadeDominio;
import rms.dominio.AgendamentoReorg;

public class ValidarAlteracao implements IStrategy {

	@Override
	public String processar(EntidadeDominio entidade) {
		AgendamentoReorg agendamentoReorg = (AgendamentoReorg)entidade;
		// apenas mostrar os dados da solicitação para possível alteração?
		if (agendamentoReorg.getEntidadeBusca() != null && agendamentoReorg.getEntidadeBusca().equals("VISUALIZAR"))
			return null;
		return null;
	} //fim-processar
	
} //fim-classe
