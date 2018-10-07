package com.risk.business;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
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

	@Test
	public void testMapCreationFromFile() {

		File file = new File();
		Map map = new Map();

		/**
		 * This part here is manually preparing File Object
		 * Later on it has to replaced with a method call
		 * retreive_file_object(fileName).
		 */
		Continent continent = new Continent();
		Territory territory = new Territory();
		List<String> neighbours = new ArrayList<>();
		List<Continent> continents = new ArrayList<>();
		List<Territory> territories = new ArrayList<>();

		map.setAuthor("Apoorv");
		map.setImage("004_I72_Cobra.bmp");
		map.setScroll("no");
		map.setWarn("none");
		map.setWrap("yes");

		continent.setName("Asia");
		continent.setScore(30);
		continents.add(continent);

		continent = new Continent();
		continent.setName("Europe");
		continent.setScore(20);
		continents.add(continent);

		continent = new Continent();
		continent.setName("Australia");
		continent.setScore(10);
		continents.add(continent);

		neighbours.add("Nepal");
		neighbours.add("Germany");
		neighbours.add("France");
		neighbours.add("Australia");
		territory.setName("India");
		territory.setPart_of_continent("Asia");
		territory.setX_coordinate(10);
		territory.setY_coordinate(10);
		territory.setAdj_territories(neighbours);
		territories.add(territory);

		neighbours = new ArrayList<>();
		neighbours.add("India");
		neighbours.add("France");
		territory = new Territory();
		territory.setName("Germany");
		territory.setPart_of_continent("Europe");
		territory.setX_coordinate(30);
		territory.setY_coordinate(40);
		territory.setAdj_territories(neighbours);
		territories.add(territory);

		territory = new Territory();
		territory.setName("Nepal");
		territory.setPart_of_continent("Asia");
		territory.setX_coordinate(20);
		territory.setY_coordinate(20);
		territory.setAdj_territories(new ArrayList<String>(Arrays.asList("India")));
		territories.add(territory);

		territory = new Territory();
		territory.setName("France");
		territory.setPart_of_continent("Europe");
		territory.setX_coordinate(45);
		territory.setY_coordinate(25);
		territory.setAdj_territories(new ArrayList<String>(Arrays.asList("India", "Germany")));
		territories.add(territory);

		territory = new Territory();
		territory.setName("Australia");
		territory.setPart_of_continent("Australia");
		territory.setX_coordinate(70);
		territory.setY_coordinate(10);
		territory.setAdj_territories(new ArrayList<String>(Arrays.asList("India")));
		territories.add(territory);

		file.setContinents(continents);
		file.setMap(map);
		file.setTerritories(territories);

		file.setContinents(continents);

		com.risk.model.Map map_out = manageMap.getFullMap("C://Test.map");

		assertEquals(continents, continents);

	}

}
