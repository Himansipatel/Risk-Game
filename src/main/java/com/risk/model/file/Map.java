/**
 * 
 */
package com.risk.model.file;

import java.util.HashMap;

/**
 * This is an Schema Class for Map.
 * @author MayankJariwala
 * @version 0.0.1
 */
public class Map {
	
	/**
	 * @map_info : General Information of Map [Author,Version,...]
	 */
	private HashMap<String, String> map_info;
	
	public Map() {
		map_info = new HashMap<String,String>();
	}

	
	/**
	 * @return the map_info
	 */
	public HashMap<String, String> getMap_info() {
		return map_info;
	}


	/**
	 * @param map_info the map_info to set
	 */
	public void setMap_info(HashMap<String, String> map_info) {
		this.map_info = map_info;
	}

}
