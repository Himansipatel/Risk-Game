package com.risk.business;

import com.risk.model.GamePlay;

/**
 * This interface defines a Player in accordance with Strategy Design Pattern.
 * Every concrete class representing a Player with a specific strategy has to  
 * implement this interface.
 * Core methods in this interface are - reinforce, attack and fortify
 * which will vary as per Strategy Design Pattern for different Player
 * behaviors.
 *  
 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
 * @version 0.0.1
 */
public interface IAbstractPlayer {

	/**
	 * This method is called to execute reinforcement phase for a particular player
	 * and updates the state of the game accordingly. It also handles the trading of
	 * cards during game play.
	 * 
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 * @param game_play state of the game i.e. entire game related info when
	 *                  reinforcement starts for a player.
	 * @return GamePlay updated state of the game after reinforcement phase ends.
	 */
	GamePlay reinforce(GamePlay game_play);
	
	/**
	 * This method is called to execute attack phase for a particular player and
	 * updates the state of the game accordingly.
	 * 
	 * @author <a href="mailto:mayankjariwala1994@gmail.com">MayankJariwala</a>
	 * @param game_play state of the game i.e. entire game related info when attack
	 *                  starts for a player.
	 * @return GamePlay updated state of the game after attack phase ends.
	 */	
	GamePlay attack(GamePlay game_play);

	/**
	 * This function is an main function of fortify which initially checks for each
	 * fortification validation to check a valid move.Based on the checks it performs
	 * a valid fortification move.
	 * 
	 * @author <a href="mailto:zinnia.rana.22@gmail.com">Zinnia Rana</a>
	 * @param game_play : GamePlay Object
	 * @return GamePlay updated state of the game after fortification phase.
	 */
	GamePlay fortify(GamePlay game_play);
	
}
