package rms.core;

import rms.core.aplicacao.Resultado;
import rms.dominio.EntidadeDominio;

public interface IFachada {

	// funcionalidades padr�o
	public Resultado salvar(EntidadeDominio entidade);
	public Resultado alterar(EntidadeDominio entidade);
	public Resultado excluir(EntidadeDominio entidade);
	public Resultado consultar(EntidadeDominio entidade);
	public Resultado visualizar(EntidadeDominio entidade);

	// funcionalidades espec�ficas
	public Resultado cancelar(EntidadeDominio entidade);
	
	
}
