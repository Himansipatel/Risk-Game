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
						
						$("#armiesToShift").keydown(function (e) {
					        // Allow: backspace, delete, tab, escape, enter and .
					        if ($.inArray(e.keyCode, [46, 8, 9, 27, 13, 110]) !== -1 ||
					             // Allow: Ctrl+A, Command+A
					            (e.keyCode === 65 && (e.ctrlKey === true || e.metaKey === true)) || 
					             // Allow: home, end, left, right, down, up
					            (e.keyCode >= 35 && e.keyCode <= 40)) {
					                 // let it happen, don't do anything
					                 return;
					        }
					        // Ensure that it is a number and stop the keypress
					        if ((e.shiftKey || (e.keyCode < 48 || e.keyCode > 57)) && (e.keyCode < 96 || e.keyCode > 105)) {
					            e.preventDefault();
					        }
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

						var idPlayer1 = "";
						var idPlayer2 = "";
						var idPlayer3 = "";
						var idPlayer4 = "";
						var idPlayer5 = "";
						var idPlayer6 = "";

						var namePlayer1 = "";
						var namePlayer2 = "";
						var namePlayer3 = "";
						var namePlayer4 = "";
						var namePlayer5 = "";
						var namePlayer6 = "";

						var currentPhase = "REINFORCEMENT";
						var currentMapName = "";
						var noOfPlayingPlayer = "";
						var whichPlayerChance = 0;
						var data_game;

						$("#player1Reinforcement").attr("disabled", "disabled");
						$("#player2Reinforcement").attr("disabled", "disabled");
						$("#player3Reinforcement").attr("disabled", "disabled");
						$("#player4Reinforcement").attr("disabled", "disabled");
						$("#player5Reinforcement").attr("disabled", "disabled");
						$("#player6Reinforcement").attr("disabled", "disabled");
						$("#player1Attack").attr("disabled", "disabled");
						$("#player2Attack").attr("disabled", "disabled");
						$("#player3Attack").attr("disabled", "disabled");
						$("#player4Attack").attr("disabled", "disabled");
						$("#player5Attack").attr("disabled", "disabled");
						$("#player6Attack").attr("disabled", "disabled");
						$("#player1Fortification").attr("disabled", "disabled");
						$("#player2Fortification").attr("disabled", "disabled");
						$("#player3Fortification").attr("disabled", "disabled");
						$("#player4Fortification").attr("disabled", "disabled");
						$("#player5Fortification").attr("disabled", "disabled");
						$("#player6Fortification").attr("disabled", "disabled");

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

						function clearGameState() {
							var noOfPlayers = $("#noOfPlayer option:selected")
									.val();
							switch (String(noOfPlayers)) {
							case "6":
								player6DataTable.clear().draw();
							case "5":
								player5DataTable.clear().draw();
							case "4":
								player4DataTable.clear().draw();
							case "3":
								player3DataTable.clear().draw();
							case "2":
								player2DataTable.clear().draw();
							case "1":
								player1DataTable.clear().draw();
							}
						}

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
											if (data != null) {
												if (data.status == null
														|| data.status == '') {
													parseMapData(data);
												}
												if (data.status != null
														&& data.status != '') {
													alert(data.status);
												}
											} else {
												alert("Error while loading map");
											}
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

						function setEachPlayerIdAndNameAndArmiesStock(
								playerData, playerOrder) {
							switch (playerOrder) {
							case 1:
								armiesStockOfPlayer1 = playerData.army_stock;
								idPlayer1 = playerData.id;
								namePlayer1 = playerData.name;
								break;
							case 2:
								armiesStockOfPlayer2 = playerData.army_stock;
								idPlayer2 = playerData.id;
								namePlayer2 = playerData.name;
								break;
							case 3:
								armiesStockOfPlayer3 = playerData.army_stock;
								idPlayer3 = playerData.id;
								namePlayer3 = playerData.name;
								break;
							case 4:
								armiesStockOfPlayer4 = playerData.army_stock;
								idPlayer4 = playerData.id;
								namePlayer4 = playerData.name;
								break;
							case 5:
								armiesStockOfPlayer5 = playerData.army_stock;
								idPlayer5 = playerData.id;
								namePlayer5 = playerData.name;
								break;
							case 6:
								armiesStockOfPlayer6 = playerData.army_stock;
								idPlayer6 = playerData.id;
								namePlayer6 = playerData.name;
								break;
							}
						}

						function getEachPlayerArmiesStock(playerNo) {
							switch (String(playerNo)) {
							case "1":
								return armiesStockOfPlayer1;
							case "2":
								return armiesStockOfPlayer2;
							case "3":
								return armiesStockOfPlayer3;
							case "4":
								return armiesStockOfPlayer4;
							case "5":
								return armiesStockOfPlayer5;
							case "6":
								return armiesStockOfPlayer6;
							}
						}

						function getEachPlayerId(playerNo) {
							switch (String(playerNo)) {
							case "1":
								return idPlayer1;
							case "2":
								return idPlayer2;
							case "3":
								return idPlayer3;
							case "4":
								return idPlayer4;
							case "5":
								return idPlayer5;
							case "6":
								return idPlayer6;
							}
						}

						function getEachPlayerName(playerNo) {
							switch (String(playerNo)) {
							case "1":
								return namePlayer1;
							case "2":
								return namePlayer2;
							case "3":
								return namePlayer3;
							case "4":
								return namePlayer4;
							case "5":
								return namePlayer5;
							case "6":
								return namePlayer6;
							}
						}

						function findAndFillPlayerData(data, playerOrder) {
							for (var i = 0; i < data.length; i++) {
								if (playerOrder == data[i].id) {
									fillAndInitializeDataTable(data[i],
											playerOrder);
									setEachPlayerIdAndNameAndArmiesStock(
											data[i], playerOrder);
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
								saveGameState();
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

						function initStartUpPhase(allocationType) {
							var allocType = 'm';
							if (allocationType) {
								allocType = 'a';
							}
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
															.text(),
													allocationType : allocType
												}),
										url : "gamePlay/initStartUpPhase",
										success : function(data) {
											data_game = data;
											parseGamePlayData(data.game_state);
											currentMapName = (data.file_name);
											//set currentPhase also(later)
											//read player no from data
											noOfPlayingPlayer = $(
													"#noOfPlayer option:selected")
													.val();
											stopLoading();
											if (!allocationType) {
												startArmyAllocation();
											}
										},
										error : function(XMLHttpRequest,
												textStatus, errorThrown) {
											stopLoading();
											alert("Startup Phase Failure");
										}
									});
						}

						$('#manualAllocate').on('click', function() {
							fetchMap();
							initStartUpPhase(false);
							$('#mapSelectModal').modal('toggle');
							whichPlayerChance = 1;
							//armiesStockOfPlayer1 = 3;
							checkForNextPhaseAndDisplayOption();
						});

						$('#autoAllocate').on('click', function() {
							fetchMap();
							initStartUpPhase(true);
							$('#mapSelectModal').modal('toggle');
							whichPlayerChance = 1;
							//armiesStockOfPlayer1 = 3;
							checkForNextPhaseAndDisplayOption();
						});

						$('#check').on('click', function() {
							saveGameState();
						});

						function makePlayerData(playerNo) {
							var countryArray = [];
							var playerCountryTable = fetchDataTableforCurrentPlayer(playerNo);
							var data = playerCountryTable.rows().data();
							for (var i = 0; i < data.length; i++) {
								countryArray.push({
									territory_name : data[i][0],
									continent_name : data[i][1],
									number_of_armies : data[i][2]
								});
							}
							var playerObject = {
								id : getEachPlayerId(playerNo),
								name : getEachPlayerName(playerNo),
								army_stock : getEachPlayerArmiesStock(playerNo),
								territory_list : countryArray
							};
							return playerObject;
						}

						function saveGameState() {
							showLoading();
							var playerArray = [];
							for (var i = 1; i <= noOfPlayingPlayer; i++) {
								var playerD = makePlayerData(i);
								playerArray.push(playerD);
							}

							var game_Play = {
								game_state : playerArray,
								file_name : currentMapName,
								game_phase : currentPhase,
								fortify : data_game.fortify,
								map : data_game.map,
								current_player : data_game.current_player,
								free_cards : data_game.free_cards,
								status : '',
								card_trade : data_game.card_trade,
								attack : data_game.attack,
								fortification : data_game.fortification
							};
							var a = JSON.stringify(game_Play);
							$
									.ajax({
										type : "POST",
										url : "gamePlay/saveGameState",
										dataType : "json",
										data : a,
										contentType : "application/json",
										success : function(data) {
											data_game = data;
											clearGameState();
											parseGamePlayData(data.game_state);
											currentMapName = (data.file_name);
											
											//read player no from data
											if (currentPhase == 'ATTACK'
													|| currentPhase == "ATTACK_ON"
													|| currentPhase == "ATTACK_ALL_OUT"
													|| currentPhase == "ATTACK_END") {
												if(data.status != null && data.status != ''){
													alert(data.status);
												}
											}
											if (currentPhase != 'ATTACK'
													&& currentPhase != "ATTACK_ON"
													&& currentPhase != "ATTACK_ALL_OUT" 
													&& currentPhase != "FORTIFICATION") {
												alert(currentPhase + " ended");
											}
											currentPhase = data.game_phase;
											whichPlayerChance = data.current_player;
											checkForNextPhaseAndDisplayOption();
											stopLoading();
										},
										error : function(XMLHttpRequest,
												textStatus, errorThrown) {
											stopLoading();
											alert("Invalid GameState. Please check");
										}
									});
						}

						function displayReinforcementButtonForPlayer() {
							switch (String(whichPlayerChance)) {
							case "1":
								$("#player1Reinforcement").removeAttr(
										"disabled");
								break;
							case "2":
								$("#player2Reinforcement").removeAttr(
										"disabled");
								break;
							case "3":
								$("#player3Reinforcement").removeAttr(
										"disabled");
								break;
							case "4":
								$("#player4Reinforcement").removeAttr(
										"disabled");
								break;
							case "5":
								$("#player5Reinforcement").removeAttr(
										"disabled");
								break;
							case "6":
								$("#player6Reinforcement").removeAttr(
										"disabled");
								break;
							}
						}

						function displayAttackButtonForPlayer() {
							switch (String(whichPlayerChance)) {
							case "1":
								$("#player1Attack").removeAttr("disabled");
								break;
							case "2":
								$("#player2Attack").removeAttr("disabled");
								break;
							case "3":
								$("#player3Attack").removeAttr("disabled");
								break;
							case "4":
								$("#player4Attack").removeAttr("disabled");
								break;
							case "5":
								$("#player5Attack").removeAttr("disabled");
								break;
							case "6":
								$("#player6Attack").removeAttr("disabled");
								break;
							}
						}

						function displayFortificationButtonForPlayer() {
							switch (String(whichPlayerChance)) {
							case "1":
								$("#player1Fortification").removeAttr(
										"disabled");
								break;
							case "2":
								$("#player2Fortification").removeAttr(
										"disabled");
								break;
							case "3":
								$("#player3Fortification").removeAttr(
										"disabled");
								break;
							case "4":
								$("#player4Fortification").removeAttr(
										"disabled");
								break;
							case "5":
								$("#player5Fortification").removeAttr(
										"disabled");
								break;
							case "6":
								$("#player6Fortification").removeAttr(
										"disabled");
								break;
							}
						}

						function hideReinforcementButtonForPlayer() {
							switch (String(whichPlayerChance)) {
							case "1":
								$("#player1Reinforcement").attr("disabled",
										"disabled");
								break;
							case "2":
								$("#player2Reinforcement").attr("disabled",
										"disabled");
								break;
							case "3":
								$("#player3Reinforcement").attr("disabled",
										"disabled");
								break;
							case "4":
								$("#player4Reinforcement").attr("disabled",
										"disabled");
								break;
							case "5":
								$("#player5Reinforcement").attr("disabled",
										"disabled");
								break;
							case "6":
								$("#player6Reinforcement").attr("disabled",
										"disabled");
								break;
							}
						}

						function hideAttackButtonForPlayer() {
							switch (String(whichPlayerChance)) {
							case "1":
								$("#player1Attack")
										.attr("disabled", "disabled");
								break;
							case "2":
								$("#player2Attack")
										.attr("disabled", "disabled");
								break;
							case "3":
								$("#player3Attack")
										.attr("disabled", "disabled");
								break;
							case "4":
								$("#player4Attack")
										.attr("disabled", "disabled");
								break;
							case "5":
								$("#player5Attack")
										.attr("disabled", "disabled");
								break;
							case "6":
								$("#player6Attack")
										.attr("disabled", "disabled");
								break;
							}
						}

						function hideFortificationButtonForPlayer() {
							switch (String(whichPlayerChance)) {
							case "1":
								$("#player1Fortification").attr("disabled",
										"disabled");
								break;
							case "2":
								$("#player2Fortification").attr("disabled",
										"disabled");
								break;
							case "3":
								$("#player3Fortification").attr("disabled",
										"disabled");
								break;
							case "4":
								$("#player4Fortification").attr("disabled",
										"disabled");
								break;
							case "5":
								$("#player5Fortification").attr("disabled",
										"disabled");
								break;
							case "6":
								$("#player6Fortification").attr("disabled",
										"disabled");
								break;
							}
						}

						function checkForNextPhaseAndDisplayOption() {
							if (currentPhase == "ATTACK"
									|| currentPhase == "ATTACK_ON"
									|| currentPhase == "ATTACK_ALL_OUT"
									|| currentPhase == "ATTACK_END") {
								displayAttackButtonForPlayer();
							} else if (currentPhase == "FORTIFICATION" || currentPhase == "FORTIFICATION_END") {
								displayFortificationButtonForPlayer();
							} else if (currentPhase == "REINFORCEMENT") {
								displayReinforcementButtonForPlayer();
							}
						}

						function fillReinforcementModal(no) {
							//clear modal values
							$('#countriesForReinforcement').find('option')
									.remove();

							//fill modal values
							$('#playerIdReinforcement').val(no);
							var playerTable = fetchDataTableforCurrentPlayer(no);
							playerTableData = playerTable.rows().data();
							playerArmiesStock = getEachPlayerArmiesStock(no);
							$("#reinforcementRemainingArmies").text(
									playerArmiesStock);
							for (var i = 0; i < playerTableData.length; i++) {
								$('#countriesForReinforcement').append(
										$('<option>', {
											value : playerTableData[i][0],
											text : playerTableData[i][0]
										}));
							}
						}

						$('#player1Reinforcement').on('click', function() {
							fillReinforcementModal("1");
							$('#reinforcementModal').modal({
								backdrop : 'static',
								keyboard : false
							});
						});
						$('#player2Reinforcement').on('click', function() {
							fillReinforcementModal("2");
							$('#reinforcementModal').modal({
								backdrop : 'static',
								keyboard : false
							});
						})
						$('#player3Reinforcement').on('click', function() {
							fillReinforcementModal("3");
							$('#reinforcementModal').modal({
								backdrop : 'static',
								keyboard : false
							});
						})
						$('#player4Reinforcement').on('click', function() {
							fillReinforcementModal("4");
							$('#reinforcementModal').modal({
								backdrop : 'static',
								keyboard : false
							});
						})
						$('#player5Reinforcement').on('click', function() {
							fillReinforcementModal("5");
							$('#reinforcementModal').modal({
								backdrop : 'static',
								keyboard : false
							});
						})
						$('#player6Reinforcement').on('click', function() {
							fillReinforcementModal("6");
							$('#reinforcementModal').modal({
								backdrop : 'static',
								keyboard : false
							});
						});

						function fillAttackModal(no) {
							//clear modal values
							$('#countriesFromAttack').find('option').remove();
							$('#countriesForAttack').find('option').remove();

							//fill modal values
							var playerTable = fetchDataTableforCurrentPlayer(no);
							playerTableData = playerTable.rows().data();
							for (var i = 0; i < playerTableData.length; i++) {
								$('#countriesFromAttack').append(
										$('<option>', {
											value : playerTableData[i][0],
											text : playerTableData[i][0]
										}));
								if (i == 0) {
									fillAttackModalCountriesForAttack(playerTableData[i][0]);
								}
							}
						}

						function fillAttackModalCountriesForAttack(
								sourceCountriesSelected) {
							//clear modal values
							$('#countriesForAttack').find('option').remove();
							var countryData = countryDataTable.rows().data();
							//find neighbours
							for (var i = 0; i < countryData.length; i++) {
								if (countryData[i][0] == sourceCountriesSelected) {
									var array = countryData[i][2].split(';');
									for (var j = 0; j < array.length; j++) {
										$('#countriesForAttack').append(
												$('<option>', {
													value : array[j],
													text : array[j]
												}));
									}
									break;
								}
							}
						}

						$("#countriesFromAttack")
								.change(
										function() {
											var sourceCountriesSelected = $(
													"#countriesFromAttack option:selected")
													.val();
											fillAttackModalCountriesForAttack(sourceCountriesSelected);
										});

						$('#player1Attack').on('click', function() {
							fillAttackModal("1");
							$('#attackModal').modal({
								backdrop : 'static',
								keyboard : false
							});
						});
						$('#player2Attack').on('click', function() {
							fillAttackModal("2");
							$('#attackModal').modal({
								backdrop : 'static',
								keyboard : false
							});
						})
						$('#player3Attack').on('click', function() {
							fillAttackModal("3");
							$('#attackModal').modal({
								backdrop : 'static',
								keyboard : false
							});
						})
						$('#player4Attack').on('click', function() {
							fillAttackModal("4");
							$('#attackModal').modal({
								backdrop : 'static',
								keyboard : false
							});
						})
						$('#player5Attack').on('click', function() {
							fillAttackModal("5");
							$('#attackModal').modal({
								backdrop : 'static',
								keyboard : false
							});
						})
						$('#player6Attack').on('click', function() {
							fillAttackModal("6");
							$('#attackModal').modal({
								backdrop : 'static',
								keyboard : false
							});
						});

						function fillFortificationModal(no) {
							//clear modal values
							$('#countriesForFortificationSource')
									.find('option').remove();
							$('#countriesForFortificationDestination').find(
									'option').remove();
							$("#armiesToShift").val("");
							//fill modal values
							var playerTable = fetchDataTableforCurrentPlayer(no);
							playerTableData = playerTable.rows().data();
							for (var i = 0; i < playerTableData.length; i++) {
								$('#countriesForFortificationDestination')
										.append($('<option>', {
											value : playerTableData[i][0],
											text : playerTableData[i][0]
										}));
								$('#countriesForFortificationSource').append(
										$('<option>', {
											value : playerTableData[i][0],
											text : playerTableData[i][0]
										}));
							}
						}

						$('#player1Fortification').on('click', function() {
							fillFortificationModal("1");
							$('#fortificationModal').modal({
								backdrop : 'static',
								keyboard : false
							});
						});
						$('#player2Fortification').on('click', function() {
							fillFortificationModal("2");
							$('#fortificationModal').modal({
								backdrop : 'static',
								keyboard : false
							});
						})
						$('#player3AFortification').on('click', function() {
							fillFortificationModal("3");
							$('#fortificationModal').modal({
								backdrop : 'static',
								keyboard : false
							});
						})
						$('#player4Fortification').on('click', function() {
							fillFortificationModal("4");
							$('#fortificationModal').modal({
								backdrop : 'static',
								keyboard : false
							});
						})
						$('#player5Fortification').on('click', function() {
							fillFortificationModal("5");
							$('#fortificationModal').modal({
								backdrop : 'static',
								keyboard : false
							});
						})
						$('#player6Fortification').on('click', function() {
							fillFortificationModal("6");
							$('#fortificationModal').modal({
								backdrop : 'static',
								keyboard : false
							});
						});
						
						function decreaseArmiesStockOfCurrentPlayer(no){
							switch (String(no)) {
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

						$('#armiesSelectionForReinforcementDone')
								.on(
										'click',
										function() {
										var no = $('#playerIdReinforcement').val();
										playerArmiesStock = getEachPlayerArmiesStock(no);
										var playerTable = fetchDataTableforCurrentPlayer(no);
										playerTableData = playerTable.rows().data();
											if (playerArmiesStock > 0) {
												playerArmiesStock =playerArmiesStock -1;
												decreaseArmiesStockOfCurrentPlayer(no);
												var country = $(
														"#countriesForReinforcement option:selected")
														.val();
												addArmy(playerTableData,
														country);
												$(
														"#reinforcementRemainingArmies")
														.text(
																playerArmiesStock);
												if (playerArmiesStock == 0) {
													$('#reinforcementModal')
															.modal('toggle');
													hideReinforcementButtonForPlayer();
													saveGameState();
												}
												return;
											}
											$('#reinforcementModal').modal(
													'toggle');
											hideReinforcementButtonForPlayer();
											saveGameState();
										});

						function attack(attackType) {
							data_game.attack = {};
							data_game.attack.attacker_territory = $(
									"#countriesFromAttack option:selected")
									.val();
							data_game.attack.defender_territory = $(
									"#countriesForAttack option:selected")
									.val();
							data_game.attack.attacker_dice_no = $(
							"#noOfDiceAttacker option:selected")
							.val();
							data_game.attack.defender_dice_no = $(
							"#noOfDiceDefender option:selected")
							.val();
							currentPhase = attackType;
							$('#attackModal').modal('toggle');
							//hideReinforcementButtonForPlayer();
							saveGameState();
						}

						$('#attack').on('click', function() {
							attack('ATTACK_ON');
						});
						$('#attackAllOut').on('click', function() {
							attack('ATTACK_ALL_OUT');
						});
						$('#attackEnd').on('click', function() {
							hideAttackButtonForPlayer();
							attack('ATTACK_END');

						});
						
						function fortify(fortifyType) {
							data_game.fortification = {};
							data_game.fortification.source_territory = $(
									"#countriesForFortificationSource option:selected")
									.val();
							data_game.fortification.destination_territory = $(
									"#countriesForFortificationDestination option:selected")
									.val();
							data_game.fortification.army_count = $(
									"#armiesToShift").val();
							currentPhase = fortifyType;
							$('#fortificationModal').modal('toggle');
							saveGameState();
						}
						
						$('#fortify').on('click', function() {
							fortify('FORTIFICATION');
						});
						$('#fortifyEnd').on('click', function() {
							hideFortificationButtonForPlayer();
							fortify('FORTIFICATION_END');

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
			<button id="player1Reinforcement" type="button"
				class="btn btn-primary"
				style="background-color: black; border-color: black">Reinforce</button>
			<button id="player1Attack" type="button" class="btn btn-primary"
				style="background-color: black; border-color: black">Attack</button>
			<button id="player1Fortification" type="button"
				class="btn btn-primary"
				style="background-color: black; border-color: black">Fortification</button>
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
			<button id="player2Reinforcement" type="button"
				class="btn btn-primary"
				style="background-color: black; border-color: black">Reinforce</button>
			<button id="player2Attack" type="button" class="btn btn-primary"
				style="background-color: black; border-color: black">Attack</button>
			<button id="player2Fortification" type="button"
				class="btn btn-primary"
				style="background-color: black; border-color: black">Fortification</button>
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
			<button id="player3Reinforcement" type="button"
				class="btn btn-primary"
				style="background-color: black; border-color: black">Reinforce</button>
			<button id="player3Attack" type="button" class="btn btn-primary"
				style="background-color: black; border-color: black">Attack</button>
			<button id="player3Fortification" type="button"
				class="btn btn-primary"
				style="background-color: black; border-color: black">Fortification</button>
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
			<button id="player4Reinforcement" type="button"
				class="btn btn-primary"
				style="background-color: black; border-color: black">Reinforce</button>
			<button id="player4Attack" type="button" class="btn btn-primary"
				style="background-color: black; border-color: black">Attack</button>
			<button id="player4Fortification" type="button"
				class="btn btn-primary"
				style="background-color: black; border-color: black">Fortification</button>
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
			<button id="player5Reinforcement" type="button"
				class="btn btn-primary"
				style="background-color: black; border-color: black">Reinforce</button>
			<button id="player5Attack" type="button" class="btn btn-primary"
				style="background-color: black; border-color: black">Attack</button>
			<button id="player5Fortification" type="button"
				class="btn btn-primary"
				style="background-color: black; border-color: black">Fortification</button>
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
			<button id="player6Reinforcement" type="button"
				class="btn btn-primary"
				style="background-color: black; border-color: black">Reinforce</button>
			<button id="player6Attack" type="button" class="btn btn-primary"
				style="background-color: black; border-color: black">Attack</button>
			<button id="player6Fortification" type="button"
				class="btn btn-primary"
				style="background-color: black; border-color: black">Fortification</button>
		</div>
	</div>

	<!-- Modal map selection -->
	<div class="modal fade" id="mapSelectModal" tabindex="-1" role="dialog"
		aria-labelledby="mapSelectModalTitle" aria-hidden="true">
		<div class="modal-dialog modal-dialog-centered" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="exampleModalLongTitle">Startup
						Phase</h5>

				</div>
				<div class="modal-body">
					<h6>Select Map and Players :</h6>
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
					<button id="autoAllocate" type="button" class="btn btn-primary"
						style="background-color: black; border-color: black"
						data-toggle="tooltip" data-placement="top"
						title="random allocation of initial armies">Auto Allocate</button>
					<button id="manualAllocate" type="button" class="btn btn-primary"
						style="background-color: black; border-color: black"
						data-toggle="tooltip" data-placement="top"
						title="manual allocation of initial armies">Manual
						Allocate</button>
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

	<!-- Modal Reinforcement -->
	<div class="modal fade" id="reinforcementModal" tabindex="-1"
		role="dialog" aria-labelledby="reinforcementModalTitle"
		aria-hidden="true">
		<div class="modal-dialog modal-dialog-centered" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="exampleModalLongTitle">Reinforcement
						Phase</h5>
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					<div>
						<div style="display: none;">
							<label for="playerIdReinforcement">Player id : </label> <input
								type="text" class="form-control" id="playerIdReinforcement">
						</div>
						<label for="ReinforcementPlayerNo">Allocate the armies for
							reinforcement</label>
					</div>
					<label for="reinforcementRemainingArmies">Remaining Armies
						: </label> <span id="reinforcementRemainingArmies"></span>
					<p>Please select your country to allocate one army</p>
					<label for="countriesForReinforcement">Countries : </label> <select
						class="form-control form-control-sm"
						id="countriesForReinforcement">
						<option></option>
					</select>
				</div>
				<div class="modal-footer">
					<button id="armiesSelectionForReinforcementDone" type="button"
						class="btn btn-primary"
						style="background-color: black; border-color: black">Reinforce</button>
				</div>
			</div>
		</div>
	</div>

	<!-- Modal Attack -->
	<div class="modal fade" id="attackModal" tabindex="-1" role="dialog"
		aria-labelledby="attackModalTitle" aria-hidden="true">
		<div class="modal-dialog modal-dialog-centered" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="exampleModalLongTitle">Attack
						Phase</h5>
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					<div>
						<label></label>
					</div>
					<div class='row'>
						<div class='col-sm-6'>
							<p>Please select your country from where you want to attack</p>
							<label for="countriesFromAttack">Countries from attack: </label>
							<select class="form-control form-control-sm"
								id="countriesFromAttack">
								<option></option>
							</select>
						</div>
						<div class='col-sm-6'>
							<p>Please select your country to attack</p>
							<label for="countriesForAttack">Countries to attack : </label> <select
								class="form-control form-control-sm" id="countriesForAttack">
								<option></option>
							</select>
						</div>
					</div>
					<div class='row'>
						<div class='col-sm-6'>
							<label for="noOfDiceAttacker">Number of Dice (Attacker) :
							</label> <select class="form-control form-control-sm"
								id="noOfDiceAttacker">
								<option value='1'>1</option>
								<option value='2'>2</option>
								<option value='3'>3</option>
							</select>
						</div>
						<div class='col-sm-6'>
							<label for="noOfDiceDefender">Number of Dice (Defender) :
							</label> <select class="form-control form-control-sm"
								id="noOfDiceDefender">
								<option value='1'>1</option>
								<option value='2'>2</option>
							</select>
						</div>
					</div>
				</div>

				<div class="modal-footer">
					<button id="attack" type="button" class="btn btn-primary"
						style="background-color: black; border-color: black">Attack</button>
					<button id="attackAllOut" type="button" class="btn btn-primary"
						style="background-color: black; border-color: black">All
						out</button>
					<button id="attackEnd" type="button" class="btn btn-primary"
						style="background-color: black; border-color: black">Attack
						End</button>
				</div>
			</div>
		</div>
	</div>

	<!-- Modal Fortification -->
	<div class="modal fade" id="fortificationModal" tabindex="-1"
		role="dialog" aria-labelledby="fortificationModalTitle"
		aria-hidden="true">
		<div class="modal-dialog modal-dialog-centered" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="exampleModalLongTitle">Fortification
						Phase</h5>
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					<div>
						<label for="FortificationPlayerNo">shift your army for
							fortification phase</label>
					</div>
					<p>Please select your country to allocate one army</p>
					<label for="countriesForFortificationSource">Source
						Countries : </label> <select class="form-control form-control-sm"
						id="countriesForFortificationSource">
						<option></option>
					</select><label for="armiesToShift">Armies to shift : </label> <input
						type="text" class="form-control" id="armiesToShift"> <label
						for="countriesForFortificationDestination">Destination
						Countries : </label> <select class="form-control form-control-sm"
						id="countriesForFortificationDestination">
						<option></option>
					</select>
				</div>
				<div class="modal-footer">
					<button id="fortify" type="button" class="btn btn-primary"
						style="background-color: black; border-color: black">Fortify</button>
					<button id="fortifyEnd" type="button" class="btn btn-primary"
						style="background-color: black; border-color: black">Fortify
						End</button>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
</html>