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
		String random_attack_message = "";
		String old_message = "";
		List<String> player_territories = new ArrayList<>();
		List<GamePlayTerritory> attacker_territory_list = new ArrayList<>();
		List<GamePlayTerritory> defender_territory_list = new ArrayList<>();
		ManagePlayer player_manager = new ManagePlayer();
		Player current_player = game_play.getGame_state().get(game_play.getCurrent_player() - 1);
		int random_territory = 0;
		int attacker_army_count = 0;
		GamePlayTerritory source_territory = null;
		GamePlayTerritory destination_territory = null;
		List<String> neighbours = new ArrayList<>();
		List<GamePlayTerritory> player_territories_list = current_player.getTerritory_list();
		boolean is_territory_occupied = false;
		Attack attack = null;
		if (game_play.getAttack() != null) {
			attack = game_play.getAttack();
		} else {
			attack = new Attack();
			game_play.setAttack(attack);
		}
		for (GamePlayTerritory territory : current_player.getTerritory_list()) {
			player_territories.add(territory.getTerritory_name());
		}
		java.util.Random random = new java.util.Random();
		random_territory = random.nextInt(player_territories_list.size());
		source_territory = player_territories_list.get(random_territory);
		attacker_territory_list.add(source_territory);
		attacker_army_count = source_territory.getNumber_of_armies();
		boolean defefender_territory_found = false;
		if (attacker_army_count > 1) {
			for (com.risk.model.gui.Territory territory : game_play.getGui_map().getTerritories()) {
				if (territory.getName().equalsIgnoreCase(source_territory.getTerritory_name())) {
					neighbours = Arrays.asList(territory.getNeighbours().split(";"));
					break;
				} else {
					continue;
				}
			}

			for (com.risk.model.gui.Territory defender_territory : game_play.getGui_map().getTerritories()) {
				if (source_territory.getNumber_of_armies() <= 1) {
					break;
				}
				if (defender_territory.getName().equalsIgnoreCase(source_territory.getTerritory_name())
						|| player_territories.contains(defender_territory.getName())
						|| !neighbours.contains(defender_territory.getName())) {
					continue;
				} else {
					for (Player defender : game_play.getGame_state()) {

						if (defender.getId() == game_play.getCurrent_player()) {
							continue;
						}
						for (GamePlayTerritory defend_territory : defender.getTerritory_list()) {
							if (defend_territory.getTerritory_name().equalsIgnoreCase(defender_territory.getName())) {
								destination_territory = defend_territory;
								defender_territory_list.add(destination_territory);
								defefender_territory_found = true;
								break;
							}
						}
						if (defefender_territory_found) {
							break;
						}
					}
				}
				if (defefender_territory_found) {
					break;
				}
			}
			if (defefender_territory_found) {
				int attacker_dice_no = 0;
				int defender_dice_no = 0;
				player_manager.setAttackerDefenderDiceNo(attacker_territory_list, defender_territory_list, attack);
				attacker_dice_no = game_play.getAttack().getAttacker_dice_no();
				defender_dice_no = game_play.getAttack().getDefender_dice_no();
				int attacker_terattrtiory_armies = attacker_territory_list.get(0).getNumber_of_armies();
				int defender_territory_armies = defender_territory_list.get(0).getNumber_of_armies();
				old_message = random_attack_message;
				random_attack_message = "";
				random_attack_message += "\nAttacker territory: " + attacker_territory_list.get(0).getTerritory_name()
						+ " Defender Territory: " + defender_territory_list.get(0).getTerritory_name() + "\n";
				random_attack_message = random_attack_message + old_message;
				String valid_attack_message = player_manager.checkForValidAttack(attacker_terattrtiory_armies,
						defender_territory_armies, attacker_dice_no, defender_dice_no);
				if (valid_attack_message.trim().length() == 0) {
					List<Integer> attack_result = player_manager.rollDiceDecision(attacker_dice_no, defender_dice_no);
					old_message = random_attack_message;
					random_attack_message = "";
					random_attack_message += player_manager.getRollDiceMessage();
					random_attack_message = random_attack_message + old_message;
					for (int i = 0; i < attack_result.size(); i++) {
						int result = attack_result.get(i);
						System.out.println("result" + i + result);
						if (result == 1) {
							// Attacker Won
							GamePlayTerritory def_obj = defender_territory_list.get(0);
							def_obj.setNumber_of_armies(def_obj.getNumber_of_armies() - 1);

							if (def_obj.getNumber_of_armies() == 0) {
								attacker_territory_list.add(def_obj);
								if (attacker_territory_list.get(0).getNumber_of_armies() > 1) {
									attacker_territory_list.get(0).setNumber_of_armies(
											attacker_territory_list.get(0).getNumber_of_armies() - 1);
									old_message = random_attack_message;
									random_attack_message = "";
									random_attack_message += "Attacker territory: "
											+ attacker_territory_list.get(0).getTerritory_name()
											+ " occupies Defender Territory: " + def_obj.getTerritory_name() + "\n";
									random_attack_message = random_attack_message + old_message;
								}
							}

						} else {
							GamePlayTerritory att_obj = attacker_territory_list.get(0);
							att_obj.setNumber_of_armies(att_obj.getNumber_of_armies() - 1);
						}
					}
					for (Player defender : game_play.getGame_state()) {
						if (defender.getId() == game_play.getCurrent_player()) {
							continue;
						} else {
							for (GamePlayTerritory player_territory : defender.getTerritory_list()) {
								if (defender_territory_list.get(0).getTerritory_name()
										.equalsIgnoreCase(player_territory.getTerritory_name())) {

									if (defender_territory_list.get(0).getNumber_of_armies() == 0) {
										defender.getTerritory_list().remove(player_territory);
										break;
									} else {
										player_territory.setNumber_of_armies(
												defender_territory_list.get(0).getNumber_of_armies());
										break;
									}
								}
							}
						}
					}

					for (GamePlayTerritory attacker_territory : attacker_territory_list) {

						for (GamePlayTerritory player_territory : game_play.getGame_state()
								.get(game_play.getCurrent_player() - 1).getTerritory_list()) {
							if (attacker_territory.getTerritory_name()
									.equalsIgnoreCase(player_territory.getTerritory_name())) {
								player_territory.setNumber_of_armies(attacker_territory.getNumber_of_armies());
								break;
							} else if (!game_play.getGame_state().get(game_play.getCurrent_player() - 1)
									.getTerritory_list().contains(attacker_territory)) {
								attacker_territory.setNumber_of_armies(1);
								game_play.getGame_state().get(game_play.getCurrent_player() - 1).getTerritory_list()
										.add(attacker_territory);
								is_territory_occupied = true;
								game_play.getGame_state().get(game_play.getCurrent_player() - 1)
										.setAny_territory_occupied(is_territory_occupied);
								break;
							}

						}
					}
				}

			} else {
				random_attack_message = "All neighbours of " + source_territory.getTerritory_name()
						+ " belong to Attacker's territoy list \n";
			}

		} else {
			random_attack_message = "Selected random attacker territory : " + source_territory.getNumber_of_armies()
					+ " has only 1 army \n";

		}
		if (game_play.getGame_state().get(game_play.getCurrent_player() - 1).isAny_territory_occupied()) {
			player_manager.giveCardAtAttackEnd(game_play);
			if (game_play.getStatus() != null && game_play.getStatus().length() > 0) {
				game_play.setStatus(game_play.getStatus() + "\n" + random_attack_message);
			}
		} else {
			game_play.setStatus(random_attack_message);
		}
		game_play.setGame_phase("ATTACK");
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
