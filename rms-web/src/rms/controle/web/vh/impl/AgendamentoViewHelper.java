
package rms.controle.web.vh.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import rms.controle.web.vh.IViewHelper;
import rms.core.aplicacao.Resultado;
import rms.core.util.ConverteDate;
import rms.core.util.ValidaPadraoDt;
import rms.dominio.*;



public class AgendamentoViewHelper implements IViewHelper {
	
	public EntidadeDominio getEntidade(HttpServletRequest request) {
		// valores da tela
		String parOperacao = request.getParameter("operacao");
		String txtSolId = request.getParameter("txtReqNumber");
		String txtDtAgendamento = request.getParameter("txtDtAgendada");
		String txtStatus = request.getParameter("txtStatus");
		String txtCliId = request.getParameter("cboCliente");
		String txtBanId = request.getParameter("cboBanco");
		String txtOwnNome = request.getParameter("cboOwner");
		String[] txtTbsNomes = request.getParameterValues("cboTabela");
		// entidade de retorno
		AgendamentoReorg agendamentoReorg = new AgendamentoReorg();
		// salvar nova solicitação de reorg?
		if (parOperacao != null && (parOperacao.equals("SALVAR"))){
			agendamentoReorg.setId(Integer.parseInt(txtSolId));
			// dtAgendada está preenchida?
			if (txtDtAgendamento != null && !txtDtAgendamento.trim().equals("")){
				Date dt = null;
				if (ValidaPadraoDt.checkDatePattern("dd/MM/yy HH:mm", txtDtAgendamento)){
					dt = ConverteDate.converteStringDate(txtDtAgendamento, "dd/MM/yy HH:mm");
				}
				agendamentoReorg.setDtAgendamento(dt);
			}
			// preencher os dados do formulário nos objetos do domínio
			agendamentoReorg.setStatus(txtStatus);
			Cliente cliente = new Cliente();
			if (txtCliId != null && !txtCliId.trim().equals(""))
				cliente.setId(Integer.parseInt(txtCliId));
			Banco banco = new Banco();
			if (txtBanId != null && !txtBanId.trim().equals(""))
				banco.setId(Integer.parseInt(txtBanId));
			List<Banco> bancos = new ArrayList<Banco>();
			bancos.add(banco);
			Owner owner = new Owner();
			if (txtOwnNome != null && !txtOwnNome.trim().equals(""))
				owner.setNomeOwner(txtOwnNome);
			List<Owner> owners = new ArrayList<Owner>();
			owners.add(owner);
			Tabela tabela = null;
			List<Tabela> tabelas = new ArrayList<Tabela>();
			if (txtTbsNomes != null){
				for (String txtTabNome : txtTbsNomes){
					tabela = new Tabela();
					if (txtTabNome != null && !txtTabNome.trim().equals("")){
						tabela.setNomeTabela(txtTabNome);
					}
					tabelas.add(tabela);
				}
			}else{
				tabela = new Tabela();
				tabelas.add(tabela);
			}
			owners.get(0).setTabelas(tabelas);
			bancos.get(0).setOwners(owners);
			cliente.setBancos(bancos);
			agendamentoReorg.setCliente(cliente);
		}else{ // erro - não é para salvar?!
			return null;
		}
		return agendamentoReorg;
	} //fim-getEntidade()

	public void setView(Resultado resultado, HttpServletRequest request, 
			HttpServletResponse response) {
		AgendamentoReorg agendamentoReorg = null;
		// houve algum erro?
		if (resultado.getMsg() != null){
			request.setAttribute("msgErro", resultado.getMsg());
			// devolve a requisição, com as alterações feitas
			RequestDispatcher disp = request.getRequestDispatcher("FormErros.jsp");
			try {
				disp.forward(request, response);
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		// houve resultado?
		if (!resultado.getEntidades().isEmpty()){
			agendamentoReorg = (AgendamentoReorg)resultado.getEntidades().get(0);
		}else{ // nao houve resultado!
			agendamentoReorg = new AgendamentoReorg();
			resultado.getEntidades().add(agendamentoReorg);
		}
		String operacao = request.getParameter("operacao");
		String contentType = request.getContentType();
		String gson = request.getParameter("json");
		if (operacao.equals("VISUALIZAR")
				|| (operacao.equals("ALTERAR") && agendamentoReorg.getEntidadeBusca().equals("CANCELAR"))){
			List<AgendamentoReorg> solicitacoes = new ArrayList<AgendamentoReorg>();
			// varre cada uma das solicitações trazidas do DAO
			for (int i = 0; i < resultado.getEntidades().size(); i++){
				agendamentoReorg = (AgendamentoReorg)resultado.getEntidades().get(i);
//					if (solicitacao.getId() != null){ // tem id?
//						if (solicitacoes.size() > 0){ // buscou todos os IDs?
//							request.setAttribute("txtReqNumber", null);
//						}else{
//							request.setAttribute("txtReqNumber", Integer.toString(solicitacao.getId()));
//						}
//					}
				solicitacoes.add(agendamentoReorg);
			} // fim-varre_cada_solicitacao
			request.setAttribute("tblSolicitacao", (ArrayList<AgendamentoReorg>)solicitacoes);
			// devolve a requisição, com as alterações feitas
			RequestDispatcher disp = request.getRequestDispatcher("FormVisualizarSolicitacao.jsp");
			try {
				disp.forward(request, response);
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}else if (agendamentoReorg.getEntidadeBusca() != null
				&& agendamentoReorg.getEntidadeBusca().equals("VOLTAR")){
			// é retorno de status de TRAVADO para PENDENTE?
			try {
				response.sendRedirect("VisualizarSolicitacao?operacao=VISUALIZAR");
				return;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if (operacao.equals("ALTERAR")
				&& agendamentoReorg.getEntidadeBusca().equals("VISUALIZAR")){
			request.setAttribute("resultado", resultado);
			// devolve a requisição, com as alterações feitas
			RequestDispatcher disp = request.getRequestDispatcher("FormAlterarSolicitacao.jsp");
			try {
				disp.forward(request, response);
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}else if (operacao.equals("ALTERAR")
				&& agendamentoReorg.getEntidadeBusca().equals("ALTERAR")){
			request.setAttribute("resultado", resultado);
			// devolve a requisição, com as alterações feitas
			try {
				response.sendRedirect("ConsultarSolicitacao?operacao=CONSULTAR&entBusca=DETALHES&txtReqNumber="
						+ agendamentoReorg.getId());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		// é para ver os detalhes?
		if (agendamentoReorg.getEntidadeBusca() != null 
				&& agendamentoReorg.getEntidadeBusca().equals("DETALHES")){
			if (agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).getTabelas() != null){
				request.setAttribute("etdResultado", agendamentoReorg);
			}
			RequestDispatcher disp = request.getRequestDispatcher("FormDetalhesSolicitacao.jsp");
			try {
				disp.forward(request, response);
				return;
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// carregar combo específica?
		if (agendamentoReorg != null 
				&& agendamentoReorg.getEntidadeBusca() != null 
				&& gson != null 
				&& operacao.equals("CONSULTAR")){
			if (agendamentoReorg.getEntidadeBusca().equals("BANCO")) { // carregar combo dos bancos?
				List<Banco> bancos = agendamentoReorg.getCliente().getBancos();
				String json = null;
				json = new Gson().toJson(bancos);
				response.setContentType(contentType); // "application/json"
				try {
					response.getWriter().write(json);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else if (agendamentoReorg.getEntidadeBusca().equals("OWNER")){ // carregar combo dos owners?
				List<Owner> owners = agendamentoReorg.getCliente().getBancos().get(0).getOwners();
				String json = null;
				json = new Gson().toJson(owners);
				response.setContentType(contentType); // "application/json"
				try {
					response.getWriter().write(json);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else if (agendamentoReorg.getEntidadeBusca().equals("TABELA")){ // carregar combo das tabelas?
				List<Tabela> tabelas = agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).getTabelas();
				String json = null;
				json = new Gson().toJson(tabelas);
				response.setContentType(contentType); // "application/json"
				try {
					response.getWriter().write(json);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else if (agendamentoReorg.getEntidadeBusca().equals("METADADO")){ // carregar tabela dos metadados?
				List<Tabela> tabelas = agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).getTabelas();
				String json = null;
				json = new Gson().toJson(tabelas);
				//System.out.print("\nVH - JSON: " + json);
				response.setContentType(contentType); // "application/json"
				try {
					response.getWriter().write(json);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}else { // senao, é para carregar a página inteira
			if (agendamentoReorg != null){
				if (agendamentoReorg.getId() != null)
					request.setAttribute("txtReqNumber", Integer.toString(agendamentoReorg.getId()));
				if (agendamentoReorg.getStatus() != null)
					request.setAttribute("txtStatus", (String)agendamentoReorg.getStatus());
			
				if (operacao.equals("SALVAR") || operacao.equals("CONSULTAR")) {
					if (agendamentoReorg.getClientes() != null)
						request.setAttribute("cboCliente", (ArrayList<Cliente>)agendamentoReorg.getClientes());
					
					if (agendamentoReorg.getBancos() != null)
						request.setAttribute("cboBanco", (ArrayList<Banco>)agendamentoReorg.getBancos());
					
					if (agendamentoReorg.getOwners() != null){
						request.setAttribute("cboOwner", (ArrayList<Owner>)agendamentoReorg.getOwners());
					}
					
					if (agendamentoReorg.getTabelas() != null){
						request.setAttribute("cboTabela", (ArrayList<Tabela>)agendamentoReorg.getTabelas());
					}
					
					RequestDispatcher disp = request.getRequestDispatcher("FormSolicitacao.jsp");
					try {
						disp.forward(request, response);
					} catch (ServletException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} // fim-else carregar a página inteira
	} // fim-setView()
} // fim-classe
