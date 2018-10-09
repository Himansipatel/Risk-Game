package com.risk.model;

import java.util.List;

/**
 * Model class for Territory
 * 
 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
 * @version 0.0.1
 */
public class Territory {
	
	private String name;
	
	/**
	 * List of neighboring territories
	 */
	private List<String> neighbours;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getNeighbours() {
		return neighbours;
	}

	public void setNeighbours(List<String> neighbours) {
		this.neighbours = neighbours;
	}
	
}
