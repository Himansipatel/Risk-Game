package com.risk.file.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.risk.model.file.Continent;
import com.risk.model.file.File;
import com.risk.model.file.Map;
import com.risk.model.file.Territory;

/**
 * @author <a href="mailto:mayankjariwala1994@gmail.com">MayankJariwala</a>
 * @version 0.0.1
 */
public class ManageFile {

	private String file_absolute_path;
	private Map filelayer_map_object;
	private File file_object;
	private FileSectionDivider current_section_in_file; // Indicate Active Section such as Territory,Continent,Map
	private List<Continent> continent_object_list;
	private List<Territory> territory_object_list;

	// Indicates 3 Section in Map File
	enum FileSectionDivider {
		MAP, CONTINENT, TERRITORY;
	}

	/**
	 * 
	 * @param file_absolute_path
	 */
	public ManageFile(String file_absolute_path) {
		this.file_absolute_path = file_absolute_path;
		filelayer_map_object = null;
		current_section_in_file = null;
		file_object = new File();
		continent_object_list = new ArrayList<Continent>();
		territory_object_list = new ArrayList<Territory>();
	}

	/**
	 * This Function reads various sections and their related information from map file.
	 * 
	 * @return File Object
	 */
	public File retreiveFileObject() {
		String line = "";
		try (BufferedReader map_file_object = new BufferedReader(new FileReader(file_absolute_path))) {
			while ((line = map_file_object.readLine()) != null) {
				if (line.length() > 0) {
					// Later Change this Part to function
					if (current_section_in_file == FileSectionDivider.MAP)
						setValuesToFileLayerMapObject(line);
					if (current_section_in_file == FileSectionDivider.CONTINENT)
						setValuesToFileLayerContientObject(line);
					if (current_section_in_file == FileSectionDivider.TERRITORY)
						setValuesToFileLayerTerritoryObject(line);

					if (line.equalsIgnoreCase("[Map]")) {
						filelayer_map_object = new Map();
						current_section_in_file = FileSectionDivider.MAP;
					} else if (line.equalsIgnoreCase("[Continents]")) {
						current_section_in_file = FileSectionDivider.CONTINENT;
					} else if (line.equalsIgnoreCase("[TERRITORIES]")) {
						current_section_in_file = FileSectionDivider.TERRITORY;
					}
				} else if (current_section_in_file != FileSectionDivider.TERRITORY) {
					current_section_in_file = null;
				}
			}
			// Set Values to file Object
			file_object.setMap(filelayer_map_object);
			file_object.setContinents(continent_object_list);
			file_object.setTerritories(territory_object_list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file_object;
	}

	/**
	 * This function is use to set value of territory model entities at file layer
	 * 
	 * @param territory_info
	 */
	private void setValuesToFileLayerTerritoryObject(String territory_info) {
		if (territory_info.length() > 0) {
			List<String> adjacent_territories = new ArrayList<String>();
			Territory territory = new Territory();
			String[] key_value = territory_info.split(",");
			int length = key_value.length;
			territory.setName(key_value[0]);
			territory.setX_coordinate(Integer.parseInt(key_value[1]));
			territory.setY_coordinate(Integer.parseInt(key_value[2]));
			territory.setPart_of_continent(key_value[3]);
			for (int i = 4; i < length; i++) {
				adjacent_territories.add(key_value[i]);
			}
			territory.setAdj_territories(adjacent_territories);
			if (territory != null) {
				territory_object_list.add(territory);
			}
		}
	}

	/**
	 * This function is use to set value of continent model entities at file layer
	 * 
	 * @param continent_info
	 */
	private void setValuesToFileLayerContientObject(String continent_info) {
		Continent continent = new Continent();
		String[] key_value = continent_info.split("=");
		continent.setName(key_value[0]);
		continent.setScore(Integer.parseInt(key_value[1]));
		continent_object_list.add(continent);
	}

	/**
	 * This function is use to set value of map model entities at file layer
	 * 
	 * @param map_info
	 */
	private void setValuesToFileLayerMapObject(String map_info) {
		String[] key_value = map_info.split("=");
		if (key_value[0].equalsIgnoreCase("author")) {
			filelayer_map_object.setAuthor(key_value[1]);
		} else if (key_value[0].equalsIgnoreCase("imgae")) {
			filelayer_map_object.setImage(key_value[1]);
		} else if (key_value[0].equalsIgnoreCase("wrap")) {
			filelayer_map_object.setWrap(key_value[1]);
		} else if (key_value[0].equalsIgnoreCase("scroll")) {
			filelayer_map_object.setScroll(key_value[1]);
		} else if (key_value[0].equalsIgnoreCase("warn")) {
			filelayer_map_object.setWarn(key_value[1]);
		}
	}
}
