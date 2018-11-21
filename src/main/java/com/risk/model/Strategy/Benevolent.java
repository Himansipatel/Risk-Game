package com.risk.model.Strategy;

import com.risk.business.IStrategy;
import com.risk.model.GamePlay;

/**
 * Concrete implementation of Benevolent Strategy in terms of
 * Strategy design Pattern, during our GamePlay.
 * 
 * @author <a href="mailto:mayankjariwala1994@gmail.com">MayankJariwala</a>
 * @version 0.0.1
 */
public class Benevolent implements IStrategy {

	/**
	 * Reinforcement for a Benevolent Player as per Strategy Design Pattern.
	 * 
	 * @see com.risk.business.IStrategy#reinforce(com.risk.model.GamePlay) 
	 * @author <a href="mailto:mayankjariwala1994@gmail.com">MayankJariwala</a>
	 * @param game_play state of the game i.e. entire game related info when
	 *                  reinforcement starts for a player.
	 * @return GamePlay updated state of the game after reinforcement phase ends.
	 */
	public GamePlay reinforce(GamePlay game_play) {
		return game_play;
	}

	/**
	 * Attack for a Benevolent Player as per Strategy Design Pattern.
	 *  
	 * @see com.risk.business.IStrategy#attack(com.risk.model.GamePlay)
	 * @author <a href="mailto:mayankjariwala1994@gmail.com">MayankJariwala</a>
	 * @param game_play state of the game i.e. entire game related info when attack
	 *                  starts for a player.
	 * @return GamePlay updated state of the game after attack phase ends.
	 */
	public GamePlay attack(GamePlay game_play) {
		return game_play;
	}

	/**
	 * Fortify for a Benevolent Player as per Strategy Design Pattern.
	 * 
	 * @see com.risk.business.IStrategy#fortify(com.risk.model.GamePlay)
	 * @author <a href="mailto:mayankjariwala1994@gmail.com">MayankJariwala</a>     
	 * @param game_play state of the game i.e. entire game related info when
	 *                  fortification starts for a player.
	 * @return GamePlay Object
	 */
	public GamePlay fortify(GamePlay game_play) {
		return game_play;
	}

}
