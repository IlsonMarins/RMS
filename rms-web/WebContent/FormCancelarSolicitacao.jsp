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
				<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
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
		</div><!-- /container-fluid menu superior -->
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
		<div class="container">
			<div class="page-header">
				<div class="row">
					<div class="col-xs-8">
						<h1 class="">CANCELAR SOLICITAÇÃO</h1>
					</div><!-- /col 1 -->
					<div class="col-xs-2">
						<label>REQ Nº:</label>
						<div class="input-group">
							<input type="text" class="form-control" placeholder="">
							<span class="input-group-btn">
								<button class="btn btn-primary" type="button">Buscar</button>
							</span>
						</div>
					</div><!-- /col 2 -->
				</div><!-- /row header -->
			</div>
		</div><!-- /container header -->
		<div class="container">
			<div class="row">
				<div class="col-xs-4">
					<label>Agendamento:</label>
					<div class="input-group date" id="datetimepicker5">
						<input type='text' class="form-control" disabled />
						<span class="input-group-addon">
							<span class="glyphicon glyphicon-calendar"></span>
						</span>
					</div><!-- /input-group date -->
					<script type="text/javascript">
						$(function () {
							$('#datetimepicker5').datetimepicker({
								defaultDate: "11/1/2013",
								disabledDates: [
									moment("12/25/2013"),
									new Date(2013, 11 - 1, 21),
									"11/22/2013 00:53"
								]
							});
						});
					</script>
					<br>
				</div><!-- /col 1 -->
				<div class="col-xs-4">
					<label>Status:</label>
					<input type="text" class="form-control" disabled>
					<br>
				</div><!-- /col 2 -->
			</div><!-- /row 1 -->
		</div><!-- /container 1 -->
		<div class="container">
			<div class="row">
				<div class="col-xs-4">
					<label>Banco:</label>
					<input type="text" class="form-control" disabled>
					<br>
					<label>Owner:</label>
					<input type="text" class="form-control" disabled>
					<br>
				</div><!-- /col 1 -->
				<div class="col-xs-4">
					<br>
					<div class="list-group">
						<a href="#" class="list-group-item  active">Tabela 1</a>
						<a href="#" class="list-group-item list-group-item-action">Tabela 2</a>
						<a href="#" class="list-group-item list-group-item-action">Tabela 3</a>
						<a href="#" class="list-group-item list-group-item-action">Tabela 4</a>
						<a href="#" class="list-group-item list-group-item-action">Tabela n</a>
					</div>
				</div><!-- /col 2 -->
			</div><!-- /row 2 -->
		</div><!-- /container 2 -->
		<div class="container">
			<div class="row">	
				<div class="col-xs-4">
					<label>Tamanho Tabela:</label>
					<input type="text" class="form-control" disabled>
					<br>
					<label>Parallel:</label>
					<input type="text" class="form-control" disabled>
					<br>
				</div><!-- /col 1 -->
				<div class="col-xs-4">
					<label>Tamanho Índice:</label>
					<input type="text" class="form-control" disabled>
					<br>
					<label>InitTrans:</label>
					<input type="text" class="form-control" disabled>
					<br>
				</div><!-- /col 2 -->
			</div><!-- /row 3 -->
		</div><!-- /container 3 -->
		<div class="container">
			<div class="row">	
				<div class="col-xs-2">
					<label>Compress Table:</label>
					<div class="dropdown">
						<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true" disabled>
							Dropdown
							<span class="caret"></span>
						</button>
					</div><!-- /dropdown -->
				</div><!-- /col 1.1 -->
				<div class="col-xs-2">
					<label>Critério Coluna:</label>
					<div class="dropdown">
						<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true" disabled>
							Dropdown
							<span class="caret"></span>
						</button>
					</div><!-- /dropdown -->
				</div><!-- /col 1.2 -->
				<div class="col-xs-2">
					<div class="input-group">
						<label>Compress Index:</label>
						<span class="input-group">
							<input type="checkbox" aria-label="..." disabled>
						</span>
					</div><!-- /input-group -->
				</div><!-- /col 2.1 -->
				<div class="col-xs-2">
					<label>% Estatística:</label>
					<div class="dropdown">
						<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true" disabled>
							Dropdown
							<span class="caret"></span>
						</button>
					</div><!-- /dropdown -->
				</div><!-- /col 2.2 -->
			</div><!-- /row 4 -->
		</div><!-- /container 4 -->
		<br><br>
		<div class="container">
			<div class="row">
				<div class="col-xs-2"> </div>
				<div class="col-xs-1">
					<div class="btn-group btn-group" aria-label="...">
						<button type="button" class="btn btn-default btn-lg">Voltar</button>
					</div><!-- /btn-group -->
				</div><!-- /col 2 -->
				<div class="col-xs-2"> </div>
				<div class="col-xs-1">
					<div class="btn-group btn-group" aria-label="...">
						<button type="button" class="btn btn-default btn-lg">Confirmar</button>
					</div><!-- /btn-group -->
				</div><!-- /col 1 -->
				<div class="col-xs-2"> </div>
			</div><!-- /row 5 -->
		</div><!-- /container 5 -->
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