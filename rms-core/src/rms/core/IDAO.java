package rms.core;

import java.sql.SQLException;
import java.util.List;

import rms.dominio.EntidadeDominio;

public interface IDAO {

	public void salvar(EntidadeDominio entidade) throws SQLException;
	public void alterar(EntidadeDominio entidade) throws SQLException;
	public void excluir(EntidadeDominio entidade) throws SQLException;
	public List<EntidadeDominio> visualizar(EntidadeDominio entidade) throws SQLException;
	public List<EntidadeDominio> consultar(EntidadeDominio entidade) throws SQLException;
}
