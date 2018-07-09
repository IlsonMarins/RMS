
package rms.controle.web.command.impl;

import rms.controle.web.command.ICommand;
import rms.core.IFachada;
import rms.core.impl.controle.Fachada;


/*
 * Classe abstrata que n�o pode ser instanciada, sendo chamada diretamente, por refer�ncia.
 * Caso houvesse um m�todo concreto, seria chamado diretamente. Exemplo: "AbstractCommand.metodoConcreto".
 * 
 * � por isso que a fachada fica est�tica na mem�ria para todas as classes que utilizam Command, afinal,
 * os Commands s�o subclasses de AbstractCommand.
 */
public abstract class AbstractCommand implements ICommand {
	
	protected IFachada fachada = new Fachada();

}
