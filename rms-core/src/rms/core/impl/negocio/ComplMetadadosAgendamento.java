package rms.core.impl.negocio;

import java.sql.SQLException;

import rms.core.IStrategy;
import rms.core.impl.dao.IndiceDicionarioDadosDAO;
import rms.core.impl.dao.MetadadoDicionarioDadosDAO;
import rms.dominio.EntidadeDominio;
import rms.dominio.*;

public class ComplMetadadosAgendamento implements IStrategy {
	
	@Override
	public String processar(EntidadeDominio entidade) {
		MetadadoDicionarioDadosDAO metDAO = new MetadadoDicionarioDadosDAO(); // para buscar os metadados de cada tabela
		IndiceDicionarioDadosDAO indDAO = new IndiceDicionarioDadosDAO(); // para buscar os índices de cada tabela
		AgendamentoReorg agendamentoReorg = null;
		if (entidade instanceof AgendamentoReorg)
			agendamentoReorg = (AgendamentoReorg)entidade;
		if (agendamentoReorg != null){
			// validar se o nome está preenchido
			Boolean temNome = agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).getTabelas().get(0).getNomeTabela() != null;
			Boolean temOwner = agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0) != null;
			if (temNome){
				// buscar os metadados de cada uma das tabelas do agendamento
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
				return "'" + this.getClass().getCanonicalName() + "': Sem nome! "
						+ "Verifique o(s) parâmetro(s) e tente novamente...&";
			}
		}else{
			return "Erro em '" + this.getClass().getCanonicalName() + "': Entidade '"
					+ AgendamentoReorg.class.getCanonicalName() + "' nula!\n"
					+ "Verifique o(s) parâmetro(s) e tente novamente...&";
		}
		return null;
	} //fim-processar
} // fim-classe