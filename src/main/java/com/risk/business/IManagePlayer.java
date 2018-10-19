package com.risk.business;

import java.util.List;

import com.risk.model.Player;

/**
 * @author <a href="mailto:himansipatel1994@gmail.com">Himansi Patel</a>
 * @version 0.0.1
 */
public interface IManagePlayer {

	/**
	 * This method is an abstraction for the process of retrieving A Player Object
	 * 
	 * @author <a href="mailto:himansipatel1994@gmail.com">Himansi Patel</a>
	 * @param num_of_players Number of players playing the game
	 * @param map_name       Selected Map for playing the game.
	 * @return Entire Player List which will be rendered for Playing.
	 */
	List<Player> createPlayer(int num_of_players, String map_name);

	/**
	 * This method is an abstraction for the process of converting player Object
	 * into the Game Play File to be saved/loaded.
	 * 
	 * @author <a href="mailto:himansipatel1994@gmail.com">Himansi Patel</a>
	 * @param player_info_list This is the entire Player Object which will be
	 *                         converted to a Game Play File Object and then written
	 *                         on to a GamePlay File.
	 * @param file_name        Name of the Map file to be stored in Resource Folder
	 *                         and GamePlay File.
	 * @return File write status
	 */
	boolean writePlayerToFile(List<Player> player_info_list, String file_name);
}
