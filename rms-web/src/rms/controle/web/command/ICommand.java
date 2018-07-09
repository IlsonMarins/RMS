
package rms.controle.web.command;

import rms.core.aplicacao.Resultado;
import rms.dominio.EntidadeDominio;

/*
 * Interface � um "contrato" onde quem o implementa � obrigado a cumprir com suas exig�ncias.
 * 
 * A interface, diferentemente da classe abstrata (que n�o pode ser instanciada e cont�m m�todos
 * abstratos e tamb�m m�todos concretos, fazendo com que fiquem sempre dispon�veis na mem�ria),
 * pode sim ser instanciada, entretanto, jamais pode implementar um m�todo, somente os definir; declar�-los -
 * Exatamente como num contrato de obriga��o para com as partes que o assinam.
 */
public interface ICommand {

	public Resultado execute(EntidadeDominio entidade);
	
}
