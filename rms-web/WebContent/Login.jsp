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
		if (session.getAttribute("login") != null){ // validar se há usuario conectado
//			if (session.getAttribute("user") instanceof Administrador){ // validar o tipo de usuario conectado
//				p = (Administrador)session.getAttribute("user");
//			}else
//				p = (Funcionario)session.getAttribute("user");
			response.sendRedirect("/rms-web/index.jsp");
		    return;
		}
		Resultado resultado = null;
		Pessoa p = null;
		String msgErro = null;
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
				<a class="navbar-brand" href="#">Reorg Management System</a>
			</div>
			<div id="navbar" class="navbar-collapse collapse">
				<ul class="nav navbar-nav navbar-right">
					<li><a href="#"> </a></li>
					<li><a href="#"> </a></li>
					<li><a href="#"> </a></li>
					<li><a href="#"> </a></li>
				</ul>
			</div>
		</div>
    </nav>
	<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
		<div class="container">
			<div class="page-header">
				<div class="row">
					<div class="col-xs-6">
						<h4 class="">Inicie sessão para acesso ao sistema</h4>
					</div><!-- /col 1 -->
				</div><!-- /row header -->
			</div><!-- /div page-header -->
		</div><!-- /container header -->
		<br><br>
		<div class="container">
			<% 
				if(msgErro != null){
					String[] msgs = msgErro.split("\n");
					//System.out.println(Arrays.toString(msgs));
					for (int i = 0; i < msgs.length; i++){
						out.print(msgs[i]);
						out.print("<br>");
					}
				}
// 				if (reg != null){
// 					if (reg.getLogin() == null || reg.getSenha() == null){
// 						out.print("Login ou senha inválidos!");
// 					}
// 				}
			%>
			<form action="Login" method="post">
				<div class="row">
					<div class="col-xs-3">
						<input type='text' class='form-control' name='txtLogin' id='txtLogin' placeholder='Login' />
					</div>
				</div>
				<div class="row">
					<div class="col-xs-3">
						<input type='password' class='form-control' name='txtSenha' id='txtSenha' placeholder='Senha' />
					</div>
				</div>
				<br><br>
				<div class="row">
					<div class="col-xs-2"> </div>
					<div class="col-xs-1">
						<div class="btn-group btn-group" aria-label="...">
							<button class='btn btn-default btn-lg' type="submit" name="operacao" id="operacao" value="CONSULTAR">
								Entrar
							</button>
						</div><!-- /btn-group -->
					</div><!-- /col 1 -->
					<div class="col-xs-2"> </div>
					<div class="col-xs-2"> </div>
				</div><!-- /row 3 -->
			</form> <!-- /formulario -->
		</div><!-- /container 1 -->
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