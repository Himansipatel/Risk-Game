<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">



<script type="text/javascript">
	$(document).ready(function() {
		$('#choicesSelectModal').modal({
			backdrop : 'static',
			keyboard : false
		});
	});
</script>

</head>
<body>
	<!-- Modal map selection -->
	<div class="modal fade" id="choicesSelectModal" tabindex="-1"
		role="dialog" aria-labelledby="choicesSelectModalTitle"
		aria-hidden="true">
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
						<label for="noOfPlayer">No of Players : </label> <select
							class="form-control form-control-sm" id="noOfPlayer">
							<option value="2">2</option>
							<option value="3">3</option>
							<option value="4">4</option>
							<option value="5">5</option>
							<option value="6">6</option>
						</select>
					</div>
					<div class='row'>
						<table class='table'>
							<tr>
								<th><b>Player</b></th>
								<th><b>Type</b></th>
								<th><b>Behavior</b></th>
							</tr>
							<tr id='pl1'>
								<th><b>Player 1</b></th>
								<th><select class="form-control form-control-sm"
									id="playerType1">
										<option value="HUMAN">Human</option>
										<option value="COMPUTER">Computer</option>
								</select></th>
								<th><select class="form-control form-control-sm"
									id="playerBehavior1">
										<option value="AGGRESSIVE">Aggressive</option>
										<option value="BENEVOLENT">Benevolent</option>
										<option value="RANDOM">Random</option>
										<option value="CHEATER">Cheater</option>
								</select></th>
							</tr>
							<tr id='pl2'>
								<th><b>Player 2</b></th>
								<th><select class="form-control form-control-sm"
									id="playerType2">
										<option value="HUMAN">Human</option>
										<option value="COMPUTER">Computer</option>
								</select></th>
								<th><select class="form-control form-control-sm"
									id="playerBehavior2">
										<option value="AGGRESSIVE">Aggressive</option>
										<option value="BENEVOLENT">Benevolent</option>
										<option value="RANDOM">Random</option>
										<option value="CHEATER">Cheater</option>
								</select></th>
							</tr>
							<tr id='pl3'>
								<th><b>Player 3</b></th>
								<th><select class="form-control form-control-sm"
									id="playerType3">
										<option value="HUMAN">Human</option>
										<option value="COMPUTER">Computer</option>
								</select></th>
								<th><select class="form-control form-control-sm"
									id="playerBehavior3">
										<option value="AGGRESSIVE">Aggressive</option>
										<option value="BENEVOLENT">Benevolent</option>
										<option value="RANDOM">Random</option>
										<option value="CHEATER">Cheater</option>
								</select></th>
							</tr>
							<tr id='pl4'>
								<th><b>Player 4</b></th>
								<th><select class="form-control form-control-sm"
									id="playerType4">
										<option value="HUMAN">Human</option>
										<option value="COMPUTER">Computer</option>
								</select></th>
								<th><select class="form-control form-control-sm"
									id="playerBehavior4">
										<option value="AGGRESSIVE">Aggressive</option>
										<option value="BENEVOLENT">Benevolent</option>
										<option value="RANDOM">Random</option>
										<option value="CHEATER">Cheater</option>
								</select></th>
							</tr>
							<tr id='pl5'>
								<th><b>Player 5</b></th>
								<th><select class="form-control form-control-sm"
									id="playerType5">
										<option value="HUMAN">Human</option>
										<option value="COMPUTER">Computer</option>
								</select></th>
								<th><select class="form-control form-control-sm"
									id="playerBehavior5">
										<option value="AGGRESSIVE">Aggressive</option>
										<option value="BENEVOLENT">Benevolent</option>
										<option value="RANDOM">Random</option>
										<option value="CHEATER">Cheater</option>
								</select></th>
							</tr>
							<tr id='pl6'>
								<th><b>Player 6</b></th>
								<th><select class="form-control form-control-sm"
									id="playerType6">
										<option value="HUMAN">Human</option>
										<option value="COMPUTER">Computer</option>
								</select></th>
								<th><select class="form-control form-control-sm"
									id="playerBehavior6">
										<option value="AGGRESSIVE">Aggressive</option>
										<option value="BENEVOLENT">Benevolent</option>
										<option value="RANDOM">Random</option>
										<option value="CHEATER">Cheater</option>
								</select></th>
							</tr>
						</table>
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
</body>
</html>