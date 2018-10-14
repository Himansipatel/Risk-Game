package com.risk.file.impl;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.commons.lang3.StringUtils;

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
	// Indicate Active Section such as Territory,Continent,Map
	private FileSectionDivider current_section_in_file;
	private List<Continent> continent_object_list;
	private List<Territory> territory_object_list;

	private Logger logger = Logger.getLogger("ManageFileLogger");

	// Indicates 3 Section in Map File
	enum FileSectionDivider {
		MAP, CONTINENT, TERRITORY;
	}

	public ManageFile() {
		try {
			FileHandler fh = new FileHandler("src/main/resource/ManageFile.log");
			logger.addHandler(fh);
			logger.setUseParentHandlers(false);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param file_absolute_path
	 */
	public ManageFile(String file_absolute_path) {
		// Using Default Constructor Values
		this();
		this.file_absolute_path = file_absolute_path;
		filelayer_map_object = null;
		current_section_in_file = null;
		file_object = new File();
		continent_object_list = new ArrayList<Continent>();
		territory_object_list = new ArrayList<Territory>();
	}

	/**
	 * This Function reads various sections and their related information from map
	 * file.
	 * 
	 * @author <a href="mailto:mayankjariwala1994@gmail.com">MayankJariwala</a>
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
			logger.info("Reading Map File Operation (retreiveFileObject::ManageFile)");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("IOException (retreiveFileObject::ManageFile) " + e.getMessage());
		}
		return file_object;
	}

	/**
	 * This function is use to set value of territory model entities at file layer
	 * 
	 * @author <a href="mailto:mayankjariwala1994@gmail.com">MayankJariwala</a>
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
	 * @author <a href="mailto:mayankjariwala1994@gmail.com">MayankJariwala</a>
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
	 * @author <a href="mailto:mayankjariwala1994@gmail.com">MayankJariwala</a>
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

	/**
	 * This function is use to write map data to new or existing map file.
	 * 
	 * @author <a href="mailto:mayankjariwala1994@gmail.com">MayankJariwala</a>
	 * @param file
	 * @return File Write Status Message
	 */
	public String saveFileToDisk(File file) {
		String file_writer_message = "";
		try (PrintStream map_file_writer = new PrintStream(
				new BufferedOutputStream(new FileOutputStream("src/main/resource/xyz.map")))) {
			logger.info("Performing File Write Operation (saveFileToDisk::ManageFile) Line# 167");
			map_file_writer.println("[Map]");
			map_file_writer.println("author=" + file.getMap().getAuthor());
			map_file_writer.println("image=" + file.getMap().getImage());
			map_file_writer.println("wrap=" + file.getMap().getWrap());
			map_file_writer.println("scroll=" + file.getMap().getScroll());
			map_file_writer.println("warn=" + file.getMap().getWarn());
			map_file_writer.println();

			map_file_writer.println("[Continents]");
			for (Continent continent_info : file.getContinents()) {
				map_file_writer.println(continent_info.getName() + "=" + continent_info.getScore());
			}
			map_file_writer.println();

			map_file_writer.println("[Territories]");
			for (Territory territory_info : file.getTerritories()) {
				String each_territory_info = "";
				each_territory_info = StringUtils.join(new String[] { territory_info.getName(),
						String.valueOf(territory_info.getX_coordinate()),
						String.valueOf(territory_info.getY_coordinate()), territory_info.getPart_of_continent() }, ",");
				List<String> each_adj_territory = new ArrayList<>();
				for (String adj_territories : territory_info.getAdj_territories()) {
					each_adj_territory.add(adj_territories);
				}
				each_territory_info += "," + StringUtils.join(each_adj_territory, ",");
				map_file_writer.println(each_territory_info);
			}
			if (map_file_writer.checkError()) {
				logger.warning("Error (saveFileToDisk::ManageFile)");
				file_writer_message = "Problem while saving file";
			} else {
				logger.info("Success (saveFileToDisk::ManageFile)");
				file_writer_message = "File Save Successfully";
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.warning("IOException (saveFileToDisk::ManageFile) " + e.getMessage());
		}
		return file_writer_message;
	}
}
