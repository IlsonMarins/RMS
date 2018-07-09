
package rms.controle.web.vh.impl;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import rms.controle.web.vh.IViewHelper;
import rms.core.aplicacao.Resultado;
import rms.dominio.EntidadeDominio;
import rms.dominio.Login;
import rms.dominio.Pessoa;
import rms.dominio.Registro;



public class LoginViewHelper implements IViewHelper {

	@Override
	public EntidadeDominio getEntidade(HttpServletRequest request) {
		
		Login login = new Login();
		Registro registro = new Registro();
		String strLogin = null;
		String strSenha = null;
		if (request.getParameter("txtLogin") != null)
			strLogin = request.getParameter("txtLogin").toUpperCase();
		if (request.getParameter("txtSenha") != null)
			strSenha = request.getParameter("txtSenha");
		
		if(strLogin != null && !strLogin.trim().equals("")){
			registro.setUsuario(strLogin);
		}
		if(strSenha != null && !strSenha.trim().equals("")){
			registro.setSenha(strSenha);
		}
		
		login.setRegistro(registro);
		return login;
		
	} // fim-getEntidade()

	@Override
	public void setView(Resultado resultado, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		RequestDispatcher disp = null;
		HttpSession sessao = null;
		String operacao = request.getParameter("operacao");
		String view = "Login.jsp";
		sessao = request.getSession(false); // caso não tenha sessao na requisicao, NAO crie uma nova.
		
		boolean temMensagem, temEntidades, temResultado;
		
		temResultado = resultado != null;
		temEntidades = temResultado && (resultado.getEntidades() != null && resultado.getEntidades().size() > 0);
		temMensagem = temResultado && (resultado.getMsg() != null && !resultado.getMsg().equals(""));
		
		disp = request.getRequestDispatcher(view);
		
		// usuário já logado e não é logout?
		if(sessao != null && sessao.getAttribute("login") != null && (operacao == null || !operacao.equals("EXCLUIR"))){
			//disp = request.getRequestDispatcher("/WEB-INF/jsp/index.jsp"); // outra possibilidade, mas com o mesmo contexto
			response.sendRedirect("/rms-web/index.jsp");
			return;
		}else if(operacao.equals("EXCLUIR")){ // é logout?
			request.setAttribute("login", null);
			request.getSession().invalidate();
		}else if(!temMensagem && temEntidades){ // fazer login?
			if(operacao.equals("CONSULTAR")){
				sessao = request.getSession(true); // crie uma sessao caso nao exista na requisição
				if(!sessao.isNew()){ // garantir que a sessão seja nova ou renovada
					sessao.invalidate();
					sessao = request.getSession(true);
				}
				if(temEntidades && resultado.getEntidades().get(0) instanceof Login){ // garantir que o login esteja correto
					sessao.setAttribute("login", resultado.getEntidades().get(0));
					//disp = request.getRequestDispatcher("/WEB-INF/jsp/index.jsp"); // outra possibilidade, mas com o mesmo contexto
					response.sendRedirect("/rms-web/index.jsp");
					return;
				}else{
					resultado.setMsg(resultado.getMsg() + "\nFATAL ERROR! Entidade recebida diferente de Login! " + this.getClass().getSimpleName());
					request.setAttribute("resultado", resultado);
					disp.forward(request, response);
					return;
				}
			}
		}else if(temMensagem){ // houve algum erro?
			if(operacao.equals("CONSULTAR")){ // foi no login?
				request.setAttribute("resultado", resultado);
			} 			
		}
		
		disp.forward(request, response);
		
	}
	
} // fim-classe
