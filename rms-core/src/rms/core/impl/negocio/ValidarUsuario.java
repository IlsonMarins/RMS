package rms.core.impl.negocio;

import java.sql.SQLException;
import java.util.List;

import rms.core.IDAO;
import rms.core.IStrategy;
import rms.core.impl.dao.FuncionarioDAO;
import rms.core.impl.dao.RegistroDAO;
import rms.dominio.*;

public class ValidarUsuario implements IStrategy {

	@Override
	public String processar(EntidadeDominio entidade) {
		
		List<EntidadeDominio> entidades = null;
		Login login = null;
		IDAO registroDAO = new RegistroDAO();
		
		if (entidade instanceof Login)
			login = (Login)entidade;
		else
			return "FATAL ERROR! Entidade recebida diferente de um Registro! " + this.getClass().getName();
		try { // executar a consulta
			entidades = registroDAO.consultar(login.getRegistro()); // traz o registro caso login e senha estejam corretos
			if (entidades != null && entidades.size() == 1){ // confirmar que houve retorno do registro correto
				login.setRegistro((Registro)entidades.get(0));
				if (!login.getRegistro().getFlgAtivo())
					return "LOGIN ERROR! Usuário com acesso bloqueado! " + this.getClass().getName();
			}else{
				login.setRegistro(null);
				return "LOGIN ERROR! Login ou senha incorretos! " + this.getClass().getName();
			}
		}catch (SQLException e){
			e.printStackTrace();
			return "DAO ERROR! Não foi possível realizar a consulta no banco! " + this.getClass().getName();
		}
		
		return null;
		
	} //fim-processar
	
} //fim-classe
