package rms.core.impl.negocio;

import java.util.Date;

import rms.core.IStrategy;
import rms.dominio.EntidadeDominio;
import rms.dominio.AgendamentoReorg;

public class ComplDtCadastro implements IStrategy {

	@Override
	public String processar(EntidadeDominio entidade) {
		// validar que a entidade está preenchida
		if (entidade != null){
			if (entidade instanceof AgendamentoReorg){
				AgendamentoReorg agendamentoReorg = (AgendamentoReorg) entidade;
				// sair sem utilizar a strategy?
				if (agendamentoReorg.getEntidadeBusca() != null
						&& (agendamentoReorg.getEntidadeBusca().equals("CANCELAR")
						|| agendamentoReorg.getEntidadeBusca().equals("VISUALIZAR")
						|| agendamentoReorg.getEntidadeBusca().equals("VOLTAR"))){
					return null;
				}
			}else{
				return "Erro em '" + this.getClass().getCanonicalName() + "': Entidade "
						+ AgendamentoReorg.class.getCanonicalName() + " nula!\n"
						+ "Verifique o(s) parâmetro(s) e tente novamente...&";
			}
			// complementar a data de cadastro, independentemente da entidade que seja
			Date data = new Date();
			entidade.setDtCadastro(data);
		}else{ // a entidade está nula
			return "Erro em '" + this.getClass().getCanonicalName() + "': Entidade nula!\n"
					+ "Verifique o(s) parâmetro(s) e tente novamente...&";
		}
		return null;
	} //fim-processar

} //fim-classe
