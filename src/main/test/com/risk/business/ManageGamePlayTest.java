package com.risk.business;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.risk.business.impl.ManageGamePlay;
import com.risk.business.impl.ManagePlayer;
import com.risk.model.GamePlay;
import com.risk.model.PlayerDetails;

/**
 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
 * @version 0.0.1
 */
public class ManageGamePlayTest {

	private IManagePlayer   player_manager;
	private IManageGamePlay game_manager;

	@Before
	public void initMapManager() {
		player_manager = new ManagePlayer();
		game_manager   = new ManageGamePlay();
	}

	/**
	 * Creating a game state using AutoAllocationMode - A.
	 * Auto Allocation Mode Performs an initial automatic allocation of armies
	 * during startup phase and then calls calculateArmiesReinforce to give an
	 * additional amount of armies based on the Continent score if any player
	 * holds control over an multiple continents.
	 *
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 */
	@Test
	public void testCalculateArmiesReinforceMultiContinent() {

		/**
		 * Using auto allocation Player 2 captures entire 
		 * "Lowlands" Continent - Score = 1
		 * "Basel" Continent - Score = 1
		 * Initial army stock 0 - New Stock after calculation = 5 = (3 + 2)
		 * 3 on the basis of 5 Territories it holds and 2 from Continent Scores
		 */
		PlayerDetails      single_game_input = new PlayerDetails();
		single_game_input.setAllocationType("A");
		single_game_input.setFileName("Switzerland.map");
		single_game_input.setPlayersNo(6);
		GamePlay game_state = player_manager.createPlayer(single_game_input);

		game_state.setCurrent_player(2);
		game_manager.calculateArmiesReinforce(game_state.getGame_state(), game_state.getMap(), game_state.getCurrent_player());		
		assertEquals(5, game_state.getGame_state().get(1).getArmy_stock());

	}

	/**
	 * Creating a game state using AutoAllocationMode - A.
	 * Auto Allocation Mode Performs an initial automatic allocation of armies
	 * during startup phase and then calls calculateArmiesReinforce to give an
	 * additional amount of armies based on the Continent score if any player
	 * holds control over an entire continent.
	 *
	 * 
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 */
	@Test
	public void testCalculateArmiesReinforceSingleContinent() {

		/**
		 * Using auto allocation Player 3 captures entire 
		 * "Liechtenstien" Continent - Score = 1
		 * Initial army stock 0 - New Stock after calculation = 4 = (3 + 1)
		 * 3 on the basis of 5 Territories it holds and 1 from Continent Score.
		 */		
		PlayerDetails      single_game_input = new PlayerDetails();
		single_game_input.setAllocationType("A");
		single_game_input.setFileName("Switzerland.map");
		single_game_input.setPlayersNo(6);
		GamePlay game_state = player_manager.createPlayer(single_game_input);

		game_state.setCurrent_player(3);
		game_manager.calculateArmiesReinforce(game_state.getGame_state(), game_state.getMap(), game_state.getCurrent_player());
		assertEquals(4, game_state.getGame_state().get(2).getArmy_stock());
	}

}
