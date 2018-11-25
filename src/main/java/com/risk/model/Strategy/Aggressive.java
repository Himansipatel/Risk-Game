package com.risk.model.Strategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.risk.business.IStrategy;
import com.risk.business.impl.ManageGamePlay;
import com.risk.business.impl.ManagePlayer;
import com.risk.model.Attack;
import com.risk.model.GamePlay;
import com.risk.model.GamePlayTerritory;
import com.risk.model.Player;

/**
 * Concrete implementation of Aggressive strategy in terms of
 * Strategy design Pattern, during our GamePlay.
 * 
 * @author <a href="mailto:apoorv.semwal20@gmail.com">Apoorv Semwal</a>
 * @version 0.0.1
 */
public class Aggressive implements IStrategy {

	/**
	 * Reinforcement of an Aggressive Player.
	 * 
	 * @see com.risk.business.IStrategy#reinforce(GamePlay)
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 */
	public GamePlay reinforce(GamePlay game_play) {

		ManagePlayer player_manager = new ManagePlayer();
		ManageGamePlay game_manager = new ManageGamePlay();
		Player current_player = null;
		GamePlayTerritory strongest_territory = null;

		if (game_play != null) {

			game_manager.calculateArmiesReinforce(game_play.getGame_state(), game_play.getMap(),
					game_play.getCurrent_player());

			for (Player player : game_play.getGame_state()) {
				if (player.getId() == game_play.getCurrent_player()) {
					current_player = player;
					break;
				} else {
					continue;
				}
			}

			if (current_player != null) {
				
				if (current_player.getCard_list().size() > 4) {
					game_play.setCard_trade(player_manager.prepareCardTrade(current_player));
					if (game_play.getCard_trade()!=null) {
						game_play.setStatus("3 Cards Traded.");
						player_manager.tradeCards(game_play);
						game_play.setStatus(game_play.getStatus()+"/n"+"New Army Stock for Reinforcement: "+current_player.getArmy_stock());
					}else {
						game_play.setStatus("No Card Trading.");
					}
					
				}
				
				strongest_territory = player_manager.findStrongestTerritory(current_player);
			}

			if (strongest_territory != null) {
				strongest_territory.setNumber_of_armies(
						strongest_territory.getNumber_of_armies() + current_player.getArmy_stock());
				current_player.setArmy_stock(0);
			}
		}
		game_play.setStatus(game_play.getStatus()+"/n"+
				            "Strongest Territory Reinforced: "+ strongest_territory.getTerritory_name()+"/n"+
				            "New Army count on:"+ strongest_territory.getNumber_of_armies()+"/n");
		return game_play;
	}

	/**
	 * Attack of an Aggressive Player.
	 * 
	 * @see com.risk.business.IStrategy#attack(GamePlay)
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 */
	public GamePlay attack(GamePlay game_play) {

		Player current_player  = null;
		Player defender_player = null;
		Attack attack_details  = null;
		GamePlayTerritory strongest_territory = null;
		List<String> neighbours = new ArrayList<>();
		List<String> player_territories = new ArrayList<>();
		GamePlayTerritory defender_territory_data = null;

		for (Player player : game_play.getGame_state()) {
			if (player.getId() == game_play.getCurrent_player()) {
				current_player = player;
				break;
			} else {
				continue;
			}
		}

		if (current_player != null) {

			int max = 0;
			for (GamePlayTerritory territory : current_player.getTerritory_list()) {
				player_territories.add(territory.getTerritory_name());
				if (territory.getNumber_of_armies() > max) {
					max = territory.getNumber_of_armies();
					strongest_territory = territory;
				}
			}

			for (com.risk.model.gui.Territory territory : game_play.getGui_map().getTerritories()) {
				if (territory.getName().equalsIgnoreCase(strongest_territory.getTerritory_name())) {
					neighbours = Arrays.asList(territory.getNeighbours().split(";"));
					break;
				} else {
					continue;
				}
			}

			if (strongest_territory != null) {

				for (com.risk.model.gui.Territory defender_territory : game_play.getGui_map().getTerritories()) {
					if (strongest_territory.getNumber_of_armies() <= 1) {
						break;
					}
					if (defender_territory.getName().equalsIgnoreCase(strongest_territory.getTerritory_name())
							|| player_territories.contains(defender_territory.getName())
							|| !neighbours.contains(defender_territory.getName())) {
						continue;
					} else {
						for (Player defender : game_play.getGame_state()) {
							defender_player = null;
							defender_territory_data = null;
							if (defender.getId() == current_player.getId()) {
								continue;
							}
							for (GamePlayTerritory defend_territory : defender.getTerritory_list()) {
								if (defend_territory.getTerritory_name()
										.equalsIgnoreCase(defender_territory.getName())) {
									defender_player = defender;
									defender_territory_data = defend_territory;
									break;
								}
							}
							if (defender_player != null) {
								break;
							}
						}

						if (defender_player != null) {
							attack_details = new Attack();
							attack_details.setAttacker_territory(strongest_territory.getTerritory_name());
							attack_details.setDefender_territory(defender_territory.getName());
							game_play.setGame_phase("ATTACK_ALL_OUT");
							game_play.setAttack(attack_details);
							attack(game_play);
							if (game_play.getStatus().contains("Attacker Occupies Defender Territory")) {
								strongest_territory.setNumber_of_armies(strongest_territory.getNumber_of_armies() - 1);
								defender_territory_data.setNumber_of_armies(1);
								player_territories.add(defender_territory_data.getTerritory_name());
								current_player.getTerritory_list().set(
										current_player.getTerritory_list().indexOf(strongest_territory),
										strongest_territory);
								current_player.getTerritory_list().set(
										current_player.getTerritory_list().indexOf(defender_territory_data),
										defender_territory_data);
							} else {
								break;
							}
						}
					}
				}
			}
		}
		return game_play;
	}

	/**
	 * Fortify of an Aggressive Player.
	 * 
	 * @see com.risk.business.IStrategy#fortify(GamePlay)
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 */
	public GamePlay fortify(GamePlay game_play) {

		Player current_player = null;
		Boolean neighbour_flag = false;
		ManagePlayer player_manager = new ManagePlayer();
		
		for (Player player : game_play.getGame_state()) {
			if (player.getId() == game_play.getCurrent_player()) {
				current_player = player;
				break;
			} else {
				continue;
			}
		}

		if (current_player != null) {

			for (GamePlayTerritory territory_a : current_player.getTerritory_list()) {

				if (territory_a.getNumber_of_armies() == 1) {
					continue;
				}
				for (GamePlayTerritory territory_b : current_player.getTerritory_list()) {

					neighbour_flag = false;

					if (territory_a.getTerritory_name().equalsIgnoreCase(territory_b.getTerritory_name())
							|| territory_b.getNumber_of_armies() == 1) {
						continue;
					} else {
						neighbour_flag = player_manager.checkIfNeighbours(territory_a.getTerritory_name(),
								territory_b.getTerritory_name(), game_play.getGui_map());
					}

					if (neighbour_flag == true) {
						territory_a.setNumber_of_armies(
								territory_a.getNumber_of_armies() + territory_b.getNumber_of_armies() - 1);
						territory_b.setNumber_of_armies(1);
					}
				}
			}
		}
		return game_play;
	}

}
