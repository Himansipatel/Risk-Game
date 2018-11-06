package com.risk.business.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.stereotype.Service;

import com.risk.business.IManageGamePlay;
import com.risk.business.IManageMap;
import com.risk.file.IManageFile;
import com.risk.file.impl.ManageFile;
import com.risk.file.impl.ManageGamePlayFile;
import com.risk.model.Card;
import com.risk.model.CardTrade;
import com.risk.model.Continent;
import com.risk.model.GamePlay;
import com.risk.model.GamePlayTerritory;
import com.risk.model.Map;
import com.risk.model.Player;
import com.risk.model.Territory;
import com.risk.model.file.File;

/**
 * This class is the Concrete Implementation for interface IManageGamePlay.
 * 
 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
 * @version 0.0.1
 */
@Service
public class ManageGamePlay implements IManageGamePlay, Observer {

	/**
	 * @see com.risk.business.IManageGamePlay#savePhase(GamePlay)
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 */
	@Override
	public GamePlay savePhase(GamePlay game_state) {

		IManageMap  map_manager  = new ManageMap();
		String[] file_name = game_state.getFile_name().split("_");
		IManageFile file_manager = new ManageFile(file_name[0].concat(".map"));		
		File file = file_manager.retreiveFileObject();

		Map map = map_manager.convertFileToMap(file);
		ManageGamePlayFile game_file = new ManageGamePlayFile();
		game_file.saveGameStateToDisk(game_state);
		switch (game_state.getGame_phase()) {

		case "REINFORCEMENT":	
			game_state.setGame_state(calculateArmiesReinforce(game_state.getGame_state(),map));
			break;
		case "FORTIFICATION":			
			break;
		case "ATTACK":			
			break;
		default:
			break;
		}

		return game_state;
	}

	/**
	 * @see com.risk.business.IManageGamePlay#managePhase(GamePlay)
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 */
	@Override
	public GamePlay managePhase(GamePlay game_state) {		

		if (game_state!=null) {

			Player player = new Player();
			Map map = game_state.getMap();

			if (map==null) {
				game_state.setStatus("Inavlid Map.");
				return game_state; 
			}else if(!map.getStatus().equalsIgnoreCase("")) {
				game_state.setStatus(map.getStatus());
				return game_state;
			}

			switch (game_state.getGame_phase()) {

			case "STARTUP":
				game_state.setGame_state(calculateArmiesReinforce(game_state.getGame_state(),map));
				setCurrentPlayerAndPhase(game_state, game_state.getGame_phase());
				break;

			case "REINFORCEMENT":
				setCurrentPlayerAndPhase(game_state, game_state.getGame_phase());
				game_state = player.reInforce(game_state);
				break;

			case "TRADE_CARDS":
				game_state = tradeCards(game_state);
				break;

			case "ATTACK_ON":
				player.attack(game_state);
				break;

			case "ATTACK_ALL_OUT":
				player.attack(game_state);
				break;
				
			case "ATTACK_ARMY_MOVE":
				player.attack(game_state);
				break;
				
			case "ATTACK_END":
				player.attack(game_state);
				setCurrentPlayerAndPhase(game_state, game_state.getGame_phase());
				break;

			case "FORTIFICATION":
				player.fortify(game_state);
				break;

			case "FORTIFICATION_END":
				setCurrentPlayerAndPhase(game_state, game_state.getGame_phase());
				break;
				
			default:
				break;
			}
			return game_state;
		}else {
			return game_state;
		}
	}

	/**
	 * This method handles the trading of cards during reinforcement phase of the game-play.
	 * 
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 * @param  game_state State of the game at point of time holding the entire info
	 *                    about game-play. Like the current phase and player.
	 * @return game_state after updating info on trading of cards.
	 */	
	private GamePlay tradeCards(GamePlay game_state) {	
		
		CardTrade trade_card = game_state.getCard_trade();
		if (trade_card!=null) {
			if (trade_card.getCard1() == null || trade_card.getCard2() == null || trade_card.getCard3() == null) {
				game_state.setStatus("Trading requires a minimum of three cards to be selected.");
			}else {
				
				/**
				 * First part of condition (before OR) checks if all images on the three cards are same and the second part 
				 * (after OR) checks if all the three are different.
				 */
				if (    (trade_card.getCard1().getArmy_type().equalsIgnoreCase(trade_card.getCard2().getArmy_type())
					     && trade_card.getCard1().getArmy_type().equalsIgnoreCase(trade_card.getCard3().getArmy_type()))
					||  (!trade_card.getCard1().getArmy_type().equalsIgnoreCase(trade_card.getCard2().getArmy_type())	
						&& !trade_card.getCard2().getArmy_type().equalsIgnoreCase(trade_card.getCard3().getArmy_type())
						&& !trade_card.getCard3().getArmy_type().equalsIgnoreCase(trade_card.getCard1().getArmy_type()))){
					
					int current_player = game_state.getCurrent_player();
					
					for (Player player : game_state.getGame_state()) {
						
						if (player.getId()==current_player) {
							
							updateTradedArmies(player);
							updateCardLists(player, game_state.getFree_cards(), trade_card);
							
							/**
							 * Check if the Player controls any territory which is present in one of the cards being traded.
							 */
							List<GamePlayTerritory> player_territory_list = player.getTerritory_list();
							
							if (player_territory_list != null) {
								
								for (GamePlayTerritory gamePlayTerritory : player_territory_list) {
									
									if (   gamePlayTerritory.getTerritory_name().equalsIgnoreCase(trade_card.getCard1().getTerritory_name())
										|| gamePlayTerritory.getTerritory_name().equalsIgnoreCase(trade_card.getCard2().getTerritory_name())
										|| gamePlayTerritory.getTerritory_name().equalsIgnoreCase(trade_card.getCard3().getTerritory_name())) {
										
										/**
										 * An additional two armies given if the Player controls any territory 
										 * which is present in one of the cards being traded.
										 */
										gamePlayTerritory.setNumber_of_armies(gamePlayTerritory.getNumber_of_armies() + 2);			
										break;
									}
								}
							}
							break;
						}
					}
				}else {
					game_state.setStatus("Either all three cards should have same image or all three different.");
				}
			}
		}else {
			game_state.setStatus("Inavlid Trade State during Gameplay");
		}
		return game_state;
	}	
	
	/**
	 * This method assigns a random card to a player and removes that card from the free stock.
	 *    
	 * @param game_state Overall game_state to be updated after this move
	 */
	public void addCardToPlayer(GamePlay game_state) {
		
		if (game_state!=null) {
			
			for (Player player : game_state.getGame_state()) {
				
				if (player.getId()==game_state.getCurrent_player()) {
					
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
	 * @param player State of the current Player. 
	 */
	private void updateTradedArmies(Player player) {
		if (player.getTrade_count()==0) {
			player.setArmy_stock(player.getArmy_stock() + 4);
			player.setTrade_count(1);
		}else if (player.getTrade_count()==1) {
			player.setArmy_stock(player.getArmy_stock() + 6);
			player.setTrade_count(2);											
		}else if (player.getTrade_count()==2) {
			player.setArmy_stock(player.getArmy_stock() + 8);
			player.setTrade_count(3);											
		}else if (player.getTrade_count()==3) {
			player.setArmy_stock(player.getArmy_stock() + 10);
			player.setTrade_count(4);											
		}else if (player.getTrade_count()==4) {
			player.setArmy_stock(player.getArmy_stock() + 12);
			player.setTrade_count(5);											
		}else if (player.getTrade_count()==5) {
			player.setArmy_stock(player.getArmy_stock() + 15);
			player.setTrade_count(6);											
		}else if (player.getTrade_count()>5) {
			player.setArmy_stock(player.getArmy_stock() + 15 + ((player.getTrade_count() - 5) * 5) );
			player.setTrade_count(player.getTrade_count()+1);																						
		}		
	}
		
	/**
	 * This method updates the player's card list and well as the free card list after the trading is over.
	 * 
	 * @param player State of the current Player.
	 * @param free_cards List of cards which are free for allocation
	 * @param traded_cards The set of three cards being traded.
	 */
	private void updateCardLists(Player player, List<Card> free_cards, CardTrade traded_cards) {
		free_cards.add(traded_cards.getCard1());
		free_cards.add(traded_cards.getCard2());
		free_cards.add(traded_cards.getCard3());
		Iterator<Card> i = player.getCard_list().iterator();
		while (i.hasNext()) {
			Card card = (Card) i.next();
			if (   (card.getArmy_type().equalsIgnoreCase(traded_cards.getCard1().getArmy_type()) && card.getTerritory_name().equalsIgnoreCase(traded_cards.getCard1().getTerritory_name())) 
				|| (card.getArmy_type().equalsIgnoreCase(traded_cards.getCard2().getArmy_type()) && card.getTerritory_name().equalsIgnoreCase(traded_cards.getCard2().getTerritory_name()))
				|| (card.getArmy_type().equalsIgnoreCase(traded_cards.getCard3().getArmy_type()) && card.getTerritory_name().equalsIgnoreCase(traded_cards.getCard3().getTerritory_name()))) {
				i.remove();				
			}
		}
	}
	
	/**
	 * This method decides the next player and the phase during game-play.
	 * 
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 * @param game_state State of the game at point of time holding the entire info
	 *                   about game-play. Like the current phase and player.
	 * @param game_phase Name of the phase which is ending.
	 */	
	private void setCurrentPlayerAndPhase(GamePlay game_state, String game_phase) {	

		switch (game_phase) {

		case "STARTUP":
			game_state.setCurrent_player(1);
			game_state.setGame_phase("REINFORCEMENT");
			break;

		case "REINFORCEMENT":
			game_state.setGame_phase("ATTACK");
			break;
			
		case "ATTACK_END": 
			game_state.setGame_phase("FORTIFICATION");
			break;

		case "FORTIFICATION_END":
			if (game_state.getCurrent_player() + 1 > game_state.getGame_state().size()) {
				game_state.setCurrent_player(1);
			}else {
				game_state.setCurrent_player(game_state.getCurrent_player() + 1);
			}
			game_state.setGame_phase("REINFORCEMENT");
			break;

		default:
			break;
		}
	}


	/**
	 * @see com.risk.business.IManageGamePlay#calculateArmiesReinforce(java.util.List, com.risk.model.Map)
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 */
	@Override
	public List<Player> calculateArmiesReinforce(List<Player> gameplay, Map map){

		List<Continent> continents = new ArrayList<>();
		List<Territory> territories;

		java.util.Map<String, SortedSet<String>>  continents_territories  = new HashMap<>();		
		java.util.Map<Integer, SortedSet<String>> player_territories      = new HashMap<>();

		SortedSet<String> territories_game;
		SortedSet<String> territories_player;

		java.util.Map<String, Integer> continents_score                   = new HashMap<>(); 
		java.util.Map<Integer, Integer> players_army                      = new HashMap<>(); 

		for (java.util.Map.Entry<String, Continent> continent : map.getContinents().entrySet()) {
			continents.add(continent.getValue());
		}

		//Preparing a list of all players along with the continents they hold.
		for (Player player : gameplay) {
			List<GamePlayTerritory> player_territories_game = player.getTerritory_list();
			territories_player = new TreeSet<>();
			for (GamePlayTerritory territority : player_territories_game) {
				territories_player.add(territority.getTerritory_name());
			}
			player_territories.put(player.getId(), territories_player);
		}		

		//Preparing List of all players along with their current army stock.
		for (Player player : gameplay) {
			players_army.put(player.getId(), player.getArmy_stock());
		}

		//Preparing a list of all continents along with their territories.
		for (Continent continent : continents) {
			territories = new ArrayList<>();
			territories_game = new TreeSet<>();
			territories = continent.getTerritories();
			for (Territory territory : territories) {
				territories_game.add(territory.getName());
			}
			continents_score.put(continent.getName(),continent.getScore());
			continents_territories.put(continent.getName(), territories_game);
		}

		//Updating Player's army stock on the basis of territories it hold.
		//If the player holds less than 9 territories then allocate 3 army elements as per Risk Rules
		for (Player player : gameplay) {
			int army_count = player_territories.get(player.getId()).size() / 3;
			if (army_count < 3) {
				army_count = 3;
			}
			army_count = army_count + players_army.get(player.getId());
			players_army.replace(player.getId(), army_count);
		}		

		//Verifying if a player holds the entire continent and updating its army stock.
		for (Iterator<Entry<Integer, SortedSet<String>>> iterator_player = player_territories.entrySet().iterator(); iterator_player.hasNext();) {
			java.util.Map.Entry<Integer, SortedSet<String>> player = iterator_player.next();
			territories_player = player.getValue();
			for (Iterator<Entry<String, SortedSet<String>>> iterator_continent = continents_territories.entrySet().iterator(); iterator_continent.hasNext();) {
				java.util.Map.Entry<String, SortedSet<String>> continent = iterator_continent.next();			
				territories_game = continent.getValue();
				if (territories_player.containsAll(territories_game)) {
					int continent_score_val   = continents_score.get(continent.getKey());
					int player_army_count     = players_army.get(player.getKey()) + continent_score_val;
					players_army.replace(player.getKey(),player_army_count);
				}
			}
		}

		//Preparing List of all players along with their updated army stock.
		for (Player player : gameplay) {
			player.setArmy_stock(players_army.get(player.getId()));
		}

		return gameplay;

	}

	/**
	 * This method here serves for the implementation of Observer Pattern in our Project. It handles multiple phases during 
	 * game play as per risk rules. As the GUI captures events for a particular phase it triggers an state change for GamePlay Object
	 * and any trigger for GamePlay object is being observed by ManageGamePlay as an observer. 
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 */	
	@Override
	public void update(Observable o, Object arg) {
		managePhase((GamePlay)arg); 
	}

}