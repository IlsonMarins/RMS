package rms.core.impl.negocio;

import rms.core.IStrategy;
import rms.dominio.EntidadeDominio;
import rms.dominio.Login;

public class ValidarDadosLogin implements IStrategy {

	@Override
	public String processar(EntidadeDominio entidade) {
		
		Login login = null;
		StringBuilder msg = null;
		if (entidade instanceof Login)
			login = (Login)entidade;
		else
			return "FATAL ERROR! Entidade recebida diferente de um Login! " + this.getClass().getSimpleName();
		
		String strLogin = login.getRegistro().getUsuario();
		String strSenha = login.getRegistro().getSenha();
		if (strLogin == null || strLogin.equals("")){ // campo login n�o preenchido?
			if (msg == null)
				msg = new StringBuilder();
			msg.append("Login n�o preenchido!");
			msg.append("\n");
		}
		if (strSenha == null || strSenha.equals("")){ // campo senha n�o preenchida?
			if (msg == null)
				msg = new StringBuilder();
			msg.append("Senha n�o preenchida!");
			msg.append("\n");
		}
		
		if (msg != null) // houve algum erro?
			return msg.toString();
		else
			return null;
		
	} //fim-processar
	
} //fim-classe
