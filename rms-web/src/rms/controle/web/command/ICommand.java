
package rms.controle.web.command;

import rms.core.aplicacao.Resultado;
import rms.dominio.EntidadeDominio;

/*
 * Interface é um "contrato" onde quem o implementa é obrigado a cumprir com suas exigências.
 * 
 * A interface, diferentemente da classe abstrata (que não pode ser instanciada e contém métodos
 * abstratos e também métodos concretos, fazendo com que fiquem sempre disponíveis na memória),
 * pode sim ser instanciada, entretanto, jamais pode implementar um método, somente os definir; declará-los -
 * Exatamente como num contrato de obrigação para com as partes que o assinam.
 */
public interface ICommand {

	public Resultado execute(EntidadeDominio entidade);
	
}
