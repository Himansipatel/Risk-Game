package com.risk.business;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

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

	/**
	 * Domination Test for Map Coverage of each Player
	 * @author <a href="mayankjariwala1994@gmail.com">Mayank Jariwala</a>
	 * @author <a href="himansipatel1994@gmail.com">Himansi Patel</a>
	 * 
	 */
	@Test
	public void checkDominationResultPlayerMapCoverageTest() {
		ManagePlayer manage_player = new ManagePlayer();
		game_play = manage_player.createPlayer(2, "Switzerland.map", "A");
		assertEquals(51.9, game_play.getDomination().get(0).getMap_coverage(), 0.0);
		assertEquals(48.1, game_play.getDomination().get(1).getMap_coverage(), 0.0);
	}

	/**
	 * Domination Test for Total Armies of each Player
	 * @author <a href="mayankjariwala1994@gmail.com">Mayank Jariwala</a>
	 * @author <a href="himansipatel1994@gmail.com">Himansi Patel</a>
	 * 
	 */
	@Test
	public void checkDominationResultPlayerTotalArmiesTest() {
		ManagePlayer manage_player = new ManagePlayer();
		game_play = manage_player.createPlayer(2, "Switzerland.map", "A");
		assertEquals(40, game_play.getDomination().get(0).getPlayer_army_count());
		assertEquals(40, game_play.getDomination().get(1).getPlayer_army_count());
	}

	/**
	 * Domination Test for Continent Ows of each Player
	 * @author <a href="mayankjariwala1994@gmail.com">Mayank Jariwala</a>
	 * @author <a href="himansipatel1994@gmail.com">Himansi Patel</a>
	 * 
	 */
	@Test
	public void checkDominationResultPlayerContinentOwsTest() {
		ManagePlayer manage_player = new ManagePlayer();
		game_play = manage_player.createPlayer(2, "Switzerland.map", "A");
		assertEquals(Arrays.asList("Liechtenstien"), game_play.getDomination().get(0).getPlayer_continent_list());
		assertEquals(Arrays.asList("Basel", "Lowlands"), game_play.getDomination().get(1).getPlayer_continent_list());
	}
}
