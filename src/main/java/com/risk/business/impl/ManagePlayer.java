package com.risk.business.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.risk.business.IManagePlayer;
import com.risk.file.impl.ManageGamePlayFile;
import com.risk.model.Attack;
import com.risk.model.Card;
import com.risk.model.CardTrade;
import com.risk.model.Continent;
import com.risk.model.GamePlay;
import com.risk.model.GamePlayTerritory;
import com.risk.model.Map;
import com.risk.model.Player;
import com.risk.model.Territory;

/**
 * This class is responsible for managing players state for each game phases.
 * 
 * <b><i>Attack Related Functions</i></b>
 * <li>Valid Attack</li>
 * <li>Roll Dice</li>
 * <li>Main Attack(attack function)</li>
 * 
 * @author <a href="mailto:himansipatel1994@gmail.com">Himansi Patel</a>
 * @author <a href="mailto:mayankjariwala1994@gmail.com">Mayank Jariwala</a>
 * @version 0.0.1
 */
@Service
public class ManagePlayer implements IManagePlayer {

	private List<Player> player_info_list;
	private Map map;

	/**
	 * @see com.risk.business.IManagePlayer#createPlayer(int, java.lang.String,
	 *      java.lang.String)
	 * @author <a href="mailto:himansipatel1994@gmail.com">Himansi Patel</a>
	 * @author <a href="mayankjariwala1994@gmail.com"> Mayank Jariwala </a>
	 *         Modifications of function performed by Mayank Jariwala
	 */
	@Override
	public GamePlay createPlayer(int num_of_players, String file_name, String army_allocation_type) {
		GamePlay game_play = new GamePlay();
		player_info_list = new ArrayList<Player>();
		int army_stock = getArmyStock(num_of_players);
		for (int i = 1; i <= num_of_players; i++) {
			String player_name = "player" + i;
			List<GamePlayTerritory> gameplay_territory_list = new ArrayList<>();
			List<Card> card_list = new ArrayList<Card>();
			Player p = new Player();
			p.setId(i);
			p.setName(player_name);
			p.setArmy_stock(army_stock);
			p.setTerritory_list(gameplay_territory_list);
			p.setCard_list(card_list);
			p.setTrade_count(0);
			player_info_list.add(p);
		}
		ManageMap manage_map_object = new ManageMap();
		map = new Map();
		map = manage_map_object.getFullMap(file_name);
		if (map != null && map.getStatus().equalsIgnoreCase("")) {
			assingTerritoriesToPlayers(map);
			if (army_allocation_type.equalsIgnoreCase("A")) {
				assignArmiesOnTerritories(army_stock);
			}
			game_play = writePlayerToFile(player_info_list, file_name, army_allocation_type);
		} else if (map != null && map.getStatus() != "") {
			game_play.setStatus(map.getStatus());
		} else {
			game_play.setStatus("Invalid Map");
		}
		return game_play;
	}

	/**
	 * This method is used for assigning armies on territories
	 * 
	 * @author <a href="mailto:himansipatel1994@gmail.com">Himansi Patel</a>
	 * @param army_stock number of armies assigned to territories
	 */
	private void assignArmiesOnTerritories(int army_stock) {
		for (int player_index = 0; player_index < player_info_list.size(); player_index++) {
			player_info_list.get(player_index).setArmy_stock(0);
			int i = 0;
			for (int territory_list_index = 0; territory_list_index < player_info_list.get(player_index)
					.getTerritory_list().size(); territory_list_index++) {
				if (army_stock >= player_info_list.get(player_index).getTerritory_list().size()) {
					if (i < army_stock) {
						int sum_armies = player_info_list.get(player_index).getTerritory_list()
								.get(territory_list_index).getNumber_of_armies() + 1;
						player_info_list.get(player_index).getTerritory_list().get(territory_list_index)
								.setNumber_of_armies(sum_armies);
						if (territory_list_index + 1 == player_info_list.get(player_index).getTerritory_list().size()) {
							territory_list_index = -1;
						}
						i++;
					} else {
						break;
					}
				} else {
					int sum_armies = player_info_list.get(player_index).getTerritory_list().get(territory_list_index)
							.getNumber_of_armies() + 1;
					player_info_list.get(player_index).getTerritory_list().get(territory_list_index)
							.setNumber_of_armies(sum_armies);
				}
			}
		}
	}

	/**
	 * This method is an abstraction for the process of converting player Object
	 * into the Game Play File to be saved/loaded.
	 * 
	 * @author <a href="mayankjariwala1994@gmail.com"> Mayank Jariwala </a>
	 *         Modifications done by Mayank Jariwala
	 * @author <a href="mailto:himansipatel1994@gmail.com">Himansi Patel</a>
	 * @param player_info_list This is the entire Player Object which will be
	 *                         converted to a Game Play File Object and then written
	 *                         on to a GamePlay File.
	 * @param file_name        Name of the Map file to be stored in Resource Folder
	 *                         and GamePlay File.
	 * @return GamePlay File Object
	 */
	private GamePlay writePlayerToFile(List<Player> player_info_list, String file_name, String allocation_type) {
		List<Player> player_list_at_file = convertPlayerToFileLayer(player_info_list);
		ManageGamePlay game_manager = new ManageGamePlay();
		ManageGamePlayFile manage_game_play_file = new ManageGamePlayFile();
		String game_phase;
		int current_player = 1;
		game_phase = allocation_type.equalsIgnoreCase("A") ? "REINFORCE" : "STARTUP";
		file_name = (file_name.endsWith(".map") ? file_name.split("\\.")[0] : file_name) + "_"
				+ String.valueOf(System.currentTimeMillis());
		GamePlay game_play = new GamePlay();
		game_play.setFile_name(file_name);
		game_play.setCurrent_player(current_player);
		game_play.setGame_phase(game_phase);
		game_play.setGame_state(player_list_at_file);
		game_play.setMap(map);
		game_play.setCard_trade(new CardTrade());
		List<Card> free_cards = getFreeCards();
		game_play.setFree_cards(free_cards);
		if (allocation_type.equalsIgnoreCase("A")) {
			game_manager.calculateArmiesReinforce(game_play.getGame_state(), map);
		}
		boolean file_write_message = manage_game_play_file.saveGameStateToDisk(game_play);
		if (file_write_message)
			return game_play;
		else
			return null;
	}

	/**
	 * This method set cards according to territory and army name
	 * 
	 * @return
	 */
	private List<Card> getFreeCards() {
		List<Card> card_list = new ArrayList<>();
		List<GamePlayTerritory> territories = getTerritories(map);
		List<List<String>> card_type_list = new ArrayList<>();
		List<String> Infantry = new ArrayList<>();
		List<String> Cavalry = new ArrayList<>();
		List<String> Artillery = new ArrayList<>();
		Infantry.add("Infantry");
		Cavalry.add("Cavalry");
		Artillery.add("Artillery");
		card_type_list.add(Infantry);
		card_type_list.add(Cavalry);
		card_type_list.add(Artillery);
		int total_card_type = card_type_list.size();
		int count = -1;
		for (int i = 0; i < territories.size(); i++) {
			count++;
			if (card_type_list.get(count) != null) {
				try {
					card_type_list.get(count).add(territories.get(i).getTerritory_name());

				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			} else {
				continue;
			}
			if (count == total_card_type - 1) {
				count = -1;
			}

		}
		for (int i = 0; i < card_type_list.size(); i++) {
			String army_type = null;
			for (int j = 0; j < card_type_list.get(i).size(); j++) {

				if (j == 0) {
					army_type = card_type_list.get(i).get(j);
				} else {
					Card card = new Card();
					card.setTerritory_name(card_type_list.get(i).get(j));
					card.setArmy_type(army_type);
					card_list.add(card);
				}
			}
		}

		return card_list;

	}

	/**
	 * This function just convert current Player Object to Game Play File Layer
	 * Object
	 * 
	 * @author <a href="mailto:himansipatel1994@gmail.com">Himansi Patel</a>
	 * @param player_list Entire Player List which will needed to be converted to
	 *                    Game play file object
	 * @return list of converted Game play file object
	 */
	private List<Player> convertPlayerToFileLayer(List<Player> player_list) {
		List<Player> player_list_at_file = new ArrayList<>();
		for (int i = 0; i < player_list.size(); i++) {

			Player player_object_at_file = new Player();
			List<GamePlayTerritory> game_play_territory_list = new ArrayList<>();
			List<Card> card_list = new ArrayList<>();
			player_object_at_file.setName(player_list.get(i).getName());
			player_object_at_file.setId(player_list.get(i).getId());
			player_object_at_file.setArmy_stock(player_list.get(i).getArmy_stock());

			for (int j = 0; j < player_list.get(i).getTerritory_list().size(); j++) {
				GamePlayTerritory game_play_territory = new GamePlayTerritory();
				game_play_territory
						.setTerritory_name(player_list.get(i).getTerritory_list().get(j).getTerritory_name());
				game_play_territory
						.setContinent_name(player_list.get(i).getTerritory_list().get(j).getContinent_name());
				game_play_territory
						.setNumber_of_armies(player_list.get(i).getTerritory_list().get(j).getNumber_of_armies());
				game_play_territory_list.add(game_play_territory);
			}
			player_object_at_file.setTerritory_list(game_play_territory_list);

			for (int k = 0; k < player_list.get(i).getCard_list().size(); k++) {
				Card card = new Card();
				card.setTerritory_name(player_list.get(i).getCard_list().get(k).getTerritory_name());
				card.setArmy_type(player_list.get(i).getCard_list().get(k).getArmy_type());

				card_list.add(card);
			}
			player_object_at_file.setCard_list(card_list);

			player_list_at_file.add(player_object_at_file);
		}
		return player_list_at_file;
	}

	/**
	 * This method is responsible for an initial distribution of armies in Startup
	 * Phase of the game.
	 * 
	 * @author <a href="mailto:himansipatel1994@gmail.com">Himansi Patel</a>
	 * @param num_of_players Number of players playing the game
	 * @return Army count being received by each Player.
	 */
	private int getArmyStock(int num_of_players) {
		int army_stock = 0;
		if (num_of_players == 2) {
			army_stock = 40;
		} else if (num_of_players == 3) {
			army_stock = 35;
		} else if (num_of_players == 4) {
			army_stock = 30;
		} else if (num_of_players == 5) {
			army_stock = 25;
		} else if (num_of_players == 6) {
			army_stock = 20;
		}
		return army_stock;
	}

	/**
	 * This method is an abstraction for the process of initially allocating
	 * territories to each player, in a round robin fashion.
	 * 
	 * @author <a href="mailto:himansipatel1994@gmail.com">Himansi Patel</a>
	 * @param map Map Object for the active map in Game.
	 */
	private void assingTerritoriesToPlayers(Map map) {
		List<GamePlayTerritory> territories = getTerritories(map);
		int total_player = player_info_list.size();
		int count = -1;
		for (int i = 0; i < territories.size(); i++) {
			count++;
			if (player_info_list.get(count) != null) {
				try {
					player_info_list.get(count).getTerritory_list().add(territories.get(i));
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			} else {
				continue;
			}
			if (count == total_player - 1) {
				count = -1;
			}
		}
	}

	/**
	 * This method is randomly linking territories to game play territory object
	 * 
	 * @author <a href="mailto:himansipatel1994@gmail.com">Himansi Patel</a>
	 * @param map Map Object for retrieving Territories.
	 * @return List of territories
	 */
	private List<GamePlayTerritory> getTerritories(Map map) {
		Continent map_continent;
		Territory map_territory;
		HashMap<String, Continent> continents = new HashMap<String, Continent>();
		continents = map.getContinents();
		List<Territory> temp_territory_list;
		String continent_name;
		ArrayList<GamePlayTerritory> total_territory_list = new ArrayList<GamePlayTerritory>();
		for (Entry<String, Continent> m : continents.entrySet()) {
			continent_name = m.getKey();
			map_continent = m.getValue();
			temp_territory_list = map_continent.getTerritories();
			for (int i = 0; i < temp_territory_list.size(); i++) {
				map_territory = temp_territory_list.get(i);
				GamePlayTerritory game_play_territory = new GamePlayTerritory();
				game_play_territory.setTerritory_name(map_territory.getName());
				game_play_territory.setContinent_name(continent_name);
				game_play_territory.setNumber_of_armies(0);
				total_territory_list.add(game_play_territory);
			}
		}
		return total_territory_list;
	}

	/**
	 * 
	 * @see com.risk.business.IManagePlayer#fortify(com.risk.model.GamePlay)
	 * 
	 * @author <a href="zinnia.rana.22@gmail.com">Zinnia Rana</a>
	 */
	@Override
	public GamePlay fortify(GamePlay game_play) {
		// Attacker can move armies between adjacent territories
		int destination_territory_armies = 0;
		int source_territory_armies = 0;
		String source_territory = game_play.getFortification().getSource_territory();
		String destination_territory = game_play.getFortification().getDestination_territory();
		int army_count = game_play.getFortification().getArmy_count();
		List<Player> players_list = game_play.getGame_state();
		for (Player player : players_list) {
			List<GamePlayTerritory> territory_list = player.getTerritory_list();
			for (GamePlayTerritory each_territory : territory_list) {
				if (each_territory.getTerritory_name().equalsIgnoreCase(source_territory)) {
					// Attacker Territory Object
					source_territory_armies = each_territory.getNumber_of_armies();
				}
				if (each_territory.getTerritory_name().equalsIgnoreCase(destination_territory)) {
					// Attacker Territory Object
					destination_territory_armies = each_territory.getNumber_of_armies();
				}
			}
		}
		if (source_territory.equalsIgnoreCase(destination_territory)) {
			game_play.setStatus("Fortification cannot be performed because same territory is selected in destination.");
			return game_play;
		} else if (source_territory_armies <= army_count) {
			game_play.setStatus(source_territory + " is not having minimum armies to transfer");
			return game_play;
		} else {
			for (Iterator<Entry<String, Continent>> iterator = game_play.getMap().getContinents().entrySet()
					.iterator(); iterator.hasNext();) {
				java.util.Map.Entry<String, Continent> continent_entry = iterator.next();
				for (Territory territory : continent_entry.getValue().getTerritories()) {
					if (source_territory.equalsIgnoreCase(territory.getName())) {
						for (String each_neighbour_territory : territory.getNeighbours())
							if (destination_territory.equalsIgnoreCase(each_neighbour_territory)) {
								source_territory_armies = source_territory_armies - army_count;
								destination_territory_armies = destination_territory_armies + army_count;
								game_play.setStatus("Fortification Successful");
								break;
							} else {
								game_play.setStatus("Invalid Fortification - " + destination_territory
										+ " is not a Neighbouring Territory");
							}
					}
				}
			}
		}
		return game_play;
	}
	
	/**
	 * 
	 * @see com.risk.business.IManagePlayer#attack(com.risk.model.GamePlay)
	 * 
	 * @author <a href="mailto:himansipatel1994@gmail.com">Himansi Patel</a>
	 * @author <a href="mayankjariwala1994@gmail.com"> Mayank Jariwala </a>
	 */
	@Override
	public GamePlay attack(GamePlay game_play) {
		int attacker_id = 0;
		int defender_id = 0;
		String attack_message = "";

		// Counter Variable : Keep track of territory found for attacker and defender
		int found_territory = 0;
		List<GamePlayTerritory> attacker_territory_list = new ArrayList<>();
		List<GamePlayTerritory> defender_territory_list = new ArrayList<>();
		List<String> attack_message_list = new ArrayList<>();
		Attack attack = game_play.getAttack();

		// Local Variable : To get actual army values from getter
		int attacker_terrtiory_armies = 0;
		int defender_territory_armies = 0;
		String attacker_territory_name = game_play.getAttack().getAttacker_territory();
		String defender_territory_name = game_play.getAttack().getDefender_territory();
		List<Player> players_list = game_play.getGame_state();

		for (Player player : players_list) {
			List<GamePlayTerritory> territory_list = player.getTerritory_list();
			for (GamePlayTerritory territory : territory_list) {
				// Attacker Territory Object
				if (territory.getTerritory_name().equalsIgnoreCase(attacker_territory_name)) {
					attacker_id = player.getId();
					attacker_terrtiory_armies = territory.getNumber_of_armies();
					attacker_territory_list.add(territory);
					found_territory++;
				}
				// Defender Territory Object
				if (territory.getTerritory_name().equalsIgnoreCase(defender_territory_name)) {
					defender_id = player.getId();
					defender_territory_armies = territory.getNumber_of_armies();
					defender_territory_list.add(territory);
					found_territory++;
				}
				if (found_territory == 2) {
					break;
				}
			}
		}
		int attacker_dice_no = 0;
		int defender_dice_no = 0;
		if (game_play.getGame_phase().equalsIgnoreCase("ATTACK_ALL_OUT")) {
			setAttackerDefenderDiceNo(attacker_territory_list, defender_territory_list, attack);
		}
		attacker_dice_no = game_play.getAttack().getAttacker_dice_no();
		defender_dice_no = game_play.getAttack().getDefender_dice_no();
		String valid_attack_message = checkForValidAttack(attacker_terrtiory_armies, defender_territory_armies,
				attacker_dice_no, defender_dice_no);
		if (valid_attack_message.trim().length() == 0) {
			// Roll Dice
			List<Integer> attack_result = rollDiceDecision(attacker_dice_no, defender_dice_no);
			for (int i = 0; i < attack_result.size(); i++) {
				int result = attack_result.get(i);
				if (result == 1) {
					// Attacker Won
					GamePlayTerritory def_obj = defender_territory_list.get(0);
					def_obj.setNumber_of_armies(def_obj.getNumber_of_armies() - 1);
					attack_message = attacker_id + " Won !!";
					attack_message_list.add(attack_message);
					if (def_obj.getNumber_of_armies() == 0) {
						attacker_territory_list.add(def_obj);
						attack_message = attacker_id + " Occupies Defender Territory";
						attack_message_list.add(attack_message);
					}
				} else {
					// Defender Won
					GamePlayTerritory att_obj = attacker_territory_list.get(0);
					att_obj.setNumber_of_armies(att_obj.getNumber_of_armies() - 1);
					attack_message = defender_id + " Won !!";
					attack_message_list.add(attack_message);
				}
			}

			// Iterating Attacker Territory List for performing actions regarding dice
			// result
			for (GamePlayTerritory att_territory : attacker_territory_list) {
				for (Player player : players_list) {
					if (player.getId() == attacker_id) {
						List<GamePlayTerritory> territory_list = player.getTerritory_list();
						for (int j = 0; j < territory_list.size(); j++) {
							if (att_territory.getTerritory_name()
									.equalsIgnoreCase(territory_list.get(j).getTerritory_name())) {
								territory_list.get(j).setNumber_of_armies(att_territory.getNumber_of_armies());
							} else if (!territory_list.contains(att_territory)) {
								territory_list.add(att_territory);
							}
						}
					}
				}
			}

			// Iterating Defender Territory List for performing actions regarding dice
			// result
			for (GamePlayTerritory deff_territory : defender_territory_list) {
				for (Player player : players_list) {
					if (player.getId() == defender_id) {
						List<GamePlayTerritory> territory_list = player.getTerritory_list();
						for (GamePlayTerritory territory : territory_list) {
							if (deff_territory.getTerritory_name().equalsIgnoreCase(territory.getTerritory_name())) {
								if (deff_territory.getNumber_of_armies() == 0) {
									territory_list.remove(territory);
									game_play.setGame_phase("ATTACK_MOVE_ON");
								} else {
									territory.setNumber_of_armies(deff_territory.getNumber_of_armies());
								}
								break;
							}
						}
					}
				}
			}
			attack_message = String.join(",", attack_message_list);
			System.out.println(attack_message);
			game_play.setStatus(attack_message);
		} else {
			game_play.setStatus(valid_attack_message);
		}
		if (game_play.getGame_phase().equalsIgnoreCase("ATTACK_ALL_OUT")
				&& attacker_territory_list.get(0).getNumber_of_armies() > 1) {
			setAttackerDefenderDiceNo(attacker_territory_list, defender_territory_list, attack);
			attack(game_play);
		}
		return game_play;
	}

	/**
	 * This function decide how many maximum dice attacker and defender can roll max
	 * 
	 * @author <a href="mailto:mayankjariwala1994@gmail.com">Mayank Jariwala</a>
	 * @author <a href="mailto:himansipatel1994@gmail.com">Himansi Patel</a>
	 * @param attacker_territory_list : List of Territories Attacker owes
	 * @param defender_territory_list : List of Territories Defender owes
	 * @param attack
	 */
	private void setAttackerDefenderDiceNo(List<GamePlayTerritory> attacker_territory_list,
			List<GamePlayTerritory> defender_territory_list, Attack attack) {
		int attacker_dice = attacker_territory_list.get(0).getNumber_of_armies() - 1;
		int defender_dice = defender_territory_list.get(0).getNumber_of_armies();
		if (attacker_dice >= 3) {
			attack.setAttacker_dice_no(3);
		} else {
			attack.setAttacker_dice_no(attacker_dice);
		}
		if (defender_dice >= 2) {
			attack.setDefender_dice_no(2);
		} else {
			attack.setDefender_dice_no(defender_dice);
		}
	}

	/**
	 * This method perform validation regarding attack.
	 * 
	 * @param attacker_territory_armies
	 * @param defender_territory_armies
	 * @param attacker_dice_no          : No. of dice attacker decided to roll
	 * @param defender_dice_no          : No. of dice defender decided to roll
	 * @return Attack Result Message
	 */
	private String checkForValidAttack(int attacker_territory_armies, int defender_territory_armies,
			int attacker_dice_no, int defender_dice_no) {

		String message = "";
		if (attacker_territory_armies <= attacker_dice_no || attacker_territory_armies == 1) {
			if (attacker_territory_armies - 1 == 0) {
				message = "Invalid Attack By Attacker (You can't attack with this territory)";
			} else {
				message = "Invalid Attack By Attacker (You can roll max " + (attacker_territory_armies - 1) + " dice)";
			}
		}

		if (defender_territory_armies < defender_dice_no) {
			message = "Invalid Defend(You can roll max " + defender_territory_armies + " dice )";
		}

		return message;
	}

	/**
	 * This method rolls dice for both player and return dice result for respective
	 * round as list.
	 * 
	 * @param attacker_dice_no : No. of dice attacker decided to roll
	 * @param defender_dice_no : No. of dice defender decided to roll
	 * @return List of Attack Result
	 */
	private List<Integer> rollDiceDecision(int attacker_dice_no, int defender_dice_no) {
		List<Integer> dice_roll_result = new ArrayList<>();
		String dice_case = attacker_dice_no + "_" + defender_dice_no;
		switch (dice_case) {
		case "1_1":
			dice_roll_result = rollDiceOneOnOne();
			break;
		case "1_2":
			dice_roll_result = rollDiceOneOnTwo();
			break;
		case "2_1":
			dice_roll_result = rollDiceTwoOnOne();
			break;
		case "2_2":
			dice_roll_result = rollDiceTwoOnTwo();
			break;
		case "3_1":
			dice_roll_result = rollDiceThreeOnOne();
			break;
		case "3_2":
			dice_roll_result = rollDiceThreeOnTwo();
			break;
		default:
			break;
		}
		return dice_roll_result;
	}

	/**
	 * This method roll dice for the case of one attacker army v/s one defender
	 * army.
	 * 
	 * @return Attack Result List
	 */
	private List<Integer> rollDiceOneOnOne() {
		Random random = new Random();
		int dice_result_flag = 0;
		int attacker_dice_result = random.nextInt(6);
		int defender_dice_result = random.nextInt(6);
		if (attacker_dice_result > defender_dice_result) {
			dice_result_flag = 1;
		} else if (attacker_dice_result <= defender_dice_result) {
			dice_result_flag = 0;
		}
		return Arrays.asList(dice_result_flag);
	}

	/**
	 * This method roll dice for the case of one attacker army v/s two defender
	 * army.
	 * 
	 * @return Attack Result List
	 */
	private List<Integer> rollDiceOneOnTwo() {
		Random random = new Random();
		int dice_result_flag = 0;
		int attacker_dice_result = random.nextInt(6);
		int defender_dice_result = Math.max(random.nextInt(6), random.nextInt(6));
		if (attacker_dice_result > defender_dice_result) {
			dice_result_flag = 1;
		} else if (attacker_dice_result <= defender_dice_result) {
			dice_result_flag = 0;
		}
		return Arrays.asList(dice_result_flag);
	}

	/**
	 * This method roll dice for the case of two attacker army v/s one defender
	 * army.
	 * 
	 * @return Attack Result List
	 */
	private List<Integer> rollDiceTwoOnOne() {
		Random random = new Random();
		int dice_result_flag = 0;
		int attacker_dice_result = Math.max(random.nextInt(6), random.nextInt(6));
		int defender_dice_result = random.nextInt(6);
		if (attacker_dice_result > defender_dice_result) {
			dice_result_flag = 1;
		} else if (attacker_dice_result <= defender_dice_result) {
			dice_result_flag = 0;
		}
		return Arrays.asList(dice_result_flag);
	}

	/**
	 * This method roll dice for the case of two attacker army v/s two defender
	 * army.
	 * 
	 * @return Attack Result List
	 */
	private List<Integer> rollDiceTwoOnTwo() {
		Random random = new Random();
		HashMap<String, List<Integer>> dice_result_list = new HashMap<>();
		int attacker_roll_one = random.nextInt(6);
		int attacker_roll_two = random.nextInt(6);
		dice_result_list.put("attacker", Arrays.asList(attacker_roll_one, attacker_roll_two));
		int defender_roll_one = random.nextInt(6);
		int defender_roll_two = random.nextInt(6);
		dice_result_list.put("defender", Arrays.asList(defender_roll_one, defender_roll_two));
		int attacker_max = Collections.max(dice_result_list.get("attacker"));
		int defender_max = Collections.max(dice_result_list.get("defender"));
		int max_result = attacker_max > defender_max ? 1 : 0;
		int attacker_min = Collections.min(dice_result_list.get("attacker"));
		int defender_min = Collections.min(dice_result_list.get("defender"));
		int min_result = attacker_min > defender_min ? 1 : 0;
		dice_result_list.clear();
		return Arrays.asList(max_result, min_result);
	}

	/**
	 * This method roll dice for the case of three attacker army v/s two defender
	 * army.
	 * 
	 * @return Attack Result List
	 */
	private List<Integer> rollDiceThreeOnTwo() {
		Random random = new Random();
		List<Integer> attacker_list = new ArrayList<>();
		List<Integer> defender_list = new ArrayList<>();

		attacker_list.add(random.nextInt(6));
		attacker_list.add(random.nextInt(6));
		attacker_list.add(random.nextInt(6));

		defender_list.add(random.nextInt(6));
		defender_list.add(random.nextInt(6));

		int attacker_max = Collections.max(attacker_list);
		attacker_list.remove((attacker_list.indexOf(attacker_max)));
		int defender_max = Collections.max(defender_list);
		defender_list.remove((defender_list.indexOf(defender_max)));
		int first_result = attacker_max > defender_max ? 1 : 0;

		int attacker_second_max = Collections.max(attacker_list);
		attacker_list.remove((attacker_list.indexOf(attacker_second_max)));
		int defender_second_max = Collections.max(defender_list);
		defender_list.remove((defender_list.indexOf(defender_second_max)));
		int second_result = attacker_second_max > defender_second_max ? 1 : 0;
		return Arrays.asList(first_result, second_result);
	}

	/**
	 * This method roll dice for the case of three attacker army v/s one defender
	 * army.
	 * 
	 * @return Attack Result List
	 */
	private List<Integer> rollDiceThreeOnOne() {
		Random random = new Random();
		int dice_result_flag = 0;
		int attacker_dice_result = Math.max(Math.max(random.nextInt(6), random.nextInt(6)), random.nextInt(6));
		int defender_dice_result = random.nextInt(6);
		if (attacker_dice_result > defender_dice_result) {
			dice_result_flag = 1;
		} else if (attacker_dice_result <= defender_dice_result) {
			dice_result_flag = 0;
		}
		return Arrays.asList(dice_result_flag);
	}
}