
package rms.controle.web.vh.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rms.controle.web.vh.IViewHelper;
import rms.core.aplicacao.Resultado;
import rms.dominio.*;



public class ConsoleViewHelper implements IViewHelper {

	@Override
	public EntidadeDominio getEntidade(HttpServletRequest request) {
		// entidade de retorno
		Console console = new Console();
		return console;
	} // fim-getEntidade()

	@Override
	public void setView(Resultado resultado, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String view = "Relatorios.jsp";
		request.setAttribute("resultado", resultado);
		request.getRequestDispatcher(view).forward(request, response);
	}
	
} // fim-classe
