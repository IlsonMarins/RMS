package rms.core.impl.negocio;

import java.sql.SQLException;
import java.util.List;

import rms.core.IDAO;
import rms.core.IStrategy;
import rms.core.impl.dao.FuncionarioDAO;
import rms.dominio.EntidadeDominio;
import rms.dominio.*;

public class ComplementarUsuario implements IStrategy {

	@Override
	public String processar(EntidadeDominio entidade) {
		
		List<EntidadeDominio> entidades = null;
		Login login = null;
		IDAO funcionarioDAO = new FuncionarioDAO();
		
		if (entidade != null && entidade instanceof Login)
			login = (Login)entidade;
		else
			return "FATAL ERROR! Entidade recebida diferente de um Registro! " + this.getClass().getSimpleName();
		Pessoa func = new Funcionario();
		func.setRegistro((Registro)login.getRegistro()); // preencher o registro do funcion�rio
		try { // executar a consulta
			if (func.getRegistro() != null)
				entidades = funcionarioDAO.consultar(func);
			if (entidades != null && entidades.size() == 1) // confirmar que houve o retorno correto
				login.setUsuario((Pessoa)entidades.get(0));
			else
				login.setUsuario(null);
		}catch (SQLException e){
			e.printStackTrace();
			return "DAO ERROR! N�o foi poss�vel realizar a consulta no banco!" + this.getClass().getSimpleName();
		}
		
		return null; // n�o necess�rio retornar "ok" pois o Java trabalha por refer�ncia
		
	} //fim-processar
	
} //fim-classe
