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
			<li class="active"><a href="VisualizarConsole?operacao=VISUALIZAR">Monitor de Atividade</a></li>
			<li><a href="VisualizarAnalises?operacao=CONSULTAR">Análise Gerencial</a></li>
		</ul>
		<ul class="nav nav-sidebar">
			<li><a href="ConsultarSolicitacao?operacao=CONSULTAR&entBusca=CARREGAR">Agendar Reorg</a></li>
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
		<div class="container">
			<div class="page-header">
				<div class="row">
					<div class="col-xs-8">
						<h1 class="">MONITOR DE ATIVIDADE</h1>
					</div><!-- /col 1 -->
					<div class="col-xs-3">
						<form action="AlterarConsole" method="get">
							<button class="btn btn-default btn-md" type="submit" id="operacao" name="operacao" 
								value="ALTERAR"> Limpar Console
							</button>
						</form> <!-- /formulario -->
						<%
							// Set refresh, autoload time as 30 seconds
							//response.setIntHeader("Refresh", 30);
							
							// Get current time
							Date dtHora = new Date();
							SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
							out.println("Hora Atual: " + sdfTime.format(dtHora));
						%>
						<script type="text/javascript">
							setTimeout(function(){
								location = 'http://localhost:8080/rms-web/VisualizarConsole?operacao=VISUALIZAR'
							}, 60 * 250) // 60 * 1000 = 1 min.
						</script>
					</div><!-- /col 2 -->
				</div><!-- /row header -->
			</div><!-- /div page-header -->
		</div><!-- /container header -->
		<div class="container">
			<%
				if (request.getAttribute("resultado") != null){
					//Resultado resultado = (Resultado)request.getAttribute("resultado");
					out.print("<table class='table' id='tblConsole'>");
					/*out.print("<thead>");
					out.print("<tr>");
					out.print("<th>Req. Nº</th>");
					out.print("<th>Data de Cadastro</th>");
					out.print("<th>Data Agendada</th>");
					out.print("<th>Status</th>");
					out.print("<th>Hora Início Reorg</th>");
					out.print("<th>Hora Fim Reorg</th>");
					out.print("</tr>");
					out.print("</thead>");*/
					out.print("<tbody>");
					//out.print("<th>Data / Hora</th>");
					//out.print("<th>Atividade</th>");
					// formatação da apresentação de data/hora
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm");
					// formatação da apresentação de hora/segundos
					// SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yy HH:mm:ss:SS");
					// varre o array de entidades e adiciona cada console
					List<Console> consoles = new ArrayList<Console>();
					for(EntidadeDominio e: resultado.getEntidades()){
						Console console = (Console)e;
						consoles.add(console);
					}
					for (Console c : consoles){
						out.print("<tr>");
						out.print("<td>" + sdf.format(c.getDtCadastro()) + "</td>");
						out.print("<td>" + c.getMsg() + "</td>");
						out.print("</tr>");
					}
					out.print("</tbody>");
					out.print("</table>");
				}else{
					out.print("<h3 class=''> Não há registros no console a apresentar! </h3>");
				}
			%>
		</div><!-- /container-tabela -->
		<br><br>
		<div class="container">
			<div class="row">
				<div class="col-xs-2"> </div>
				<div class="col-xs-1">
					<div class="btn-group btn-group" aria-label="...">
						<a href='http://localhost:8080/rms-web/index.jsp'>
							<button class='btn btn-default btn-lg' type='button' id='btnLog'>
								Voltar
							</button>
						</a>
					</div><!-- /btn-group -->
				</div><!-- /col 1 -->
				<div class="col-xs-2"> </div>
				<div class="col-xs-1">
					<div class="btn-group btn-group" aria-label="...">
						<a href='ConsultarLog?operacao=CONSULTAR'>
							<button class='btn btn-default btn-lg' type='button' id='btnLog'>
								Histórico de Atividade
							</button>
						</a>
						<!-- <button type="button" class="btn btn-default btn-lg" onclick="window.open('reg.log')">Gerar Log</button>-->
					</div><!-- /btn-group -->
				</div><!-- /col 2 -->
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