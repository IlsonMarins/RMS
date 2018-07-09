package rms.core.impl.negocio;

import rms.core.IStrategy;
import rms.core.impl.dao.SolicitacaoDAO;
import rms.dominio.EntidadeDominio;
import rms.dominio.AgendamentoReorg;

public class AlterarStatusSolicitacao implements IStrategy {

	@Override
	public String processar(EntidadeDominio entidade) {
		AgendamentoReorg agendamentoReorg = (AgendamentoReorg)entidade;
		// sair sem utilizar a strategy?
		if (agendamentoReorg.getEntidadeBusca() != null 
				&& (agendamentoReorg.getEntidadeBusca().equals("METADADO")
						|| agendamentoReorg.getEntidadeBusca().equals("DETALHES")
						|| agendamentoReorg.getEntidadeBusca().equals("VISUALIZAR"))){
			return null;
		}else if (agendamentoReorg.getEntidadeBusca() != null 
				&& (agendamentoReorg.getEntidadeBusca().equals("CANCELAR"))){ // � cancelamento de solicitacao?
			// confirma se o status atual realmente permite o cancelamento
			SolicitacaoDAO solDAO = new SolicitacaoDAO();
			solDAO.visualizar(agendamentoReorg);
			if (agendamentoReorg.getStatus().equals("PENDENTE")){ // o status realmente � pendente?
				agendamentoReorg.setStatus("CANCELADO"); // altera o status
			}
		}else if (agendamentoReorg.getStatus() == null){ // � carregamento inicial da p�gina de solicita��o?
			agendamentoReorg.setStatus("RASCUNHO");
		}else if (agendamentoReorg.getStatus().equals("RASCUNHO")){ // � salvamento de nova solicita��o?
			agendamentoReorg.setStatus("PENDENTE");
		}
		return null;
	} //fim-processar
	
} //fim-classe
