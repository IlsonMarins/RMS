
package rms.controle.web.vh.impl;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import rms.dominio.Banco;
import rms.dominio.Cliente;
import rms.dominio.EntidadeDominio;
import rms.dominio.Owner;
import rms.dominio.AgendamentoReorg;
import rms.dominio.Tabela;



public class SolicitacaoCarregarViewHelper implements IViewHelper {
	
	public EntidadeDominio getEntidade(HttpServletRequest request) {
		String operacao = request.getParameter("operacao");
		String reqNumber = request.getParameter("txtReqNumber");
		String dtAgendada = request.getParameter("txtDtAgendada");
		String status = request.getParameter("txtStatus");
		String cliId = request.getParameter("cboCliente");
		String banId = request.getParameter("cboBanco");
		String ownId = request.getParameter("cboOwner");
		String[] tbsId = request.getParameterValues("cboTabela");
		
		String idBuscaEspecifica = request.getParameter("idBuscaEspecifica");
		String entBusca = request.getParameter("entBusca");
		String idTabelaAdd = request.getParameter("addTabela");
		String idBanTabelaAdd = request.getParameter("banTabela");
		String idOwnTabelaAdd = request.getParameter("ownTabela");
		
		AgendamentoReorg agendamentoReorg  = new AgendamentoReorg();
		
		if (idTabelaAdd != null){ // adicionar à tabela de visualização dos metadados?
			Tabela t = new Tabela();
			Owner o = new Owner();
			Banco b = new Banco();
			List<Tabela> tbs = new ArrayList<Tabela>();
			
			b.setId(Integer.parseInt(idBanTabelaAdd));
			o.setId(Integer.parseInt(idOwnTabelaAdd));
			o.setBanco(b);
			t.setId(Integer.parseInt(idTabelaAdd));
			t.setOwner(o);
			tbs.add(t);
			agendamentoReorg.setTabelas(tbs);
			return agendamentoReorg;
		}
		
		if (entBusca != null){ // é para carregar combo específica?
			if (entBusca.equals("BANCO")){ // buscar_todos_os_bancos_de_cliente_específico?
				Cliente c = new Cliente();
				Banco b = new Banco();
				agendamentoReorg.setEntidadeBusca(entBusca);
				c.setId(Integer.parseInt(idBuscaEspecifica));
				agendamentoReorg.setCliente(c);
				b.setCliente(c);
				agendamentoReorg.setBanco(b);
				return agendamentoReorg;
			}else if (entBusca.equals("OWNER")){ // buscar_todos_os_owners_de_banco_específico?
				Banco b = new Banco();
				Owner o = new Owner();
				agendamentoReorg.setEntidadeBusca(entBusca);
				b.setId(Integer.parseInt(idBuscaEspecifica));
				agendamentoReorg.setBanco(b);
				o.setBanco(b);
				agendamentoReorg.setOwner(o);
				return agendamentoReorg;
			}else if (entBusca.equals("TABELA")) { // buscar_todas_as_tabelas_de_owner_específico?
				Owner o = new Owner();
				Tabela t = new Tabela();
				agendamentoReorg.setEntidadeBusca(entBusca);
				o.setId(Integer.parseInt(idBuscaEspecifica));
				agendamentoReorg.setOwner(o);
				t.setOwner(o);
				ArrayList<Tabela> tbs = new ArrayList<Tabela>();
				tbs.add(t);
				agendamentoReorg.setTabelas(tbs);
				return agendamentoReorg;
			}
		} //fim-carregar_combo_específica
		
		if(reqNumber != null && !reqNumber.trim().equals("")) { // número da requisição está preenchido?
			agendamentoReorg.setId(Integer.parseInt(reqNumber));
			if (operacao.equals("CONSULTAR"))
				return agendamentoReorg;
		}else {
			return agendamentoReorg;
		}
		
		if (dtAgendada != null && !dtAgendada.trim().equals("")){ // data_agendada está preenchida?
			Date data = new Date();
			try {
				data = new SimpleDateFormat("dd/MM/yy HH:mm").parse(dtAgendada);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			agendamentoReorg.setDtAgendamento(data);
		}
		
		agendamentoReorg.setStatus(status);
		
		Cliente c = new Cliente();
		Banco b = new Banco();
		Owner o = new Owner();
		Tabela t = new Tabela();
		
		if (cliId != null){ // cliente está selecionado?
			c.setId(Integer.parseInt(cliId));
			ArrayList<Cliente> cls = new ArrayList<Cliente>();
			cls.add(c);
			agendamentoReorg.setClientes(cls);
			b.setCliente(c);
		}else{
			System.out.print("\nVH - cliId nulo!\n");
		}
		
		if (banId != null){ // banco está selecionado?
			b.setId(Integer.parseInt(banId));
			ArrayList<Banco> bcs = new ArrayList<Banco>();
			bcs.add(b);
			agendamentoReorg.setBancos(bcs);
			o.setBanco(b);
		}else{
			System.out.print("\nVH - banId nulo!\n");
		}
		
		if (ownId != null){ // owner está selecionado?
			o.setId(Integer.parseInt(ownId));
			ArrayList<Owner> ows = new ArrayList<Owner>();
			ows.add(o);
			agendamentoReorg.setOwners(ows);
			t.setOwner(o);
		}else{
			System.out.print("\nVH - ownId nulo!\n");
		}
		
		if (tbsId != null){ // tabela(s) está(ão) selecionada(s)?
			ArrayList<Tabela> tbs = new ArrayList<Tabela>();
			for (String idTbl: tbsId){
				t = new Tabela();
				t.setId(Integer.parseInt(idTbl));
				tbs.add(t);
			}
			agendamentoReorg.setTabelas(tbs);
		}else{
			System.out.print("\nVH - tabId nulo!\n");
		}
		
		return agendamentoReorg;
	} //fim-getEntidade()

	public void setView(Resultado resultado, HttpServletRequest request, 
			HttpServletResponse response) {
		
		String operacao = request.getParameter("operacao");
		String contentType = request.getContentType();
		String gson = request.getParameter("json");
		
		if (operacao.equals("VISUALIZAR")){
			List<AgendamentoReorg> solicitacoes = new ArrayList<AgendamentoReorg>();
			for (int i = 0; i < resultado.getEntidades().size(); i++){ // varre cada uma das solicitações trazidas do DAO
				AgendamentoReorg agendamentoReorg = (AgendamentoReorg)resultado.getEntidades().get(i);
				
				if (agendamentoReorg.getId() != null){ // tem id?
					if (solicitacoes.size() > 0){ // buscou todos os IDs?
						request.setAttribute("txtReqNumber", null);
					}else{
							request.setAttribute("txtReqNumber", Integer.toString(agendamentoReorg.getId()));
					}
				}
				
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
		} //fim-VISUALIZAR
		
		//System.out.print("\nVH - contentType: " + contentType);
		//System.out.print("\nVH - gson: " + gson);
		
		AgendamentoReorg agendamentoReorg = (AgendamentoReorg)resultado.getEntidades().get(0);
		if (agendamentoReorg != null && agendamentoReorg.getEntidadeBusca() != null && gson != null && operacao.equals("CONSULTAR")){ // carregar combo específica?
			if (agendamentoReorg.getEntidadeBusca().equals("BANCO")) { // carregar combo dos bancos?
				List<Banco> bancos = agendamentoReorg.getBancos();
				String json = null;
				json = new Gson().toJson(bancos);
				System.out.print("\nVH - JSON: " + json);
				response.setContentType(contentType); // "application/json"
				try {
					response.getWriter().write(json);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else if (agendamentoReorg.getEntidadeBusca().equals("OWNER")){ // carregar combo dos owners?
				List<Owner> owners = agendamentoReorg.getOwners();
				String json = null;
				json = new Gson().toJson(owners);
				System.out.print("\nVH - JSON: " + json);
				response.setContentType(contentType); // "application/json"
				try {
					response.getWriter().write(json);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else if (agendamentoReorg.getEntidadeBusca().equals("TABELA")){ // carregar combo das tabelas?
				List<Tabela> tabelas = agendamentoReorg.getTabelas();
				String json = null;
				json = new Gson().toJson(tabelas);
				System.out.print("\nVH - JSON: " + json);
				response.setContentType(contentType); // "application/json"
				try {
					response.getWriter().write(json);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}/*else if (operacao.equals("SALVAR")){
			try {
				response.sendRedirect("SalvarSolicitacao");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;*/
		else { // senao, é para carregar a página inteira
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
				
				/*if (operacao.equals("VISUALIZAR")) {
					request.setAttribute("tblTabela", (ArrayList<Tabela>)solicitacao.getTabelas());
					
					RequestDispatcher disp = request.getRequestDispatcher("FormVisualizarSolicitacao.jsp");
					try {
						disp.forward(request, response);
					} catch (ServletException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}*/
			}
		} // fim-else carregar a página inteira
	} // fim-setView()

} // fim-classe
