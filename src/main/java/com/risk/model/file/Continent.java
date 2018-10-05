package com.risk.model.file;

import java.util.Map;

/**
 * This is an Schema Class for Continent. 
 * @author MayankJariwala
 * @version 0.0.1
 */
public class Continent {
	
	/**
	 * @continents
	 */
	private Map<String, Integer> continents;

	
	/**
	 * @return the continents
	 */
	public Map<String, Integer> getContinents() {
		return continents;
	}

	/**
	 * @param continents the continents to set
	 */
	public void setContinents(Map<String, Integer> continents) {
		this.continents = continents;
	}
	
	
}

