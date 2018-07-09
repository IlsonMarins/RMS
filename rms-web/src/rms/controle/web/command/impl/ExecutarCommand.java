
package rms.controle.web.command.impl;


import rms.core.aplicacao.Resultado;
import rms.core.impl.controle.FachadaReorg;
import rms.dominio.EntidadeDominio;


public class ExecutarCommand extends AbstractCommand{
	
	// atributos privados
	private FachadaReorg fachadaReorg = new FachadaReorg();

	@Override
	public Resultado execute(EntidadeDominio entidade) {
		/*
		 * retorna uma entidade de Resultado que vem do método run() da FachadaReorg,
		 * instanciada no AbstractCommand e consequentemente herdada a partir do extends desta classe (ExecutarCommand).
		 */
		return fachadaReorg.executaRun();
	}
	
	public FachadaReorg executar (EntidadeDominio entidade){
		//fachadaReorg.setEntidade(entidade);
		return fachadaReorg;
	}

}
