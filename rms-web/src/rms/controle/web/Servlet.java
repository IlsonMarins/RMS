package rms.controle.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rms.controle.web.command.*;
import rms.controle.web.command.impl.*;
import rms.controle.web.vh.*;
import rms.controle.web.vh.impl.*;
import rms.core.aplicacao.Resultado;
import rms.dominio.*;

//Servlet implementation class Servlet
public class Servlet extends HttpServlet {
	// atributos privados
	private static final long serialVersionUID = 1L;
	
	private static Map<String, ICommand> commands;
	private static Map<String, IViewHelper> vhs;
	
    // Default constructor
    public Servlet() {
    	// ################################ ################################    	
    	// Utilizando o command para chamar a fachada e indexando cada command 
    	// pela operação garantimos que esta servelt atenderá qualquer operação
		// ################################ ################################    	
    	commands = new HashMap<String, ICommand>();
    	
    	commands.put("SALVAR", new SalvarCommand());
    	commands.put("EXCLUIR", new ExcluirCommand());
    	commands.put("CONSULTAR", new ConsultarCommand());
    	commands.put("VISUALIZAR", new VisualizarCommand());
    	commands.put("ALTERAR", new AlterarCommand());
    	commands.put("CANCELAR", new CancelarCommand());
    	
		// ################################ ################################    	
    	// Utilizando o ViewHelper para tratar especificações de qualquer tela e indexando 
    	// cada viewhelper pela url em que esta servlet é chamada no form
    	// garantimos que esta servlet atenderá qualquer entidade
    	
    	// A chave do mapa é o mapeamento da servlet para cada form que
    	// está configurado no web.xml e sendo utilizada no action do html
		// ################################ ################################
    	vhs = new HashMap<String, IViewHelper>();
    	vhs.put("/rms-web/SalvarSolicitacao", new AgendamentoViewHelper());
    	vhs.put("/rms-web/AlterarAgendamento", new SolicitacaoViewHelper());
    	vhs.put("/rms-web/ConsultarSolicitacao", new SolicitacaoViewHelper());
    	vhs.put("/rms-web/VisualizarSolicitacao", new SolicitacaoViewHelper());
    	vhs.put("/rms-web/ExcluirSolicitacao", new SolicitacaoViewHelper());
    	vhs.put("/rms-web/AlterarSolicitacao", new SolicitacaoViewHelper());
    	vhs.put("/rms-web/VisualizarAnalises", new AnalyzeViewHelper());
    	vhs.put("/rms-web/ConsultarAnalises", new AnalyzeViewHelper());
    	vhs.put("/rms-web/ConsultarMetadado", new MetadadoViewHelper());
    	vhs.put("/rms-web/VisualizarConsole", new ConsoleViewHelper());
    	vhs.put("/rms-web/AlterarConsole", new ConsoleViewHelper());
    	vhs.put("/rms-web/ConsultarLog", new LogViewHelper());
    	vhs.put("/rms-web/Login", new LoginViewHelper());
    	System.out.println("\nServlet iniciada com sucesso!\n");
    } // fim-construtor_default
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
    		IOException {
    	// chama doProcessRequest()
    	doProcessRequest(request, response);
    } // fim-doGet()
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// chama doProcessRequest()		
		doProcessRequest(request, response);
	} // fim-doPost()
	
	protected void doProcessRequest(HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException {
		
		// Obtêm a uri que invocou esta servlet (o que foi definido no method do form html)
		String uri = request.getRequestURI();
		
		// Obtém um viewhelper indexado pela uri que invocou esta servlet
		IViewHelper vh = vhs.get(uri);
		
		// O viewhelper retorna a entidade especifica para a tela que chamou esta servlet
		EntidadeDominio entidade =  vh.getEntidade(request);
		
		// Obtem a operação executada
		String operacao = request.getParameter("operacao");
		
		// Obtêm o command para executar a respectiva operação, chamando o respectivo método da Fachada
		ICommand command = commands.get(operacao);
				
		// ################################ ################################
		// Executa o command que chamará a fachada para executar a operação requisitada
		// o retorno é uma instância da classe resultado que pode conter mensagens de erro 
		// ou entidades de retorno
		// ################################ ################################
		Resultado resultado = command.execute(entidade);
		
		// ################################ ################################
		// Executa o método setView do view helper específico para definir como deverá
		// ser apresentado o resultado para o usuário
		// ################################ ################################
		vh.setView(resultado, request, response);
		
	} // fim-doProcessRequest()
	
} // fim-class
