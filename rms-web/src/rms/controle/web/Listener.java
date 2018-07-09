package rms.controle.web;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServletRequest;

import rms.controle.web.command.ICommand;
import rms.controle.web.command.impl.ExecutarCommand;
import rms.controle.web.vh.IViewHelper;
import rms.controle.web.vh.impl.ReorgViewHelper;
import rms.core.impl.controle.FachadaReorg;
import rms.dominio.EntidadeDominio;

public class Listener implements ServletContextListener {
	
	// atributos privados	
	private static Map<String, ICommand> commands;
	private static Map<String, IViewHelper> vhs;
	
	// Default constructor
    public Listener() {
    	
    	/* Utilizando o command para chamar a fachada e indexando cada command 
    	 * pela operação garantimos que este listener atenderá qualquer operação
    	*/
    	commands = new HashMap<String, ICommand>();
    	commands.put("EXECUTAR", new ExecutarCommand());
    	
    	/* Utilizando o ViewHelper para tratar especificações de qualquer tela e indexando 
    	 * cada viewhelper pela chave em que este listener é chamado no contexto
    	 * garantimos que este listener atenderá qualquer entidade
    	*/
    	vhs = new HashMap<String, IViewHelper>();
    	vhs.put("/rms-web/ExecutarReorg", new ReorgViewHelper());
    } //fim-construtor_default
	
    // métodos sobrescritos do ServletContextListener
	@Override
	public void contextInitialized(ServletContextEvent event) {
		// Obtêm a chave que invocou esta servlet (O que foi definido no method do form html)
		// está estático, pois o 'event' não tem URI como o 'request' tem
		String vhKey = "/rms-web/ExecutarReorg";
		// Obtêm um viewhelper indexado pela uri que invocou esta servlet
		IViewHelper vh = vhs.get(vhKey);
		// O viewhelper retorna a entidade especifica para a tela que chamou esta servlet
		HttpServletRequest request = null;
//		HttpServletResponse response = null;
		//request.setAttribute("operacao", "run");
		// pega a entidade na VH
		EntidadeDominio entidade =  vh.getEntidade(request);
		// Obtem a operação executada
		// está estático, pois o 'event' não tem parâmetros como o 'request' tem
		String operacao = "EXECUTAR";
		//Obtêm o command para executar a respectiva operação
		ICommand command = commands.get(operacao);
		/*Executa o command que chamará a fachada para executar a operação requisitada
		 * o retorno é uma instância da classe resultado que pode conter mensagens de erro 
		 * ou entidades de retorno
		 */
		// executa o Command, que retorna com a task a ser executada
		FachadaReorg fachadaReorg = ((ExecutarCommand) command).executar(entidade);
		/*FachadaReorg fachadaReorg = new FachadaReorg();
		Resultado resultado = command.execute(entidade);
		try {
			vh.setView(resultado, request, response);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServletException e) {
			e.printStackTrace();
		}*/
		/*
		 * define o cronograma de agendamento de tempo a ser pingada a task -
		 * que está definida na classe FachadaReorg através do método run()
		 */
		// objeto que usa threads internamente para o tratamento das execuções dos Reorgs
		ServletContext servletContext = event.getServletContext();
		Timer timer = new Timer();
		// executa o run() da FachadaReorg.
		// Parâmetros: (fachadaReorg, inicializa agora, repete a cada 1 minuto)
		timer.schedule(fachadaReorg, 0, 60000);
		servletContext.setAttribute("timer", timer);
		System.out.println("\nLISTENER - Listener iniciado com sucesso!\n");
	} // fim-contextInitialized

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		ServletContext servletContext = event.getServletContext();
		Timer timer = (Timer)servletContext.getAttribute("timer");
		if (timer != null)
		   timer.cancel();
		servletContext.removeAttribute("timer");
		System.out.println("\nLISTENER - Listener finalizado com sucesso!\n");
	} // fim-contextDestroyed
	
} // fim-classe
