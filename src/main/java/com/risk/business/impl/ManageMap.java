package com.risk.business.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.risk.business.IManageMap;
import com.risk.file.impl.ManageFile;
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
		ManageFile file_object = new ManageFile(fileName);
		file = file_object.retreiveFileObject();
		map = convertFileToMap(file);
		return map;
	}

	/**
	 * @see com.risk.business.IManageMap#getFullMap(java.lang.String)
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 */
	@Override
	public boolean writeMapToFile(Map map, String file_name) {
		file = convertMapToFile(map);
		ManageFile manage_file = new ManageFile();
		Boolean file_write_message = manage_file.saveFileToDisk(file, file_name);
		return file_write_message;
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
	public String checkDuplicateTerritory(Map map, String territory) {
		String message = "";
		List<Territory> territories = new ArrayList<>();
		for (Iterator<Entry<String, Continent>> iterator = map.getContinents().entrySet().iterator(); iterator
				.hasNext();) {
			java.util.Map.Entry<String, Continent> continent = iterator.next();
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
		List<Territory> territories = new ArrayList<>();
		HashMap<String, Boolean> territory_visited = new HashMap<>();
		HashMap<String, List<String>> neighbours_link = new HashMap<>();

		for (Iterator<Entry<String, Continent>> iterator = map.getContinents().entrySet().iterator(); iterator
				.hasNext();) {
			java.util.Map.Entry<String, Continent> continent = iterator.next();
			territories.addAll(continent.getValue().getTerritories());
		}

		/**
		 * If some territory does not have any neighbor then we can immediately arrive
		 * at a conclusion that Map is disconnected.
		 */
		for (Territory map_territory : territories) {
			neighbours_link.put(map_territory.getName(), map_territory.getNeighbours());
			territory_visited.put(map_territory.getName(), false);
			if (!(map_territory.getNeighbours() != null && map_territory.getNeighbours().size() > 0)) {
				message = map_territory.getName().concat(": Territory is disconnected from the rest of the Map.");
				error_flag = true;
				break;
			}
		}

		if (!error_flag) {
			start_node = territories.get(0).getName();
			territory_visited = find_link(start_node, neighbours_link, territory_visited);
			for (Iterator<Entry<String, Boolean>> iterator = territory_visited.entrySet().iterator(); iterator
					.hasNext();) {
				java.util.Map.Entry<String, Boolean> territory_link = iterator.next();
				if (!territory_link.getValue()) {
					message = "Map is disconnected.";
					break;
				}
			}
			return message;
		} else {
			return message;
		}
	}

	/**
	 * @see com.risk.business.IManageMap#saveMap(com.risk.model.gui.Map)
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 */
	@Override
	public Boolean saveMap(com.risk.model.gui.Map map, String file_name) {
		return guiMapToFile(map,file_name);
	}

	/**
	 * @see com.risk.business.IManageMap#fetchMap(String)
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 */
	@Override
	public com.risk.model.gui.Map fetchMap(String file_name) {
		return fileToGuiMap(file_name);
	}

	/**
	 * This method receives the World Map Object from GUI and checks it for any
	 * discontinuities. If its valid then the map is converted to a FILE Object and
	 * forwarded to File Access Layer for saving to a physical MAP File. If its
	 * invalid then a boolean value False is returned to GUI.
	 * 
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 * @param map GUI Object Representation of the World Map.
	 * @return False If its an invalid Map otherwise True.
	 */
	private Boolean guiMapToFile(com.risk.model.gui.Map map, String file_name) {
		
		List<com.risk.model.gui.Continent> continents_gui = new ArrayList<>();
		List<com.risk.model.gui.Territory> territories_gui = new ArrayList<>();
		List<Territory> territories_model;
		Territory territory_model;
		Continent continent_model;

		continents_gui = map.getContinents();
		territories_gui = map.getTerritories();
		HashMap<String, Continent> map_parsed = new HashMap<>();
		Map map_model = new Map();

		for (com.risk.model.gui.Continent continent_gui : continents_gui) {
			continent_model = new Continent();
			continent_model.setName(continent_gui.getName());
			continent_model.setScore(Integer.parseInt(continent_gui.getScore()));
			territories_model = new ArrayList<>();

			for (com.risk.model.gui.Territory territory_gui : territories_gui) {
				if (continent_gui.getName().equals(territory_gui.getContinentName())) {
					String[] neighbours_gui = territory_gui.getNeighbours().split(";");
					List<String> neighbours_model = new ArrayList<>(Arrays.asList(neighbours_gui));

					territory_model = new Territory();
					territory_model.setName(territory_gui.getName());
					territory_model.setNeighbours(neighbours_model);
					territories_model.add(territory_model);
				}
			}
			continent_model.setTerritories(territories_model);
			map_parsed.put(continent_model.getName(), continent_model);
		}

		map_model.setContinents(map_parsed);
		String message = checkDiscontinuity(map_model);
		if (message.equals("")) {
			Boolean write_file_status = writeMapToFile(map_model, file_name);
			return write_file_status;
		} else {
			return false;
		}
	}

	/**
	 * @see com.risk.business.IManageMap#fetchMaps()
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 */
	@Override
	public List<String> fetchMaps() {
		List<String> map_list = new ArrayList<>();
		ManageFile file_object = new ManageFile();
		map_list = file_object.fetchMapFilesFromResource();
		return map_list;
	}

	/**
	 * This method retrieves an existing World Map File from local system and
	 * returns a MAP Object that can be rendered to UI. If the loaded file is an
	 * invalid Map then it returns Null.
	 * 
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 * @param file_name Absolute Path of the map file stored on local system.
	 * @return GUI Map object if its a valid map otherwise Null.
	 */
	private com.risk.model.gui.Map fileToGuiMap(String file_name) {

		List<com.risk.model.gui.Continent> continents_gui = new ArrayList<>();
		List<com.risk.model.gui.Territory> territories_gui = new ArrayList<>();

		com.risk.model.gui.Continent continent_gui;
		com.risk.model.gui.Territory territory_gui;

		map = getFullMap(file_name);

		for (Iterator<Entry<String, Continent>> iterator = map.getContinents().entrySet().iterator(); iterator
				.hasNext();) {
			java.util.Map.Entry<String, Continent> continent_entry = iterator.next();
			continent_gui = new com.risk.model.gui.Continent(continent_entry.getKey(),
					Integer.toString(continent_entry.getValue().getScore()));
			continents_gui.add(continent_gui);
			for (Territory territory : continent_entry.getValue().getTerritories()) {
				String neighbours = String.join(";", territory.getNeighbours());
				territory_gui = new com.risk.model.gui.Territory(territory.getName(), continent_entry.getKey(),
						neighbours);
				territories_gui.add(territory_gui);
			}
		}

		if (continents_gui.size() > 0 && territories_gui.size() > 0) {
			com.risk.model.gui.Map map_gui = new com.risk.model.gui.Map(continents_gui, territories_gui);
			return map_gui;
		} else {
			return null;
		}
	}

	/**
	 * This recursive method here is an implementation of DepthFirstSearch, which is
	 * being run for checking any discontinuity in our Map. Each Territory is
	 * assumed to be a node of the graph. Logic is like we start with any territory
	 * and do a DepthFirstTraversal of the entire Map. If in the end all the
	 * Nodes(Territories) are visited at least once then the graph is connected else
	 * not.
	 * 
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 * @param start_node        : Starting territory for DFS traversal.
	 * @param neighbours_link   : Hash Map representing the entire Graph.
	 * @param territory_visited : Marker HashMap to keep track of which all
	 *                          territories are visited.
	 * @return HashMap<String, Boolean> : Resultant HashMap telling which all
	 *         territories could be visited and which not.
	 */
	private HashMap<String, Boolean> find_link(String start_node, HashMap<String, List<String>> neighbours_link,
			HashMap<String, Boolean> territory_visited) {
		if (!territory_visited.get(start_node)) {
			territory_visited.put(start_node, true);
		}
		Iterator<String> neighbour = neighbours_link.get(start_node).listIterator();
		while (neighbour.hasNext()) {
			String next_neighbour = neighbour.next();
			if (!territory_visited.get(next_neighbour)) {
				find_link(next_neighbour, neighbours_link, territory_visited);
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
		com.risk.model.file.Continent file_continent;
		com.risk.model.file.Territory file_territory;
		List<com.risk.model.file.Continent> continents = new ArrayList<>();
		List<com.risk.model.file.Territory> territories = new ArrayList<>();

		if (map != null) {

			file = new File();
			mapHead = new com.risk.model.file.Map();

			mapHead.setAuthor("Dummy"); // Dummy Hard-coded Values as MAP Object at business layer does not have any
			// author.
			mapHead.setImage("Dummy"); // Dummy Hard-coded Values as MAP Object at business layer does not have any
			// image.
			mapHead.setScroll("Dummy"); // Dummy Hard-coded Values as MAP Object at business layer does not have any
			// scroll.
			mapHead.setWarn("Dummy"); // Dummy Hard-coded Values as MAP Object at business layer does not have any
			// warn.
			mapHead.setWrap("Dummy"); // Dummy Hard-coded Values as MAP Object at business layer does not have any
			// wrap.

			for (Iterator<Entry<String, Continent>> iterator = map.getContinents().entrySet().iterator(); iterator
					.hasNext();) {
				java.util.Map.Entry<String, Continent> continent = iterator.next();

				file_continent = new com.risk.model.file.Continent();
				file_continent.setName(continent.getKey());
				file_continent.setScore(continent.getValue().getScore());
				continents.add(file_continent);

				for (Territory territory : continent.getValue().getTerritories()) {
					file_territory = new com.risk.model.file.Territory();
					file_territory.setName(territory.getName());
					file_territory.setPart_of_continent(continent.getKey());
					file_territory.setX_coordinate(20); // Dummy Hard-coded Values as MAP Object at business layer does
					// not have any coordinates.
					file_territory.setY_coordinate(28); // Dummy Hard-coded Values as MAP Object at business layer does
					// not have any coordinates.
					file_territory.setAdj_territories(territory.getNeighbours());
					territories.add(file_territory);
				}
			}

			file.setMap(mapHead);
			file.setContinents(continents);
			file.setTerritories(territories);
			return file;

		} else {
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
		Continent map_continent;
		Territory map_territory;
		HashMap<String, Continent> continents = new HashMap<>();
		List<String> neighbours;
		List<Territory> territories;
		if (file != null) {
			for (com.risk.model.file.Continent continent : file.getContinents()) {
				map_continent = new Continent();
				map_continent.setName(continent.getName());
				map_continent.setScore(continent.getScore());
				territories = new ArrayList<Territory>();

				for (com.risk.model.file.Territory territory : file.getTerritories()) {
					if (territory.getPart_of_continent().equals(continent.getName())) {
						map_territory = new Territory();
						map_territory.setName(territory.getName());
						neighbours = new ArrayList<>();
						for (String neighbour : territory.getAdj_territories()) {
							neighbours.add(neighbour);
						}
						map_territory.setNeighbours(neighbours);
						territories.add(map_territory);
					}
				}
				map_continent.setTerritories(territories);
				continents.put(continent.getName(), map_continent);
			}
			map.setContinents(continents);
			return map;
		} else {
			return null;
		}
	}
}