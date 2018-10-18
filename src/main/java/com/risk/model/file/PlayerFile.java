/**
 * 
 */
package com.risk.model.file;

import java.util.List;

import com.risk.model.Card;
import com.risk.model.GamePlayTerritory;

/**
 * @author <a href="mailto:himansipatel1994@gmail.com">Himansi Patel</a>
 *
 */
public class PlayerFile {
	private int id;
	private String name;
	private int army_stock;
	private List<com.risk.model.file.GamePlayTerritory> territory_list;
	private List<com.risk.model.file.Card> card_list;
	
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
	public List<com.risk.model.file.GamePlayTerritory> getTerritory_list() {
		return territory_list;
	}
	
	/**
	 * @param territory_list the territory_list to set
	 */
	public void setTerritory_list(List<com.risk.model.file.GamePlayTerritory> territory_list) {
		this.territory_list = territory_list;
	}
	
	/**
	 * @return the card_list
	 */
	public List<com.risk.model.file.Card> getCard_list() {
		return card_list;
	}
	
	/**
	 * @param card_list the card_list to set
	 */
	public void setCard_list(List<com.risk.model.file.Card> card_list) {
		this.card_list = card_list;
	}	
		
}
