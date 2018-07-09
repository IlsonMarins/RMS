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
    	 * pela opera��o garantimos que este listener atender� qualquer opera��o
    	*/
    	commands = new HashMap<String, ICommand>();
    	commands.put("EXECUTAR", new ExecutarCommand());
    	
    	/* Utilizando o ViewHelper para tratar especifica��es de qualquer tela e indexando 
    	 * cada viewhelper pela chave em que este listener � chamado no contexto
    	 * garantimos que este listener atender� qualquer entidade
    	*/
    	vhs = new HashMap<String, IViewHelper>();
    	vhs.put("/rms-web/ExecutarReorg", new ReorgViewHelper());
    } //fim-construtor_default
	
    // m�todos sobrescritos do ServletContextListener
	@Override
	public void contextInitialized(ServletContextEvent event) {
		// Obt�m a chave que invocou esta servlet (O que foi definido no method do form html)
		// est� est�tico, pois o 'event' n�o tem URI como o 'request' tem
		String vhKey = "/rms-web/ExecutarReorg";
		// Obt�m um viewhelper indexado pela uri que invocou esta servlet
		IViewHelper vh = vhs.get(vhKey);
		// O viewhelper retorna a entidade especifica para a tela que chamou esta servlet
		HttpServletRequest request = null;
//		HttpServletResponse response = null;
		//request.setAttribute("operacao", "run");
		// pega a entidade na VH
		EntidadeDominio entidade =  vh.getEntidade(request);
		// Obtem a opera��o executada
		// est� est�tico, pois o 'event' n�o tem par�metros como o 'request' tem
		String operacao = "EXECUTAR";
		//Obt�m o command para executar a respectiva opera��o
		ICommand command = commands.get(operacao);
		/*Executa o command que chamar� a fachada para executar a opera��o requisitada
		 * o retorno � uma inst�ncia da classe resultado que pode conter mensagens de erro 
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
		 * que est� definida na classe FachadaReorg atrav�s do m�todo run()
		 */
		// objeto que usa threads internamente para o tratamento das execu��es dos Reorgs
		ServletContext servletContext = event.getServletContext();
		Timer timer = new Timer();
		// executa o run() da FachadaReorg.
		// Par�metros: (fachadaReorg, inicializa agora, repete a cada 1 minuto)
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
