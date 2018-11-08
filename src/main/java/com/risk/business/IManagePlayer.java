package com.risk.business;

import com.risk.model.GamePlay;

/**
 * This interface is responsible for handling all player related functionalities
 * like: Managing players state for each game phases ,Such as reinforcement
 * phase,attack phase and fortification phase.
 * 
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
	 * This method is called to execute reinforcement phase for a particular player
	 * and updates the state of the game accordingly. It also handles the trading of
	 * cards during game play.
	 * 
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 * @param game_play state of the game i.e. entire game related info when
	 *                  reinforcement starts for a player.
	 * @return GamePlay updated state of the game after reinforcement phase ends.
	 * 
	 */
	GamePlay reinforce(GamePlay game_play);

	/**
	 * This method implements the functionality for attack which initially check for
	 * each attack validation and get results and based on result it perform certain
	 * actions like populating message , increment/decrement armies from respective
	 * player and last if attacker occupy defender territory then add defender
	 * territory to attacker territory list and remove from defender territory list
	 * 
	 * @author <a href="mailto:himansipatel1994@gmail.com">Himansi Patel</a>
	 * @author <a href="mayankjariwala1994@gmail.com">Mayank Jariwala</a>
	 * @param game_play : GamePlay Object
	 * @return GamePlay updated state of the game during attack phase.
	 */
	GamePlay attack(GamePlay game_play);

	/**
	 * This function is an main function of fortify which initially check for each
	 * fortification validation and get results and based on result it perform
	 * certain actions like populating message , increment/decrement armies from
	 * respective player and fortification is performed if destination is valid
	 * adjacency territory of source and check valid army count available in source
	 * territory and update army stock accordingly
	 * 
	 * @author <a href="mailto:zinnia.rana.22@gmail.com">Zinnia Rana</a>
	 * @param game_play : GamePlay Object
	 * @return GamePlay updated state of the game during fortification phase.
	 */
	GamePlay fortify(GamePlay game_play);
}
