
package rms.controle.web.vh.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rms.controle.web.vh.IViewHelper;
import rms.core.aplicacao.Resultado;
import rms.dominio.*;



public class MetadadoViewHelper implements IViewHelper {

	@Override
	public EntidadeDominio getEntidade(HttpServletRequest request) {
		// entidade de retorno
		EntidadeDominio entidade = new EntidadeDominio();
		// valores da tela
//		String cliId = request.getParameter("clienteId");
//		String banId = request.getParameter("bancoId");
//		String ownId = request.getParameter("ownerId");
		String tabId = request.getParameter("tabelaId");
		// objetos necessários
		Metadado metadado = new Metadado();
		metadado.setId(Integer.parseInt(tabId));
		entidade = metadado;
		return entidade;
	} // fim-getEntidade()

	@Override
	public void setView(Resultado resultado, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String view = "Analyze.jsp";
		request.setAttribute("resultado", resultado);
		request.getRequestDispatcher(view).forward(request, response);
	}
	
} // fim-classe
