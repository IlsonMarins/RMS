<%@page import="rms.dominio.*"%>
<%@page import="rms.core.aplicacao.Resultado"%>
<%@page import="java.util.*"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="rms.core.util.*"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
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
	
	<!-- Select2 core - Para seleção fluída  -->
	<!-- <script src="./scripts/select2.full.min.js"></script> -->
	<!-- <link href="./styles/select2.css" rel="stylesheet"> -->
	
	<style>		
		.tabelaEditavel {
		    border:solid 1px;
		    width:100%
		}
		
		.tabelaEditavel td {
		    border:solid 1px;
		}
		
		.tabelaEditavel .celulaEmEdicao {
		    padding: 0;
		}
		
		.tabelaEditavel .celulaEmEdicao input[type=text]{
		    width:100%;
		    border:0;
		    background-color:rgb(255,253,210);  
		}
	</style>
    
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
			}
		}
	%>
</head>
<body>
    <nav class="navbar navbar-inverse navbar-fixed-top">
		<div class="container-fluid">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" 
					aria-expanded="false" aria-controls="navbar">
					<span class="sr-only">...</span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="http://localhost:8080/rms-web/index.jsp">Reorg Management System</a>
			</div>
			<div id="navbar" class="navbar-collapse collapse">
				<ul class="nav navbar-nav navbar-right">
					<li><a href="#">Ajuda</a></li>
					<li><a href="Login?operacao=EXCLUIR">Sair</a></li>
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
			<li><a href="http://localhost:8080/rms-web/index.jsp">Visão Geral<span class="sr-only"> </span></a></li>
			<li><a href="VisualizarConsole?operacao=VISUALIZAR">Monitor de Atividade</a></li>
			<li><a href="VisualizarAnalises?operacao=CONSULTAR">Análise Gerencial</a></li>
		</ul>
		<ul class="nav nav-sidebar">
			<li class="active"><a href="ConsultarSolicitacao?operacao=CONSULTAR&entBusca=CARREGAR">Agendar Reorg</a></li>
			<li><a href="VisualizarSolicitacao?operacao=VISUALIZAR">Gerenciar Reorg</a></li>
		</ul>
		<ul class="nav nav-sidebar">
			<li><a href="VisualizarSolicitacao?operacao=VISUALIZAR">Cadastrar Banco</a></li>
			<li><a href="VisualizarSolicitacao?operacao=VISUALIZAR">Gerenciar Banco</a></li>
		</ul>
	</div>
	<%
		// ... 
	%>
	<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
		<form action="SalvarSolicitacao" method="post">
			<div class="container">
				<div class="page-header">
					<div class="row">
						<div class="col-xs-6">
							<h1 class="">AGENDAR REORG</h1>
						</div>
						<div class="col-xs-2">
							<label>REQ Nº:</label>
							<%
								String txtReqNumber = (String)request.getAttribute("txtReqNumber");
								out.print("<input type='text' class='form-control' id='txtReqNumber' name='txtReqNumber'" 
										+ "readonly value='" + txtReqNumber + "' />");
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
							msgErro = (String)request.getAttribute("msgErro");
							if (msgErro != null){
								out.print("<input type='text' class='form-control' id='txtDtAgendada' name='txtDtAgendada' "
										+ "value='Erro! " + msgErro + "' />");
							}else{
								out.print("<input type='text' class='form-control' id='txtDtAgendada' name='txtDtAgendada' "
										+ "placeholder='dd/mm/yyyy hh:mm' />");
							}
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
							String txtStatus = (String)request.getAttribute("txtStatus");
							if (txtStatus != null){
								out.print("<input type='text' class='form-control' id='txtStatus' name='txtStatus' readonly value='"
									+ txtStatus + "' />");
							}else{
								out.print("<input type='text' class='form-control' id='txtStatus' name='txtStatus' readonly value='"
										+ "'txtStatus Nulo!' />");
							}
						%>
						<br>
					</div><!-- /col 2 - lin 1 -->
				</div><!-- /row 1 -->
				<div class="row">
					<div class="col-xs-4">
						<label>Cliente:</label>
						<select class='form-control' id='cboCliente' name='cboCliente'>
							<option value=''>
								SELECIONE
							</option>
							<%
								if (request.getAttribute("cboCliente") != null) {
									ArrayList<Cliente> cls = (ArrayList<Cliente>)request.getAttribute("cboCliente");
									String strMsg = "";
									for (Cliente c: cls){
										strMsg = strMsg + c.getNomeCliente() + " - ";
									}
									if(cls != null){
										for(Cliente c: cls){
											out.print("<option value='");
											out.print(c.getId());
											out.print("'>");
											out.print(c.getNomeCliente());
											out.print("</option>");
										}
									}
								}
							%>
						</select>
						<br>
					</div> <!-- /col 1 - row 2 -->
					<div class="col-xs-4">
						<label>Banco:</label>
						<select class='form-control' id='cboBanco' name='cboBanco'>
							<option value=''>
								SELECIONE
							</option>
							<%
								if (request.getAttribute("cboBanco") != null) {
									ArrayList<Banco> bcs = (ArrayList<Banco>)request.getAttribute("cboBanco");
									String strMsg = "";
									for (Banco b: bcs){
										strMsg = strMsg + b.getNomeBanco() + " - ";
									}
									if(bcs != null){
										for(Banco b: bcs){
											out.print("<option value='");
											out.print(b.getId());
											out.print("'>");
											out.print(b.getNomeBanco());
											out.print("</option>");
										}
									}
								}
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
					</div> <!-- /col 2 - lin 2 -->
				</div> <!-- /row 2 -->
				<div class="row">
					<div class="col-xs-4">
						<label>Owner:</label>
						<select class='form-control' id='cboOwner' name='cboOwner'>
							<option value=''>
								SELECIONE
							</option>
							<%
								if (request.getAttribute("cboOwner") != null) {
									ArrayList<Owner> ows = (ArrayList<Owner>)request.getAttribute("cboOwner");
									String strMsg = "";
									for (Owner o: ows){
										strMsg = strMsg + o.getNomeOwner() + " - ";
									}
									if(ows != null){
										for(Owner o: ows){
											out.print("<option value='");
											out.print(o.getId());
											out.print("'>");
											out.print(o.getNomeOwner());
											out.print("</option>");
										}
									}
								}
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
												alert("\tERRO!\n" + errorThrown);
											}
										});
									});
								});
							});
						</script>
					</div> <!-- /col 1 - lin 3 -->
					<div class="col-xs-4">
						<label>Tabelas:</label>
						<!-- <div class="input-group"> -->
							<select multiple class='form-control' id='cboTabela' name='cboTabela'>
								<option value=''>
									SELECIONE
								</option>
								<%
									if (request.getAttribute("cboTabela") != null) {
										ArrayList<Tabela> tbs = (ArrayList<Tabela>)request.getAttribute("cboTabela");
										String strMsg = "";
										for (Tabela t: tbs){
											strMsg = strMsg + t.getNomeTabela() + " - ";
										}
										if(tbs != null){
											for(Tabela t: tbs){
												out.print("<option value='");
												out.print(t.getId());
												out.print("'>");
												out.print(t.getNomeTabela());
												out.print("</option>");
											}
										}
									}
								%>
							</select>
							<!--<span class='input-group-btn'>
								<button class="btn btn-primary btn-md" type="button" id="btnAdd" name="btnAdd"> >> </button>
							</span>-->
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
					</div> <!-- /col 2 - lin 3 -->
				</div>  <!-- /lin 3 -->
			</div> <!-- /container 2 -->
			<!--<div class="container">
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
									data: {idBuscaEspecifica : $('#cboTabela').val(), ownTabela : $('#cboOwner').val(), banTabela : $('#cboBanco').val(), operacao: "CONSULTAR", json: "json", entBusca: "METADADO"},
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
						<input class="btn btn-default btn-lg" onclick="window.location='http://localhost:8080/rms-web/index.jsp';" 
							type="text" id="" name="" value="VOLTAR"
						/>
					</div><!-- /col 2 -->
					<div class="col-xs-1 col-xs-offset-2">
						<input class="btn btn-default btn-lg" type="submit" id="operacao" name="operacao" value="SALVAR"/>
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