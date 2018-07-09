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
				&& (agendamentoReorg.getEntidadeBusca().equals("CANCELAR"))){ // é cancelamento de solicitacao?
			// confirma se o status atual realmente permite o cancelamento
			SolicitacaoDAO solDAO = new SolicitacaoDAO();
			solDAO.visualizar(agendamentoReorg);
			if (agendamentoReorg.getStatus().equals("PENDENTE")){ // o status realmente é pendente?
				agendamentoReorg.setStatus("CANCELADO"); // altera o status
			}
		}else if (agendamentoReorg.getStatus() == null){ // é carregamento inicial da página de solicitação?
			agendamentoReorg.setStatus("RASCUNHO");
		}else if (agendamentoReorg.getStatus().equals("RASCUNHO")){ // é salvamento de nova solicitação?
			agendamentoReorg.setStatus("PENDENTE");
		}
		return null;
	} //fim-processar
	
} //fim-classe
