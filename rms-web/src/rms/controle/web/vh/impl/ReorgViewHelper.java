
package rms.controle.web.vh.impl;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rms.controle.web.vh.IViewHelper;
import rms.core.aplicacao.Resultado;
import rms.dominio.EntidadeDominio;
import rms.dominio.ExecucaoReorg;



public class ReorgViewHelper implements IViewHelper {

	@Override
	public EntidadeDominio getEntidade(HttpServletRequest request) {
		ExecucaoReorg execucaoReorg = new ExecucaoReorg();
		
		return execucaoReorg;
	} // fim-getEntidade()

	@Override
	public void setView(Resultado resultado, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		
	}
	
} // fim-classe
