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

	/**
	 * This function is use by random player to fortify armies from source to
	 * territory by selecting random source and any of its random neighbors
	 * 
	 * @see com.risk.business.IStrategy#fortify(com.risk.model.GamePlay)
	 * @author <a href="zinnia.rana.22@gmail.com">Zinnia Rana</a>
	 * @author <a href="mayankjariwala1994@gmail.com">Mayank Jariwala</a>
	 *         Modifications done by Mayank Jariwala
	 * @param game_play state of the game i.e. entire game related info when
	 *                  fortification starts for a player.
	 * @return GamePlay Object
	 */
	public GamePlay fortify(GamePlay game_play) {
		boolean fortify_possible = true;
		Player current_player = game_play.getGame_state().get(game_play.getCurrent_player() - 1);
		int random_territory = 0;
		int random_destination = 0;
		// To check the army count of random territory selected
		int attacker_army_count = 0;
		int territory_processed = 0;
		GamePlayTerritory source_territory = null;
		GamePlayTerritory destination_territory = null;
		List<String> neighbours = new ArrayList<>();
		List<GamePlayTerritory> own_neighbour_territory = new ArrayList<>();
		List<GamePlayTerritory> player_territories_list = current_player.getTerritory_list();
		java.util.Random random = new java.util.Random();
		String fortify_message = "";
		l1: while (own_neighbour_territory.size() == 0) {
			random_territory = random.nextInt(player_territories_list.size());
			source_territory = player_territories_list.get(random_territory);
			attacker_army_count = source_territory.getNumber_of_armies();
			if (attacker_army_count <= 1) {
				// Handling Case in which player each territory has 1 army
				if (territory_processed == current_player.getTerritory_list().size()) {
					fortify_possible = true;
					break l1;
				}
				territory_processed++;
				continue l1;
			}
			for (com.risk.model.gui.Territory territory : game_play.getGui_map().getTerritories()) {
				if (territory.getName().equalsIgnoreCase(source_territory.getTerritory_name())) {
					if (territory.getNeighbours().isEmpty()) {
						continue l1;
					} else {
						neighbours = Arrays.asList(territory.getNeighbours().split(";"));
						for (String neighbour_territory : neighbours) {
							for (GamePlayTerritory player_territory : player_territories_list) {
								if (neighbour_territory.equalsIgnoreCase(player_territory.getTerritory_name())) {
									own_neighbour_territory.add(player_territory);
								}
							}
						}
					}

				}
			}
			if (own_neighbour_territory.size() == 0) {
				continue l1;
			}
		}
		if (!fortify_possible) {
			game_play.setStatus("Fortificaition not possible for random player");
		} else {
			random_destination = random.nextInt(own_neighbour_territory.size());
			destination_territory = player_territories_list.get(random_destination);
			fortify_message += "Random Player Fortification Started\n";
			fortify_message += "Source Territory : [ " + source_territory.getTerritory_name() + ","
					+ source_territory.getNumber_of_armies() + "]";
			fortify_message += " / Destination Territory : [ " + destination_territory.getTerritory_name() + ","
					+ destination_territory.getNumber_of_armies() + "]\n";
			source_territory.setNumber_of_armies(source_territory.getNumber_of_armies() - 1);
			destination_territory.setNumber_of_armies(destination_territory.getNumber_of_armies() + 1);
			fortify_message += "Moved 1 army from " + source_territory.getTerritory_name() + " to "
					+ destination_territory.getTerritory_name() + "\n";
			fortify_message += "After Fortification Results : \n";
			fortify_message += "Source Territory : [ " + source_territory.getTerritory_name() + ","
					+ source_territory.getNumber_of_armies() + "]";
			fortify_message += " / Destination Territory : [ " + destination_territory.getTerritory_name() + ","
					+ destination_territory.getNumber_of_armies() + "]\n";
			fortify_message += "Random Player Fortification Ended\n";
			game_play.setStatus(fortify_message);
		}
		return game_play;
	}
}
