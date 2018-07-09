
package rms.controle.web.vh.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rms.controle.web.vh.IViewHelper;
import rms.core.aplicacao.Resultado;
import rms.dominio.*;



public class ClienteViewHelper implements IViewHelper {

	@Override
	public EntidadeDominio getEntidade(HttpServletRequest request) {
		// entidade de retorno
		EntidadeDominio entidade = new EntidadeDominio();
		// valores da tela
		String cliId = request.getParameter("clienteId");
		String banId = request.getParameter("bancoId");
		String ownId = request.getParameter("ownerId");
		String tabId = request.getParameter("tabelaId");
		// objetos necessários
		Cliente cliente = new Cliente();
		Banco banco = new Banco();
		Owner owner = new Owner();
		Tabela tabela = new Tabela();
		// preencher os valores nos objetos
		cliente.setId(Integer.parseInt(cliId));
		banco.setId(Integer.parseInt(banId));
		owner.setId(Integer.parseInt(ownId));
		tabela.setId(Integer.parseInt(tabId));
		// preencher as tabelas do owner
		List<Tabela> tabelas = new ArrayList<Tabela>();
		tabelas.add(tabela);
		owner.setTabelas(tabelas);
		// preencher os owners do banco
		List<Owner> owners = new ArrayList<Owner>();
		owners.add(owner);
		banco.setOwners(owners);
		// preencher os bancos do cliente
		List<Banco> bancos = new ArrayList<Banco>();
		bancos.add(banco);
		cliente.setBancos(bancos);
		// retornar com a entidade preenchida
		entidade = cliente;
		return entidade;
	} // fim-getEntidade()

	@Override
	public void setView(Resultado resultado, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String view = "Analyze.jsp";
		request.setAttribute("resultado", resultado);
		request.getRequestDispatcher(view).forward(request, response);
	}
	
} // fim-classe
