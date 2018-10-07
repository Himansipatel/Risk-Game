package com.risk.model;

import java.util.List;

/**
 * Model class for Continent
 * 
 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
 * @version 0.0.1
 */
public class Continent {
	
	private String name;
	
	/**
	 * Number of armies received if you capture the entire continent.
	 */
	private int score;
	
	/**
	 * Continent represented as a collection of Territories
	 */
	private List<Territory> territories;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public List<Territory> getTerritories() {
		return territories;
	}
	public void setTerritories(List<Territory> territories) {
		this.territories = territories;
	}

}
