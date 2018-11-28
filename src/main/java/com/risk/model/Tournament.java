package com.risk.model;

import java.util.List;

/**
 * This class represents a collection of Game Plays to be executed in tournament mode.
 * 
 * @author <a href="apoorv.semwal20@gmail.com">Apoorv Semwal</a>
 * @version 0.0.1
 */

public class Tournament {

	/**
	 * List of GamePlay to be executed in tournament mode.
	 */
	private List<GamePlay> tournament;
	
	/**
	 * Represents the currently active GamePlay in tournament.
	 */
	private int current_game_play_id;
	
	/**
	 * Status message for Tournament. Contains messages from various validations
	 * during Tournament Creation and Execution.
	 */
	private String status;
	
	/**
	 * Maximum number of turns in any GamePlay
	 */
	private int max_turns;
	
	/**
	 * Result of the tournament
	 */
	private TournamentResults tournament_results;
	
	public int getMax_turns() {
		return max_turns;
	}

	public void setMax_turns(int max_turns) {
		this.max_turns = max_turns;
	}
	
	public TournamentResults getTournament_results() {
		return tournament_results;
	}

	public void setTournament_results(TournamentResults tournament_results) {
		this.tournament_results = tournament_results;
	}

	public int getCurrent_game_play_id() {
		return current_game_play_id;
	}

	public void setCurrent_game_play_id(int current_game_play_id) {
		this.current_game_play_id = current_game_play_id;
	}

	public List<GamePlay> getTournament() {
		return tournament;
	}

	public void setTournament(List<GamePlay> tournament) {
		this.tournament = tournament;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
}