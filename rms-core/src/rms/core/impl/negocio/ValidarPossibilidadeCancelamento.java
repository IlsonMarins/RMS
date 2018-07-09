package rms.core.impl.negocio;

import rms.core.IStrategy;
import rms.core.impl.dao.SolicitacaoDAO;
import rms.dominio.EntidadeDominio;
import rms.dominio.AgendamentoReorg;

public class ValidarPossibilidadeCancelamento implements IStrategy {

	@Override
	public String processar(EntidadeDominio entidade) {
		SolicitacaoDAO solDAO = new SolicitacaoDAO();
		if(entidade != null){
			AgendamentoReorg agendamentoReorg = (AgendamentoReorg)entidade;
			// utilizar essa strategy?
			if (agendamentoReorg.getEntidadeBusca() != null
					&& agendamentoReorg.getEntidadeBusca().equals("CANCELAR")){
				solDAO.consultar(agendamentoReorg);
				// pode ser cancelado?
				if (agendamentoReorg.getStatus().equals("PENDENTE")){
					agendamentoReorg.setStatus("CANCELADO");
				}
			}
		}else{
			return "Erro em '" + this.getClass().getCanonicalName() + "': Entidade nula!\n"
					+ "Verifique os parâmetros e tente novamente...&";
		}
		return null;
	} // fim-processar
} // fim-classe
