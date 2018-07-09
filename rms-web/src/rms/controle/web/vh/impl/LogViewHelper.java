
package rms.controle.web.vh.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rms.controle.web.vh.IViewHelper;
import rms.core.aplicacao.Resultado;
import rms.dominio.EntidadeDominio;
import rms.dominio.Log;



public class LogViewHelper implements IViewHelper {

	@Override
	public EntidadeDominio getEntidade(HttpServletRequest request) {
		Log log = new Log();
		return log;
	} // fim-getEntidade()

	@Override
	public void setView(Resultado resultado, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String view = "Log.jsp";
		request.setAttribute("resultado", resultado);
		request.getRequestDispatcher(view).forward(request, response);
	}
} // fim-classe
