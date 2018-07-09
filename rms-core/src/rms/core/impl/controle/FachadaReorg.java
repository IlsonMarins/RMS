package rms.core.impl.controle;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import rms.core.IDAO;
import rms.core.IFachadaReorg;
import rms.core.IStrategy;
import rms.core.aplicacao.Resultado;
import rms.core.impl.dao.ReorgDAO;
import rms.core.impl.negocio.ComplSolicitacoesPendentes;
import rms.core.impl.negocio.ExecutarReorg;
import rms.core.impl.negocio.ValidarEspacoTS;
import rms.dominio.EntidadeDominio;
import rms.dominio.ExecucaoReorg;

public class FachadaReorg extends TimerTask implements IFachadaReorg {
	
	// atributos privados
	private Fachada fachada = new Fachada();
	private EntidadeDominio entidade = null;
	/*
	 * Mapa de DAOS, ser� indexado pelo nome da entidade 
	 * O valor � uma inst�ncia do DAO para uma dada entidade
	*/
	private Map<String, IDAO> daos;
	/*
	 * Mapa para conter as regras de neg�cio de todas as opera��es por entidade -
	 * O valor � um mapa que de regras de neg�cio indexado pela opera��o
	 */
	private Map<String, Map<String, List<IStrategy>>> rns;
	private Resultado resultado = new Resultado();
	
	// construtor default
	public FachadaReorg(){
		// Int�nciando o Map de DAOS
		daos = new HashMap<String, IDAO>();
		// Criando inst�ncias dos DAOs a serem utilizados
		ReorgDAO reorgDAO = new ReorgDAO();
		// Adicionando cada dao no MAP indexando pelo nome da classe
		daos.put(ExecucaoReorg.class.getName(), reorgDAO);
		
		// Int�nciando o Map de Regras de Neg�cio
		rns = new HashMap<String, Map<String, List<IStrategy>>>();
		// Criando inst�ncias de regras de neg�cio a serem utilizadas
		ComplSolicitacoesPendentes cSolPendentes = new ComplSolicitacoesPendentes();
		ValidarEspacoTS valEspacoTablespace = new ValidarEspacoTS();
		ExecutarReorg exeReorg = new ExecutarReorg();
		// Cria o mapa que poder� conter todas as listas de regras de neg�cio espec�fica por opera��o do reorg
		Map<String, List<IStrategy>> rnsReorg = new HashMap<String, List<IStrategy>>();
		
		/* *************************************************************
		 * Criando uma lista para conter as regras de neg�cio de reorg
		 * quando a opera��o for salvar
		 * *************************************************************/
		List<IStrategy> rnsSalvarReorg = new ArrayList<IStrategy>();
		// Adicionando as regras a serem utilizadas na opera��o salvar do reorg
		rnsSalvarReorg.add(cSolPendentes);
		rnsSalvarReorg.add(exeReorg);
		// Adiciona a lista de regras na opera��o salvar no mapa do reorg
		rnsReorg.put("SALVAR", rnsSalvarReorg);
		// Adiciona o mapa com as regras indexadas pelas opera��es no mapa geral indexado pelo nome da entidade
		rns.put(ExecucaoReorg.class.getName(), rnsReorg);
		
		/* ******************************************************************
		 * Criando uma lista para conter as regras de neg�cio de solicitacao
		 * quando a opera��o for consultar
		 * ******************************************************************/
		List<IStrategy> rnsConsultarReorg = new ArrayList<IStrategy>();
		// Adicionando as regras a serem utilizadas na opera��o consultar
		//rnsConsultarReorg.add(cSolPendentes);
		// Adiciona a lista de regras na opera��o consultar do mapa de regras do reorg
		rnsReorg.put("CONSULTAR", rnsConsultarReorg);
		// Adiciona o mapa com as regras indexadas pelas opera��es no mapa geral indexado pelo nome da entidade
		rns.put(ExecucaoReorg.class.getName(), rnsReorg);
		
		/* *************************************************************
		 * Criando uma lista para conter as regras de neg�cio do reorg
		 * quando a opera��o for alterar
		 * *************************************************************/
		List<IStrategy> rnsAlterarReorg = new ArrayList<IStrategy>();
		//Adicionando as regras a serem utilizadas na opera��o alterar da solicitacao
		// Adiciona a lista de regras na opera��o alterar no mapa da solicitacao
		rnsReorg.put("ALTERAR", rnsAlterarReorg);
		// Adiciona o mapa com as regras indexadas pelas opera��es no mapa geral indexado pelo nome da entidade
		rns.put(ExecucaoReorg.class.getName(), rnsReorg);
		
	} // fim-construtor
	
	// m�todos de acesso
	public EntidadeDominio getEntidade() {
		return entidade;
	}

	public void setEntidade(EntidadeDominio entidade) {
		this.entidade = entidade;
	}

	// m�todos sobrescritos de IFachadaReorg
	@Override
	public Resultado salvar(EntidadeDominio entidade) {
		return fachada.salvar(entidade);
	}

	@Override
	public Resultado alterar(EntidadeDominio entidade) {
		return fachada.alterar(entidade);
	}

	@Override
	public Resultado excluir(EntidadeDominio entidade) {
		return fachada.excluir(entidade);
	}

	@Override
	public Resultado consultar(EntidadeDominio entidade) {
		return fachada.consultar(entidade);
	}

	@Override
	public Resultado visualizar(EntidadeDominio entidade) {
		return fachada.visualizar(entidade);
	}
	
	public Resultado executaRun(){
		run();
		return resultado;
	}

	@Override
	public void run() {
		//System.out.println("Entrou no FachadaReorg.run()!");
		entidade = new ExecucaoReorg();
		String nmClasse = entidade.getClass().getName();
		//List<EntidadeDominio> entidades = new ArrayList<EntidadeDominio>();
		// efetuar as consultas e altera��es necess�rias para iniciar o ExecucaoReorg
		String msg = executarRegras(entidade, "SALVAR");
		// salvar?
		if(msg == null){
			IDAO dao = daos.get(nmClasse);
			try {
				dao.salvar(entidade);
			} catch (SQLException e) {
				e.printStackTrace();
				resultado.setMsg("N�o foi poss�vel salvar o ExecucaoReorg!");
			}
		}else{
			resultado.setMsg(msg);
		}
		
	} // fim-run()

	private String executarRegras(EntidadeDominio entidade, String operacao) {
		String nmClasse = entidade.getClass().getName();		
		StringBuilder msg = new StringBuilder();
		
		Map<String, List<IStrategy>> regrasOperacao = rns.get(nmClasse);
		
		
		if(regrasOperacao != null){
			List<IStrategy> regras = regrasOperacao.get(operacao);
			
			if(regras != null){
				for(IStrategy s: regras){			
					String m = s.processar(entidade);			
					
					if(m != null){
						msg.append(m);
						msg.append("\n");
					}			
				}	
			}			
			
		}
		if(msg.length() > 0){
			System.out.println("\nFachadaReorg - " + msg.toString());
			return msg.toString();
		}else
			return null;
	} // fim-executarRegras

	@Override
	public Resultado cancelar(EntidadeDominio entidade) {
		return fachada.cancelar(entidade);
	}
	
}
