
package rms.controle.web.command.impl;

import rms.core.aplicacao.Resultado;
import rms.dominio.EntidadeDominio;

public class AlterarCommand extends AbstractCommand{
	public Resultado execute(EntidadeDominio entidade) {
		return fachada.alterar(entidade);
	}
}
