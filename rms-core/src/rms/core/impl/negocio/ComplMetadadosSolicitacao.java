package rms.core.impl.negocio;

import java.sql.SQLException;

import rms.core.IStrategy;
import rms.core.impl.dao.IndiceDAO;
import rms.core.impl.dao.MetadadoDAO;
import rms.dominio.EntidadeDominio;
import rms.dominio.*;

public class ComplMetadadosSolicitacao implements IStrategy {
	
	@Override
	public String processar(EntidadeDominio entidade) {
		MetadadoDAO metDAO = new MetadadoDAO(); // para buscar os metadados de cada tabela
		IndiceDAO indDAO = new IndiceDAO(); // para buscar os índices de cada tabela
		AgendamentoReorg agendamentoReorg = (AgendamentoReorg)entidade;
		if (agendamentoReorg != null){
			// utilizar essa strategy?
			if (agendamentoReorg.getEntidadeBusca() == null
					|| agendamentoReorg.getEntidadeBusca().equals("METADADO")
					|| agendamentoReorg.getEntidadeBusca().equals("DETALHES")
					|| agendamentoReorg.getEntidadeBusca().equals("VISUALIZAR")
					|| agendamentoReorg.getEntidadeBusca().equals("ALTERAR")){
				// validar se o ID ou o nome está preenchido
				Boolean temId = agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).getTabelas().get(0).getId() != null;
				Boolean temNome = agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).getTabelas().get(0).getNomeTabela() != null;
				Boolean temOwner = agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0) != null;
				if (temId || temNome){
					// buscar os metadados de cada uma das tabelas da solicitação
					for (int i = 0; i < agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).getTabelas().size(); i++){
						if (temOwner)
							agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).getTabelas().get(i).setOwner(agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0));
						try {
							// traz os metadados de cada tabela por referência
							metDAO.consultar(agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).getTabelas().get(i));
							// traz os índices de cada tabela por referência
							indDAO.consultar(agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).getTabelas().get(i));
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}else{
					return "'" + this.getClass().getCanonicalName() + "': Sem nome ou ID! "
							+ "Verifique o(s) parâmetro(s) e tente novamente...&";
				}
			}
		}else{
			return "Erro em '" + this.getClass().getCanonicalName() + "': Entidade '"
					+ AgendamentoReorg.class.getCanonicalName() + "' nula!\n"
					+ "Verifique o(s) parâmetro(s) e tente novamente...&";
		}
		return null;
	} //fim-processar
} // fim-classe