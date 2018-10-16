<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">



<script type="text/javascript">
	$(document).ready(
			function() {

				$('#mapSelectModal').modal({
					backdrop : 'static',
					keyboard : false
				});

				$.ajax({
					type : "GET",
					url : "maps/getAvailableMaps",
					success : function(data) {
						$('#availableMapsName').find('option').remove();
						for (var i = 0; i < data.length; i++) {
							$('#availableMapsName').append($('<option>', {
								value : data[i],
								text : data[i]
							}));
						}
					},
					error : function(XMLHttpRequest, textStatus, errorThrown) {
						alert("Failure fetching map");
					}
				});

				var continentDataTable = $('#continentsDesc').DataTable();
				var countryDataTable = $('#countriesDesc').DataTable();

				function parseMapData(data) {

					continentDataTable.clear().draw();
					for (var i = 0; i < data.continents.length; i++) {
						addRowInContinentDataTable(data.continents[i].name,
								data.continents[i].score);
					}

					countryDataTable.clear().draw();
					for (var i = 0; i < data.territories.length; i++) {
						addRowInCountryDataTable(data.territories[i].name,
								data.territories[i].continentName,
								data.territories[i].neighbours);
					}

				}

				function addRowInContinentDataTable(column1, column2) {
					continentDataTable.row.add([ column1, column2 ])
							.draw(false);
				}

				function addRowInCountryDataTable(column1, column2, column3) {
					countryDataTable.row.add([ column1, column2, column3 ])
							.draw(false);
				}

				function fetchMap() {
					$.ajax({
						type : "GET",
						data : $.param({
							fileName : $("#availableMapsName option:selected")
									.text()
						}),
						url : "maps/map",
						success : function(data) {
							parseMapData(data);
						},
						error : function(XMLHttpRequest, textStatus,
								errorThrown) {
							alert("Failure loading map");
						}
					});
				}

				$('#playNow').on('click', function() {
					fetchMap();
					$('#mapSelectModal').modal('toggle');
				});

			});
</script>

</head>
<body>
	<h1>Map</h1>
	<div>
		<h2>Continents</h2>
		<p>Please select row to update</p>
		<div>
			<table id="continentsDesc" class="display" style="width: 100%">
				<thead>
					<tr>
						<th>Name</th>
						<th>Score</th>
					</tr>
				</thead>
				<tfoot>
					<tr>
						<th>Name</th>
						<th>Score</th>
					</tr>
				</tfoot>
			</table>
		</div>
	</div>

	<div style="margin-top: 2%">
		<h2>Territories</h2>
		<p>Please select row to update</p>
		<div>
			<table id="countriesDesc" class="display" style="width: 100%">
				<thead>
					<tr>
						<th>Name</th>
						<th>Continent</th>
						<th>Neighbors</th>
					</tr>
				</thead>
				<tfoot>
					<tr>
						<th>Name</th>
						<th>Continent</th>
						<th>Neighbors</th>
					</tr>
				</tfoot>
			</table>
		</div>
	</div>

	<!-- Modal -->
	<div class="modal fade" id="mapSelectModal" tabindex="-1" role="dialog"
		aria-labelledby="mapSelectModalTitle" aria-hidden="true">
		<div class="modal-dialog modal-dialog-centered" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="exampleModalLongTitle">Select Map
						to load</h5>

				</div>
				<div class="modal-body">
					<select class="form-control form-control-sm" id="availableMapsName">
						<option></option>
					</select>
				</div>
				<div class="modal-footer">
					<button id="playNow" type="button" class="btn btn-primary"
						style="background-color: black; border-color: black">Play
						Now</button>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
</html>