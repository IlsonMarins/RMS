
package rms.controle.web.command.impl;

import rms.core.aplicacao.Resultado;
import rms.dominio.EntidadeDominio;


public class VisualizarCommand extends AbstractCommand{

	
	public Resultado execute(EntidadeDominio entidade) {
		
		return fachada.visualizar(entidade);
	}

}
