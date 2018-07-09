
package rms.controle.web.command.impl;

import rms.core.aplicacao.Resultado;
import rms.dominio.EntidadeDominio;


public class SalvarCommand extends AbstractCommand{

	
	public Resultado execute(EntidadeDominio entidade) {
		
		/*
		 * retorna uma entidade de Resultado que vem do método salvar da Fachada,
		 * instanciada no AbstractCommand e consequentemente herdada a partir do extends desta classe (SalvarCommand).
		 */
		return fachada.salvar(entidade);
	}

}
