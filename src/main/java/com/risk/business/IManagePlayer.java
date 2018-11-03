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
	
	/**
	 * This function is an main function of attack which initially check for each
	 * attack validation and get results and based on result it perform certain
	 * actions like populating message , increment/decrement armies from respective
	 * player and last if attacker occupy defender territory then add defender
	 * territory to attacker territory list and remove from defender territory list
	 * 
	 * @author <a href="mailto:himansipatel1994@gmail.com">Himansi Patel</a>
	 * @author <a href="mayankjariwala1994@gmail.com">Mayank Jariwala</a>
	 * @param game_play : GamePlay Object
	 * @return GamePlay Object
	 */
	GamePlay attack(GamePlay game_play);
}
