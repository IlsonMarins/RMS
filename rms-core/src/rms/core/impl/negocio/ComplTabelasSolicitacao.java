package rms.core.impl.negocio;

import java.sql.SQLException;

import rms.core.IStrategy;
import rms.core.impl.dao.TabelaDicionarioDadosDAO;
import rms.dominio.EntidadeDominio;
import rms.dominio.*;

public class ComplTabelasSolicitacao implements IStrategy {
	
	@Override
	public String processar(EntidadeDominio entidade) {
		TabelaDicionarioDadosDAO tabDAO = new TabelaDicionarioDadosDAO(); // para buscar as tabelas da solicitacao
		AgendamentoReorg agendamentoReorg = (AgendamentoReorg)entidade;
		if (agendamentoReorg != null){
			// utilizar essa strategy?
			if (agendamentoReorg.getEntidadeBusca() != null){
				if (agendamentoReorg.getEntidadeBusca().equals("TABELA")
						|| agendamentoReorg.getEntidadeBusca().equals("DETALHES")
						|| agendamentoReorg.getEntidadeBusca().equals("VISUALIZAR")){
					// buscar as tabelas da solicita��o
					try {
						// traz as tabelas da solicita��o por refer�ncia
						tabDAO.consultar(agendamentoReorg);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}else{
				return "Erro em '" + this.getClass().getCanonicalName() + "': entidadeBusca nula!\n"
						+ "Verifique o(s) par�metro(s) e tente novamente...&";
			}
		}else{
			return "Erro em '" + this.getClass().getCanonicalName() + "': Entidade "
					+ AgendamentoReorg.class.getCanonicalName() + " nula!\n"
					+ "Verifique o(s) par�metro(s) e tente novamente...&";
		}
		return null;
	} //fim-processar
} // fim-classe