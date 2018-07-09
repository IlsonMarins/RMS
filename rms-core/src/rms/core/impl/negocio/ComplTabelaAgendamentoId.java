package rms.core.impl.negocio;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import rms.core.IStrategy;
import rms.core.impl.dao.OwnerDAO;
import rms.core.impl.dao.TabelaDAO;
import rms.dominio.*;

public class ComplTabelaAgendamentoId implements IStrategy {
	
	@Override
	public String processar(EntidadeDominio entidade) {
		TabelaDAO tabDAO = new TabelaDAO(); // para buscar as tabelas e se necessário inserir na tabela 'TABELA' do RMS
		AgendamentoReorg agendamentoReorg = null;
		if (entidade instanceof AgendamentoReorg)
			agendamentoReorg = (AgendamentoReorg)entidade;
		if (agendamentoReorg != null){
			// validar se a tabela está preenchida
			Boolean temTabela = agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).getTabelas() != null;
			if (temTabela){
				// buscar os IDs da(s) tabela(s)
				try{
					// altera a(s) tabela(s) por referência
					for (int i = 0; i < agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).getTabelas().size(); i++)
						tabDAO.visualizar(agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).getTabelas().get(i));
				}catch (SQLException e){
					e.printStackTrace();
				}
			}else{
				return "'" + this.getClass().getCanonicalName() + "': Sem banco! "
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