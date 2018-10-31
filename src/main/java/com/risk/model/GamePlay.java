package com.risk.model;

import java.util.List;

/**
 * This class represents the Game Play state at any point of time.
 * 
 * @author <a href="zinnia.rana.22@gmai.com">Zinnia Rana</a>
 * @version 0.0.1
 */

public class GamePlay {

	private List<Player> game_state;
	private String file_name;
	private String game_phase;
	private String current_player;
	private String status;
	
	/**
	 * @return the game_state
	 */
	public List<Player> getGame_state() {
		return game_state;
	}

	/**
	 * @param game_state the game_state to set
	 */
	public void setGame_state(List<Player> game_state) {
		this.game_state = game_state;
	}

	/**
	 * @return the file_name
	 */
	public String getFile_name() {
		return file_name;
	}

	/**
	 * @param file_name the file_name to set
	 */
	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	/**
	 * @return the game_phase
	 */
	public String getGame_phase() {
		return game_phase;
	}

	/**
	 * @param game_phase the game_phase to set
	 */
	public void setGame_phase(String game_phase) {
		this.game_phase = game_phase;
	}

	/**
	 * @return the status of the game state with any error message if they exist
	 */	
	public String getStatus() {
		return status;
	}

	/**
	 * @param status Any messages to be displayed while rendering the game state.
	 */	
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the currently active player in the game state
	 */		
	public String getCurrent_player() {
		return current_player;
	}

	/**
	 * @param current_player sets the currently active player in the game state
	 */
	public void setCurrent_player(String current_player) {
		this.current_player = current_player;
	}

}