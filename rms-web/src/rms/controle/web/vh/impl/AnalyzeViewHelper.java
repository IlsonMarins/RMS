
package rms.controle.web.vh.impl;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rms.controle.web.vh.IViewHelper;
import rms.core.aplicacao.Resultado;
import rms.core.util.ConverteDate;
import rms.core.util.ValidaPadraoDt;
import rms.dominio.EntidadeDominio;
import rms.dominio.Analyze;



public class AnalyzeViewHelper implements IViewHelper {

	@Override
	public EntidadeDominio getEntidade(HttpServletRequest request) {
		//String operacao = request.getParameter("operacao");
		String txtDtInicial = request.getParameter("txtDtInicial");
		String txtDtFinal = request.getParameter("txtDtFinal");
		Analyze analyze = new Analyze();
		if (txtDtInicial != null && !txtDtInicial.trim().equals("")){
			Date dtInicial = null;
			if (ValidaPadraoDt.checkDatePattern("dd/MM/yy", txtDtInicial))
				dtInicial = ConverteDate.converteStringDate(txtDtInicial, "dd/MM/yy");
			analyze.setDtInicial(dtInicial);
		}
		if (txtDtFinal != null && !txtDtFinal.trim().equals("")){
			Date dtFinal = null;
			if (ValidaPadraoDt.checkDatePattern("dd/MM/yy", txtDtFinal))
				dtFinal = ConverteDate.converteStringDate(txtDtFinal, "dd/MM/yy");
			analyze.setDtFinal(dtFinal);
		}
		return analyze;
	} // fim-getEntidade()

	@Override
	public void setView(Resultado resultado, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String view = "Analises.jsp";
		request.setAttribute("resultado", resultado);
		request.getRequestDispatcher(view).forward(request, response);
	}
} // fim-classe
