
package rms.controle.web.vh;

import javax.servlet.ServletContextEvent;
import rms.dominio.EntidadeDominio;


public interface IViewHelperReorg extends IViewHelper {

	public EntidadeDominio getEntidade(ServletContextEvent event);
	
}
