package com.risk.business;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.risk.business.impl.ManagePlayer;
import com.risk.model.GamePlay;

/**
 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
 * @version 0.0.1
 */
public class ManageGamePlayTest {

	private IManagePlayer player_manager;

	@Before
	public void initMapManager() {
		player_manager = new ManagePlayer();
	}

	/**
	 * This test checks if a player gets correct armies based on multiple continents
	 * it holds.
	 * 
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 */
	@Test
	public void testCalculateArmiesReinforceMultiContinent() {

		/**
		 * Creating a game state using AutoAllocationMode - A.
		 * Auto Allocation Mode Performs an initial automatic allocation of armies
		 * during startup phase and then calls calculateArmiesReinforce to give an
		 * additional amount of armies based on the Continent score if any player
		 * holds control over an entire continent.
		 */

		GamePlay game_state = new GamePlay();		
		game_state = player_manager.createPlayer(6,"Switzerland.map","A");

		/**
		 * Using auto allocation Player 2 captures entire 
		 * "Lowlands" Continent - Score = 1
		 * "Basel" Continent - Score = 1
		 * Initial army stock 0 - New Stock after calculation = 5 = (3 + 2)
		 * 3 on the basis of 5 Territories it holds and 2 from Continent Scores
		 */
		assertEquals(5, game_state.getGame_state().get(1).getArmy_stock());

		/**
		 * Using auto allocation Player 3 captures entire 
		 * "Liechtenstien" Continent - Score = 1
		 * Initial army stock 0 - New Stock after calculation = 4 = (3 + 1)
		 * 3 on the basis of 5 Territories it holds and 1 from Continent Score.
		 */		
		assertEquals(4, game_state.getGame_state().get(2).getArmy_stock());
	}

	/**
	 * This test checks if a player gets correct armies based on single continent
	 * it holds.
	 * 
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 */
	@Test
	public void testCalculateArmiesReinforceSingleContinent() {

		/**
		 * Creating a game state using AutoAllocationMode - A.
		 * Auto Allocation Mode Performs an initial automatic allocation of armies
		 * during startup phase and then calls calculateArmiesReinforce to give an
		 * additional amount of armies based on the Continent score if any player
		 * holds control over an entire continent.
		 */

		GamePlay game_state = new GamePlay();		
		game_state = player_manager.createPlayer(6,"Switzerland.map","A");

		/**
		 * Using auto allocation Player 3 captures entire 
		 * "Liechtenstien" Continent - Score = 1
		 * Initial army stock 0 - New Stock after calculation = 4 = (3 + 1)
		 * 3 on the basis of 5 Territories it holds and 1 from Continent Score.
		 */		
		assertEquals(4, game_state.getGame_state().get(2).getArmy_stock());
	}

}
