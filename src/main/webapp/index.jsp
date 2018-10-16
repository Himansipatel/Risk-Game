<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
<meta name="description" content="">
<meta name="author" content="">
<link rel="icon" href="../../favicon.ico">

<title>Game Risk</title>
<!-- Bootstrap core CSS -->
<link href="webjars/bootstrap/4.1.3/css/bootstrap.min.css"
	rel="stylesheet">

<script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
<link rel="stylesheet" type="text/css"
	href="https://cdn.datatables.net/1.10.15/css/dataTables.jqueryui.min.css" />

<script type="text/javascript"
	src="https://cdn.datatables.net/1.10.15/js/jquery.dataTables.min.js"></script>
<script type="text/javascript"
	src="https://cdn.datatables.net/1.10.15/js/dataTables.jqueryui.min.js"></script>


<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
<!-- <link href="../../assets/css/ie10-viewport-bug-workaround.css"
	rel="stylesheet"> -->

<!-- Custom styles for this template -->
<link href="navbar.css" rel="stylesheet">

<!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
<!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
<!-- <script src="../../assets/js/ie-emulation-modes-warning.js"></script> -->

<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->

<script type="text/javascript">
	$(document).ready(function() {

		$("#manageMap").click(function() {
			$.ajax({
				type : "GET",
				url : "maps/getMapView",
				success : function(data) {
					$("#manageMap").addClass("active");
					$("#play").removeClass("active");
					$("#jumbotron").html(data);

				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					alert("Failure");
				}
			});
		});

		$("#play").click(function() {
			$.ajax({
				type : "GET",
				url : "gamePlay/getPlayView",
				success : function(data) {
					$("#play").addClass("active");
					$("#manageMap").removeClass("active");
					$("#jumbotron").html(data);

				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					alert("Failure");
				}
			});
		});
	});
</script>
</head>

<body>

	<div class="container">

		<nav class="navbar navbar-expand-lg navbar-light bg-light"
			style="background-color: #16161780 !important; border: 1px solid #5094da">
			<a class="navbar-brand" href="#" style="color: #5d88d6">Risk</a>
			<button class="navbar-toggler" type="button" data-toggle="collapse"
				data-target="#navbarNav" aria-controls="navbarNav"
				aria-expanded="false" aria-label="Toggle navigation">
				<span class="navbar-toggler-icon"></span>
			</button>
			<div class="collapse navbar-collapse" id="navbarNav">
				<ul class="navbar-nav">
					<li class="nav-item" id="manageMap"><a class="nav-link"
						href="#" style="color: #5d88d6">Map Management <span
							class="sr-only">(current)</span>
					</a></li>
					<li class="nav-item" id="play"><a class="nav-link" href="#"
						style="color: #5d88d6">Play</a></li>
				</ul>
			</div>
		</nav>

		<!-- Main component for a primary marketing message or call to action -->
		<div class="jumbotron" id="jumbotron"
			style="background-color: #d7e2fd8f">
			<h1>Risk Game</h1>
			<p>Welcome to the Risk Game. Please choose navigation tool above
				to continue.</p>

		</div>

	</div>
	<!-- /container -->


	<!-- Bootstrap core JavaScript
    ================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->
	<script>
		window.jQuery
				|| document
						.write('<script src="../../assets/js/vendor/jquery.min.js"><\/script>')
	</script>
	<script src="webjars/bootstrap/4.1.3/js/bootstrap.min.js"></script>
	<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
	<!-- <script src="../../assets/js/ie10-viewport-bug-workaround.js"></script> -->
</body>
</html>
