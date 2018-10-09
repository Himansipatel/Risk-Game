package com.risk.business;

import com.risk.model.Map;

/**
 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
 * @version 0.0.1
 */
public interface IManageMap {
	/**
	 * This method is an abstraction for the process of retrieving A Map Object
	 * from the Map File to be saved/loaded.
	 * 
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 * @param fileName : Fully Qualified Map File Name on local disk.
	 *  				 This file will be rendered for Playing as well as for Editing Map.
	 * @return Map : This is the entire Map Object which will be rendered for Playing.
	 */
	Map getFullMap(String fileName);

	/**
	 * This method is an abstraction for the process of converting A Map Object
	 * into the Map File to be saved/loaded.
	 * 
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 * @param map  : This is the entire Map Object which will be converted to a File
	 * 				 Object and then written on to a Map File.
	 */	
	void writeMapToFile(Map map); 

	/**
	 * This method checks the World Map Object for any duplicate continents.
	 * 
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 * @param map : Object Representation of the Map File.
	 * @return String message : Any Errors is they exist otherwise empty String.
	 */
	String checkDuplicateContinent(Map map, String continent);

	/**
	 * This method checks the World Map Object for any duplicate territories.
	 * 
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 * @param map : Object Representation of the Map File.
	 * @return String message : Any Errors is they exist otherwise empty String.
	 */
	String checkDuplicateTerritory(Map map,String territory);

	/**
	 * This method checks the World Map Object for any discontinuities,
	 * i.e. any part(territories/set of territories) that are disconnected 
	 * from the rest of the connected map.
	 * 
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 * @param map : Object Representation of the Map File.
	 * @return String message : Any Errors is they exist otherwise empty String.
	 */	
	String checkDiscontinuity(Map map);	

}
