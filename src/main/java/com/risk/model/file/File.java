package com.risk.model.file;

import java.util.List;

/**
 * This represents file as a whole and will be returned to our business layer.
 * 
 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
 * @version 0.0.1
 */

public class File {

	/**
	 * The three sections marked in a Map file i.e. 
	 * [Map], [Continents], [Territories] have an exact equivalent here
	 * in form of objects.
	 */
	private Map map;
	private List<Continent> continents;
	private List<Territory> Territories;

	public Map getMap() {
		return map;
	}
	public void setMap(Map map) {
		this.map = map;
	}
	public List<Continent> getContinents() {
		return continents;
	}
	public void setContinents(List<Continent> continents) {
		this.continents = continents;
	}
	public List<Territory> getTerritories() {
		return Territories;
	}
	public void setTerritories(List<Territory> territories) {
		Territories = territories;
	}

}
