package com.risk.model.Strategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.risk.business.IStrategy;
import com.risk.business.impl.ManagePlayer;
import com.risk.model.GamePlay;
import com.risk.model.GamePlayTerritory;
import com.risk.model.Player;

/**
 * Concrete implementation of Cheating Strategy in terms of
 * Strategy design Pattern, during our GamePlay.
 * 
 * @author <a href="mailto:himansipatel1994@gmail.com">Himansi Patel</a>
 * @version 0.0.1
 */
public class Cheater implements IStrategy {

	/**
	 * Reinforcement of an Cheater Player.
	 * 
	 * @see com.risk.business.AbstractPlayer#reinforce(com.risk.model.GamePlay)
	 * @author <a href="mailto:himansipatel1994@gmail.com">Himansi Patel</a>
	 */
	@Override
	public GamePlay reinforce(GamePlay game_play) {
		for (Player player : game_play.getGame_state()) {
			if (player.getId() == game_play.getCurrent_player()) {
				player.setArmy_stock(0);
				for (GamePlayTerritory territory : player.getTerritory_list()) {
					int old_army_value = territory.getNumber_of_armies();
					int new_army_value = old_army_value * 2;
					territory.setNumber_of_armies(new_army_value);
				}
			}
		}
		return game_play;
	}

	/**
	 * Attack of an Cheater Player.
	 * 
	 * @see com.risk.business.AbstractPlayer#attack(com.risk.model.GamePlay)
	 * @author <a href="mailto:himansipatel1994@gmail.com">Himansi Patel</a>
	 */
	@Override
	public GamePlay attack(GamePlay game_play) {
		boolean any_territory_occupied = false;
		Player current_player = null;
		Player defender_player = null;
		List<String> neighbours = null;
		List<String> player_territories = new ArrayList<>();
		GamePlayTerritory defender_territory_data = null;
		GamePlayTerritory attacker_territory_data = null;
		for (Player player : game_play.getGame_state()) {
			if (player.getId() == game_play.getCurrent_player()) {
				current_player = player;
				break;
			} else {
				continue;
			}
		}
		if (current_player != null) {
			for (GamePlayTerritory territory : current_player.getTerritory_list()) {
				player_territories.add(territory.getTerritory_name());
			}
			List<GamePlayTerritory> attacker_territories = new ArrayList<>();
			for (GamePlayTerritory gamePlayTerritory : current_player.getTerritory_list()) {
				GamePlayTerritory newTerritory = new GamePlayTerritory();
				newTerritory.setContinent_name(gamePlayTerritory.getContinent_name());
				newTerritory.setNumber_of_armies(gamePlayTerritory.getNumber_of_armies());
				newTerritory.setTerritory_name(gamePlayTerritory.getTerritory_name());
				attacker_territories.add(newTerritory);
			}

			for (GamePlayTerritory territory : attacker_territories) {
				attacker_territory_data = territory;
				for (com.risk.model.gui.Territory neighbour : game_play.getGui_map().getTerritories()) {
					if (territory.getTerritory_name().equalsIgnoreCase(neighbour.getName())) {
						neighbours = new ArrayList<>();
						neighbours = Arrays.asList(neighbour.getNeighbours().split(";"));
						break;
					} else {
						continue;
					}
				}
				for (com.risk.model.gui.Territory defender_territory : game_play.getGui_map().getTerritories()) {
					if (defender_territory.getName().equalsIgnoreCase(territory.getTerritory_name())
							|| player_territories.contains(defender_territory.getName())
							|| !neighbours.contains(defender_territory.getName())) {
						continue;
					} else {
						Boolean flag_attack_over = false;
						for (Player defender : game_play.getGame_state()) {
							if (defender.getId() == current_player.getId()) {
								continue;
							}
							for (GamePlayTerritory defend_territory : defender.getTerritory_list()) {
								if (attacker_territory_data.getNumber_of_armies() <= 1) {
									flag_attack_over = true;
									break;
								}
								if (defend_territory.getTerritory_name()
										.equalsIgnoreCase(defender_territory.getName())) {
									defender_player = defender;
									defender_territory_data = defend_territory;
									defender_territory_data.setNumber_of_armies(1);
									current_player.getTerritory_list().add(defender_territory_data);
									player_territories.add(defender_territory_data.getTerritory_name());
									attacker_territory_data
									.setNumber_of_armies(attacker_territory_data.getNumber_of_armies() - 1);
									current_player.getTerritory_list().set(
											current_player.getTerritory_list().indexOf(attacker_territory_data),
											attacker_territory_data);
									defender_player.getTerritory_list().remove(
											defender_player.getTerritory_list().indexOf(defender_territory_data));
									flag_attack_over = true;
									any_territory_occupied = true;
									current_player.setAny_territory_occupied(any_territory_occupied);
									break;
								}
							}

							if (flag_attack_over) {
								break;
							}
						}
					}
				}
			}
			if (current_player.isAny_territory_occupied()) {
				ManagePlayer manage_player = new ManagePlayer();
				manage_player.giveCardAtAttackEnd(game_play);
			}
		}
		return game_play;
	}

	/**
	 * Fortify of an Cheater Player.
	 * 
	 * @see com.risk.business.AbstractPlayer#fortify(com.risk.model.GamePlay)
	 * @author <a href="mailto:himansipatel1994@gmail.com">Himansi Patel</a>
	 */
	@Override
	public GamePlay fortify(GamePlay game_play) {
		Player current_player = null;
		List<String> neighbours = null;
		List<String> player_territories = new ArrayList<>();
		GamePlayTerritory attacker_territory_data = null;

		for (Player player : game_play.getGame_state()) {
			if (player.getId() == game_play.getCurrent_player()) {
				current_player = player;
				break;
			} else {
				continue;
			}
		}
		if (current_player != null) {
			for (GamePlayTerritory territory : current_player.getTerritory_list()) {
				player_territories.add(territory.getTerritory_name());
			}
			for (GamePlayTerritory territory : current_player.getTerritory_list()) {
				attacker_territory_data = territory;
				for (com.risk.model.gui.Territory neighbour : game_play.getGui_map().getTerritories()) {
					if (territory.getTerritory_name().equalsIgnoreCase(neighbour.getName())) {
						neighbours = new ArrayList<>();
						neighbours = Arrays.asList(neighbour.getNeighbours().split(";"));
						break;
					} else {
						continue;
					}
				}
				for (com.risk.model.gui.Territory defender_territory : game_play.getGui_map().getTerritories()) {
					if (defender_territory.getName().equalsIgnoreCase(territory.getTerritory_name())
							|| player_territories.contains(defender_territory.getName())
							|| !neighbours.contains(defender_territory.getName())) {
						continue;
					} else {
						attacker_territory_data.setNumber_of_armies(attacker_territory_data.getNumber_of_armies() * 2);

						current_player.getTerritory_list().set(
								current_player.getTerritory_list().indexOf(attacker_territory_data),
								attacker_territory_data);
						break;
					}
				}
			}
		}
		return game_play;
	}

}
