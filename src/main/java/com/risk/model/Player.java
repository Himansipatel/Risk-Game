package com.risk.model;

import java.util.List;

/**
 * <b>GamePlay Player File Model</b> - Stores information about all players
 * playing in game.
 * 
 * @author <a href="mailto:himansipatel1994@gmail.com">Himansi Patel</a>
 * @author <a href="mailto:mayankjariwala1994@gmail.com">Mayank Jariwala</a> -
 *         Added Model Description
 * @version 0.0.1
 */
public class Player {
	private int id;
	private String name;
	private int army_stock;
	private List<GamePlayTerritory> territory_list;
	private List<Card> card_list;

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
}
