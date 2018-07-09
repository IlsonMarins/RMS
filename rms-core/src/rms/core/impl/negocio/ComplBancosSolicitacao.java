package rms.core.impl.negocio;

import java.sql.SQLException;

import rms.core.IStrategy;
import rms.core.impl.dao.BancoDAO;
import rms.dominio.EntidadeDominio;
import rms.dominio.*;

public class ComplBancosSolicitacao implements IStrategy {
	
	@Override
	public String processar(EntidadeDominio entidade) {
		// para buscar o pr�ximo ID a partir do �ltimo ID inserido no banco:
		BancoDAO banDAO = new BancoDAO();
		AgendamentoReorg agendamentoReorg = (AgendamentoReorg)entidade;
		if (agendamentoReorg != null){
			if (agendamentoReorg.getEntidadeBusca() != null){
				// utilizar essa strategy?
				if (agendamentoReorg.getEntidadeBusca().equals("BANCO")
						|| agendamentoReorg.getEntidadeBusca().equals("DETALHES")
						|| agendamentoReorg.getEntidadeBusca().equals("VISUALIZAR")){
					try {
						// recuperar os bancos por refer�ncia
						banDAO.consultar(agendamentoReorg);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}else{
				return "Erro em '" + this.getClass().getCanonicalName() + "': entidadeBusca nula!\n"
						+ "Verifique o(s) par�metro(s) e tente novamente...&";
			}
		}else{
			return "Erro em '" + this.getClass().getCanonicalName() + "': Entidade' "
					+ AgendamentoReorg.class.getCanonicalName() + "' nula!\n"
					+ "Verifique o(s) par�metro(s) e tente novamente...";
		}
		return null;
	} //fim-processar
} // fim-classe