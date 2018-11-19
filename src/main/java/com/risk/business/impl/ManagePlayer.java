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

import com.risk.business.AbstractPlayer;
import com.risk.business.IManageMap;
import com.risk.business.IManagePlayer;
import com.risk.file.impl.ManageGamePlayFile;
import com.risk.model.Attack;
import com.risk.model.AttackArmyMove;
import com.risk.model.Card;
import com.risk.model.CardTrade;
import com.risk.model.Continent;
import com.risk.model.Domination;
import com.risk.model.GamePlay;
import com.risk.model.GamePlayTerritory;
import com.risk.model.Map;
import com.risk.model.Player;
import com.risk.model.Territory;

/**
 * This class is the Concrete Implementation for interface IManagePlayer
 * 
 * @author <a href="mailto:himansipatel1994@gmail.com">Himansi Patel</a>
 * @author <a href="mailto:mayankjariwala1994@gmail.com">Mayank Jariwala</a>
 * @version 0.0.1
 */
@Service
public class ManagePlayer implements IManagePlayer {

	private List<AbstractPlayer> player_info_list;
	private Map map;
	private boolean is_territory_occupied = false;

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
		player_info_list = new ArrayList<AbstractPlayer>();
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
			return game_play;
		} else {
			game_play.setStatus("Invalid Map");
			return game_play;
		}
		Domination domination = new Domination();
		ManageDomination manage_domination = new ManageDomination();
		domination.addObserver(manage_domination);
		domination.updateDomination(game_play);
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
	private GamePlay writePlayerToFile(List<AbstractPlayer> player_info_list, String file_name, String allocation_type) {
		List<AbstractPlayer> player_list_at_file = convertPlayerToFileLayer(player_info_list);
		ManageGamePlay game_manager = new ManageGamePlay();
		ManageGamePlayFile manage_game_play_file = new ManageGamePlayFile();
		IManageMap map_manager = new ManageMap();
		String game_phase;
		int current_player = 1;
		GamePlay game_play = new GamePlay();
		game_play.setGui_map(map_manager.fetchMap(file_name));
		game_phase = allocation_type.equalsIgnoreCase("A") ? "REINFORCEMENT" : "STARTUP";
		file_name = (file_name.endsWith(".map") ? file_name.split("\\.")[0] : file_name) + "_"
				+ String.valueOf(System.currentTimeMillis());
		game_play.setFile_name(file_name);
		game_play.setCurrent_player(current_player);
		game_play.setGame_phase(game_phase);
		game_play.setGame_state(player_list_at_file);
		game_play.setMap(map);
		game_play.setCard_trade(new CardTrade());
		List<Card> free_cards = getFreeCards();
		game_play.setFree_cards(free_cards);
		if (allocation_type.equalsIgnoreCase("A")) {
			game_manager.calculateArmiesReinforce(game_play.getGame_state(), map, game_play.getCurrent_player());
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
	 * @return List of Cards Total No. of Cards in entire game before game starts
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
	private List<AbstractPlayer> convertPlayerToFileLayer(List<AbstractPlayer> player_list) {
		List<AbstractPlayer> player_list_at_file = new ArrayList<>();
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
	public List<GamePlayTerritory> getTerritories(Map map) {
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
	 * @see com.risk.business.IManagePlayer#reinforce(com.risk.model.GamePlay)
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 */
	@Override
	public GamePlay reinforce(GamePlay game_play) {
		if (game_play.getGame_phase().equalsIgnoreCase("TRADE_CARDS")) {
			tradeCards(game_play);
		}
		return game_play;
	}

	/**
	 * This method handles the trading of cards during reinforcement phase of the
	 * game-play.
	 * 
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 * @param game_state State of the game at point of time holding the entire info
	 *                   about game-play. Like the current phase and player.
	 * @return game_state after updating info on trading of cards.
	 */
	private GamePlay tradeCards(GamePlay game_state) {

		CardTrade trade_card = game_state.getCard_trade();
		if (trade_card != null) {
			if (trade_card.getCard1() == null || trade_card.getCard2() == null || trade_card.getCard3() == null) {
				game_state.setStatus("Trading requires a minimum of three cards to be selected.");
			} else {

				/**
				 * First part of condition (before OR) checks if all images on the three cards
				 * are same and the second part (after OR) checks if all the three are
				 * different.
				 */
				if ((trade_card.getCard1().getArmy_type().equalsIgnoreCase(trade_card.getCard2().getArmy_type())
						&& trade_card.getCard1().getArmy_type().equalsIgnoreCase(trade_card.getCard3().getArmy_type()))
						|| (!trade_card.getCard1().getArmy_type().equalsIgnoreCase(trade_card.getCard2().getArmy_type())
								&& !trade_card.getCard2().getArmy_type()
								.equalsIgnoreCase(trade_card.getCard3().getArmy_type())
								&& !trade_card.getCard3().getArmy_type()
								.equalsIgnoreCase(trade_card.getCard1().getArmy_type()))) {

					int current_player = game_state.getCurrent_player();

					for (AbstractPlayer player : game_state.getGame_state()) {

						if (player.getId() == current_player) {

							updateTradedArmies(player);
							updateCardLists(player, game_state.getFree_cards(), trade_card);

							/**
							 * Check if the Player controls any territory which is present in one of the
							 * cards being traded.
							 */
							List<GamePlayTerritory> player_territory_list = player.getTerritory_list();

							if (player_territory_list != null) {

								for (GamePlayTerritory gamePlayTerritory : player_territory_list) {

									if (gamePlayTerritory.getTerritory_name()
											.equalsIgnoreCase(trade_card.getCard1().getTerritory_name())
											|| gamePlayTerritory.getTerritory_name()
											.equalsIgnoreCase(trade_card.getCard2().getTerritory_name())
											|| gamePlayTerritory.getTerritory_name()
											.equalsIgnoreCase(trade_card.getCard3().getTerritory_name())) {

										/**
										 * An additional two armies given if the Player controls any territory which is
										 * present in one of the cards being traded.
										 */
										gamePlayTerritory
										.setNumber_of_armies(gamePlayTerritory.getNumber_of_armies() + 2);
										break;
									}
								}
							}
							break;
						}
					}
				} else {
					game_state.setStatus("Either all three cards should have same image or all three different.");
				}
			}
		} else {
			game_state.setStatus("Inavlid Trade State during Gameplay");
		}
		return game_state;
	}

	/**
	 * This method assigns a random card to a player and removes that card from the
	 * free stock.
	 * 
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 * @param game_state Overall game_state to be updated after this move
	 */
	public void addCardToPlayer(GamePlay game_state) {

		if (game_state != null) {

			for (AbstractPlayer player : game_state.getGame_state()) {

				if (player.getId() == game_state.getCurrent_player()) {

					Random rand = new Random();
					int idx = rand.nextInt(game_state.getFree_cards().size());

					player.getCard_list().add(game_state.getFree_cards().get(idx));
					game_state.getFree_cards().remove(idx);

					break;
				}
			}
		}
	}

	/**
	 * This method updates the player's army count during the trade of cards.
	 * 
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 * @param player State of the current Player.
	 */
	private void updateTradedArmies(AbstractPlayer player) {
		if (player.getTrade_count() == 0) {
			player.setArmy_stock(player.getArmy_stock() + 4);
			player.setTrade_count(1);
		} else if (player.getTrade_count() == 1) {
			player.setArmy_stock(player.getArmy_stock() + 6);
			player.setTrade_count(2);
		} else if (player.getTrade_count() == 2) {
			player.setArmy_stock(player.getArmy_stock() + 8);
			player.setTrade_count(3);
		} else if (player.getTrade_count() == 3) {
			player.setArmy_stock(player.getArmy_stock() + 10);
			player.setTrade_count(4);
		} else if (player.getTrade_count() == 4) {
			player.setArmy_stock(player.getArmy_stock() + 12);
			player.setTrade_count(5);
		} else if (player.getTrade_count() == 5) {
			player.setArmy_stock(player.getArmy_stock() + 15);
			player.setTrade_count(6);
		} else if (player.getTrade_count() > 5) {
			player.setArmy_stock(player.getArmy_stock() + 15 + ((player.getTrade_count() - 5) * 5));
			player.setTrade_count(player.getTrade_count() + 1);
		}
	}

	/**
	 * This method updates the player's card list and well as the free card list
	 * after the trading is over.
	 * 
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 * @param player       State of the current Player.
	 * @param free_cards   List of cards which are free for allocation
	 * @param traded_cards The set of three cards being traded.
	 */
	private void updateCardLists(AbstractPlayer player, List<Card> free_cards, CardTrade traded_cards) {
		free_cards.add(traded_cards.getCard1());
		free_cards.add(traded_cards.getCard2());
		free_cards.add(traded_cards.getCard3());
		Iterator<Card> i = player.getCard_list().iterator();
		while (i.hasNext()) {
			Card card = (Card) i.next();
			if ((card.getArmy_type().equalsIgnoreCase(traded_cards.getCard1().getArmy_type())
					&& card.getTerritory_name().equalsIgnoreCase(traded_cards.getCard1().getTerritory_name()))
					|| (card.getArmy_type().equalsIgnoreCase(traded_cards.getCard2().getArmy_type())
							&& card.getTerritory_name().equalsIgnoreCase(traded_cards.getCard2().getTerritory_name()))
					|| (card.getArmy_type().equalsIgnoreCase(traded_cards.getCard3().getArmy_type()) && card
							.getTerritory_name().equalsIgnoreCase(traded_cards.getCard3().getTerritory_name()))) {
				i.remove();
			}
		}
	}

	/**
	 * @see com.risk.business.IManagePlayer#attack(com.risk.model.GamePlay)
	 * @author <a href="mailto:himansipatel1994@gmail.com">Himansi Patel</a>
	 * @author <a href="mayankjariwala1994@gmail.com"> Mayank Jariwala </a>
	 */
	@Override
	public GamePlay attack(GamePlay game_play) {
		if (game_play.getGame_phase().equalsIgnoreCase("ATTACK_ARMY_MOVE")) {
			attackArmyMove(game_play);
			return game_play;
		}

		if (game_play.getGame_phase().equalsIgnoreCase("ATTACK_END")) {
			giveCardAtAttackEnd(game_play);
			return game_play;
		}

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
		List<AbstractPlayer> players_list = game_play.getGame_state();

		for (AbstractPlayer player : players_list) {
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
		String valid_territory_message = checkForValidAttackTerritory(attacker_id, defender_id);
		if (valid_territory_message.trim().length() == 0) {
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
						attack_message = "Attacker Won";
						attack_message_list.add(attack_message);
						if (def_obj.getNumber_of_armies() == 0) {
							attacker_territory_list.add(def_obj);
							attack_message = "Attacker Occupies Defender Territory";
							attack_message_list.add(attack_message);
						}
					} else {
						// Defender Won
						GamePlayTerritory att_obj = attacker_territory_list.get(0);
						att_obj.setNumber_of_armies(att_obj.getNumber_of_armies() - 1);
						attack_message = "Defender Won";
						attack_message_list.add(attack_message);
					}
				}

				// Iterating Attacker Territory List for performing actions regarding dice
				// result
				for (GamePlayTerritory att_territory : attacker_territory_list) {
					for (AbstractPlayer player : players_list) {
						if (player.getId() == attacker_id) {
							List<GamePlayTerritory> territory_list = player.getTerritory_list();
							for (int j = 0; j < territory_list.size(); j++) {
								if (att_territory.getTerritory_name()
										.equalsIgnoreCase(territory_list.get(j).getTerritory_name())) {
									if (attacker_territory_list.size() > 1) {
										if (att_territory.getNumber_of_armies() == 1) {
											territory_list.get(j)
											.setNumber_of_armies(att_territory.getNumber_of_armies() + 1);
										} else {
											territory_list.get(j)
											.setNumber_of_armies(att_territory.getNumber_of_armies());
										}
									} else {
										territory_list.get(j).setNumber_of_armies(att_territory.getNumber_of_armies());
									}
								} else if (!territory_list.contains(att_territory)) {
									territory_list.add(att_territory);
									is_territory_occupied = true;
									break;
								}
							}
							player.setAny_territory_occupied(is_territory_occupied);
						}
					}
				}

				// Iterating Defender Territory List for performing actions regarding dice
				// result
				for (GamePlayTerritory deff_territory : defender_territory_list) {
					for (AbstractPlayer player : players_list) {
						if (player.getId() == defender_id) {
							List<GamePlayTerritory> territory_list = player.getTerritory_list();
							for (GamePlayTerritory territory : territory_list) {
								if (deff_territory.getTerritory_name()
										.equalsIgnoreCase(territory.getTerritory_name())) {
									if (deff_territory.getNumber_of_armies() == 0) {
										territory_list.remove(territory);
										game_play.setGame_phase("ATTACK_ARMY_MOVE");
										AttackArmyMove attack_army_move = game_play.getArmy_move();
										if (attack_army_move == null) {
											attack_army_move = new AttackArmyMove();
										}
										attack_army_move
										.setAttacker_territory(game_play.getAttack().getAttacker_territory());
										attack_army_move
										.setDefender_territory(game_play.getAttack().getDefender_territory());
										attack_army_move.setAmry_count(0);
										game_play.setArmy_move(attack_army_move);
										break;
									} else {
										territory.setNumber_of_armies(deff_territory.getNumber_of_armies());
									}
								}
							}
						}
					}
				}
				boolean is_winner = declareGameWinner(attacker_territory_list, game_play.getMap());
				if (is_winner) {
					game_play.setGame_phase("GAME_FINISH");
					game_play.setStatus("Player" + game_play.getCurrent_player() + " is winner !!");
				} else {
					attack_message = String.join("\n", attack_message_list);
					game_play.setStatus(attack_message);
				}
			} else {
				game_play.setStatus(valid_attack_message);
			}
			if (game_play.getGame_phase().equalsIgnoreCase("ATTACK_ALL_OUT")
					&& attacker_territory_list.get(0).getNumber_of_armies() > 1) {
				attack(game_play);
			}
		} else {
			game_play.setStatus(valid_territory_message);
		}

		return game_play;
	}

	/**
	 * This method performs army move when attacker occupies defender territory.
	 * 
	 * @author <a href="himansipatel1994@gmail.com"> Himansi Patel </a>
	 * @param game_play : GamePlay Object
	 * @return GamePlay updated state of the game after army move during attack
	 *         phase.
	 */
	private GamePlay attackArmyMove(GamePlay game_play) {
		String source_territory = game_play.getArmy_move().getAttacker_territory();
		String destination_territory = game_play.getArmy_move().getDefender_territory();
		if (source_territory.equalsIgnoreCase(destination_territory)) {
			game_play.setStatus(" cannot move armies because same territory is selected in destination.");
			game_play.setGame_phase("ATTACK_ARMY_MOVE");
			return game_play;
		}

		int army_count = game_play.getArmy_move().getAmry_count();

		if (army_count == 0) {
			game_play.setStatus("Atleast 1 army should be moved.");
			game_play.setGame_phase("ATTACK_ARMY_MOVE");
			return game_play;
		}

		GamePlayTerritory source_territory_instance = null, dest_territory_instance = null;

		for (AbstractPlayer player : game_play.getGame_state()) {

			if (player.getId() != game_play.getCurrent_player()) {
				continue;
			}

			for (GamePlayTerritory each_territory : player.getTerritory_list()) {

				if (each_territory.getTerritory_name().equalsIgnoreCase(source_territory)) {
					source_territory_instance = each_territory;
				} else if (each_territory.getTerritory_name().equalsIgnoreCase(destination_territory)) {
					dest_territory_instance = each_territory;
				}
				if (source_territory_instance != null && dest_territory_instance != null) {
					if (source_territory_instance.getNumber_of_armies() <= army_count) {
						game_play.setStatus(source_territory + " is not having minimum armies to transfer.");
						game_play.setGame_phase("ATTACK_ARMY_MOVE");
						return game_play;
					} else {
						source_territory_instance
						.setNumber_of_armies(source_territory_instance.getNumber_of_armies() - army_count);
						dest_territory_instance
						.setNumber_of_armies(dest_territory_instance.getNumber_of_armies() + army_count);
						game_play.setStatus(
								army_count + " army moved from " + source_territory_instance.getTerritory_name()
								+ " to " + dest_territory_instance.getTerritory_name());
						game_play.setGame_phase("ATTACK_ARMY_ON");
					}
					break;
				}
			}
			if (source_territory_instance == null || dest_territory_instance == null) {
				game_play.setStatus("Invalid Move (Not Neighboring Territory)");
				game_play.setGame_phase("ATTACK_ARMY_MOVE");
				return game_play;
			}
		}
		return game_play;

	}

	/**
	 * 
	 * @see com.risk.business.IManagePlayer#fortify(com.risk.model.GamePlay)
	 * 
	 * @author <a href="zinnia.rana.22@gmail.com">Zinnia Rana</a>
	 */
	@Override
	public GamePlay fortify(GamePlay game_play) {
		String source_territory = game_play.getFortification().getSource_territory();
		String destination_territory = game_play.getFortification().getDestination_territory();
		if (source_territory.equalsIgnoreCase(destination_territory)) {
			game_play.setStatus("Fortification cannot be performed because same territory is selected in destination.");
			return game_play;
		}

		int army_count = game_play.getFortification().getArmy_count();

		if (army_count == 0) {
			game_play.setStatus("Atleast 1 army should be moved.");
			return game_play;
		}

		GamePlayTerritory source_territory_instance = null, dest_territory_instance = null;

		for (AbstractPlayer player : game_play.getGame_state()) {

			if (player.getId() != game_play.getCurrent_player()) {
				continue;
			}

			for (GamePlayTerritory each_territory : player.getTerritory_list()) {

				if (each_territory.getTerritory_name().equalsIgnoreCase(source_territory)) {
					source_territory_instance = each_territory;
				} else if (each_territory.getTerritory_name().equalsIgnoreCase(destination_territory)) {
					dest_territory_instance = each_territory;
				}
				if (source_territory_instance != null && dest_territory_instance != null) {
					if (source_territory_instance.getNumber_of_armies() <= army_count) {
						game_play.setStatus(source_territory + " is not having minimum armies to transfer.");
						return game_play;
					} else {
						source_territory_instance
						.setNumber_of_armies(source_territory_instance.getNumber_of_armies() - army_count);
						dest_territory_instance
						.setNumber_of_armies(dest_territory_instance.getNumber_of_armies() + army_count);
						game_play.setStatus(
								army_count + " army moved from " + source_territory_instance.getTerritory_name()
								+ " to " + dest_territory_instance.getTerritory_name());
					}
					break;
				}
			}
			if (source_territory_instance == null || dest_territory_instance == null) {
				game_play.setStatus("Invalid Move (Not Neighboring Territory)");
				return game_play;
			}
		}
		return game_play;
	}

	/**
	 * This function is use to detect if current player(i.e attacker) is winner once
	 * attacker occupy defender territory
	 * 
	 * @author <a href="mayankjariwala1994@gmail.com">Mayank Jariwala</a>
	 * @author <a href="himansipatel1994@gmail.com">Himansi Patel</a>
	 * @param attacker_territory_list Total territory list of attacker
	 * @param map                     The Entire Game Map Object
	 * @return Flag Current Player(Attacker) is Winner or not
	 */
	public boolean declareGameWinner(List<GamePlayTerritory> attacker_territory_list, Map map) {
		boolean is_winner = false;
		int total_territory_in_game = getTerritories(map).size();
		if (total_territory_in_game > 0) {
			// Current Player(Attacker) Territory Size
			int player_territory_count = attacker_territory_list.size();
			if (player_territory_count == total_territory_in_game) {
				is_winner = true;
			}
		}
		return is_winner;
	}

	/**
	 * This function is call at the end of each player attack phase , which performs
	 * checks that whether player has occupied any territory during each turn and if
	 * any territory occupied then as per risk rules player should get one card from
	 * card deck.
	 * 
	 * @author <a href="mailto:himansipatel1994@gmail.com">Himansi Patel</a>
	 *         Description added by Mayank Jariwala
	 * @param game_play : GamePlay Object
	 */
	private void giveCardAtAttackEnd(GamePlay game_play) {
		int current_player_id = game_play.getCurrent_player();
		boolean is_territory_occupied = false;
		for (int player_list_index = 0; player_list_index < game_play.getGame_state().size(); player_list_index++) {
			if (current_player_id == game_play.getGame_state().get(player_list_index).getId()) {
				is_territory_occupied = game_play.getGame_state().get(player_list_index).isAny_territory_occupied();
			}
		}
		if (is_territory_occupied) {
			if (game_play.getFree_cards().size() > 0) {
				Card card = game_play.getFree_cards().get(0);
				game_play.getFree_cards().remove(0);
				for (int player_list_index = 0; player_list_index < game_play.getGame_state()
						.size(); player_list_index++) {
					if (current_player_id == game_play.getGame_state().get(player_list_index).getId()) {
						game_play.getGame_state().get(player_list_index).getCard_list().add(card);
						String message = "Card - ".concat(card.getTerritory_name()).concat(" Army - ")
								.concat(card.getArmy_type()).concat(" has been assigned to Attacker.");
						game_play.setStatus(message);
						game_play.getGame_state().get(player_list_index).setAny_territory_occupied(false);
					}
				}
			}
		} else {
			game_play.setStatus("");
		}
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
	 * This function performs check for valid attack on defender territory (If
	 * Territory is occupy by attacker then player cannot attack on his own
	 * territory)
	 * 
	 * @param attacker_id
	 * @param defender_id
	 * @return Valid Attack Message
	 */
	private String checkForValidAttackTerritory(int attacker_id, int defender_id) {
		String message = "";
		if (attacker_id == defender_id) {
			message = "This territory is already with the attacker";
		}
		return message;
	}

	/**
	 * This function performs valid attack check.
	 * 
	 * @param attacker_territory_armies
	 * @param defender_territory_armies
	 * @param attacker_dice_no
	 * @param defender_dice_no
	 * @return Valid Attack Message
	 */
	private String checkForValidAttack(int attacker_territory_armies, int defender_territory_armies,
			int attacker_dice_no, int defender_dice_no) {

		String message = "";
		if (attacker_territory_armies <= attacker_dice_no || attacker_territory_armies == 1) {
			if (attacker_territory_armies - 1 == 0) {
				message = "Invalid Attack By Attacker (You can't attack with this territory)";
			} else {
				if (attacker_territory_armies - 1 <= 3) {
					message = "Invalid Attack By Attacker (You can roll max " + (attacker_territory_armies - 1)
							+ " dice)";
				} else {
					message = "Invalid Attack By Attacker (You can roll max 3 dice)";
				}
			}
		}

		if (defender_territory_armies < defender_dice_no) {
			if (defender_territory_armies <= 2) {
				message = "Invalid Defend(You can roll max " + defender_territory_armies + " dice )";
			} else {
				message = "Invalid defend By defender (You can roll max 2 dice)";
			}
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
		int attacker_dice_result = random.nextInt(6) + 1;
		int defender_dice_result = random.nextInt(6) + 1;
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
		int attacker_dice_result = random.nextInt(6) + 1;
		int defender_dice_result = Math.max(random.nextInt(6) + 1, random.nextInt(6) + 1);
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
		int attacker_dice_result = Math.max(random.nextInt(6) + 1, random.nextInt(6) + 1);
		int defender_dice_result = random.nextInt(6) + 1;
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
		int attacker_roll_one = random.nextInt(6) + 1;
		int attacker_roll_two = random.nextInt(6) + 1;
		dice_result_list.put("attacker", Arrays.asList(attacker_roll_one, attacker_roll_two));
		int defender_roll_one = random.nextInt(6) + 1;
		int defender_roll_two = random.nextInt(6) + 1;
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

		attacker_list.add(random.nextInt(6) + 1);
		attacker_list.add(random.nextInt(6) + 1);
		attacker_list.add(random.nextInt(6) + 1);

		defender_list.add(random.nextInt(6) + 1);
		defender_list.add(random.nextInt(6) + 1);

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
		int attacker_dice_result = Math.max(Math.max(random.nextInt(6) + 1, random.nextInt(6) + 1),
				random.nextInt(6) + 1);
		int defender_dice_result = random.nextInt(6) + 1;
		if (attacker_dice_result > defender_dice_result) {
			dice_result_flag = 1;
		} else if (attacker_dice_result <= defender_dice_result) {
			dice_result_flag = 0;
		}
		return Arrays.asList(dice_result_flag);
	}

}