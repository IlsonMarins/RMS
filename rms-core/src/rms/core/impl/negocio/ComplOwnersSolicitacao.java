package rms.core.impl.negocio;

import java.sql.SQLException;

import rms.core.IStrategy;
import rms.core.impl.dao.OwnerDicionarioDadosDAO;
import rms.dominio.EntidadeDominio;
import rms.dominio.*;

public class ComplOwnersSolicitacao implements IStrategy {
	
	@Override
	public String processar(EntidadeDominio entidade) {
		// para buscar o próximo ID a partir do último ID inserido no banco:
		OwnerDicionarioDadosDAO ownDAO = new OwnerDicionarioDadosDAO();
		AgendamentoReorg agendamentoReorg = (AgendamentoReorg)entidade;
		if (agendamentoReorg != null){
			if (agendamentoReorg.getEntidadeBusca() != null){
				// utilizar essa strategy?
				if (agendamentoReorg.getEntidadeBusca().equals("OWNER")
						|| agendamentoReorg.getEntidadeBusca().equals("DETALHES")
						|| agendamentoReorg.getEntidadeBusca().equals("VISUALIZAR")){
					try {
						// recuperar os owners por referência
						ownDAO.consultar(agendamentoReorg);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}else{
				return "Erro em '" + this.getClass().getCanonicalName() + "': entidadeBusca nula!\n"
						+ "Verifique o(s) parâmetro(s) e tente novamente...&";
			}
		}else{
			return "Erro em '" + this.getClass().getCanonicalName() + "': Entidade' "
					+ AgendamentoReorg.class.getCanonicalName() + "' nula!\n"
					+ "Verifique o(s) parâmetro(s) e tente novamente...";
		}
		return null;
	} //fim-processar
} // fim-classe