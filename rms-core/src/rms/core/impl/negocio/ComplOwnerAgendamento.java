package rms.core.impl.negocio;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import rms.core.IStrategy;
import rms.core.impl.dao.OwnerDAO;
import rms.dominio.EntidadeDominio;
import rms.dominio.*;

public class ComplOwnerAgendamento implements IStrategy {
	
	@Override
	public String processar(EntidadeDominio entidade) {
		OwnerDAO ownDAO = new OwnerDAO(); // para buscar os owners e se necess�rio inserir na tabela 'OWNER' do RMS
		AgendamentoReorg agendamentoReorg = null;
		Owner ownerASerBuscado = new Owner();
		if (entidade instanceof AgendamentoReorg)
			agendamentoReorg = (AgendamentoReorg)entidade;
		if (agendamentoReorg != null){
			// pegar o owner vindo da tela, antes dele ser modificado pela busca do DAO
			Boolean temOwner = agendamentoReorg.getCliente().getBancos().get(0).getOwners() != null;
			if (temOwner){
				Boolean temNomeOwner = agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).getNomeOwner() != null;
				if (temNomeOwner)
					ownerASerBuscado.setNomeOwner(agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).getNomeOwner());
				else
					return "Erro em '" + this.getClass().getCanonicalName() 
								+ " - N�o recebido nenhum nome de owner para efetuar a valida��o de consist�ncia no banco!&";
				ownerASerBuscado.setBanco(agendamentoReorg.getCliente().getBancos().get(0));
				ownerASerBuscado.setDtCadastro(agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).getDtCadastro());
				ownerASerBuscado.setTabelas(agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).getTabelas());
				// zerar o owner para o DAO receber um owner limpo e poder preench�-lo
				/*agendamentoReorg.getCliente().getBancos().get(0).setOwner(new Owner()); // desnecess�rio pq o DAO ja cria novo Objeto*/
			}else{
				return "Erro em '" + this.getClass().getCanonicalName() 
						+ " - N�o recebido nenhum owner da ViewHelper!&";
			}
			// validar se o banco est� preenchido
			Boolean temBanco = agendamentoReorg.getCliente().getBancos().get(0) != null;
			if (temBanco){
				// buscar os owners do banco, e caso n�o exista o owner corrente, inser�-lo
				try{
					// traz os owners do banco por refer�ncia
					ownDAO.consultar(agendamentoReorg);
				}catch (SQLException e){
					e.printStackTrace();
				}
				// validar se o owner corrente existe na base, caso n�o exista, inser�-lo.
				Boolean inserirOwner = true;
				for (Owner o: agendamentoReorg.getCliente().getBancos().get(0).getOwners()){
					// o owner j� existe na base?
					if (o.getNomeOwner().equals(ownerASerBuscado.getNomeOwner())){
						// o owner j� existe, ent�o n�o inserir, e partir para a valida��o da exist�ncia da tabela!
						// retornar o valor original do objeto, pois ele foi alterado pelo DAO
						List<Owner> owners = new ArrayList<Owner>();
						owners.add(ownerASerBuscado);
						agendamentoReorg.getCliente().getBancos().get(0).setOwners(owners);
						return null;
					}
				}
				if (inserirOwner){
					// o owner n�o existe - fazer sua inser��o!
					try{
						ownDAO.salvar(ownerASerBuscado);
					}catch (SQLException e){
						e.printStackTrace();
					}
				}
			}else{
				return "'" + this.getClass().getCanonicalName() + "': Sem banco! "
						+ "Verifique o(s) par�metro(s) e tente novamente...&";
			}
		}else{
			return "Erro em '" + this.getClass().getCanonicalName() + "': Entidade '"
					+ AgendamentoReorg.class.getCanonicalName() + "' nula!\n"
					+ "Verifique o(s) par�metro(s) e tente novamente...&";
		}
		return null;
	} //fim-processar
} // fim-classe