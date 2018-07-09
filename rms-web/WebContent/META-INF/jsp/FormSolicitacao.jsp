<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
	<link rel="icon" href="../../favicon.ico">
	<!-- Bootstrap core CSS -->
	<link href="../../dist/css/bootstrap.min.css" rel="stylesheet">

	<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
	<link href="../../assets/css/ie10-viewport-bug-workaround.css" rel="stylesheet">

	<!-- Custom styles for this template -->
	<link href="../../styles/dashboard.css" rel="stylesheet">

	<!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
	<!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
	<script src="../../assets/js/ie-emulation-modes-warning.js"></script>

	<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
	<!--[if lt IE 9]>
	  <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
	  <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
	<![endif]-->
	<title>Reorg Management System</title>
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
					<li><a href="#">Painel de Controle</a></li>
					<li><a href="#">Configurações</a></li>
					<li><a href="#">Perfil</a></li>
					<li><a href="#">Ajuda</a></li>
				</ul>
				<form class="navbar-form navbar-right">
					<input type="text" class="form-control" placeholder="Pesquisar...">
				</form>
			</div>
		</div>
    </nav>
	<div class="col-sm-3 col-md-2 sidebar">
		<ul class="nav nav-sidebar">
			<li class="active"><a href="#">Visão Geral<span class="sr-only">(current)</span></a></li>
			<li><a href="#">Relatórios</a></li>
			<li><a href="#">Análises</a></li>
		</ul>
		<ul class="nav nav-sidebar">
			<li><a href="http://localhost:8080/rms-web/FormSolicitacao.jsp">Nova Solicitação</a></li>
			<li><a href="http://localhost:8080/rms-web/FormVerSolicitacao.jsp">Ver Solicitação</a></li>
			<li><a href="http://localhost:8080/rms-web/FormAlterarSolicitacao.jsp">Alterar Solicitação</a></li>
			<li><a href="http://localhost:8080/rms-web/FormCancelarSolicitacao.jsp">Cancelar Solicitação</a></li>
		</ul>
	</div>
	<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
		<div class="container">
			<div class="page-header">
				<div class="row">
					<div class="col-xs-6">
						<h1 class="">NOVA SOLICITAÇÃO DE REORG</h1>
					</div>
					<div class="col-xs-2">
						<label>REQ Nº:</label>
						<input type="text" class="form-control" disabled>
					</div>
				</div><!-- /row header -->
			</div>
		</div><!-- /container header -->
		<div class="container">
			<div class="row">
				<div class="col-xs-4">
					<label>Banco:</label>
					<input type="text" class="form-control">
					<br>
					<label>Owner:</label>
					<input type="text" class="form-control">
					<br>
					<label>Tabelas:</label>
					<div class="input-group">
						<input type="text" class="form-control" placeholder="">
						<span class="input-group-btn">
							<button class="btn btn-primary" type="button">>></button>
						</span>
					</div>
					<br>
				</div><!-- /col 1 -->
				<div class="col-xs-4">
					<br>
					<div class="list-group">
						<a href="#" class="list-group-item active">Tabela 1</a>
						<a href="#" class="list-group-item list-group-item-action">Tabela 2</a>
						<a href="#" class="list-group-item list-group-item-action">Tabela 3</a>
						<a href="#" class="list-group-item list-group-item-action">Tabela 4</a>
						<a href="#" class="list-group-item list-group-item-action disabled">Tabela n</a>
					</div>
				</div><!-- /col 2 -->
			</div><!-- /row 1 -->
		</div><!-- /container 1 -->
		<div class="container">
			<div class="row">	
				<div class="col-xs-4">
					<label>Tamanho Tabela:</label>
					<input type="text" class="form-control" disabled>
					<br>
					<label>Parallel:</label>
					<input type="text" class="form-control">
					<br>
				</div><!-- /col 1 -->
				<div class="col-xs-4">
					<label>Tamanho Índice:</label>
					<input type="text" class="form-control" disabled>
					<br>
					<label>InitTrans:</label>
					<input type="text" class="form-control">
					<br>
				</div><!-- /col 2 -->
			</div><!-- /row 2 -->
		</div><!-- /container 2 -->
		<div class="container">
			<div class="row">	
				<div class="col-xs-2">
					<label>Compress Table:</label>
					<div class="dropdown">
						<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
							Dropdown
							<span class="caret"></span>
						</button>
						<ul class="dropdown-menu" aria-labelledby="dropdownMenu1">
							<li><a href="#">Action</a></li>
							<li><a href="#">Another action</a></li>
							<li><a href="#">Something else here</a></li>
							<li role="separator" class="divider"></li>
							<li><a href="#">Separated link</a></li>
						</ul>
					</div><!-- /dropdown -->
				</div><!-- /col 1.1 -->
				<div class="col-xs-2">
					<label>Critério Coluna:</label>
					<div class="dropdown">
						<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
							Dropdown
							<span class="caret"></span>
						</button>
						<ul class="dropdown-menu" aria-labelledby="dropdownMenu1">
							<li><a href="#">Action</a></li>
							<li><a href="#">Another action</a></li>
							<li><a href="#">Something else here</a></li>
							<li role="separator" class="divider"></li>
							<li><a href="#">Separated link</a></li>
						</ul>
					</div><!-- /dropdown -->
				</div><!-- /col 1.2 -->
				<div class="col-xs-2">
					<div class="input-group">
						<label>Compress Index:</label>
						<span class="input-group">
							<input type="checkbox" aria-label="...">
						</span>
					</div><!-- /input-group -->
				</div><!-- /col 2.1 -->
				<div class="col-xs-2">
					<label>% Estatística:</label>
					<div class="dropdown">
						<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
							Dropdown
							<span class="caret"></span>
						</button>
						<ul class="dropdown-menu" aria-labelledby="dropdownMenu1">
							<li><a href="#">Action</a></li>
							<li><a href="#">Another action</a></li>
							<li><a href="#">Something else here</a></li>
							<li role="separator" class="divider"></li>
							<li><a href="#">Separated link</a></li>
						</ul>
					</div><!-- /dropdown -->
				</div><!-- /col 2.2 -->
			</div><!-- /row 3 -->
		</div><!-- /container 3 -->
		<br><br>
		<div class="container">
			<div class="row">
				<div class="col-xs-3">
				</div>
				<div class="col-xs-1">
					<div class="btn-group btn-group" aria-label="...">
						<button type="button" class="btn btn-default btn-lg">Salvar</button>
					</div><!-- /btn-group -->
				</div><!-- /col 1 -->
				<div class="col-xs-1">
					<div class="btn-group btn-group" aria-label="...">
						<button type="button" class="btn btn-default btn-lg">Voltar</button>
					</div><!-- /btn-group -->
				</div><!-- /col 2 -->
				<div class="col-xs-3">
				</div>
			</div><!-- /row 4 -->
		</div><!-- /container 4 -->
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