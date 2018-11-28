package com.risk.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class represents tournament results for each game played on each map by
 * different computer players
 * 
 * @author <a href="mayankjariwala1994@gmail.com">Mayank Jariwala</a>
 * @version 0.0.3
 */
public class TournamentResults {

	// List of each Game Played Object
	List<GamePlay> game_results;

	// Map of total game played on each map
	HashMap<String, List<GamePlay>> each_map_results;

	public TournamentResults() {
		super();
		game_results = new ArrayList<GamePlay>();
		each_map_results = new HashMap<String, List<GamePlay>>();
	}

	/**
	 * @return the game_results
	 */
	public List<GamePlay> getGame_results() {
		return game_results;
	}

	/**
	 * @param game_results the game_results to set
	 */
	public void setGame_results(List<GamePlay> game_results) {
		this.game_results = game_results;
	}

	/**
	 * @return the each_map_results
	 */
	public HashMap<String, List<GamePlay>> getEach_map_results() {
		return each_map_results;
	}

	/**
	 * @param each_map_results the each_map_results to set
	 */
	public void setEach_map_results(HashMap<String, List<GamePlay>> each_map_results) {
		this.each_map_results = each_map_results;
	}

}
