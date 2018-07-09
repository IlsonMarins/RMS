package rms.core.impl.negocio;

import rms.core.IStrategy;
import rms.dominio.EntidadeDominio;
import rms.dominio.AgendamentoReorg;

public class TravarExecucao implements IStrategy {

	@Override
	public String processar(EntidadeDominio entidade) {
		AgendamentoReorg agendamentoReorg = null;
		// validar que a entidade está preenchida
		if (entidade != null){
			if (entidade instanceof AgendamentoReorg){
				agendamentoReorg = (AgendamentoReorg) entidade;
				// utilizar essa strategy?
				if (agendamentoReorg.getEntidadeBusca() != null){
					// é para travar?
					if (agendamentoReorg.getEntidadeBusca().equals("VISUALIZAR")){
						if (agendamentoReorg.getStatus().equals("PENDENTE")){
							// alterar o status do agendamento para que não seja executado até destravamento
							agendamentoReorg.setStatus("TRAVADO");
						}
					}else{ // não é para travar
						// é para destravar?
						if (agendamentoReorg.getStatus() != null 
								&& agendamentoReorg.getStatus().equals("TRAVADO")){
							// alterar o status do agendamento para destravar e permitir a continuação do reorg
							agendamentoReorg.setStatus("PENDENTE");
						}else { // não é para destravar
							
						}
					}
				}else{ // não utilizar essa strategy
					
				}
			}else{ // a entidade não é AgendamentoReorg
				return "Erro em '" + this.getClass().getCanonicalName() + "': Entidade diferente de AgendamentoReorg!\n"
						+ "Verifique o(s) parâmetro(s) e tente novamente...&";
			}
		}else{ // a entidade está nula
			return "Erro em '" + this.getClass().getCanonicalName() + "': Entidade nula!\n"
					+ "Verifique o(s) parâmetro(s) e tente novamente...&";
		}
		return null;
	} //fim-processar

} //fim-classe
