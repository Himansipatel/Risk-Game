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
import com.risk.model.Continent;
import com.risk.model.Domination;
import com.risk.model.GamePlay;
import com.risk.model.GamePlayTerritory;
import com.risk.model.Map;
import com.risk.model.Player;
import com.risk.model.Territory;

/**
 * This class is the Concrete Implementation for interface IManageGamePlay.
 * 
 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
 * @version 0.0.1
 */
@Service
public class ManageGamePlay implements IManageGamePlay, Observer {

	/**
	 * @see com.risk.business.IManageGamePlay#dominatonView(GamePlay)
	 * @author <a href="mailto:zinnia.rana.22@gmail.com">ApoorvSemwal</a>
	 */
	@Override
	public GamePlay dominationView(GamePlay game_play) {
		int player_id = 0;
		int player_armies_count;
		List<Domination> player_domination_object = new ArrayList<>();
		List<String> continent_occupy = new ArrayList<>();
		Domination domination;
		ManagePlayer manage_player = new ManagePlayer();
		int player_territories_count = 0;
		int total_map_territories = manage_player.getTerritories(game_play.getMap()).size();
		HashMap<String, Integer> continent_territory_count = continentTerritoryCount(game_play);
		for (Player player : game_play.getGame_state()) {
			domination = new Domination();
			player_armies_count = 0;
			player_id = player.getId();
			domination.setPlayer_id(player_id);
			player_territories_count = player.getTerritory_list().size();
			double coverage = (double) player_territories_count / (double) total_map_territories;
			coverage = Double.valueOf(String.format("%.1f", coverage * 100));
			domination.setMap_coverage(coverage);
			for (GamePlayTerritory territory : player.getTerritory_list()) {
				player_armies_count += territory.getNumber_of_armies();
			}
			domination.setPlayer_army_count(player_armies_count);
			for (GamePlayTerritory territory : player.getTerritory_list()) {
				if (continent_territory_count.containsKey(territory.getContinent_name())) {
					int territory_count = continent_territory_count.get(territory.getContinent_name());
					if (territory_count == 0) {
						continent_occupy.add(territory.getContinent_name());
					} else {
						continent_territory_count.put(territory.getContinent_name(), territory_count - 1);
					}
				}
			}
			continent_occupy = continent_occupy.size() > 0 ? continent_occupy : null;
			domination.setPlayer_continent_list(continent_occupy);
			player_domination_object.add(domination);
		}
		game_play.setDomination(player_domination_object);
		return game_play;
	}

	/**
	 * 
	 * @param game_play
	 * @return
	 */
	private static HashMap<String, Integer> continentTerritoryCount(GamePlay game_play) {
		// Count of Territory in Specific Continent
		HashMap<String, Integer> continent_territory_count = new HashMap<>();
		HashMap<String, Continent> continents_list = game_play.getMap().getContinents();
		for (Entry<String, Continent> entry : continents_list.entrySet()) {
			continent_territory_count.put(entry.getValue().getName(), entry.getValue().getTerritories().size());
		}
		return continent_territory_count;
	}

	/**
	 * @see com.risk.business.IManageGamePlay#managePhase(GamePlay)
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 */
	@Override
	public GamePlay managePhase(GamePlay game_state) {

		if (game_state != null) {

			Player player = new Player();
			Map map = game_state.getMap();

			if (map == null) {
				game_state.setStatus("Inavlid Map.");
				return game_state;
			} else if (!map.getStatus().equalsIgnoreCase("")) {
				game_state.setStatus(map.getStatus());
				return game_state;
			}

			switch (game_state.getGame_phase()) {

			case "STARTUP":
				calculateArmiesReinforce(game_state.getGame_state(), map, 1);
				setCurrentPlayerAndPhase(game_state, game_state.getGame_phase());
				break;

			case "REINFORCEMENT":
				setCurrentPlayerAndPhase(game_state, game_state.getGame_phase());
				game_state = player.reinforce(game_state);
				break;

			case "TRADE_CARDS":
				game_state = player.reinforce(game_state);
				setCurrentPlayerAndPhase(game_state, game_state.getGame_phase());
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
				calculateArmiesReinforce(game_state.getGame_state(), map, game_state.getCurrent_player());
				break;

			default:
				break;
			}
			return game_state;
		} else {
			return game_state;
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
	 * @see com.risk.business.IManageGamePlay#calculateArmiesReinforce(List<Player>,Map)
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
	 * This method here serves for the implementation of Observer Pattern in our
	 * Project. It handles multiple phases during game play as per risk rules. As
	 * the GUI captures events for a particular phase it triggers an state change
	 * for GamePlay Object and any trigger for GamePlay object is being observed by
	 * ManageGamePlay as an observer.
	 * 
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 */
	@Override
	public void update(Observable o, Object arg) {
		managePhase((GamePlay) arg);
	}

}