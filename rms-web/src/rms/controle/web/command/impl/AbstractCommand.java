
package rms.controle.web.command.impl;

import rms.controle.web.command.ICommand;
import rms.core.IFachada;
import rms.core.impl.controle.Fachada;


/*
 * Classe abstrata que não pode ser instanciada, sendo chamada diretamente, por referência.
 * Caso houvesse um método concreto, seria chamado diretamente. Exemplo: "AbstractCommand.metodoConcreto".
 * 
 * É por isso que a fachada fica estática na memória para todas as classes que utilizam Command, afinal,
 * os Commands são subclasses de AbstractCommand.
 */
public abstract class AbstractCommand implements ICommand {
	
	protected IFachada fachada = new Fachada();

}
