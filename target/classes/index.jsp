<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Welcome</title>
<link rel="stylesheet" href="resources/cssFiles/jquery-ui.css" />
<link rel="stylesheet" href="resources/cssFiles/test.css" />
<script src="https://code.jquery.com/jquery-3.3.1.min.js"
	integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8="
	crossorigin="anonymous"></script>

<script type="text/javascript">
	$(document).ready(function() {
		 $("p").text("Hello world!");
		$("#map").click(function() {
			getMap();
		});
		$("#clear").click(function() {
			$("#country").val("");
		});
		function getMap() {
			$.ajax({
				url : "http://localhost:8080/GameRisk/maps/map"
			}).then(function(data) {
				//$("#country").value("yoooo");
				//alert(data.countries);
				$("#country").val(data.countries);
			});
			
		} 
		
	});
</script>

</head>
<body>
	<p>yoooo</p>

	<table align="center"><tr>
		<td><button id="map">Map</button></td>
		<td><button id="clear">Clear</button></td>
	</tr>
	<tr>
		Country :
		<input id="country"></input>

	</tr>
	</table>
</body>
</html>