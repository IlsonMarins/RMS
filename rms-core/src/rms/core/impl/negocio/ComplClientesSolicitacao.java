package rms.core.impl.negocio;

import java.sql.SQLException;

import rms.core.IStrategy;
import rms.core.impl.dao.ClienteDAO;
import rms.dominio.EntidadeDominio;
import rms.dominio.*;

public class ComplClientesSolicitacao implements IStrategy {
	
	@Override
	public String processar(EntidadeDominio entidade) {
		// para buscar o próximo ID a partir do último ID inserido no banco:
		ClienteDAO cliDAO = new ClienteDAO();
		AgendamentoReorg agendamentoReorg = (AgendamentoReorg)entidade;
		if (agendamentoReorg != null){
			// utilizar essa strategy?
			if (agendamentoReorg.getEntidadeBusca() != null
					&& (agendamentoReorg.getEntidadeBusca().equals("CARREGAR")
							|| agendamentoReorg.getEntidadeBusca().equals("DETALHES")
							|| agendamentoReorg.getEntidadeBusca().equals("VISUALIZAR"))){
				try {
					// recuperar os clientes cadastrados no sistema por referência
					cliDAO.consultar(agendamentoReorg);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}else{
			return "Erro em '" + this.getClass().getCanonicalName() + "': Entidade' "
					+ AgendamentoReorg.class.getCanonicalName() + "' nula!\n"
					+ "Verifique o(s) parâmetro(s) e tente novamente...";
		}
		return null;
	} //fim-processar
} // fim-classe