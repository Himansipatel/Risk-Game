package com.risk.model;

import java.util.HashMap;

/**
 * Model Class for Map.
 * 
 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
 * @version 0.0.1
 */

public class Map {
	
	/**
	 * Map is represented by a collection of continents.
	 */
	private HashMap<String, Continent> continents;

	public HashMap<String, Continent> getContinents() {
		return continents;
	}

	public void setContinents(HashMap<String, Continent> continents) {
		this.continents = continents;
	}

}
