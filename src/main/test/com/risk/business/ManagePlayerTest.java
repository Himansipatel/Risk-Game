/**
 * 
 */
package com.risk.business;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.risk.business.impl.ManagePlayer;
import com.risk.model.Attack;
import com.risk.model.Fortification;
import com.risk.model.GamePlay;

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
		manage_player = new ManagePlayer();
	}

	@Test
	public void checkValidAttackTestDiceGreater() {
		Attack attack = GamePlay.getAttack();
		game_play = manage_player.createPlayer(2, "Switzerland.map", "A");
		attack.setAttacker_territory("Neuchtel");
		attack.setDefender_territory("Varduz");
		attack.setAttacker_dice_no(6);
		attack.setDefender_dice_no(2);
		game_play = manage_player.attack(game_play);
		String message = game_play.getStatus();
		assertTrue(containsInvalid(message));
	}

	@Test
	public void checkValidAttackTestDiceEqualLesser() {
		Attack attack = GamePlay.getAttack();
		game_play = manage_player.createPlayer(2, "Switzerland.map", "A");
		attack.setAttacker_territory("Neuchtel");
		attack.setDefender_territory("Varduz");
		attack.setAttacker_dice_no(3);
		attack.setDefender_dice_no(2);
		game_play = manage_player.attack(game_play);
		String message = game_play.getStatus();
		assertTrue(containsWon(message));
	}

	@Test
	public void checkValidFortifyTestDestination() {
		Fortification fortify = game_play.getFortify();
		game_play = manage_player.createPlayer(2, "Switzerland.map", "A");
		fortify.setSource_territory("Neuchtel");
		fortify.setDestination_territory("Varduz");
		game_play = manage_player.fortify(game_play);
		String message = game_play.getStatus();
		assertTrue(containsInvalidDestination(message));
	}

	@Test
	public void checkValidFortifyTestSourceArmyCount() {
		Fortification fortify = game_play.getFortify();
		game_play = manage_player.createPlayer(2, "Switzerland.map", "A");
		fortify.setSource_territory("Neuchtel");
		fortify.setDestination_territory("Varduz");
		game_play = manage_player.fortify(game_play);
		String message = game_play.getStatus();
		assertTrue(containsInvalidArmyCount(message));
	}

	@Ignore
	public boolean containsInvalidArmyCount(String s) {
		return s.matches(".*Invalid Army Count.*");
	}

	@Ignore
	public boolean containsInvalidDestination(String s) {
		return s.matches(".*Invalid Destination chosen.*");
	}

	@Ignore
	public boolean containsWon(String s) {
		return s.matches(".*Won.*");
	}

	@Ignore
	public boolean containsInvalid(String s) {
		return s.matches(".*Invalid.*");
	}
}
