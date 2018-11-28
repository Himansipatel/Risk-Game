package com.risk.model.Strategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.risk.business.IStrategy;
import com.risk.business.impl.ManageGamePlay;
import com.risk.model.GamePlay;
import com.risk.model.GamePlayTerritory;
import com.risk.model.Player;

/**
 * Concrete implementation of Random Strategy in terms of Strategy design
 * Pattern, during our GamePlay.
 * 
 * @author <a href="zinnia.rana.22@gmail.com">Zinnia Rana</a>
 * @author <a href="mayankjariwala1994@gmail.com">Mayank Jariwala</a> Functions
 *         Modifications done by Mayank Jariwala
 * @version 0.0.1
 */
public class Random implements IStrategy {

	/**
	 * This function is use by random player to reinforce armies on any randomly
	 * selected territory with given army stock
	 * 
	 * @see com.risk.business.IStrategy#reinforce(com.risk.model.GamePlay)
	 * @author <a href="zinnia.rana.22@gmail.com">Zinnia Rana</a>
	 * @author <a href="mayankjariwala1994@gmail.com">Mayank Jariwala</a>
	 *         Modifications done by Mayank Jariwala
	 * @param game_play state of the game i.e. entire game related info when
	 *                  reinforcement starts for a player.
	 * @return GamePlay updated state of the game after reinforcement phase ends.
	 */
	public GamePlay reinforce(GamePlay game_play) {
		String reinforce_message = "Reinforcement Started For Random Player\n";
		ManageGamePlay manage_obj = new ManageGamePlay();
		java.util.Random random = new java.util.Random();
		int player_army_stock;
		Player current_player = game_play.getGame_state().get(game_play.getCurrent_player() - 1);
		List<GamePlayTerritory> player_territories_list = current_player.getTerritory_list();
		manage_obj.calculateArmiesReinforce(game_play.getGame_state(), game_play.getMap(),
				game_play.getCurrent_player());
		player_army_stock = current_player.getArmy_stock();
		int selected_territory_index = random.nextInt(player_territories_list.size());
		GamePlayTerritory selected_territory_obj = player_territories_list.get(selected_territory_index);
		reinforce_message += "Before Reinforcement,Territory Info : [ " + selected_territory_obj.getTerritory_name()
				+ "," + selected_territory_obj.getNumber_of_armies() + " ]\n";
		reinforce_message += "Placing " + player_army_stock + " armies on " + selected_territory_obj.getTerritory_name()
				+ "\n";
		while (player_army_stock != 0) {
			selected_territory_obj.setNumber_of_armies(selected_territory_obj.getNumber_of_armies() + 1);
			player_army_stock--;
		}
		reinforce_message += "After Reinforcement,Territory Info : [ " + selected_territory_obj.getTerritory_name()
				+ "," + selected_territory_obj.getNumber_of_armies() + " ]\n";
		reinforce_message += "Reinforcement Ended For Random Player";
		game_play.setStatus(reinforce_message);
		return game_play;
	}

	/**
	 * Attack for a Random Player as per Strategy Design Pattern.
	 * 
	 * @see com.risk.business.IStrategy#attack(com.risk.model.GamePlay)
	 * @author <a href="zinnia.rana.22@gmail.com">Zinnia Rana</a>
	 * @param game_play state of the game i.e. entire game related info when attack
	 *                  starts for a player.
	 * @return GamePlay updated state of the game after attack phase ends.
	 */
	public GamePlay attack(GamePlay game_play) {
		return game_play;
	}

	@Override
	public GamePlay fortify(GamePlay game_play) {
		// TODO Auto-generated method stub
		return null;
	}
}
