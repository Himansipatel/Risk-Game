/**
 * 
 */
package com.risk.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.risk.business.impl.ManagePlayer;
import com.risk.model.Attack;
import com.risk.model.Card;
import com.risk.model.Fortification;
import com.risk.model.GamePlay;
import com.risk.model.GamePlayTerritory;

/**
 * @author <a href="mailto:himansipatel1994@gmail.com">Himansi Patel</a>
 * @author <a href="mailto:mayankjariwala1994@gmail.com">Mayank Jariwala</a>
 * @version 0.0.1
 */
public class ManagePlayerTest {

	private static IManagePlayer manage_player;
	private GamePlay game_play;

	@Before
	public void initManagePlayer() {
		game_play = new GamePlay();
		manage_player = new ManagePlayer();
	}

	/**
	 * This test validate that if player is rolling no. of dice greater than no. of
	 * armies present on selected attacking territory then as per game rules player
	 * is not allow to attack on any neighboring territory
	 * 
	 * @author <a href="mailto:mayankjariwala1994@gmail.com">Mayank Jariwala</a>
	 *         Function description added by Mayank Jariwala
	 * @author <a href="mailto:himansipatel1994@gmail.com">Himansi Patel</a>
	 */
	@Test
	public void checkValidAttackTestDiceGreater() {
		Attack attack = new Attack();
		game_play = manage_player.createPlayer(2, "Switzerland.map", "A");
		attack.setAttacker_territory("Neuchtel");
		attack.setDefender_territory("Varduz");
		attack.setAttacker_dice_no(6);
		attack.setDefender_dice_no(2);
		game_play.setAttack(attack);
		game_play = manage_player.attack(game_play);
		String message = game_play.getStatus();
		assertTrue(containsInvalid(message));
	}

	/**
	 * This test validates that if player want to attack neighboring territory then
	 * the player is only allow to roll maximum number of dice by keeping one army
	 * on his territory.
	 * 
	 * @author <a href="mailto:mayankjariwala1994@gmail.com">Mayank Jariwala</a>
	 *         Function description added by Mayank Jariwala
	 * @author <a href="mailto:himansipatel1994@gmail.com">Himansi Patel</a>
	 */
	@Test
	public void checkValidAttackTestDiceLesser() {
		Attack attack = new Attack();
		game_play = manage_player.createPlayer(2, "Switzerland.map", "A");
		attack.setAttacker_territory("Neuchtel");
		attack.setDefender_territory("Varduz");
		attack.setAttacker_dice_no(3);
		attack.setDefender_dice_no(2);
		game_play.setAttack(attack);
		game_play = manage_player.attack(game_play);
		String message = game_play.getStatus();
		assertTrue(containsWon(message));
	}

	/**
	 * This test performs check on creation of card which is equal to no. of
	 * territories in map (Inserting Free Cards to card stock) if map is valid and
	 * game is ready for players to play.
	 * 
	 * @author <a href="mailto:mayankjariwala1994@gmail.com">Mayank Jariwala</a>
	 *         Function description added by Mayank Jariwala
	 * @author <a href="mailto:himansipatel1994@gmail.com">Himansi Patel</a>
	 */
	@Test
	public void checkValidCardCreationTest() {
		ManagePlayer managePlayer = new ManagePlayer();
		GamePlay gamePlay = managePlayer.createPlayer(2, "Switzerland.map", "A");
		List<GamePlayTerritory> map_territory_list = managePlayer.getTerritories(gamePlay.getMap());
		int map_territory_list_size = map_territory_list.size();
		List<Card> free_card_list = gamePlay.getFree_cards();
		int free_card_list_size = free_card_list.size();
		assertEquals(map_territory_list_size, free_card_list_size);
	}

	/**
	 * This test performs validation of placing armies manually by player on their
	 * assign territories in round robin fashion during startup phase.
	 * 
	 * @author <a href="mailto:mayankjariwala1994@gmail.com">Mayank Jariwala</a>
	 *         Function description added by Mayank Jariwala
	 * @author <a href="mailto:himansipatel1994@gmail.com">Himansi Patel</a>
	 */
	@Test
	public void validateManuallyAssignArmyStock() {
		ManagePlayer managePlayer = new ManagePlayer();
		GamePlay gamePlay = managePlayer.createPlayer(2, "Switzerland.map", "M");
		for (int i = 0; i < gamePlay.getGame_state().size(); i++) {
			assertEquals(40, gamePlay.getGame_state().get(i).getArmy_stock());
		}

	}

	/**
	 * This test performs validation of placing armies automatically on player
	 * assign territories in round robin fashion during startup phase.
	 * 
	 * @author <a href="mailto:mayankjariwala1994@gmail.com">Mayank Jariwala</a>
	 *         Function description added by Mayank Jariwala
	 * @author <a href="mailto:himansipatel1994@gmail.com">Himansi Patel</a>
	 */
	@Test
	public void validateAutomaticallyAssignArmyStock() {
		ManagePlayer managePlayer = new ManagePlayer();
		GamePlay gamePlay = managePlayer.createPlayer(2, "Switzerland.map", "A");
		assertEquals(5, gamePlay.getGame_state().get(0).getArmy_stock());
		assertEquals(6, gamePlay.getGame_state().get(1).getArmy_stock());
	}

	/**
	 * This test checks for <i>Invalid</i> fortification move to player <b>
	 * Neighboring Territory </b>
	 * 
	 * @author <a href="mailto:mayankjariwala1994@gmail.com">Mayank Jariwala</a>
	 *         Function and Comments modification by Mayank Jariwala
	 * @author <a href="mailto:zinnia.rana.22@gmail.com">Zinnia Rana</a>
	 */
	@Test
	public void checkFortifyPhaseInvalidNeighbouringTerritoryTest() {
		Fortification fortify = new Fortification();
		game_play = manage_player.createPlayer(2, "Switzerland.map", "A");
		fortify.setSource_territory("Fribourg");
		fortify.setDestination_territory("Leistal Canton");
		fortify.setArmy_count(2);
		game_play.setFortification(fortify);
		game_play = manage_player.fortify(game_play);
		String message = game_play.getStatus();
		assertEquals("Invalid Move (Not Neighboring Territory)", message);
	}

	/**
	 * This test checks for <i>Valid</i> fortification move to player <b>
	 * Neighboring Territory </b>
	 * 
	 * @author <a href="mailto:mayankjariwala1994@gmail.com">Mayank Jariwala</a>
	 *         Function and Comments modification by Mayank Jariwala
	 * @author <a href="mailto:zinnia.rana.22@gmail.com">Zinnia Rana</a>
	 */
	@Test
	public void checkFortifyPhaseValidNeighbouringTerritoryTest() {
		Fortification fortify = new Fortification();
		game_play = manage_player.createPlayer(2, "Switzerland.map", "A");
		fortify.setSource_territory("Fribourg");
		fortify.setDestination_territory("Jura Canton");
		fortify.setArmy_count(2);
		game_play.setFortification(fortify);
		game_play = manage_player.fortify(game_play);
		String message = game_play.getStatus();
		assertTrue(message.contains("move"));
	}

	/**
	 * Test to check if there is only 1 army on source territory and player want to
	 * move from source to destination then as per risk rule player is not allow to
	 * 
	 * 
	 * @author <a href="mailto:mayankjariwala1994@gmail.com">Mayank Jariwala</a>
	 */
	@Test
	public void checkFortifyInvalidFortificationArmyMoveTest() {
		Fortification fortify = new Fortification();
		fortify.setSource_territory("Fribourg");
		fortify.setDestination_territory("Jura Canton");
		fortify.setArmy_count(3);
		game_play = manage_player.createPlayer(2, "Switzerland.map", "A");
		game_play.setFortification(fortify);
		game_play = manage_player.fortify(game_play);
		String message = game_play.getStatus();
		assertTrue(message.contains("not having minimum armies"));
	}

	/**
	 * Test to check if there is more than one army on source territory and player
	 * want to move from source to destination then as per risk rule player is allow
	 * to move to few armies destination territory
	 * 
	 * 
	 * @author <a href="mailto:mayankjariwala1994@gmail.com">Mayank Jariwala</a>
	 */
	@Test
	public void checkFortifyValidFortificationArmyMoveTest() {
		Fortification fortify = new Fortification();
		game_play = manage_player.createPlayer(2, "Switzerland.map", "A");
		fortify.setSource_territory("Fribourg");
		fortify.setDestination_territory("Jura Canton");
		fortify.setArmy_count(2);
		game_play.setFortification(fortify);
		game_play = manage_player.fortify(game_play);
		String message = game_play.getStatus();
		assertTrue(message.contains("move"));
	}

	@Ignore
	public boolean containsWon(String s) {
		return s.contains("Won");
	}

	@Ignore
	public boolean containsInvalid(String s) {
		return s.contains("Invalid");
	}
}
