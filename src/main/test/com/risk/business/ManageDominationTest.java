package com.risk.business;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.risk.business.impl.ManagePlayer;
import com.risk.model.GamePlay;

/**
 * 
 * @author <a href="mayankjariwala1994@gmail.com">Mayank Jariwala</a>
 * @author <a href="himansipatel1994@gmail.com">Himansi Patel</a>
 * @version 0.0.1
 */
public class ManageDominationTest {

	private GamePlay game_play;

	@Before
	public void createObject() {
		game_play = new GamePlay();
	}

	@Test
	public void checkDominationResultPlayerOneTest() {
		ManagePlayer manage_player = new ManagePlayer();
		game_play = manage_player.createPlayer(2, "Switzerland.map", "A");
		assertEquals(51.9, game_play.getDomination().get(0).getMap_coverage(), 0.0);
	}

	@Test
	public void checkDominationResultPlayerTwoTest() {
		ManagePlayer manage_player = new ManagePlayer();
		game_play = manage_player.createPlayer(2, "Switzerland.map", "A");
		assertEquals(48.1, game_play.getDomination().get(1).getMap_coverage(), 0.0);
	}
}
