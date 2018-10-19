<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">



<script type="text/javascript">
	$(document)
			.ready(
					function() {
						$('#mapSelectModal').modal({
							backdrop : 'static',
							keyboard : false
						});

						var continentDataTable = $('#continentsDesc')
								.DataTable();
						var countryDataTable = $('#countriesDesc').DataTable();
						var player1DataTable = $('#player1').DataTable();
						var player2DataTable = $('#player2').DataTable();
						var player3DataTable = $('#player3').DataTable();
						var player4DataTable = $('#player4').DataTable();
						var player5DataTable = $('#player5').DataTable();
						var player6DataTable = $('#player6').DataTable();

						var armiesStockOfPlayer1 = 0;
						var armiesStockOfPlayer2 = 0;
						var armiesStockOfPlayer3 = 0;
						var armiesStockOfPlayer4 = 0;
						var armiesStockOfPlayer5 = 0;
						var armiesStockOfPlayer6 = 0;

						//css change
						$("#countriesDesc_next").css("color", "black");
						$("#countriesDesc_previous").css("color", "black");
						$("#continentsDesc_previous").css("color", "black");
						$("#continentsDesc_next").css("color", "black");

						$
								.ajax({
									type : "GET",
									url : "maps/getAvailableMaps",
									success : function(data) {
										$('#availableMapsName').find('option')
												.remove();
										for (var i = 0; i < data.length; i++) {
											$('#availableMapsName').append(
													$('<option>', {
														value : data[i],
														text : data[i]
													}));
										}
									},
									error : function(XMLHttpRequest,
											textStatus, errorThrown) {
										alert("Failure fetching map");
									}
								});

						function parseMapData(data) {

							continentDataTable.clear().draw();
							for (var i = 0; i < data.continents.length; i++) {
								addRowInContinentDataTable(
										data.continents[i].name,
										data.continents[i].score);
							}

							countryDataTable.clear().draw();
							for (var i = 0; i < data.territories.length; i++) {
								addRowInCountryDataTable(
										data.territories[i].name,
										data.territories[i].continentName,
										data.territories[i].neighbours);
							}

						}

						function addRowInContinentDataTable(column1, column2) {
							continentDataTable.row.add([ column1, column2 ])
									.draw(false);
						}

						function addRowInCountryDataTable(column1, column2,
								column3) {
							countryDataTable.row.add(
									[ column1, column2, column3 ]).draw(false);
						}

						function addRowInPlayerDataTable(playerid, column1,
								column2, column3) {

							switch (playerid) {
							case 1:
								player1DataTable.row.add(
										[ column1, column2, column3 ]).draw(
										false);
								break;
							case 2:
								player2DataTable.row.add(
										[ column1, column2, column3 ]).draw(
										false);
								break;
							case 3:
								player3DataTable.row.add(
										[ column1, column2, column3 ]).draw(
										false);
								break;
							case 4:
								player4DataTable.row.add(
										[ column1, column2, column3 ]).draw(
										false);
								break;
							case 5:
								player5DataTable.row.add(
										[ column1, column2, column3 ]).draw(
										false);
								break;
							case 6:
								player6DataTable.row.add(
										[ column1, column2, column3 ]).draw(
										false);
								break;
							default:
								alert("Incorrect id of player data table");
							}

						}

						function fetchMap() {
							$
									.ajax({
										type : "GET",
										data : $
												.param({
													fileName : $(
															"#availableMapsName option:selected")
															.text()
												}),
										url : "maps/map",
										success : function(data) {
											parseMapData(data);
										},
										error : function(XMLHttpRequest,
												textStatus, errorThrown) {
											alert("Failure loading map");
										}
									});
						}

						function fillAndInitializeDataTable(playerData,
								playerOrder) {
							for (var i = 0; i < playerData.territory_list.length; i++) {
								addRowInPlayerDataTable(
										playerOrder,
										playerData.territory_list[i].territory_name,
										playerData.territory_list[i].continent_name,
										playerData.territory_list[i].number_of_armies);
							}

						}

						function setEachPlayerArmiesStock(playerData,
								playerOrder) {
							switch (playerOrder) {
							case 1:
								armiesStockOfPlayer1 = playerData.army_stock;
								break;
							case 2:
								armiesStockOfPlayer2 = playerData.army_stock;
								break;
							case 3:
								armiesStockOfPlayer3 = playerData.army_stock;
								break;
							case 4:
								armiesStockOfPlayer4 = playerData.army_stock;
								break;
							case 5:
								armiesStockOfPlayer5 = playerData.army_stock;
								break;
							case 6:
								armiesStockOfPlayer6 = playerData.army_stock;
								break;
							}
						}

						function getEachPlayerArmiesStock(playerNo) {
							switch (playerNo) {
							case 1:
								return armiesStockOfPlayer1;
							case 2:
								return armiesStockOfPlayer2;
							case 3:
								return armiesStockOfPlayer3;
							case 4:
								return armiesStockOfPlayer4;
							case 5:
								return armiesStockOfPlayer5;
							case 6:
								return armiesStockOfPlayer6;
							}
						}

						function findAndFillPlayerData(data, playerOrder) {
							for (var i = 0; i < data.length; i++) {
								if (playerOrder == data[i].id) {
									fillAndInitializeDataTable(data[i],
											playerOrder);
									setEachPlayerArmiesStock(data[i],
											playerOrder);
									return;
								}
							}
						}

						function parseGamePlayData(data) {
							var noOfPlayers = $("#noOfPlayer option:selected")
									.val();
							for (var i = 1; i <= noOfPlayers; i++) {
								findAndFillPlayerData(data, i);
							}
							//hide Extra Player Data table
							switch (noOfPlayers) {
							case "2":
								$("#p3").hide();
							case "3":
								$("#p4").hide();
							case "4":
								$("#p5").hide();
							case "5":
								$("#p6").hide();
							}
						}

						function fetchDataTableforCurrentPlayer(whichPlayer) {
							switch (String(whichPlayer)) {
							case "1":
								return player1DataTable;
							case "2":
								return player2DataTable;
							case "3":
								return player3DataTable;
							case "4":
								return player4DataTable;
							case "5":
								return player5DataTable;
							case "6":
								return player6DataTable;
							}
						}

						function checkIfAnyOtherCountriesContainNoArmy(
								currentPlayerDataTable) {
							var countrySelected = $(
									"#countriesForArmies option:selected")
									.val();
							var data = currentPlayerDataTable.rows().data();
							for (var i = 0; i < data.length; i++) {
								if (data[i][0] != countrySelected
										&& data[i][2] == 0) {
									return true;
								}
							}
							return false;
						}

						function checkIfCountryAlreadyContainAnyArmy(
								currentPlayerDataTable) {
							var countrySelected = $(
									"#countriesForArmies option:selected")
									.val();
							var data = currentPlayerDataTable.rows().data();
							for (var i = 0; i < data.length; i++) {
								if (data[i][0] == countrySelected
										&& data[i][2] > 0) {
									return true;
								}
							}
							return false;
						}

						function validateArmyAllocation() {
							var whichPlayer = $("#playerNo").text();
							var currentPlayerArmies = $("#RemainingArmies")
									.text();
							var currentPlayerDataTable = fetchDataTableforCurrentPlayer(whichPlayer);
							if (checkIfCountryAlreadyContainAnyArmy(currentPlayerDataTable)
									&& checkIfAnyOtherCountriesContainNoArmy(currentPlayerDataTable)) {
								return false;
							} else {
								return true;
							}
						}

						function addArmy(currentPlayerDataTable, country) {
							var lCountry;
							var lContinent;
							var lArmies;
							var data = currentPlayerDataTable.rows().data();
							for (var i = 0; i < data.length; i++) {
								if (data[i][0] == country) {
									lCountry = data[i][0];
									lContinent = data[i][1];
									lArmies = data[i][2];
									break;
								}
							}
							currentPlayerDataTable.row(
									function(idx, data, node) {
										return data[0] === country;
									}).remove().draw(false);
							lArmies = lArmies + 1;
							currentPlayerDataTable.row.add(
									[ lCountry, lContinent, lArmies ]).draw(
									false);
						}

						function addArmyToPlayerChosenCountry() {
							var whichPlayer = $("#playerNo").text();
							var currentPlayerArmies = $("#RemainingArmies")
									.text();
							var currentPlayerDataTable = fetchDataTableforCurrentPlayer(whichPlayer);
							var countrySelected = $(
									"#countriesForArmies option:selected")
									.val();
							addArmy(currentPlayerDataTable, countrySelected);

							//decrease current player armies stock by one
							switch (String(whichPlayer)) {
							case "1":
								armiesStockOfPlayer1 = armiesStockOfPlayer1 - 1;
								break;
							case "2":
								armiesStockOfPlayer2 = armiesStockOfPlayer2 - 1;
								break;
							case "3":
								armiesStockOfPlayer3 = armiesStockOfPlayer3 - 1;
								break;
							case "4":
								armiesStockOfPlayer4 = armiesStockOfPlayer4 - 1;
								break;
							case "5":
								armiesStockOfPlayer5 = armiesStockOfPlayer5 - 1;
								break;
							case "6":
								armiesStockOfPlayer6 = armiesStockOfPlayer6 - 1;
								break;
							}
						}

						function checkAndDisplayIfMoreArmyAllocationNeeded(
								newPlayerNo) {
							var currentPlayerDataTable = fetchDataTableforCurrentPlayer(newPlayerNo);
							var currentPlayerArmyStock = getEachPlayerArmiesStock(newPlayerNo);
							if (currentPlayerArmyStock > 0) {
								currentPlayerDTable = currentPlayerDataTable
										.rows().data();
								armySelectionInStartupPhase(
										currentPlayerDTable,
										currentPlayerArmyStock);
								alert("Player " + newPlayerNo
										+ " - Allocate your army");
							} else {
								$('#mapSelectArmy').modal('toggle');
								alert("army selection successfull");
							}
						}

						function calculateNextPlayerNo(whichPlayer) {
							var noOfPlayer = $("#noOfPlayer option:selected")
									.val();
							if (whichPlayer == noOfPlayer) {
								whichPlayer = 1;
							} else {
								++whichPlayer;
							}
							return whichPlayer;
						}

						$('#armiesSelectionDone')
								.on(
										'click',
										function() {
											if (!validateArmyAllocation()) {
												alert("Please add atleast one army to countries which doesnot have any army yet");
												return;
											}
											addArmyToPlayerChosenCountry();
											//set next Player no
											var whichPlayer = $("#playerNo")
													.text();
											var newPlayerNo = calculateNextPlayerNo(whichPlayer);
											$("#playerNo").text(newPlayerNo);
											checkAndDisplayIfMoreArmyAllocationNeeded(newPlayerNo);
										});

						function armySelectionInStartupPhase(playerDTable,
								remaingArmies) {
							$('#countriesForArmies').find('option').remove();
							$("#RemainingArmies").text(remaingArmies);
							for (var i = 0; i < playerDTable.length; i++) {
								$('#countriesForArmies').append($('<option>', {
									value : playerDTable[i][0],
									text : playerDTable[i][0]
								}));
							}
						}

						function startArmyAllocation() {
							var noOfPlayers = $("#noOfPlayer option:selected")
									.val();
							$("#playerNo").text("1");
							player1DTable = player1DataTable.rows().data();
							armySelectionInStartupPhase(player1DTable,
									armiesStockOfPlayer1);
							$('#mapSelectArmy').modal({
								backdrop : 'static',
								keyboard : false
							});
						}

						function initStartUpPhase() {
							showLoading();
							$
									.ajax({
										type : "GET",
										data : $
												.param({
													playersNo : $(
															"#noOfPlayer option:selected")
															.text(),
													fileName : $(
															"#availableMapsName option:selected")
															.text()
												}),
										url : "gamePlay/initStartUpPhase",
										success : function(data) {
											parseGamePlayData(data);
											stopLoading();
											startArmyAllocation();
										},
										error : function(XMLHttpRequest,
												textStatus, errorThrown) {
											stopLoading();
											alert("Startup Phase Failure");
										}
									});
						}

						$('#playNow').on('click', function() {
							fetchMap();
							initStartUpPhase();
							$('#mapSelectModal').modal('toggle');
						});

					});
</script>

</head>
<body>
	<h1>Map</h1>
	<div>
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
		<div>
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
	</div>
	<div>
		<h2>Game Play</h2>
		<div id="p1">
			<h3>Player 1 :</h3>
			<table id="player1" class="display" style="width: 100%">
				<thead>
					<tr>
						<th>country</th>
						<th>continent</th>
						<th>armies</th>
					</tr>
				</thead>
				<tfoot>
					<tr>
						<th>country</th>
						<th>continent</th>
						<th>armies</th>
					</tr>
				</tfoot>
			</table>
		</div>
		<div id="p2">
			<h3>Player 2 :</h3>
			<table id="player2" class="display" style="width: 100%">
				<thead>
					<tr>
						<th>country</th>
						<th>continent</th>
						<th>armies</th>
					</tr>
				</thead>
				<tfoot>
					<tr>
						<th>country</th>
						<th>continent</th>
						<th>armies</th>
					</tr>
				</tfoot>
			</table>
		</div>
		<div id="p3">
			<h3>Player 3 :</h3>
			<table id="player3" class="display" style="width: 100%">
				<thead>
					<tr>
						<th>country</th>
						<th>continent</th>
						<th>armies</th>
					</tr>
				</thead>
				<tfoot>
					<tr>
						<th>country</th>
						<th>continent</th>
						<th>armies</th>
					</tr>
				</tfoot>
			</table>
		</div>
		<div id="p4">
			<h3>Player 4 :</h3>
			<table id="player4" class="display" style="width: 100%">
				<thead>
					<tr>
						<th>country</th>
						<th>continent</th>
						<th>armies</th>
					</tr>
				</thead>
				<tfoot>
					<tr>
						<th>country</th>
						<th>continent</th>
						<th>armies</th>
					</tr>
				</tfoot>
			</table>
		</div>
		<div id="p5">
			<h3>Player 5 :</h3>
			<table id="player5" class="display" style="width: 100%">
				<thead>
					<tr>
						<th>country</th>
						<th>continent</th>
						<th>armies</th>
					</tr>
				</thead>
				<tfoot>
					<tr>
						<th>country</th>
						<th>continent</th>
						<th>armies</th>
					</tr>
				</tfoot>
			</table>
		</div>
		<div id="p6">
			<h3>Player 6 :</h3>
			<table id="player6" class="display" style="width: 100%">
				<thead>
					<tr>
						<th>country</th>
						<th>continent</th>
						<th>armies</th>
					</tr>
				</thead>
				<tfoot>
					<tr>
						<th>country</th>
						<th>continent</th>
						<th>armies</th>
					</tr>
				</tfoot>
			</table>
		</div>
	</div>

	<!-- Modal map selection -->
	<div class="modal fade" id="mapSelectModal" tabindex="-1" role="dialog"
		aria-labelledby="mapSelectModalTitle" aria-hidden="true">
		<div class="modal-dialog modal-dialog-centered" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="exampleModalLongTitle">Select Map
						and Players</h5>

				</div>
				<div class="modal-body">
					<div class="form-group">
						<label for="availableMapsName">Map : </label> <select
							class="form-control form-control-sm" id="availableMapsName">
							<option></option>
						</select>
					</div>
					<div class="form-group">
						<label for="continentName">No of Players : </label> <select
							class="form-control form-control-sm" id="noOfPlayer">
							<option value="2">2</option>
							<option value="3">3</option>
							<option value="4">4</option>
							<option value="5">5</option>
							<option value="6">6</option>
						</select>
					</div>
				</div>
				<div class="modal-footer">
					<button id="playNow" type="button" class="btn btn-primary"
						style="background-color: black; border-color: black">Play
						Now</button>
				</div>
			</div>
		</div>
	</div>

	<!-- Modal Army allocation -->
	<div class="modal fade" id="mapSelectArmy" tabindex="-1" role="dialog"
		aria-labelledby="mapSelectModalTitle" aria-hidden="true">
		<div class="modal-dialog modal-dialog-centered" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="exampleModalLongTitle">Allocate
						army</h5>
				</div>
				<div class="modal-body">
					<div>
						<label for="playerNo">Player No</label> <span id="playerNo"></span>
					</div>
					<label for="RemainingArmies">Remaining Armies : </label> <span
						id="RemainingArmies"></span>
					<p>Please select your country to allocate one army</p>
					<label for="countriesForArmies">Countries : </label> <select
						class="form-control form-control-sm" id="countriesForArmies">
						<option></option>
					</select>
				</div>
				<div class="modal-footer">
					<button id="armiesSelectionDone" type="button"
						class="btn btn-primary"
						style="background-color: black; border-color: black">Done</button>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
</html>