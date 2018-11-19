package com.risk.model;
import com.risk.business.AbstractPlayer;
import com.risk.business.impl.ManagePlayer;

/**
 * This Player Model represents a Human Player in terms of Strategy-Pattern implementation,
 * during our GamePlay.
 * 
 * @author <a href="mailto:himansipatel1994@gmail.com">Himansi Patel</a>
 * @author <a href="mailto:mayankjariwala1994@gmail.com">Mayank Jariwala</a> -
 *         Added Model Description
 * @version 0.0.1
 */
public class Player extends AbstractPlayer {

	/**
	 * Reinforce for a Human Player as per Strategy Design Pattern. 
	 * This method is called to execute reinforcement phase for a particular player
	 * and updates the state of the game accordingly. It also handles the trading of
	 * cards during game play.
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 * @param game_play state of the game i.e. entire game related info when
	 *                  reinforcement starts for a player.
	 * @return GamePlay updated state of the game after reinforcement phase ends.
	 */
	public GamePlay reinforce(GamePlay game_play) {
		ManagePlayer manage_player = new ManagePlayer();
		game_play = manage_player.reinforce(game_play);
		return game_play;
	}

	/**
	 * Attack for a Human Player as per Strategy Design Pattern. 
	 * This method is called to execute attack phase for a particular player and
	 * updates the state of the game accordingly.
	 * @author <a href="mailto:mayankjariwala1994@gmail.com">MayankJariwala</a>
	 * @param game_play state of the game i.e. entire game related info when attack
	 *                  starts for a player.
	 * @return GamePlay updated state of the game after attack phase ends.
	 */
	public GamePlay attack(GamePlay game_play) {
		ManagePlayer manage_player = new ManagePlayer();
		game_play = manage_player.attack(game_play);
		return game_play;
	}

	/**
	 * Fortify for a Human Player as per Strategy Design Pattern.
	 * @see com.risk.business.IManagePlayer#fortify(com.risk.model.GamePlay)
	 * @author <a href="mailto:mayankjariwala1994@gmail.com">Mayank Jariwala</a>
	 *         Comments and Function added by Mayank Jariwala
	 * @param game_play state of the game i.e. entire game related info when
	 *                  fortification starts for a player.
	 * @return GamePlay Object
	 */
	public GamePlay fortify(GamePlay game_play) {
		ManagePlayer manage_player = new ManagePlayer();
		game_play = manage_player.fortify(game_play);
		return game_play;
	}
}
