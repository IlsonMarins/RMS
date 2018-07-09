package rms.core.impl.negocio;

import rms.core.IStrategy;
import rms.dominio.EntidadeDominio;
import rms.dominio.AgendamentoReorg;

public class ValidarAgendamentoSolicitacao implements IStrategy {
	
	@Override
	public String processar(EntidadeDominio entidade) {
		if(entidade != null){
			AgendamentoReorg agendamentoReorg = (AgendamentoReorg)entidade;
			// utilizar essa strategy?
			if (agendamentoReorg.getEntidadeBusca() == null){
				// validar campo agendamento
				if (agendamentoReorg.getDtAgendamento() == null){
					return "Erro em '" + this.getClass().getCanonicalName() + "': "
							+ "Data de agendamento não encontrada ou padrão incorreto!\n"
							+ "Revise a data informada seguindo o espelho dd/MM/yy HH:mm e tente novamente...&";
				}
			}
		}else{
			return "Erro em '" + this.getClass().getCanonicalName() + "': Entidade '" + AgendamentoReorg.class.getCanonicalName() + "' nula!\n"
					+ "Verifique o(s) parâmetro(s) e tente novamente...&";
		}
		return null;
	} //fim-processar

} //fim-classe
