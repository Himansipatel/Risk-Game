/**
 * 
 */
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
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 * @param num_of_players Number of players playing the game
	 * @param map_name       Selected Map for playing the game.
	 * @return Entire Player List which will be rendered for Playing.
	 */
	List<Player> createPlayer(int num_of_players, String map_name);
}
