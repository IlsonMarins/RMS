package rms.core.impl.negocio;

import rms.core.IStrategy;
import rms.core.impl.dao.SolicitacaoDAO;
import rms.dominio.EntidadeDominio;
import rms.dominio.*;

public class ComplInfosSolicitacao implements IStrategy {
	
	@Override
	public String processar(EntidadeDominio entidade) {
		// para buscar o pr�ximo ID a partir do �ltimo ID inserido no banco:
		SolicitacaoDAO solDAO = new SolicitacaoDAO();
		AgendamentoReorg agendamentoReorg = (AgendamentoReorg)entidade;
		if (agendamentoReorg != null){
			if (agendamentoReorg.getEntidadeBusca() != null){
				// utilizar essa strategy?
				if (agendamentoReorg.getEntidadeBusca() != null
						&& (agendamentoReorg.getEntidadeBusca().equals("CARREGAR")
								|| agendamentoReorg.getEntidadeBusca().equals("DETALHES")
								|| agendamentoReorg.getEntidadeBusca().equals("VISUALIZAR"))){
					// recebe as informa��es da solicita��o por refer�ncia
					solDAO.consultar(agendamentoReorg);
				}
			}else{
				return "Erro em '" + this.getClass().getCanonicalName() + "': entidadeBusca nula!\n"
						+ "Verifique o(s) par�metro(s) e tente novamente...&";
			}
		}else{
			return "Erro em '" + this.getClass().getCanonicalName() + "': Entidade' "
					+ AgendamentoReorg.class.getCanonicalName() + "' nula!\n"
					+ "Verifique o(s) par�metro(s) e tente novamente...&";
		}
		return null;
	} //fim-processar
} // fim-classe