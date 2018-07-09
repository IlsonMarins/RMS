<%@page import="rms.dominio.*"%>
<%@page import="rms.core.aplicacao.Resultado"%>
<%@page import="java.util.*"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="rms.core.util.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
	
	<link rel="icon" href="./favicon.ico">
	
	<script src="https://code.jquery.com/jquery-3.2.1.js" integrity="sha256-DZAnKJ/6XZ9si04Hgrsxu/8s717jcIzLy3oi35EouyE=" 
		crossorigin="anonymous"> </script>
    
	<!-- Bootstrap core CSS -->
	<link href="./dist/css/bootstrap.min.css" rel="stylesheet">
	<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
	<link href="./assets/css/ie10-viewport-bug-workaround.css" rel="stylesheet">
	<!-- Custom styles for this template -->
	<link href="./styles/dashboard.css" rel="stylesheet">
	<!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
	<!--[if lt IE 9]><script src="./assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
	<script src="./assets/js/ie-emulation-modes-warning.js"></script>
	<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
	<!--[if lt IE 9]>
	  <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
	  <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
	<![endif]-->
	
	<title>Reorg Management System</title>
	<%
		Login login = null;
		boolean isAdmin = false;
		String user = null;
		Registro registro = null;
		Resultado resultado = null;
		AgendamentoReorg solicitacao = null;
		Pessoa p = null;
		String msgErro = null;
		
		if (session.getAttribute("login") != null){ // tem login?
			login = (Login)session.getAttribute("login");
			if (login.getRegistro() != null){ // tem registro no login?
				registro = (Registro)login.getRegistro();
				user = registro.getUsuario();
				isAdmin = registro.getFlgAdmin();
			}
		}else{
			msgErro = "Necessário efetuar login para acesso ao sistema!";%>
			<script>
				var mensagem = '<%if(msgErro != null){out.print(msgErro);}%>';
				if(mensagem){
					alert(mensagem);
				}
			</script><%
			response.setHeader("Refresh", "0.5;url=/rms-web/Login.jsp"); // meio segundo antes de redirecionar
		    return;
		}
		
		if (request.getAttribute("resultado") != null){ // houve retorno de requisição?
			resultado = (Resultado)request.getAttribute("resultado");
			if (resultado.getMsg() != null){
				msgErro = resultado.getMsg();
			}else if (resultado.getEntidades().get(0) instanceof AgendamentoReorg){
				solicitacao = (AgendamentoReorg)resultado.getEntidades().get(0);
			}
		}
	%>
</head>
<body>
    <nav class="navbar navbar-inverse navbar-fixed-top">
		<div class="container-fluid">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
					<span class="sr-only">...</span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="#">Reorg Management System</a>
			</div>
			<div id="navbar" class="navbar-collapse collapse">
				<ul class="nav navbar-nav navbar-right">
					<li><a href="#">Ajuda</a></li>
					<li><a href="#">Sair</a></li>
				</ul>
				<!-- OPÇÃO FUTURA DE PESQUISA PELO HEADER DA PÁGINA
				<form class="navbar-form navbar-right">
					<input type="text" class="form-control" placeholder="Pesquisar..." />
				</form>
				-->
			</div>
		</div>
    </nav>
	<div class="col-sm-3 col-md-2 sidebar">
		<ul class="nav nav-sidebar">
			<li><a href="#">Visão Geral<span class="sr-only"> </span></a></li>
			<li><a href="#">Monitor de Atividade</a></li>
			<li><a href="#">Análise Gerencial</a></li>
		</ul>
		<ul class="nav nav-sidebar">
			<li><a href="#">Agendar Reorg</a></li>
			<li class="active"><a href="#">Gerenciar Reorg</a></li>
		</ul>
		<ul class="nav nav-sidebar">
			<li><a href="#">Cadastrar Banco</a></li>
			<li><a href="#">Gerenciar Banco</a></li>
		</ul>
	</div>
	<%
		// ... 
	%>
	<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
		<form action="AlterarAgendamento" method="post">
			<div class="container">
				<div class="page-header">
					<div class="row">
						<div class="col-xs-8">
							<h1 class="">ALTERAR SOLICITAÇÃO</h1>
						</div>
						<div class="col-xs-2">
							<label>REQ Nº:</label>
							<%
								out.print("<input type='text' class='form-control' id='txtReqNumber' name='txtReqNumber'" 
										+ "readonly value='" + solicitacao.getId().toString() + "' />");
							%>
						</div>
					</div><!-- /row header -->
				</div><!-- /div page-header -->
			</div><!-- /container header -->
			<div class="container">
				<div class="row">
					<div class="col-xs-4">
						<label>Agendar Execução:</label>
						<div class="input-group date" id="datetimepicker5">
							<%
								// formatação da apresentação de data/hora
								SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm");
								out.print("<input type='text' class='form-control' id='txtDtAgendada' name='txtDtAgendada'"
									+ "value='" + sdf.format(solicitacao.getDtAgendamento()) + "' />");
							%>
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div><!-- /input-group date -->
						<br>
					</div><!-- /col 1 -->
					<div class="col-xs-4">
						<label>Status:</label>
						<%
							out.print("<input readonly type='text' class='form-control' id='txtStatus' name='txtStatus'"
								+ "value='" + solicitacao.getStatus() + "' />");
						%>
						<br>
					</div><!-- /col 2 -->
				</div><!-- /row 1 -->
				<div class="row">
					<div class="col-xs-4">
						<label>Cliente:</label>
						<select class='form-control' id='cboCliente' name='cboCliente'>
							<option value=''>
								SELECIONE
							</option>
							<%
								Cliente c = solicitacao.getCliente();
								out.print("<option value='");
								out.print(c.getId());
								out.print("' selected>");
								out.print(c.getNomeCliente());
								out.print("</option>");
							%>
						</select>
					</div> <!-- /col 1 - row 2 -->
					<div class="col-xs-4">
						<label>Banco:</label>
						<select class='form-control' id='cboBanco' name='cboBanco'>
							<option value=''>
								SELECIONE
							</option>
							<%
								Banco b = solicitacao.getCliente().getBancos().get(0);
								out.print("<option value='");
								out.print(b.getId());
								out.print("' selected>");
								out.print(b.getNomeBanco());
								out.print("</option>");
							%>
						</select>
						<br>
						<script>
							$(document).ready(function () {
								$(function() {
									$('#cboCliente').change(function() {
										$.ajax({
											url: 'ConsultarSolicitacao',
											dataType: 'json',
											type: 'POST',
											data: {idBuscaEspecifica : $('#cboCliente').val(), operacao: "CONSULTAR", json: "json", entBusca: "BANCO"},
											success: function(data) {
												$('#cboBanco').empty(); // clear the current elements in select box
												$('#cboBanco').append($('<option></option>').attr('value', "").text("SELECIONE"));
												for (var row in data) {
													//console.log(row, data[row]);
													$('#cboBanco').append($('<option></option>').attr('value', data[row].id).text(data[row].nomeBanco));
												}
											},
											error: function(jqXHR, textStatus, errorThrown) {
												//alert("ERRO!\n" + errorThrown);
											}
										});
									});
								});
							});
						</script>
					</div> <!-- /col 2 - row 2 -->
				</div> <!-- /row 2 -->
				<div class="row">
					<div class="col-xs-4">
						<label>Owner:</label>
						<select class='form-control' id='cboOwner' name='cboOwner'>
							<option value=''>
								SELECIONE
							</option>
							<%
								Owner o = solicitacao.getCliente().getBancos().get(0).getOwners().get(0);
								out.print("<option value='");
								out.print(o.getId());
								out.print("' selected>");
								out.print(o.getNomeOwner());
								out.print("</option>");
							%>
						</select>
						<br>
						<script>
							$(document).ready(function () {
								$(function() {
									$('#cboBanco').change(function() {
										$.ajax({
											url: 'ConsultarSolicitacao',
											dataType: 'json',
											type: 'POST',
											data: {idBuscaEspecifica : $('#cboBanco').val(), operacao: "CONSULTAR", json: "json", entBusca: "OWNER"},
											success: function(data) {
												$('#cboOwner').empty(); // clear the current elements in select box
												$('#cboOwner').append($('<option></option>').attr('value', "").text("SELECIONE"));
												for (var row in data) {
													$('#cboOwner').append($('<option></option>').attr('value', data[row].id).text(data[row].nomeOwner));
												}
											},
											error: function(jqXHR, textStatus, errorThrown) {
												//alert("\tERRO!\n" + errorThrown);
											}
										});
									});
								});
							});
						</script>
					</div> <!-- /col 1 - row 3 -->
					<div class="col-xs-4">
						<label>Tabelas:</label>
						<!-- <div class="input-group"> -->
							<select multiple class='form-control' id='cboTabela' name='cboTabela'>
								<option value=''>
									SELECIONE
								</option>
								<%
									if (solicitacao.getCliente().getBancos().get(0).getOwners().get(0).getTabelas() != null){
										request.setAttribute("cboTabela", solicitacao.getCliente().getBancos().get(0).getOwners().get(0).getTabelas());
									}
									if (request.getAttribute("cboTabela") != null) {
										List<Tabela> tbs = (ArrayList<Tabela>)request.getAttribute("cboTabela");
										if(tbs != null){
											for(Tabela t: tbs){
												out.print("<option value='"
													+ t.getId() + "'> "
													+ t.getNomeTabela()
													+ " </option>");
											}
										}
									}
								%>
							</select>
							<!-- <span class='input-group-btn'>
								<button class="btn btn-primary btn-md" type="button" id="btnAdd" name="btnAdd"> >> </button>
								<!-- <button class="btn btn-default btn-md" type="submit" id="operacao" name="operacao" 
										value="VISUALIZAR"> Pesquisar </button>
							</span> -->
						<!-- </div> <!-- /input-group -->
						<script>
							$(document).ready(function () {
								$(function() {
									$('#cboOwner').change(function() {
										$.ajax({
											url: 'ConsultarSolicitacao',
											dataType: 'json',
											type: 'POST',
											data: {idBuscaEspecifica : $('#cboOwner').val(), operacao: "CONSULTAR", json: "json", entBusca: "TABELA"},
											success: function(data) {
												$('#cboTabela').empty(); // clear the current elements in select box
												$('#cboTabela').append($('<option></option>').attr('value', "").text("SELECIONE"));
												for (var row in data) {
													$('#cboTabela').append($('<option></option>').attr('value', data[row].id).text(data[row].nomeTabela));
												}
											},
											error: function(jqXHR, textStatus, errorThrown) {
												//alert("\tERRO!\n" + errorThrown);
											}
										});
									});
								});
							});
						</script>
					</div> <!-- /col 2 - row 3 -->
				</div> <!-- /row 3 -->
			</div> <!-- /container 2 -->
			<!-- <div class="container">
				<table class='tabelaEditavel' id ='tblMetadado'>
					<thead>
						<tr>
							<th>Tabela</th>
							<th>Tamanho (KB)</th>
							<th>Parallel</th>
							<th>Init Trans</th>
							<th>Compress Table</th>
							<th>Critério Coluna</th>
							<th>% Estatística</th>
							<th>Tablespace Dados</th>
							<th>Data Última Análise</th>
						</tr>
					</thead>
					<tbody>
					<%/*
						if (request.getAttribute("tblMetadado") != null) {
							List<Tabela> tbs = (ArrayList<Tabela>)request.getAttribute("tblMetadado");
							Tabela t = tbs.get(0);
							if(t != null){
									out.print("<table class='table' name='tblMetadado' id ='tblMetadado'>");
									out.print("<thead>");
										out.print("<tr>");
											out.print("<th>Tabela</th>");
											out.print("<th>Tamanho Tabela</th>");
											out.print("<th>Parallel</th>");
											out.print("<th>Init Trans</th>");
											out.print("<th>Compress Table</th>");
											out.print("<th>Critério Coluna</th>");
											out.print("<th>% Estatística</th>");
											out.print("<th>Tablespace Dados</th>");
										out.print("</tr>");
									out.print("</thead>");
									out.print("<tbody>");
										out.print("<tr>");
											out.print("<td>" + t.getNomeTabela() + "</td>");
											out.print("<td>" + t.getMetadado().getTamanhoTabela() + "</td>");
											out.print("<td>" + t.getMetadado().getParallel() + "</td>");
											out.print("<td>" + t.getMetadado().getIniTrans() + "</td>");
											out.print("<td>" + t.getMetadado().getCompressTable() + "</td>");
											out.print("<td>" + t.getMetadado().getCriterioColuna() + "</td>");
											out.print("<td>" + t.getMetadado().getPctEstatistica() + "</td>");
											out.print("<td>" + t.getMetadado().getTablespaceDados() + "</td>");
										out.print("</tr>");
									out.print("</tbody>");
								out.print("</table>");
							}
						}*/
				 	%>
			 		</tbody>
			 	</table>
			 	<script>
				 	$(document).ready(function () {
				 	    $("td").dblclick(function () {
				 	        var conteudoOriginal = $(this).text();
				 	        
				 	        $(this).addClass("celulaEmEdicao");
				 	        $(this).html("<input type='text' value='" + conteudoOriginal + "' />");
				 	        $(this).children().first().focus();
	
				 	        $(this).children().first().keypress(function (e) {
				 	            if (e.which == 13) {
				 	                var novoConteudo = $(this).val();
				 	                $(this).parent().text(novoConteudo);
				 	                $(this).parent().removeClass("celulaEmEdicao");
				 	            }
				 	        });
				 			
				 		$(this).children().first().blur(function(){
				 			$(this).parent().text(conteudoOriginal);
				 			$(this).parent().removeClass("celulaEmEdicao");
				 		});
				 	    });
				 	});
			 	</script>
				<script>
					$(document).ready(function () {
						$(function() {
							$('#btnAdd').click(function() {
								//alert('entrou!');
								$.ajax({
									url: 'ConsultarSolicitacao',
									dataType: 'json',
									type: 'POST',
									data: {addTabela : $('#cboTabela').val(), ownTabela : $('#cboOwner').val(), banTabela : $('#cboBanco').val(), operacao: "CONSULTAR", json: "json", entBusca: "METADADO"},
									success: function(data) {
										for (var row in data) {
											var date = new Date(data[row].metadado.dtUltimaAnalise);
											//console.log(date);
											var currMin = date.getMinutes(); // pegar os minutos para ajustar dois digitos
											currMin = currMin + ""; // transformar em string para poder tratar
											if (currMin.length == 1) // valida se existe somente um dígito
												currMin = "0" + currMin; // transforma em dois dígitos
											var meses = new Array("01","02","03","04","05","06","07","08","09","10","11","12");
											var dateString = date.getDate() + "/" + meses[date.getMonth()] 
												+ "/" + date.getFullYear() + " " + date.getHours() + ":" + currMin;
											$('#tblMetadado')
												.append($('<tr></tr>')
													.append($('<td></td>').text(data[row].nomeTabela))
													.append($('<td></td>').text(data[row].metadado.tamanhoTabela))
													.append($('<td></td>').text(data[row].metadado.parallel))
													.append($('<td></td>').text(data[row].metadado.iniTrans))
													.append($('<td></td>').text(data[row].metadado.compressTable))
													.append($('<td></td>').text(data[row].metadado.criterioColuna))
													.append($('<td></td>').text(data[row].metadado.pctEstatistica))
													.append($('<td></td>').text(data[row].metadado.tablespaceDados))
													.append($('<td></td>').text(dateString))
												);
										}
									},
									error: function(jqXHR, textStatus, errorThrown) {
										//alert("ERRO! " + errorThrown);
									}
								});
							});
						});
					});
				</script>
			</div> <!-- /container 3 - table dos metadados -->
			<br><br>
			<div class="container">
				<div class="row">
					<div class="col-xs-1 col-xs-offset-2">
						<a href='AlterarSolicitacao?operacao=ALTERAR&entBusca=VOLTAR
							&txtReqNumber=<%out.print(solicitacao.getId().toString());%>
							&txtStatus=<%out.print(solicitacao.getStatus());%>'>
							<button class='btn btn-default btn-lg' type='button' id='btnLog'>
								VOLTAR
							</button>
						</a>
					</div><!-- /col 2 -->
					<div class="col-xs-1 col-xs-offset-2">
						<input class="btn btn-default btn-lg" type="submit" id="operacao" name="operacao" value="ALTERAR" />
					</div><!-- /col 1 -->
				</div><!-- /row 4 -->
			</div><!-- /container 4 -->
		</form><!-- /formulario -->
	</div><!-- /col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main -->
	<!-- Bootstrap core JavaScript
	================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
	<script>window.jQuery || document.write('<script src="./assets/js/vendor/jquery.min.js"><\/script>')</script>
	<script src="./dist/js/bootstrap.min.js"></script>
	<!-- Just to make our placeholder images work. Don't actually copy the next line! -->
	<script src="./assets/js/vendor/holder.min.js"></script>
	<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
	<script src="./assets/js/ie10-viewport-bug-workaround.js"></script>
</body>
</html>