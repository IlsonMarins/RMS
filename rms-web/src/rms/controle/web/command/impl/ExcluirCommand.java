
package rms.controle.web.command.impl;

import rms.core.aplicacao.Resultado;
import rms.dominio.EntidadeDominio;


public class ExcluirCommand extends AbstractCommand{

	
	public Resultado execute(EntidadeDominio entidade) {
		
		return fachada.excluir(entidade);
	}

}
