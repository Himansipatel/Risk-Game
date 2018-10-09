package com.risk.business;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.risk.business.impl.ManageMap;
import com.risk.model.file.Continent;
import com.risk.model.file.File;
import com.risk.model.file.Map;
import com.risk.model.file.Territory;

/**
 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
 * @version 0.0.1
 */
public class ManageMapTest {

	private static IManageMap manageMap;

	@BeforeClass
	public static void initMapManager() {
		manageMap = new ManageMap();
	}

	/**
	 * This test here will check for a Hard-Coded Map created in the method itself
	 * for discontinuity.
	 * If Lets say for instance "Tibet" is removed as a territory and from all
	 * Neighbor-Relationships as well, it will lead to a discontinuity in Map.
	 * 
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 */
	@Test
	public void testMapDiscontinuity() {
		HashMap<String, com.risk.model.Continent> map_test = new HashMap<>();
		List<com.risk.model.Territory> territories_model = new ArrayList<>();
		List<String> neighbours_model     = new ArrayList<>();
		com.risk.model.Continent continent_model = new com.risk.model.Continent();
		com.risk.model.Territory territory_model = new com.risk.model.Territory();
		com.risk.model.Map       map_model       = new com.risk.model.Map();

		neighbours_model.add("SriLanka");
		neighbours_model.add("Germany");
		territory_model.setName("India");
		territory_model.setNeighbours(neighbours_model);
		territories_model.add(territory_model);

		neighbours_model = new ArrayList<>();
		neighbours_model.add("India");
		neighbours_model.add("Tibet");
		territory_model = new com.risk.model.Territory();
		territory_model.setName("SriLanka");
		territory_model.setNeighbours(neighbours_model);
		territories_model.add(territory_model);

		neighbours_model = new ArrayList<>();
		neighbours_model.add("China");
		territory_model = new com.risk.model.Territory();
		territory_model.setName("Nepal");
		territory_model.setNeighbours(neighbours_model);
		territories_model.add(territory_model);

		neighbours_model = new ArrayList<>();
		neighbours_model.add("Nepal");
		neighbours_model.add("Tibet");
		territory_model = new com.risk.model.Territory();
		territory_model.setName("China");
		territory_model.setNeighbours(neighbours_model);
		territories_model.add(territory_model);

		neighbours_model = new ArrayList<>();
		neighbours_model.add("China");
		neighbours_model.add("SriLanka");
		territory_model = new com.risk.model.Territory();
		territory_model.setName("Tibet");
		territory_model.setNeighbours(neighbours_model);
		territories_model.add(territory_model);

		continent_model.setName("Asia");
		continent_model.setScore(10);
		continent_model.setTerritories(territories_model);

		map_test.put("Asia", continent_model);

		territories_model = new ArrayList<>();
		neighbours_model = new ArrayList<>();
		neighbours_model.add("India");
		territory_model = new com.risk.model.Territory();
		territory_model.setName("Germany");
		territory_model.setNeighbours(neighbours_model);
		territories_model.add(territory_model);

		continent_model = new com.risk.model.Continent();
		continent_model.setName("Europe");
		continent_model.setScore(6);
		continent_model.setTerritories(territories_model);

		map_test.put("Europe", continent_model);

		map_model.setContinents(map_test);
		String message             = manageMap.checkDiscontinuity(map_model);
		assertEquals(message, "");		
	}

	/**
	 * This test here creates a dummy Hard-Coded representation of a File Object
	 * and converts it to a Map Object.
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 */
	@Test
	public void testMapCreationFromFile() {

		File file = new File();
		Map file_map = new Map();

		/**
		 * This part here is manually preparing File Object
		 * Later on it has to replaced with a method call
		 * retreive_file_object(fileName). It'll serve as test run for 
		 * method "retreive_file_object(fileName)", as well. 
		 */
		Continent continent_file = new Continent();
		Territory territory_file = new Territory();
		List<String> neighbours_file = new ArrayList<>();
		List<Continent> continents_file = new ArrayList<>();
		List<Territory> territories_file = new ArrayList<>();

		file_map.setAuthor("Apoorv");
		file_map.setImage("004_I72_Cobra.bmp");
		file_map.setScroll("no");
		file_map.setWarn("none");
		file_map.setWrap("yes");

		continent_file.setName("Asia");
		continent_file.setScore(30);
		continents_file.add(continent_file);

		continent_file = new Continent();
		continent_file.setName("Europe");
		continent_file.setScore(20);
		continents_file.add(continent_file);

		continent_file = new Continent();
		continent_file.setName("Australia");
		continent_file.setScore(10);
		continents_file.add(continent_file);

		neighbours_file.add("Nepal");
		neighbours_file.add("Germany");
		neighbours_file.add("France");
		neighbours_file.add("Australia");
		territory_file.setName("India");
		territory_file.setPart_of_continent("Asia");
		territory_file.setX_coordinate(10);
		territory_file.setY_coordinate(10);
		territory_file.setAdj_territories(neighbours_file);
		territories_file.add(territory_file);

		neighbours_file = new ArrayList<>();
		neighbours_file.add("India");
		neighbours_file.add("France");
		territory_file = new Territory();
		territory_file.setName("Germany");
		territory_file.setPart_of_continent("Europe");
		territory_file.setX_coordinate(30);
		territory_file.setY_coordinate(40);
		territory_file.setAdj_territories(neighbours_file);
		territories_file.add(territory_file);

		territory_file = new Territory();
		territory_file.setName("Nepal");
		territory_file.setPart_of_continent("Asia");
		territory_file.setX_coordinate(20);
		territory_file.setY_coordinate(20);
		territory_file.setAdj_territories(new ArrayList<String>(Arrays.asList("India")));
		territories_file.add(territory_file);

		territory_file = new Territory();
		territory_file.setName("France");
		territory_file.setPart_of_continent("Europe");
		territory_file.setX_coordinate(45);
		territory_file.setY_coordinate(25);
		territory_file.setAdj_territories(new ArrayList<String>(Arrays.asList("India", "Germany")));
		territories_file.add(territory_file);

		territory_file = new Territory();
		territory_file.setName("Australia");
		territory_file.setPart_of_continent("Australia");
		territory_file.setX_coordinate(70);
		territory_file.setY_coordinate(10);
		territory_file.setAdj_territories(new ArrayList<String>(Arrays.asList("India")));
		territories_file.add(territory_file);

		file.setContinents(continents_file);
		file.setMap(file_map);
		file.setTerritories(territories_file);
		file.setContinents(continents_file);

		//com.risk.model.Map map_out = manageMap.getFullMap("C://Test.map");
	}
}
