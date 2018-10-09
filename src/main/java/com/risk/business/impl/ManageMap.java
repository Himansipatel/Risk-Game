package com.risk.business.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

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
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 */
	@Override
	public Map getFullMap(String fileName) {
		//file = retreive_file_object(fileName);
		map = convertFileToMap(file);
		return map;
	}


	/**
	 * @see com.risk.business.IManageMap#getFullMap(java.lang.String)
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 */
	@Override
	public void writeMapToFile(Map map) {
		file = convertMapToFile(map);
		//saveFileToDisk(file);
	}

	/**
	 * @see com.risk.business.IManageMap#getFullMap(java.lang.String)
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 */
	@Override	
	public String checkDuplicateContinent(Map map, String continent) {
		String message = "";
		Set<String> continents = map.getContinents().keySet();
		for (String map_continent : continents) {
			if (map_continent.equalsIgnoreCase(continent)) {
				message = continent.concat(": Continent is already added in the Map.");
				break;
			}
		}
		return message;
	}

	/**
	 * @see com.risk.business.IManageMap#getFullMap(java.lang.String)
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 */
	@Override	
	public String checkDuplicateTerritory(Map map,String territory) {
		String message = "";
		List<Territory> territories = new ArrayList<>(); 
		for (Iterator<Entry<String, Continent>> iterator = map.getContinents().entrySet().iterator(); iterator.hasNext();) {
			java.util.Map.Entry<String,Continent> continent = iterator.next();
			territories.addAll(continent.getValue().getTerritories());
		}
		for (Territory map_territory : territories) {
			if (map_territory.getName().equalsIgnoreCase(territory)) {
				message = territory.concat(": Territory is already added in the Map.");
				break;				
			}
		}
		return message;		
	}


	/**
	 * @see com.risk.business.IManageMap#getFullMap(java.lang.String)
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 */
	@Override	
	public String checkDiscontinuity(Map map) {

		boolean error_flag = false;
		String message = "";
		String start_node;
		List<Territory> territories     = new ArrayList<>();
		HashMap<String, Boolean> territory_visited = new HashMap<>();
		HashMap<String, List<String>> neighbours_link = new HashMap<>();

		for (Iterator<Entry<String, Continent>> iterator = map.getContinents().entrySet().iterator(); iterator.hasNext();) {
			java.util.Map.Entry<String,Continent> continent = iterator.next();
			territories.addAll(continent.getValue().getTerritories());
		}		
		
		/**
		 * If some territory does not have any neighbor then we can immediately 
		 * arrive at a conclusion that Map is disconnected.
		 */
		for (Territory map_territory : territories) {
			neighbours_link.put(map_territory.getName(), map_territory.getNeighbours());
			territory_visited.put(map_territory.getName(),false);
			if (!(map_territory.getNeighbours() != null && map_territory.getNeighbours().size() > 0)) {
				message = map_territory.getName().concat(": Territory is disconnected from the rest of the Map.");
				error_flag = true;
				break;
			}
		}

		if (!error_flag) {
			start_node = territories.get(0).getName();
			territory_visited = find_link(start_node, neighbours_link, territory_visited);
			for (Iterator<Entry<String, Boolean>> iterator = territory_visited.entrySet().iterator(); iterator.hasNext();) {
				java.util.Map.Entry<String,Boolean> territory_link = iterator.next();
				if (!territory_link.getValue()) {
					message = "Map is disconnected.";
					break;
				}
			}
			return message;
		}else {
			return message;	
		}
	}
	
	/**
	 * This recursive method here is an implementation of DepthFirstSearch,
	 * which is being run for checking any discontinuity in our Map.
	 * Each Territory is assumed to be a node of the graph.
	 * Logic is like we start with any territory and do a DepthFirstTraversal of
	 * the entire Map. If in the end all the Nodes(Territories) are visited
	 * at least once then the graph is connected else not.
	 * 
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 * @param start_node        : Starting territory for DFS traversal.
	 * @param neighbours_link   : Hash Map representing the entire Graph.
	 * @param territory_visited : Marker HashMap to keep track of which all 
	 *                            territories are visited.
	 * @return HashMap<String, Boolean> : Resultant HashMap telling which all 
	 * 									  territories could be visited and which
	 * 									  not.
	 */
	private HashMap<String, Boolean> find_link(String start_node, HashMap<String, List<String>> neighbours_link, HashMap<String, Boolean> territory_visited) {
		if (!territory_visited.get(start_node)) {
			territory_visited.put(start_node,true);
		}
		Iterator<String> neighbour = neighbours_link.get(start_node).listIterator();
		while(neighbour.hasNext())
		{
			String next_neighbour = neighbour.next();
			if (!territory_visited.get(next_neighbour)) {
				find_link(next_neighbour,neighbours_link,territory_visited);
			}
		}
		return territory_visited;
	}

	/**
	 * This method parses the Map Object and converts it to a File Object.
	 * 
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 * @param map : Object Representation of the Map File.
	 * @return File file : File Object Representing the Map File to be saved/loaded.
	 */
	private File convertMapToFile(Map map) {

		com.risk.model.file.Map mapHead;
		com.risk.model.file.Continent file_continent = new com.risk.model.file.Continent();
		com.risk.model.file.Territory file_territory = new com.risk.model.file.Territory();
		List<com.risk.model.file.Continent> continents  = new ArrayList<>();
		List<com.risk.model.file.Territory> territories = new ArrayList<>();
		List<Territory> continent_territories = new ArrayList<>();

		if (map!=null) {

			file = new File();
			mapHead = new com.risk.model.file.Map();

			mapHead.setAuthor("Apoorv");
			mapHead.setImage("Apoorv");
			mapHead.setScroll("Apoorv");
			mapHead.setWarn("Apoorv");
			mapHead.setWrap("Apoorv");

			for (Iterator<Entry<String, Continent>> iterator = map.getContinents().entrySet().iterator(); iterator.hasNext();) {
				java.util.Map.Entry<String,Continent> continent = iterator.next();				
				file_continent.setName(continent.getValue().getName());
				file_continent.setScore(continent.getValue().getScore());
				continents.add(file_continent);
			}

			for (Iterator<Entry<String, Continent>> iterator = map.getContinents().entrySet().iterator(); iterator.hasNext();) {
				java.util.Map.Entry<String,Continent> continent = iterator.next();				
				continent_territories = continent.getValue().getTerritories();

				for (Territory territory : continent_territories) {
					file_territory.setName(territory.getName());
					file_territory.setPart_of_continent(continent.getValue().getName());
					file_territory.setX_coordinate(20);
					file_territory.setY_coordinate(28);
					file_territory.setAdj_territories(territory.getNeighbours());
					territories.add(file_territory);
				}
			}


			file.setMap(mapHead);
			file.setContinents(continents);
			file.setTerritories(territories);
			return file;

		}else {
			return null;
		}

	}

	/**
	 * This method parses the File Object and converts it to a Map Object.
	 * 
	 * @param file : File Object Representing the Map File to be saved/loaded.
	 * @return Map map : Object Representation of the Map File.
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
				map_territory.setNeighbours(neighbours);
				map_continent = map.getContinents().get(territory.getPart_of_continent());
				map_continent.getTerritories().add(map_territory);
			}
			return map;	
		}else {
			return null;
		}
	}
}
