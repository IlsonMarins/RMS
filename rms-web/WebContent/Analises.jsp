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
		crossorigin="anonymous"></script>
	
	<!--Load the echarts API-->
    <script type="text/javascript" src="./echarts-2.2.7/echarts.min.js"></script>
	
	<script src="./scripts/select2.full.min.js"></script>
	<link href="./styles/select2.css" rel="stylesheet">
	
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
			<li class="active"><a href="VisualizarAnalises?operacao=CONSULTAR">Análise Gerencial</a></li>
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
		<form action="VisualizarAnalises" method="post">
			<div class="container">
				<div class="page-header">
					<div class="row">
						<div class="col-xs-8">
							<h1 class="">ANÁLISE GERENCIAL</h1>
						</div>
					</div><!-- /row header -->
				</div><!-- /div page-header -->
			</div><!-- /container header -->
			<div class="container">
				<div class="row">
					<div class="col-xs-2">
						<label>Data Inicial:</label>
						<div class="input-group date" id="datetimepicker5">
							<%
								String txtDtInicial = null;
								String txtDtFinal = null;
								Analyze analyze = null;
								if (request.getAttribute("resultado") != null){ // tem resultado da requisição?
									if (resultado.getEntidades().get(0) != null // tem instancia de Analyze no resultado?
											&& resultado.getEntidades().get(0) instanceof Analyze){
										analyze = (Analyze)resultado.getEntidades().get(0);
										txtDtInicial = ConverteDate.converteDateString(analyze.getDtInicial());
										txtDtFinal = ConverteDate.converteDateString(analyze.getDtFinal());
									}else{ // não tem instância de Analyze no resultado
										
									}
									if (txtDtInicial != null){ // tem informação de dtInicial no resultado?
										out.print("<input type='text' class='form-control' id='txtDtInicial' name='txtDtInicial' "
												+ "value='" + txtDtInicial + "' />");
									}else{ // não tem informação de dtInicial no resultado
										out.print("<input type='text' class='form-control' id='txtDtInicial' name='txtDtInicial' "
												+ "placeholder='dd/mm/yyyy' />");
									}
								}else{ // não tem resultado da requisição
									
								}
							%>
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div><!-- /input-group date -->
						<br>
					</div> <!-- coluna 1 -->
					<div class="col-xs-2">
						<label>Data Final:</label>
						<div class="input-group date" id="datetimepicker5">
							<%
								if (txtDtFinal != null){ // tem informação de dtFinal no resultado?
									out.print("<input type='text' class='form-control' id='txtDtFinal' name='txtDtFinal' "
											+ "value='" + txtDtFinal + "' />");
								}else{ // não tem informação de dtFinal no resultado
									out.print("<input type='text' class='form-control' id='txtDtFinal' name='txtDtFinal' "
											+ "placeholder='dd/mm/yyyy' />");
								}
							%>
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div><!-- /input-group -->
						<br>
					</div> <!-- coluna 2 -->
				</div> <!-- linha 1 -->
			</div>
			<div class="container">
				<%
					List<EntidadeDominio> analises = null;
					Analyze r1 = null;
					if (request.getAttribute("resultado") != null){ // tem resultado da requisição?
						if (resultado.getEntidades().get(0) != null // tem instancia de Analyze no resultado?
								&& resultado.getEntidades().get(0) instanceof Analyze){
							analises = resultado.getEntidades();
							r1 = (Analyze)analises.get(0);
						}
					}
				%>
				<div class="row">
					<div class="col">
						<div id="chartReorgsPorData" class="col-md-6" style="height:300px;"></div>
					    <script><%String cmm = "";%>
					        var chart = document.getElementById('chartReorgsPorData');
					        var myChart = echarts.init(chart);
					        var option = {
					            title: { text: '<%out.print(r1.title);%>' },
					            tooltip: { },
					            legend: { data: [ '<%out.print(r1.legend);%>' ] },
					            xAxis: { data: [ <%for(String el : r1.xAxis){ out.print(cmm+"'"+el+"'");cmm=",";};%> ] },
					            yAxis: { },
					            series: [{
					                name: '<%out.print(r1.series.name);%>',
					                type: '<%out.print(r1.series.type);%>',
					                data: [<%cmm = "";for(String el : r1.series.data){ out.print(cmm+"'"+el+"'");cmm=",";};%>]
					            }]
					        };
					        myChart.setOption(option);
					    </script>
			    	</div> <!-- coluna 1 -->
			    
					<div class="col">
					    <div id="chartReorgsPorCliente" class="col-md-6" style="height:300px;"></div>
					    <script><%r1 = (Analyze)analises.get(1);%>
					    var chart = document.getElementById('chartReorgsPorCliente');
					    var myChart = echarts.init(chart);
					    var option = {
					        title: { text: '<%out.print(r1.title);%>' },
					        tooltip: { },
					        legend: { data: [ '<%out.print(r1.legend);%>' ] },
					        xAxis: { data: [ <%cmm = "";for(String el : r1.xAxis){ out.print(cmm+"'"+el+"'");cmm=",";};%> ] },
					        yAxis: { },
					        series: [{
					            name: '<%out.print(r1.series.name);%>',
					            type: '<%out.print(r1.series.type);%>',
					            data: [<%cmm = "";for(String el : r1.series.data){ out.print(cmm+"'"+el+"'");cmm=",";};%>]
					        }]
					    };
					    myChart.setOption(option);
					    </script>
				    </div> <!-- coluna 2 -->
			    </div> <!-- linha 2 -->
			</div>
			<br><br>
			<div class="container">
				<div class="row">
					<div class="col-xs-1 col-xs-offset-2">
						<input class="btn btn-default btn-lg" onclick="window.location='http://localhost:8080/rms-web/index.jsp';" 
							type="text" id="" name="" value="VOLTAR"/>
					</div><!-- /col 1 -->
					<div class="col-xs-1 col-xs-offset-2">
						<input class="btn btn-default btn-lg" type="submit" id="operacao" name="operacao" value="CONSULTAR"/>
					</div><!-- /col 2 -->
				</div><!-- /row -->
			</div><!-- /container -->
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