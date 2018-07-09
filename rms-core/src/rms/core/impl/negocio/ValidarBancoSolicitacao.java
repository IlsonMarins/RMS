package rms.core.impl.negocio;

import rms.core.IStrategy;
import rms.dominio.EntidadeDominio;
import rms.dominio.AgendamentoReorg;

public class ValidarBancoSolicitacao implements IStrategy {
	
	@Override
	public String processar(EntidadeDominio entidade) {
		if(entidade != null){
			AgendamentoReorg agendamentoReorg = (AgendamentoReorg)entidade;
			// utilizar essa strategy?
			if (agendamentoReorg.getEntidadeBusca() == null){
				// validar campo banco
				if (agendamentoReorg.getCliente().getBancos().get(0).getId() == null){
					return "Erro em '" + this.getClass().getCanonicalName() + "': Banco da solicitação não encontrado! \n"
							+ "Revise o campo e tente novamente...&";
				}
			}
		}else{
			return "Erro em '" + this.getClass().getCanonicalName() + "': Entidade '" + AgendamentoReorg.class.getCanonicalName() + "' nula!\n"
					+ "Verifique o(s) parâmetro(s) e tente novamente...&";
		}
		return null;
	} //fim-processar

} //fim-classe
