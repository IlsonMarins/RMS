
package rms.controle.web.command.impl;

import rms.core.aplicacao.Resultado;
import rms.dominio.EntidadeDominio;


public class ConsultarCommand extends AbstractCommand{
	
	// Métodos públicos
	public Resultado execute(EntidadeDominio entidade) {
		
		return fachada.consultar(entidade);
		
	}

}
