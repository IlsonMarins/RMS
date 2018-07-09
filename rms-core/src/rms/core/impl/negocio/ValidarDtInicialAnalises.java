package rms.core.impl.negocio;

import rms.core.IStrategy;
import rms.dominio.Analyze;
import rms.dominio.EntidadeDominio;

public class ValidarDtInicialAnalises implements IStrategy {
	
	@Override
	public String processar(EntidadeDominio entidade) {
		if(entidade != null){
			Analyze analyze = (Analyze)entidade;
			// validar campo data inicial
			if (analyze.getDtInicial() == null){
				return "Erro em '" + this.getClass().getCanonicalName() + "': "
						+ "Data inicial não encontrada ou padrão incorreto!\n"
						+ "Revise a data informada seguindo o espelho dd/MM/yy e tente novamente...&";
			}
		}else{
			return "Erro em '" + this.getClass().getCanonicalName() + "': Entidade '" + Analyze.class.getCanonicalName() + "' nula!\n"
					+ "Verifique o(s) parâmetro(s) e tente novamente...&";
		}
		return null;
	} //fim-processar

} //fim-classe
