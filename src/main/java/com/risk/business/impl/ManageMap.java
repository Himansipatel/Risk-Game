package com.risk.business.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.risk.business.IManageMap;
import com.risk.model.Continent;
import com.risk.model.Map;
import com.risk.model.Territory;
import com.risk.model.file.File;

/**
 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
 * @version 0.0.1
 */
@Service
public class ManageMap implements IManageMap {

	private File file;
	private Map map;

	/**
	 * @see com.risk.business.IManageMap#getFullMap(java.lang.String)
	 */
	@Override
	public Map getFullMap(String fileName) {
		/**
		 * This function here is pending as. It takes Fully Qualified File Name
		 * and returns a File Object.
		 */
		//file = retreive_file_object(fileName);
		map = convertFileToMap(file);
		return map;
	}

	/**
	 * This method parses the File Object and converts it to a Map Object.
	 * 
	 * @param file : File Object Representing the Map File given in Input.
	 * @return Map : Object Representation of the Map File.
	 */
	private Map convertFileToMap(File file) {

		map = new Map();
		Continent map_continent = new Continent();
		Territory map_territory = new Territory();
		HashMap<String, Continent> continents = new HashMap<>();
		List<String> neighbours;

		if (file != null) {
			for (com.risk.model.file.Continent continent : file.getContinents()) {
				map_continent.setName(continent.getName());
				map_continent.setScore(continent.getScore());
				map_continent.setTerritories(new ArrayList<Territory>());
				continents.put(continent.getName(), map_continent);
				map_continent = new Continent();
			}

			map.setContinents(continents);

			for (com.risk.model.file.Territory territory : file.getTerritories()) {
				map_territory.setName(territory.getName());
				neighbours = new ArrayList<>();
				for (String neighbour : territory.getAdj_territories()) {
					neighbours.add(neighbour);
				}
				map_territory.setNeighbour(neighbours);
				map_continent = map.getContinents().get(territory.getPart_of_continent());
				map_continent.getTerritories().add(map_territory);
			}
			return map;	
		}else {
			return null;
		}

	}

}
