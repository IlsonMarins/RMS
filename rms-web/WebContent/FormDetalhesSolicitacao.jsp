<%@page import="rms.dominio.*"%>
<%@page import="rms.core.aplicacao.Resultado"%>
<%@page import="java.util.*"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="rms.core.util.*"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<% AgendamentoReorg solicitacao = (AgendamentoReorg)request.getAttribute("etdResultado"); %>
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
			<li><a href="ConsultarSolicitacao?operacao=CONSULTAR&entBusca=CARREGAR">Agendar Reorg</a></li>
			<li class="active"><a href="VisualizarSolicitacao?operacao=VISUALIZAR">Gerenciar Reorg</a></li>
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
						<div class="col-xs-9">
							<h1 class="">DETALHE DE AGENDAMENTO</h1>
						</div>
						<div class="col-xs-2">
							<div class="input-group">
								<label>REQ Nº:</label>
								<%
									String txtId = (String)request.getAttribute("txtReqNumber");
									if (solicitacao != null)
										txtId = solicitacao.getId().toString();
									if (txtId == null){
										txtId = "Pesquisar...";
										out.print("<input type='text' class='form-control' id='txtReqNumber' "
												+ "name='txtReqNumber' value=''"
												+ "placeholder='" + txtId + "' />");
										out.print("<span class='input-group-btn'>");
										out.print("<button class='btn btn-default btn-md' type='submit' id='operacao' name='operacao'");
										out.print("value='VISUALIZAR'> <img src='icon-search.png' /> </button>");
										out.print("</span>");
									}
									else {
										out.print("<input type='text' class='form-control' id='txtReqNumber' "
												+ "name='txtReqNumber' readonly value='"
												+ txtId + "' />");
									}
								%>
							</div>
						</div>
					</div><!-- /row header -->
				</div><!-- /div page-header -->
			</div><!-- /container header -->
			<div class="container">
				<%
					if (solicitacao != null) {
						//Solicitacao s = (Solicitacao)request.getAttribute("etdResultado");
						List<Tabela> tbs = (ArrayList<Tabela>)solicitacao.getCliente().getBancos().get(0).getOwners().get(0).getTabelas();
						out.print("<table class='table' name='tblMetadado' id ='tblMetadado'>");
						out.print("<thead>");
							out.print("<tr>");
								out.print("<th>Tabela</th>");
								out.print("<th>Tamanho (KB)</th>");
								out.print("<th>Parallel</th>");
								out.print("<th>Init Trans</th>");
								out.print("<th>Compress Table</th>");
								out.print("<th>Critério Coluna</th>");
								out.print("<th>% Estatística</th>");
								out.print("<th>Tablespace Dados</th>");
								out.print("<th>Data Última Análise</th>");
							out.print("</tr>");
						out.print("</thead>");
						out.print("<tbody>");
						// formatação da apresentação de data/hora
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm");
						// varre o array de tabelas e adiciona a cada linha da <table>
						for (Tabela t : tbs){
							out.print("<tr>");
								out.print("<td>" + t.getNomeTabela() + "</td>");
								out.print("<td>" + t.getMetadado().getTamanho() + "</td>");
								out.print("<td>" + t.getMetadado().getParallel() + "</td>");
								out.print("<td>" + t.getMetadado().getIniTrans() + "</td>");
								out.print("<td>" + t.getMetadado().getCompressTable() + "</td>");
								out.print("<td>" + t.getMetadado().getCriterioColuna() + "</td>");
								out.print("<td>" + t.getMetadado().getPctEstatistica() + "</td>");
								out.print("<td>" + t.getMetadado().getNomeTablespace() + "</td>");
								out.print("<td>" + sdf.format(t.getMetadado().getLastAnalyzed()) + "</td>");
							out.print("</tr>");
						}
						out.print("</tbody>");
						out.print("</table>");
					}
			 	%>
			</div> <!-- /container 3 - table dos metadados -->
			<br><br>
			<!-- <input type="submit" id="operacao" name="operacao" value="VOLTAR"/>
			<input type="submit" id="operacao" name="operacao" value="SALVAR"/> -->
			<div class="container">
				<div class="row">
					<div class="col-xs-1 col-xs-offset-2">
						<a href='VisualizarSolicitacao?operacao=VISUALIZAR'>
							<button class='btn btn-default btn-lg' type='button' id='btnLog'>
								VOLTAR
							</button>
						</a>
					</div><!-- /col 2 -->
					<!--
					<div class="col-xs-1 col-xs-offset-2">
						<input class="btn btn-default btn-lg" type="submit" id="operacao" name="operacao" value="SALVAR"/>
					</div>
					-->
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