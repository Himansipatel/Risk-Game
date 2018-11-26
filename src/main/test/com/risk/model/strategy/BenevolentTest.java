package com.risk.model.strategy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.risk.business.IManagePlayer;
import com.risk.business.impl.ManagePlayer;
import com.risk.model.GamePlay;
import com.risk.model.gui.PlayerDetails;
import com.risk.model.gui.SinglePlayer;

/**
 * This is a test class for benevolent player
 * 
 * @author <a href="mailto:mayankjariwala1994@gmail.com">Mayank Jariwala</a>
 * @version 0.0.3
 */
public class BenevolentTest {

	private static IManagePlayer manage_player;
	private GamePlay game_play;

	@Before
	public void initManagePlayer() {
		game_play = new GamePlay();
		manage_player = new ManagePlayer();
	}

	/**
	 * This function checks for valid reinforcement for benevolent player
	 */
	@Test
	public void checkValidReinforceTest() {
		PlayerDetails single_game_input = new PlayerDetails();
		single_game_input.setAllocationType("A");
		single_game_input.setFileName("Switzerland.map");
		single_game_input.setPlayersNo(2);
		List<SinglePlayer> players = new ArrayList<>();
		for (int i = 1; i <= 2; i++) {
			SinglePlayer player = new SinglePlayer();
			player.setId(Integer.toString(i));
			player.setType("Computer");
			player.setBehaviour("Benevolent");
			players.add(player);
		}
		single_game_input.setPlayers(players);
		game_play = manage_player.createPlayer(single_game_input);
		game_play.getGame_state().get(game_play.getCurrent_player() - 1).executeStrategy("REINFORCE", game_play);
		assertEquals(4, game_play.getGame_state().get(game_play.getCurrent_player() - 1).getTerritory_list().get(0)
				.getNumber_of_armies());
	}

	/**
	 * This function checks for valid fortification for benevolent player
	 */
	@Test
	public void checkValidForitificationTest() {
		PlayerDetails single_game_input = new PlayerDetails();
		single_game_input.setAllocationType("A");
		single_game_input.setFileName("Switzerland.map");
		single_game_input.setPlayersNo(2);
		List<SinglePlayer> players = new ArrayList<>();
		for (int i = 1; i <= 2; i++) {
			SinglePlayer player = new SinglePlayer();
			player.setId(Integer.toString(i));
			player.setType("Computer");
			player.setBehaviour("Benevolent");
			players.add(player);
		}
		single_game_input.setPlayers(players);
		game_play = manage_player.createPlayer(single_game_input);
		game_play = game_play.getGame_state().get(game_play.getCurrent_player() - 1).executeStrategy("FORTIFY",
				game_play);
		assertTrue(game_play.getStatus().contains("Fortification not possible"));
	}

}
