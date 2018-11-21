package com.risk.business.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.stereotype.Service;

import com.risk.business.IManageGamePlay;
import com.risk.model.Card;
import com.risk.model.Continent;
import com.risk.model.Domination;
import com.risk.model.GamePlay;
import com.risk.model.GamePlayTerritory;
import com.risk.model.Map;
import com.risk.model.Player;
import com.risk.model.Territory;
import com.risk.model.Tournament;
import com.risk.model.Strategy.Aggressive;
import com.risk.model.Strategy.Benevolent;
import com.risk.model.Strategy.Cheater;
import com.risk.model.Strategy.Random;
import com.risk.model.gui.TournamentChoices;

/**
 * This class is the Concrete Implementation for interface IManageGamePlay.
 * 
 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
 * @version 0.0.1
 */
@Service
public class ManageGamePlay implements IManageGamePlay, Observer {

	/**
	 * @see com.risk.business.IManageGamePlay#managePhase(GamePlay)
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 */
	@Override
	public GamePlay managePhase(GamePlay game_play) {

		Player current_player = null;

		if (game_play != null) {

			for (Player player : game_play.getGame_state()) {
				if (player.getId()==game_play.getCurrent_player()) {
					current_player = player;
					break;
				}
			}

			if (current_player != null) {
				Map map = game_play.getMap();

				if (map == null) {
					game_play.setStatus("Inavlid Map.");
					return game_play;
				} else if (!map.getStatus().equalsIgnoreCase("")) {
					game_play.setStatus(map.getStatus());
					return game_play;
				}

				switch (game_play.getGame_phase()) {

				case "STARTUP":
					calculateArmiesReinforce(game_play.getGame_state(), map, 1);
					setCurrentPlayerAndPhase(game_play, game_play.getGame_phase());
					break;

				case "REINFORCEMENT":
					setCurrentPlayerAndPhase(game_play, game_play.getGame_phase());
					game_play = current_player.executeStrategy("REINFORCE", game_play);
					break;

				case "TRADE_CARDS":
					game_play = current_player.executeStrategy("REINFORCE", game_play);
					setCurrentPlayerAndPhase(game_play, game_play.getGame_phase());
					break;

				case "ATTACK_ON":
					current_player.executeStrategy("ATTACK", game_play);
					break;

				case "ATTACK_ALL_OUT":
					current_player.executeStrategy("ATTACK", game_play);
					break;

				case "ATTACK_ARMY_MOVE":
					current_player.executeStrategy("ATTACK", game_play);
					break;

				case "ATTACK_END":
					current_player.executeStrategy("ATTACK", game_play);
					setCurrentPlayerAndPhase(game_play, game_play.getGame_phase());
					break;

				case "FORTIFICATION":
					current_player.executeStrategy("FORTIFY", game_play);
					break;

				case "FORTIFICATION_END":
					setCurrentPlayerAndPhase(game_play, game_play.getGame_phase());
					calculateArmiesReinforce(game_play.getGame_state(), map, game_play.getCurrent_player());
					break;

				default:
					break;
				}

				Domination domination = new Domination();
				ManageDomination manage_domination = new ManageDomination();
				domination.addObserver(manage_domination);
				domination.updateDomination(game_play);
				return game_play;
			} else {
				game_play.setStatus("Current Player is not Valid!");
				return game_play;
			}
		} else {
			game_play = new GamePlay();
			game_play.setStatus("Invalid Game State!");
			return game_play;
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

		case "TRADE_CARDS":
			game_state.setGame_phase("REINFORCEMENT");
			break;

		case "ATTACK_END":
			game_state.setGame_phase("FORTIFICATION");
			break;

		case "FORTIFICATION_END":
			if (game_state.getCurrent_player() + 1 > game_state.getGame_state().size()) {
				game_state.setCurrent_player(1);
			} else {
				game_state.setCurrent_player(game_state.getCurrent_player() + 1);
			}
			game_state.setGame_phase("REINFORCEMENT");
			break;

		default:
			break;
		}
	}

	/**
	 * @see com.risk.business.IManageGamePlay#calculateArmiesReinforce(java.util.List,
	 *      com.risk.model.Map, int)
	 * 
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 */
	@Override
	public List<Player> calculateArmiesReinforce(List<Player> gameplay, Map map, int current_player) {

		List<Continent> continents = new ArrayList<>();
		List<Territory> territories;

		java.util.Map<String, SortedSet<String>> continents_territories = new HashMap<>();
		java.util.Map<Integer, SortedSet<String>> player_territories = new HashMap<>();

		SortedSet<String> territories_game;
		SortedSet<String> territories_player;

		java.util.Map<String, Integer> continents_score = new HashMap<>();
		java.util.Map<Integer, Integer> players_army = new HashMap<>();

		for (java.util.Map.Entry<String, Continent> continent : map.getContinents().entrySet()) {
			continents.add(continent.getValue());
		}

		// Preparing a list of all players along with the continents they hold.
		for (Player player : gameplay) {
			if (player.getId() == current_player) {
				List<GamePlayTerritory> player_territories_game = player.getTerritory_list();
				territories_player = new TreeSet<>();
				for (GamePlayTerritory territority : player_territories_game) {
					territories_player.add(territority.getTerritory_name());
				}
				player_territories.put(player.getId(), territories_player);
			}
		}

		// Preparing List of all players along with their current army stock.
		for (Player player : gameplay) {
			if (player.getId() == current_player) {
				players_army.put(player.getId(), player.getArmy_stock());
			}
		}

		// Preparing a list of all continents along with their territories.
		for (Continent continent : continents) {
			territories = new ArrayList<>();
			territories_game = new TreeSet<>();
			territories = continent.getTerritories();
			for (Territory territory : territories) {
				territories_game.add(territory.getName());
			}
			continents_score.put(continent.getName(), continent.getScore());
			continents_territories.put(continent.getName(), territories_game);
		}

		// Updating Player's army stock on the basis of territories it hold.
		// If the player holds less than 9 territories then allocate 3 army elements as
		// per Risk Rules
		for (Player player : gameplay) {
			if (player.getId() == current_player) {
				int army_count = player_territories.get(player.getId()).size() / 3;
				if (army_count < 3) {
					army_count = 3;
				}
				army_count = army_count + players_army.get(player.getId());
				players_army.replace(player.getId(), army_count);
			}
		}

		// Verifying if a player holds the entire continent and updating its army stock.
		for (Iterator<Entry<Integer, SortedSet<String>>> iterator_player = player_territories.entrySet()
				.iterator(); iterator_player.hasNext();) {
			java.util.Map.Entry<Integer, SortedSet<String>> player = iterator_player.next();
			territories_player = player.getValue();
			for (Iterator<Entry<String, SortedSet<String>>> iterator_continent = continents_territories.entrySet()
					.iterator(); iterator_continent.hasNext();) {
				java.util.Map.Entry<String, SortedSet<String>> continent = iterator_continent.next();
				territories_game = continent.getValue();
				if (territories_player.containsAll(territories_game)) {
					int continent_score_val = continents_score.get(continent.getKey());
					int player_army_count = players_army.get(player.getKey()) + continent_score_val;
					players_army.replace(player.getKey(), player_army_count);
				}
			}
		}

		// Preparing List of all players along with their updated army stock.
		for (Player player : gameplay) {
			if (player.getId() == current_player) {
				player.setArmy_stock(players_army.get(player.getId()));
			}
		}

		return gameplay;
	}

	/**
	 * @see com.risk.business.IManageGamePlay#prepareTournamentGamePlay(GamePlay)
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 */
	@Override
	public Tournament prepareTournamentGamePlay(TournamentChoices tournament_inp) {

		Tournament     tournament      = new Tournament();
		List<GamePlay> game_play_set   = new ArrayList<>();
		ManageMap      map_manager     = new ManageMap();
		ManagePlayer   player_manager  = new ManagePlayer();

		for (int i = 1; i <= tournament_inp.getNoOfGamesToPlay(); i++) {
			for (String map  : tournament_inp.getMapNames()) {
				com.risk.model.Map map_model = map_manager.getFullMap(map);

				if (map_model==null){
					tournament.setStatus(map+" : Unable to load Map.");
					return tournament;					
				}else if (!map_model.getStatus().equals("")) {
					tournament.setStatus(map+" : "+map_model.getStatus());
					return tournament;										
				}else {
					com.risk.model.gui.Map gui_map = map_manager.fetchMap(map);
					List<Card> free_cards = player_manager.getFreeCards(map_model);
					GamePlay game_play = new GamePlay();
					game_play.setCurrent_player(1);
					map = (map.endsWith(".map") ? map.split("\\.")[0] : map) + "_"
							+ String.valueOf(System.currentTimeMillis());
					game_play.setFile_name(map);
					game_play.setMap(map_model);
					game_play.setGui_map(gui_map);
					game_play.setGame_phase("REINFORCE");
					game_play.setFree_cards(free_cards);
					update_tournament_gameplay(game_play, tournament_inp.getMultipleStrategies());
					Domination domination = new Domination();
					ManageDomination manage_domination = new ManageDomination();
					domination.addObserver(manage_domination);
					domination.updateDomination(game_play);
					game_play_set.add(game_play);
				}
			}			
		}

		tournament.setTournament(game_play_set);
		tournament.setCurrent_game_play_id(1);
		tournament.setStatus("Tournament Ready. Now starting various Games within it..../nGame 1:/n");
		return tournament;
	}

	private void update_tournament_gameplay(GamePlay game_play, List<String> strategies) {

		ManagePlayer player_manager  = new ManagePlayer();
		List<Player> players         = new ArrayList<>();

		int i = 1;

		int army_stock = player_manager.getArmyStock(strategies.size());

		for (String string : strategies) {

			Player p = new Player();

			String player_name = "Player" + 1;

			List<GamePlayTerritory> gameplay_territory_list = new ArrayList<>();

			List<Card> card_list = new ArrayList<Card>();

			if (string.equalsIgnoreCase("Aggressive")) {
				p.setStrategy(new Aggressive());
			}else if (string.equalsIgnoreCase("Benevolent")) {
				p.setStrategy(new Benevolent());
			}else if (string.equalsIgnoreCase("Cheater")) {
				p.setStrategy(new Cheater());
			}else if (string.equalsIgnoreCase("Random")){
				p.setStrategy(new Random());
			}

			p.setId(i);
			p.setName(player_name);
			i++;

			p.setArmy_stock(army_stock);
			p.setTerritory_list(gameplay_territory_list);
			p.setCard_list(card_list);
			p.setTrade_count(0);
			players.add(p);			
		}
		game_play.setGame_state(players);		
	}
	/**
	 * @see com.risk.business.IManageGamePlay#playTournamentMode(Tournament)
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 */	
	@Override
	public Tournament playTournamentMode(Tournament tournament) {

		GamePlay current_game_play    = null;
		Player current_player         = null;

		if (tournament!=null) {

			for (GamePlay game_play : tournament.getTournament()) {
				if (tournament.getCurrent_game_play_id()==game_play.getGame_play_id()) {
					current_game_play = game_play;
					break;
				}
			}

			if (current_game_play != null) {

				for (Player player : current_game_play.getGame_state()) {
					if (player.getId()==current_game_play.getCurrent_player()) {
						current_player = player;
						break;
					}
				}

				if (current_player != null) {
					Map map = current_game_play.getMap();

					if (map == null) {
						current_game_play.setStatus("Inavlid Map.");
						return tournament;
					} else if (!map.getStatus().equalsIgnoreCase("")) {
						current_game_play.setStatus(map.getStatus());
						return tournament;
					}

					switch (current_game_play.getGame_phase()) {

					case "STARTUP":
						calculateArmiesReinforce(current_game_play.getGame_state(), map, 1);
						setCurrentPlayerAndPhase(current_game_play, current_game_play.getGame_phase());
						break;

					case "REINFORCEMENT":
						setCurrentPlayerAndPhase(current_game_play, current_game_play.getGame_phase());
						current_player.executeStrategy("REINFORCE", current_game_play);
						break;

					case "TRADE_CARDS":
						current_player.executeStrategy("REINFORCE", current_game_play);
						setCurrentPlayerAndPhase(current_game_play, current_game_play.getGame_phase());
						break;

					case "ATTACK_ON":
						current_player.executeStrategy("ATTACK", current_game_play);
						break;

					case "ATTACK_ALL_OUT":
						current_player.executeStrategy("ATTACK", current_game_play);
						break;

					case "ATTACK_ARMY_MOVE":
						current_player.executeStrategy("ATTACK", current_game_play);
						break;

					case "ATTACK_END":
						current_player.executeStrategy("ATTACK", current_game_play);
						setCurrentPlayerAndPhase(current_game_play, current_game_play.getGame_phase());
						break;

					case "FORTIFICATION":
						current_player.executeStrategy("FORTIFY", current_game_play);
						break;

					case "FORTIFICATION_END":
						setCurrentPlayerAndPhase(current_game_play, current_game_play.getGame_phase());
						calculateArmiesReinforce(current_game_play.getGame_state(), map, current_game_play.getCurrent_player());
						break;

					default:
						break;
					}

					Domination domination = new Domination();
					ManageDomination manage_domination = new ManageDomination();
					domination.addObserver(manage_domination);
					domination.updateDomination(current_game_play);
					return tournament;
				} else {
					current_game_play.setStatus("Current Player is not Valid!");
					return tournament;				
				}
			}
		}
		return tournament;
	}

	/**
	 * This method here serves for the implementation of Observer Pattern in our
	 * Project. It handles multiple phases during game play as per risk rules. As
	 * the GUI captures data and events for a particular phase and triggers a state change
	 * in GamePlay Object, this class here is being observed by
	 * ManageGamePlay as an observer.
	 * 
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 */
	@Override
	public void update(Observable o, Object arg) {
		managePhase((GamePlay) arg);
	}	
}