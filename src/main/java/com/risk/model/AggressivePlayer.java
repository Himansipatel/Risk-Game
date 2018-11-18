package com.risk.model;

import java.util.List;
import com.risk.business.IAbstractPlayer;
import com.risk.business.impl.ManagePlayer;

/**
 * This Player Model represents a Human Player in terms of Strategy-Pattern implementation,
 * during our GamePlay.
 * 
 * @author <a href="mailto:apoorv.semwal20@gmail.com">Apoorv Semwal</a>
 * @version 0.0.1
 */
public class AggressivePlayer implements IAbstractPlayer {
	private int id;
	private String name;
	private int army_stock;
	private List<GamePlayTerritory> territory_list;
	private List<Card> card_list;

	/**
	 * During Attack Phase If Player is occupying territory then flag will set to
	 * true and will get one card as per risk rule at the end of attack.
	 */
	private boolean any_territory_occupied;

	/**
	 * To identify how many card sets are traded for a particular Player.
	 */
	private int trade_count;

	/**
	 * To store the type of player - Human or Computer
	 */
	private String type;
	
	/**
	 * To store the behavior of player - Human or Aggressive or Benevolent or Random or Cheater.
	 */
	private String behaviour;
		
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBehaviour() {
		return behaviour;
	}

	public void setBehaviour(String behaviour) {
		this.behaviour = behaviour;
	}

	/**
	 * @return the any_territory_occupied
	 */
	public boolean isAny_territory_occupied() {
		return any_territory_occupied;
	}

	/**
	 * @param any_territory_occupied the any_territory_occupied to set
	 */
	public void setAny_territory_occupied(boolean any_territory_occupied) {
		this.any_territory_occupied = any_territory_occupied;
	}

	/**
	 * @return the trade_count
	 */
	public int getTrade_count() {
		return trade_count;
	}

	/**
	 * @param trade_count the trade_count to set
	 */
	public void setTrade_count(int trade_count) {
		this.trade_count = trade_count;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the army_stock
	 */
	public int getArmy_stock() {
		return army_stock;
	}

	/**
	 * @param army_stock the army_stock to set
	 */
	public void setArmy_stock(int army_stock) {
		this.army_stock = army_stock;
	}

	/**
	 * @return the territory_list
	 */
	public List<GamePlayTerritory> getTerritory_list() {
		return territory_list;
	}

	/**
	 * @param territory_list the territory_list to set
	 */
	public void setTerritory_list(List<GamePlayTerritory> territory_list) {
		this.territory_list = territory_list;
	}

	/**
	 * @return the card_list
	 */
	public List<Card> getCard_list() {
		return card_list;
	}

	/**
	 * @param card_list the card_list to set
	 */
	public void setCard_list(List<Card> card_list) {
		this.card_list = card_list;
	}

	/**
	 * Reinforcement of an Aggressive Player.
	 * @see com.risk.business.IAbstractPLayer#reinforce(GamePlay)
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 */
	public GamePlay reinforce(GamePlay game_play) {
		ManagePlayer manage_player = new ManagePlayer();
		game_play = manage_player.reinforce(game_play);
		return game_play;
	}

	/**
	 * Attack of an Aggressive Player.
	 * @see com.risk.business.IAbstractPLayer#attack(GamePlay)
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 */
	public GamePlay attack(GamePlay game_play) {
		ManagePlayer manage_player = new ManagePlayer();
		game_play = manage_player.attack(game_play);
		return game_play;
	}

	/**
	 * Fortify of an Aggressive Player.
	 * @see com.risk.business.IAbstractPLayer#fortify(GamePlay)
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 */
	public GamePlay fortify(GamePlay game_play) {
		ManagePlayer manage_player = new ManagePlayer();
		game_play = manage_player.fortify(game_play);
		return game_play;
	}
}
