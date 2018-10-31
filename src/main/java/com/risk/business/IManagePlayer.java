package com.risk.business;

import com.risk.model.GamePlay;

/**
 * @author <a href="mailto:himansipatel1994@gmail.com">Himansi Patel</a>
 * @version 0.0.1
 */
public interface IManagePlayer {

	/**
	 * This method is an abstraction for the process of retrieving A Player Object
	 * 
	 * @author <a href="mailto:himansipatel1994@gmail.com">Himansi Patel</a>
	 * @param num_of_players       Number of players playing the game
	 * @param map_name             Selected Map for playing the game.
	 * @param army_allocation_type Deciding factor for allocation of armies(M-Manual
	 *                             or A-Automatic) on territory
	 * @return Entire Player List which will be rendered for Playing.
	 */
	GamePlay createPlayer(int num_of_players, String map_name, String army_allocation_type);
}
