package rms.core.impl.negocio;

import rms.core.IStrategy;
import rms.dominio.EntidadeDominio;
import rms.dominio.AgendamentoReorg;

public class TravarExecucao implements IStrategy {

	@Override
	public String processar(EntidadeDominio entidade) {
		AgendamentoReorg agendamentoReorg = null;
		// validar que a entidade est� preenchida
		if (entidade != null){
			if (entidade instanceof AgendamentoReorg){
				agendamentoReorg = (AgendamentoReorg) entidade;
				// utilizar essa strategy?
				if (agendamentoReorg.getEntidadeBusca() != null){
					// � para travar?
					if (agendamentoReorg.getEntidadeBusca().equals("VISUALIZAR")){
						if (agendamentoReorg.getStatus().equals("PENDENTE")){
							// alterar o status do agendamento para que n�o seja executado at� destravamento
							agendamentoReorg.setStatus("TRAVADO");
						}
					}else{ // n�o � para travar
						// � para destravar?
						if (agendamentoReorg.getStatus() != null 
								&& agendamentoReorg.getStatus().equals("TRAVADO")){
							// alterar o status do agendamento para destravar e permitir a continua��o do reorg
							agendamentoReorg.setStatus("PENDENTE");
						}else { // n�o � para destravar
							
						}
					}
				}else{ // n�o utilizar essa strategy
					
				}
			}else{ // a entidade n�o � AgendamentoReorg
				return "Erro em '" + this.getClass().getCanonicalName() + "': Entidade diferente de AgendamentoReorg!\n"
						+ "Verifique o(s) par�metro(s) e tente novamente...&";
			}
		}else{ // a entidade est� nula
			return "Erro em '" + this.getClass().getCanonicalName() + "': Entidade nula!\n"
					+ "Verifique o(s) par�metro(s) e tente novamente...&";
		}
		return null;
	} //fim-processar

} //fim-classe
