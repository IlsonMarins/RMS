package rms.core.impl.controle;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rms.core.IDAO;
import rms.core.IFachada;
import rms.core.IStrategy;
import rms.core.aplicacao.Resultado;
import rms.core.impl.dao.*;
import rms.core.impl.negocio.*;
import rms.dominio.*;

public class Fachada implements IFachada {
	
	// atributos comuns a todos os métodos da Fachada
	// Mapa de DAOS, será indexado pelo nome da entidade 
	// O valor é uma instância do DAO para uma dada entidade;
	private Map<String, IDAO> daos;
	// Mapa para conter as regras de negócio de todas as operações por entidade;
	// O valor é um mapa que de regras de negócio indexado pela operação
	private Map<String, Map<String, List<IStrategy>>> rns;
	private Resultado resultado;
	
	// construtor default
	public Fachada(){
		
//		#############################################################################
//		Intânciando o Map de DAOS
//		#############################################################################
		daos = new HashMap<String, IDAO>();
		
//		#############################################################################
//		Intânciando o Map de Regras de Negócio
//		#############################################################################
		rns = new HashMap<String, Map<String, List<IStrategy>>>();
		
//		#############################################################################
//		Criando instâncias de todos os DAOs a serem utilizados
//		#############################################################################
		SolicitacaoDAO solDAO = new SolicitacaoDAO();
		AnalyzeDAO anaDAO = new AnalyzeDAO();
		MetadadoDAO metDAO = new MetadadoDAO();
		ConsoleDAO conDAO = new ConsoleDAO();
		LogDAO logDAO = new LogDAO();
		AdministradorDAO admDAO = new AdministradorDAO();
		FuncionarioDAO funDAO = new FuncionarioDAO();
		RegistroDAO regDAO = new RegistroDAO();
		
//		#############################################################################
//		Adicionando cada DAO no map indexando pelo nome da classe
//		#############################################################################
		daos.put(AgendamentoReorg.class.getName(), solDAO);
		daos.put(Analyze.class.getName(), anaDAO);
		daos.put(Metadado.class.getName(), metDAO);
		daos.put(Console.class.getName(), conDAO);
		daos.put(Log.class.getName(), logDAO);
		daos.put(Administrador.class.getName(), admDAO);
		daos.put(Registro.class.getName(), regDAO);
		daos.put(Funcionario.class.getName(), funDAO);
		
//		#############################################################################
//		Criando todas as instâncias de regras de negócio a serem utilizadas
//		#############################################################################
		ComplDtCadastro compDtCadastro = new ComplDtCadastro();
		ComplInfosSolicitacao complInfosSol = new ComplInfosSolicitacao();
		AlterarStatusSolicitacao complStatusSolicitacao = new AlterarStatusSolicitacao();
		ComplClientesSolicitacao complClientesSol = new ComplClientesSolicitacao();
		ComplBancosSolicitacao complBancosSol = new ComplBancosSolicitacao();
		ComplOwnersSolicitacao complOwnersSol = new ComplOwnersSolicitacao();
		ComplTabelasSolicitacao complTabelasSol = new ComplTabelasSolicitacao();
		ComplMetadadosSolicitacao complMetadadosSol = new ComplMetadadosSolicitacao();
		ComplMetadadosAgendamento complMetadadosAgend = new ComplMetadadosAgendamento();
		ComplOwnerAgendamento complOwnerAgendamento = new ComplOwnerAgendamento();
		ComplTabelaAgendamento complTabelaAgendamento = new ComplTabelaAgendamento();
		ComplTabelaAgendamentoId complTabelaAgendamentoId = new ComplTabelaAgendamentoId();
		ValidarIdSolicitacao valIdSolicitacao = new ValidarIdSolicitacao();
		ValidarAgendamentoSolicitacao valAgendamentoSolicitacao = new ValidarAgendamentoSolicitacao();
		ValidarStatusSolicitacao valStatusSolicitacao = new ValidarStatusSolicitacao();
		ValidarClienteSolicitacao valClienteSolicitacao = new ValidarClienteSolicitacao();
		ValidarBancoSolicitacao valBancoSolicitacao = new ValidarBancoSolicitacao();
		ValidarOwnerSolicitacao valOwnerSolicitacao = new ValidarOwnerSolicitacao();
		ValidarTabelasSolicitacao valTabelasSolicitacao = new ValidarTabelasSolicitacao();
		ValidarPossibilidadeCancelamento vPosCancel = new ValidarPossibilidadeCancelamento();
		TravarExecucao travExec = new TravarExecucao();
		ValidarUsuario valUsuario = new ValidarUsuario();
		//ValidarDtInicialAnalises valDtInicialAnalises = new ValidarDtInicialAnalises();
		//ValidarDtFinalAnalises valDtFinalAnalises = new ValidarDtFinalAnalises();
		ValidarDadosLogin valDadosLogin = new ValidarDadosLogin();
		ComplementarUsuario compUsuario = new ComplementarUsuario();
		EfetuarLogoff efetLogoff = new EfetuarLogoff();
		
//		#############################################################################
//		Criando os mapas de regras que poderá conter todas as listas de regras de
//		negócio específicas por operação para cada Entidade
//		#############################################################################
		// regras de negócio de AgendamentoReorg de Nova Solicitação de ExecucaoReorg
		Map<String, List<IStrategy>> rnsAgendamento = new HashMap<String, List<IStrategy>>();
		// regras de negócio de Login
		Map<String, List<IStrategy>> rnsLogin = new HashMap<String, List<IStrategy>>();
		// regras de negócio de Analise
		Map<String, List<IStrategy>> rnsAnalyze = new HashMap<String, List<IStrategy>>();
		// regras de negócio de Console
		Map<String, List<IStrategy>> rnsConsole = new HashMap<String, List<IStrategy>>();
		
		// ========================================================================
		// Criando uma lista para conter as regras de negócio de analises
		// quando a operação for consultar
		// ========================================================================
		List<IStrategy> rnsConsultarAnalyze = new ArrayList<IStrategy>();
		// Adicionando as regras a serem utilizadas na operação consultar da analises
		//rnsConsultarAnalises.add(valDtInicialAnalises);
		//rnsConsultarAnalises.add(valDtFinalAnalises);
		// Adiciona a lista de regras na operação salvar do mapa de regras de negócio da solicitacao
		rnsAnalyze.put("CONSULTAR", rnsConsultarAnalyze);
		// Adiciona o mapa com as regras indexadas pelas operações no mapa geral indexado 
		// pelo nome da entidade.
		rns.put(Analyze.class.getName(), rnsAnalyze);
		
		// ========================================================================
		// Criando uma lista para conter as regras de negócio de console
		// quando a operação for visualizar
		// ========================================================================
		List<IStrategy> rnsVisualizarConsole = new ArrayList<IStrategy>();
		// Adicionando as regras a serem utilizadas na operação consultar da analises
		//rnsConsultarAnalises.add(valDtInicialAnalises);
		//rnsConsultarAnalises.add(valDtFinalAnalises);
		// Adiciona a lista de regras na operação salvar do mapa de regras de negócio da solicitacao
		rnsAnalyze.put("VISUALIZAR", rnsVisualizarConsole);
		// Adiciona o mapa com as regras indexadas pelas operações no mapa geral indexado 
		// pelo nome da entidade.
		rns.put(Console.class.getName(), rnsConsole);
		
		// ========================================================================
		// Criando uma lista para conter as regras de negócio de solicitacao
		// quando a operação for salvar
		// ========================================================================
		List<IStrategy> rnsSalvarAgendamento = new ArrayList<IStrategy>();
		// Adicionando as regras a serem utilizadas na operação salvar da solicitação
		rnsSalvarAgendamento.add(complStatusSolicitacao);
		rnsSalvarAgendamento.add(valIdSolicitacao);
		rnsSalvarAgendamento.add(valAgendamentoSolicitacao);
		rnsSalvarAgendamento.add(valStatusSolicitacao);
		rnsSalvarAgendamento.add(valClienteSolicitacao);
		rnsSalvarAgendamento.add(valBancoSolicitacao);
		rnsSalvarAgendamento.add(valOwnerSolicitacao);
		rnsSalvarAgendamento.add(valTabelasSolicitacao);
		rnsSalvarAgendamento.add(compDtCadastro);
		rnsSalvarAgendamento.add(complMetadadosAgend);
		rnsSalvarAgendamento.add(complOwnerAgendamento);
		rnsSalvarAgendamento.add(complTabelaAgendamento);
		rnsSalvarAgendamento.add(complTabelaAgendamentoId);
		// Adiciona a lista de regras na operação salvar do mapa de regras de negócio da solicitacao
		rnsAgendamento.put("SALVAR", rnsSalvarAgendamento);
		// Adiciona o mapa com as regras indexadas pelas operações no mapa geral indexado 
		// pelo nome da entidade.
		rns.put(AgendamentoReorg.class.getName(), rnsAgendamento);
		
		// ========================================================================
		// Criando uma lista para conter as regras de negócio de solicitacao
		// quando a operação for consultar
		// ========================================================================
		List<IStrategy> rnsConsultarAgendamento = new ArrayList<IStrategy>();
		
		// Adicionando as regras a serem utilizadas na operação consultar
		rnsConsultarAgendamento.add(complStatusSolicitacao);
		rnsConsultarAgendamento.add(complInfosSol);
		rnsConsultarAgendamento.add(complClientesSol);
		rnsConsultarAgendamento.add(complBancosSol);
		rnsConsultarAgendamento.add(complOwnersSol);
		rnsConsultarAgendamento.add(complTabelasSol);
		rnsConsultarAgendamento.add(complMetadadosSol);
		// Cria o mapa que poderá conter todas as listas de regras de negócio específica 
		// por operação da solicitacao
		// Map<String, List<IStrategy>> rnsSolicitacao = new HashMap<String, List<IStrategy>>();
		// Adiciona a lista de regras na operação consultar do mapa de regras de negócio da solicitacao
		rnsAgendamento.put("CONSULTAR", rnsConsultarAgendamento);
		// Adiciona o mapa com as regras indexadas pelas operações no mapa geral indexado 
		// pelo nome da entidade.
		rns.put(AgendamentoReorg.class.getName(), rnsAgendamento);
		
		// ========================================================================
		// Criando uma lista para conter as regras de negócio de solicitacao
		// quando a operação for visualizar
		// ========================================================================
		List<IStrategy> rnsVisualizarAgendamento = new ArrayList<IStrategy>();
		// Adicionando as regras a serem utilizadas na operação consultar
		/*rnsVisualizarSolicitacao.add(complInfosSol);
		rnsVisualizarSolicitacao.add(complClientesSol);
		rnsVisualizarSolicitacao.add(complBancosSol);
		rnsVisualizarSolicitacao.add(complOwnersSol);
		rnsVisualizarSolicitacao.add(complTabelasSol);
		rnsVisualizarSolicitacao.add(complMetadadosSol);*/
		// Cria o mapa que poderá conter todas as listas de regras de negócio específica 
		// por operação da solicitacao
		// Map<String, List<IStrategy>> rnsSolicitacao = new HashMap<String, List<IStrategy>>();
		// Adiciona a lista de regras na operação consultar do mapa de regras de negócio da solicitacao
		rnsAgendamento.put("VISUALIZAR", rnsVisualizarAgendamento);
		// Adiciona o mapa com as regras indexadas pelas operações no mapa geral indexado 
		// pelo nome da entidade.
		rns.put(AgendamentoReorg.class.getName(), rnsAgendamento);
		
		// ========================================================================
		// Criando uma lista para conter as regras de negócio de solicitacao
		// quando a operação for alterar
		// ========================================================================
		List<IStrategy> rnsAlterarAgendamento = new ArrayList<IStrategy>();
		// Adicionando as regras a serem utilizadas na operação alterar da solicitacao
		rnsAlterarAgendamento.add(compDtCadastro);
		rnsAlterarAgendamento.add(complInfosSol);
		rnsAlterarAgendamento.add(complClientesSol);
		rnsAlterarAgendamento.add(complBancosSol);
		rnsAlterarAgendamento.add(complOwnersSol);
		rnsAlterarAgendamento.add(complTabelasSol);
		rnsAlterarAgendamento.add(complMetadadosSol);
		rnsAlterarAgendamento.add(valIdSolicitacao);
		rnsAlterarAgendamento.add(vPosCancel);
		rnsAlterarAgendamento.add(travExec);
		
		// Cria o mapa que poderá conter todas as listas de regras de negócio específica 
		// por operação da solicitacao
		// Map<String, List<IStrategy>> rnsSolicitacao = new HashMap<String, List<IStrategy>>();
		// Adiciona a lista de regras na operação alterar do mapa de regras de negócio da solicitacao
		rnsAgendamento.put("ALTERAR", rnsAlterarAgendamento);
		
		// ========================================================================
		// Criando uma lista para conter as regras de negócio de solicitacao
		// quando a operação for cancelar
		// ========================================================================
		List<IStrategy> rnsCancelarAgendamento = new ArrayList<IStrategy>();
		// Adicionando as regras a serem utilizadas na operação cancelar da solicitacao
		rnsCancelarAgendamento.add(vPosCancel);
		// Adiciona a lista de regras na operação alterar no mapa de regras de negócio da solicitacao
		rnsAgendamento.put("CANCELAR", rnsCancelarAgendamento);
		// Adiciona o mapa com as regras indexadas pelas operações no mapa geral indexado 
		// pelo nome da entidade
		rns.put(AgendamentoReorg.class.getName(), rnsAgendamento);
		
//		========================================================================
//		Criando uma lista para conter as regras de negócio de LOGIN
//		quando a operação for CONSULTAR
//		========================================================================
		List<IStrategy> rnsConsultarLogin = new ArrayList<IStrategy>();
		// Adicionando as regras a serem utilizadas na operação consultar do Login
		rnsConsultarLogin.add(valDadosLogin);
		rnsConsultarLogin.add(valUsuario);
		rnsConsultarLogin.add(compUsuario);
		// Adiciona a lista de regras na operação alterar no mapa de regras de negócio do Login
		rnsLogin.put("CONSULTAR", rnsConsultarLogin);
		// Adiciona o mapa com as regras indexadas pelas operações no mapa geral indexado 
		// pelo nome da entidade
		rns.put(Login.class.getName(), rnsLogin);
		
//		========================================================================
//		Criando uma lista para conter as regras de negócio de LOGIN
//		quando a operação for EXCLUIR (logout)
//		========================================================================
		List<IStrategy> rnsExcluirLogin = new ArrayList<IStrategy>();
		// Adicionando as regras a serem utilizadas na operação consultar do Login
		rnsExcluirLogin.add(efetLogoff);
		// Adiciona a lista de regras na operação alterar no mapa de regras de negócio do Login
		rnsLogin.put("EXCLUIR", rnsExcluirLogin);
		// Adiciona o mapa com as regras indexadas pelas operações no mapa geral indexado 
		// pelo nome da entidade
		rns.put(Login.class.getName(), rnsLogin);
		
	} // fim-construtor
	
	@Override
	public Resultado salvar(EntidadeDominio entidade) {
		
		// entidade de retorno
		resultado = new Resultado();
		List<EntidadeDominio> entidades = new ArrayList<EntidadeDominio>();
		// nome da classe é a chave do mapa de DAOs
		String nmClasse = entidade.getClass().getName();	
		String msg = executarRegras(entidade, "SALVAR");
		if(msg == null){
			IDAO dao = daos.get(nmClasse);
			try {
				dao.salvar(entidade);
				entidades.add(entidade);
				resultado.setEntidades(entidades);
			} catch (SQLException e) {
				e.printStackTrace();
				resultado.setMsg("Não foi possível realizar o registro!");
				
			}
		}else{
			resultado.setMsg(msg);
		}
		
		return resultado;
		
	} //fim-salvar()

	@Override
	public Resultado alterar(EntidadeDominio entidade) {
		
		resultado = new Resultado();
		String nmClasse = entidade.getClass().getName();
		String msg = executarRegras(entidade, "ALTERAR");
		if(msg == null){
			IDAO dao = daos.get(nmClasse);
			try {
				dao.alterar(entidade);
				List<EntidadeDominio> entidades = new ArrayList<EntidadeDominio>();
				entidades.add(entidade);
				resultado.setEntidades(entidades);
			} catch (SQLException e) {
				e.printStackTrace();
				resultado.setMsg("Não foi possível realizar o registro!");
			}
		}else{
			resultado.setMsg(msg);
		}
		return resultado;
		
	}

	@Override
	public Resultado excluir(EntidadeDominio entidade) {
		
		resultado = new Resultado();
		String nmClasse = entidade.getClass().getName();
		String msg = executarRegras(entidade, "EXCLUIR");
		if(msg == null){
			IDAO dao = daos.get(nmClasse);
			try {
				dao.excluir(entidade);
				List<EntidadeDominio> entidades = new ArrayList<EntidadeDominio>();
				entidades.add(entidade);
				resultado.setEntidades(entidades);
			} catch (SQLException e) {
				e.printStackTrace();
				resultado.setMsg("Não foi possível realizar o registro!");
			}
		}else{
			resultado.setMsg(msg);
		}
		return resultado;
		
	}

	@Override
	public Resultado consultar(EntidadeDominio entidade) {
		
		// entidade de retorno
		resultado = new Resultado();
		// nome da classe é a chave do mapa de DAOS
		String nmClasse = entidade.getClass().getName();
		// executar as regras de negócio da operação de consulta
		String msg = executarRegras(entidade, "CONSULTAR"); // executa as regras de negócio através das Strategies
		if(msg == null){ // nenhum erro nas strategies?
			IDAO dao = daos.get(nmClasse); // pega o DAO correto
			if (dao != null){
				try {
					resultado.setEntidades(dao.consultar(entidade)); // acessa os dados através do DAO
				}catch (SQLException e) {
					e.printStackTrace();
					resultado.setMsg("DAO ERROR! Não foi possível realizar a consulta no banco! "  + this.getClass().getSimpleName());
				}
			}else{
				List<EntidadeDominio> entidades = new ArrayList<EntidadeDominio>();
				entidades.add(entidade);
				resultado.setEntidades(entidades);
			}
		}else{
			resultado.setMsg(msg);
		}
		
		return resultado;
		
	} //fim-consultar()
	
	@Override
	public Resultado visualizar(EntidadeDominio entidade) {
		
		resultado = new Resultado();
		String nmClasse = entidade.getClass().getName();
		String msg = executarRegras(entidade, "VISUALIZAR");
		if(msg == null){
			IDAO dao = daos.get(nmClasse);
			try {
				resultado.setEntidades(dao.visualizar(entidade));
				if (resultado.getEntidades().isEmpty())
					resultado.setMsg(this.getClass().getCanonicalName() +  "': "
							+ "Nenhum resultado obtido!&");
			} catch (SQLException e) {
				e.printStackTrace();
				resultado.setMsg("Não foi possível realizar a consulta!");
			}
		}else{
			resultado.setMsg(msg);
		}
		return resultado;
		/*
		resultado = new Resultado();
		resultado.setEntidades(new ArrayList<EntidadeDominio>());
		resultado.getEntidades().add(entidade);		
		return resultado;
		*/
		
	}
	
	private String executarRegras(EntidadeDominio entidade, String operacao){
		
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
		if(msg.length() > 0)
			return msg.toString();
		else
			return null;
		
	} // fim-executarRegras

	@Override
	public Resultado cancelar(EntidadeDominio entidade) {
		
		resultado = new Resultado();
		String nmClasse = entidade.getClass().getName();
		String msg = executarRegras(entidade, "CANCELAR");
		if(msg == null){
			IDAO dao = daos.get(nmClasse);
			try {
				dao.alterar(entidade);
				List<EntidadeDominio> eds = new ArrayList<EntidadeDominio>();
				eds.add(entidade);
				resultado.setEntidades(eds);
			} catch (SQLException e) {
				e.printStackTrace();
				resultado.setMsg("Não foi possível realizar o registro!");
			}
		}else{
			resultado.setMsg(msg);
		}
		return resultado;
		
	}
	
} // fim-classe

