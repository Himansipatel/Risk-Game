package com.risk.model;

import java.util.List;
import java.util.Observable;
import com.risk.business.AbstractPlayer;

/**
 * This class represents the Game Play state at any point of time.
 * 
 * @author <a href="zinnia.rana.22@gmail.com">Zinnia Rana</a>
 * @author <a href="apoorv.semwal20@gmail.com">Apoorv Semwal</a>
 * @version 0.0.1
 */

public class GamePlay extends Observable {

	/**
	 * State of the entire game at any point of time.
	 */
	private List<AbstractPlayer> game_state;

	/**
	 * Map File name for the current game play
	 */
	private String file_name;

	/**
	 * Map object for the current game play
	 */
	private com.risk.model.Map map;

	/**
	 * GUI Map object for the current game play
	 */
	private com.risk.model.gui.Map gui_map;

	/**
	 * Currently active phase during game play
	 */
	private String game_phase;

	/**
	 * Currently active player during game play.
	 */
	private int current_player;

	/**
	 * List of Cards which are free and are not held by any player.
	 */
	private List<Card> free_cards;

	/**
	 * Status message for GamePlay. Contains messages from various validations
	 * during GamePlay.
	 */
	private String status;

	/**
	 * Represents the info captured during trading of cards.
	 */
	private CardTrade card_trade;

	/**
	 * Represents the info captured during attack phase of any player.
	 */
	private Attack attack;

	/**
	 * Represents the info captured during army movement after Attack.
	 */
	private AttackArmyMove army_move;

	/**
	 * Represents the info captured during fortification phase of any player.
	 */
	private Fortification fortification;

	private List<Domination> domination;

	/**
	 * Captures a series of log messages generated during game play. 
	 */
	private List<String> game_play_log;

	/**
	 * Unique ID that identifies a GamePlay in Tournament Mode. 
	 */
	private int game_play_id;

	/**
	 * @return the unique id for any GamePlay.
	 */		
	public int getGame_play_id() {
		return game_play_id;
	}

	/**
	 * @param game_play_id unique id for any GamePlay.
	 */
	public void setGame_play_id(int game_play_id) {
		this.game_play_id = game_play_id;
	}

	/**
	 * @return the list of log messages.
	 */	
	public List<String> getGame_play_log() {
		return game_play_log;
	}

	/**
	 * @param game_play_log the list of log messages generated during game play.
	 */
	public void setGame_play_log(List<String> game_play_log) {
		this.game_play_log = game_play_log;
	}

	/**
	 * @return the domination
	 */
	public List<Domination> getDomination() {
		return domination;
	}

	/**
	 * @param domination List of player domination objects to set
	 */
	public void setDomination(List<Domination> domination) {
		this.domination = domination;
	}

	/**
	 * @return the AttackArmyMove
	 */
	public AttackArmyMove getArmy_move() {
		return army_move;
	}

	/**
	 * @param army_move to set after attack
	 */
	public void setArmy_move(AttackArmyMove army_move) {
		this.army_move = army_move;
	}

	/**
	 * @return the attack
	 */
	public Attack getAttack() {
		return attack;
	}

	/**
	 * @param attack the attack to set
	 */
	public void setAttack(Attack attack) {
		this.attack = attack;
	}

	/**
	 * @return the fortification
	 */
	public Fortification getFortification() {
		return fortification;
	}

	/**
	 * @param fortification the fortification to set
	 */
	public void setFortification(Fortification fortification) {
		this.fortification = fortification;
	}

	/**
	 * @return the game_state
	 */
	public List<AbstractPlayer> getGame_state() {
		return game_state;
	}

	/**
	 * Any change in the state of GamePlay based on actions at UI, will trigger
	 * calling of update method in ManageGamePlay which is acting as the Observer.
	 * 
	 * @param game_state the game_state to set
	 */
	public void setGame_state(List<AbstractPlayer> game_state) {
		this.game_state = game_state;
		setChanged();
		notifyObservers(this);
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
	public int getCurrent_player() {
		return current_player;
	}

	/**
	 * @param current_player sets the currently active player in the game state
	 */
	public void setCurrent_player(int current_player) {
		this.current_player = current_player;
	}

	/**
	 * @return the current list of free cards
	 */
	public List<Card> getFree_cards() {
		return free_cards;
	}

	/**
	 * @param free_cards list of cards to be set as freely available cards.
	 */
	public void setFree_cards(List<Card> free_cards) {
		this.free_cards = free_cards;
	}

	/**
	 * @return the currently active map object
	 */
	public com.risk.model.Map getMap() {
		return map;
	}

	/**
	 * @param map the currently active map object.
	 */
	public void setMap(com.risk.model.Map map) {
		this.map = map;
	}

	/**
	 * @return the card trading model capturing data about the cards being traded.
	 */
	public CardTrade getCard_trade() {
		return card_trade;
	}

	/**
	 * @param card_trade card trading model capturing data about the cards being
	 *                   traded.
	 */
	public void setCard_trade(CardTrade card_trade) {
		this.card_trade = card_trade;
	}

	/**
	 * @return the gui_map
	 */
	public com.risk.model.gui.Map getGui_map() {
		return gui_map;
	}

	/**
	 * @param gui_map the gui_map to set
	 */
	public void setGui_map(com.risk.model.gui.Map gui_map) {
		this.gui_map = gui_map;
	}

}