package rms.core.impl.negocio;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import rms.core.IStrategy;
import rms.core.impl.dao.OwnerDAO;
import rms.core.impl.dao.TabelaDAO;
import rms.dominio.*;

public class ComplTabelaAgendamento implements IStrategy {
	
	@Override
	public String processar(EntidadeDominio entidade) {
		TabelaDAO tabDAO = new TabelaDAO(); // para buscar as tabelas e se necessário inserir na tabela 'TABELA' do RMS
		AgendamentoReorg agendamentoReorg = null;
		Tabela tabelaASerBuscada = new Tabela();
		if (entidade instanceof AgendamentoReorg)
			agendamentoReorg = (AgendamentoReorg)entidade;
		if (agendamentoReorg != null){
			// pegar a tabela vinda da tela, antes dela ser modificada pela busca do DAO
			Boolean temTabela = agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).getTabelas() != null;
			if (temTabela){
				Boolean temNomeTabela = agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).getTabelas().get(0).getNomeTabela() != null;
				if (temNomeTabela)
					tabelaASerBuscada.setNomeTabela(agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).getTabelas().get(0).getNomeTabela());
				else
					return "Erro em '" + this.getClass().getCanonicalName() 
								+ " - Não recebido nenhum nome de owner para efetuar a validação de consistência no banco!&";
				tabelaASerBuscada.setDtCadastro(agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).getTabelas().get(0).getDtCadastro());;
				tabelaASerBuscada.setId(agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).getTabelas().get(0).getId());
				tabelaASerBuscada.setIndices(agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).getTabelas().get(0).getIndices());
				tabelaASerBuscada.setMetadado(agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).getTabelas().get(0).getMetadado());
				tabelaASerBuscada.setOwner(agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).getTabelas().get(0).getOwner());
				// zerar a tabela para o DAO receber um objeto limpao e poder preenchê-lo
				/*agendamentoReorg.getCliente().getBancos().get(0).setOwner(new Owner()); // desnecessário pq o DAO ja cria novo Objeto*/
			}else{
				return "Erro em '" + this.getClass().getCanonicalName() 
						+ " - Não recebido nenhum owner da ViewHelper!&";
			}
			// validar se o banco está preenchido
			Boolean temOwner = agendamentoReorg.getCliente().getBancos().get(0).getOwners() != null;
			if (temOwner){
				// buscar as tabelas do owner, e caso não exista o a tabela corrente, inserí-la
				try{
					// traz as tabelas do owner por referência
					tabDAO.consultar(agendamentoReorg);
				}catch (SQLException e){
					e.printStackTrace();
				}
				// validar se a tabela corrente existe na base, caso não exista, inserí-la.
				Boolean inserirTabela = true;
				for (Tabela t: agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).getTabelas()){
					// a tabela já existe na base?
					if (t.getNomeTabela().equals(tabelaASerBuscada.getNomeTabela())){
						// a tabela já existe, então não inserir, e partir para a próxima strategy!
						// retornar o valor original do objeto, pois ele foi alterado pelo DAO
						List<Tabela> tabelas = new ArrayList<Tabela>();
						tabelas.add(tabelaASerBuscada);
						agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).setTabelas(tabelas);
						return null;
					}
				}
				if (inserirTabela){
					// consultar o ID do owner na base antes de inserir a tabela efetivamente
					OwnerDAO ownDAO = new OwnerDAO();
					try{
						ownDAO.visualizar(tabelaASerBuscada.getOwner());
					}catch (SQLException e){
						e.printStackTrace();
					}
					// a tabela não existe - fazer sua inserção!
					try{
						tabDAO.salvar(tabelaASerBuscada);
					}catch (SQLException e){
						e.printStackTrace();
					}
					// retornar o valor original do objeto, pois ele foi alterado pelo DAO
					List<Tabela> tabelas = new ArrayList<Tabela>();
					tabelas.add(tabelaASerBuscada);
					agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).setTabelas(tabelas);
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