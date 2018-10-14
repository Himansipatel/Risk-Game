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
	 * @param fileName Fully Qualified Map File Name on local disk.
	 *  			   This file will be rendered for Playing as well as for Editing Map.
	 * @return Entire Map Object which will be rendered for Playing.
	 */
	Map getFullMap(String fileName);

	/**
	 * This method is an abstraction for the process of converting A Map Object
	 * into the Map File to be saved/loaded.
	 * 
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 * @param map This is the entire Map Object which will be converted to a File
	 * 			  Object and then written on to a Map File.
	 */	
	void writeMapToFile(Map map); 

	/**
	 * This method checks the World Map Object for any duplicate continents.
	 * 
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 * @param map Object Representation of the Map File.
	 * @return Any Errors if they exist otherwise empty String.
	 */
	String checkDuplicateContinent(Map map, String continent);

	/**
	 * This method checks the World Map Object for any duplicate territories.
	 * 
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 * @param map Object Representation of the Map File.
	 * @return Any Errors if they exist otherwise empty String.
	 */
	String checkDuplicateTerritory(Map map,String territory);

	/**
	 * This method checks the World Map Object for any discontinuities,
	 * i.e. any part(territories/set of territories) that are disconnected 
	 * from the rest of the connected map.
	 * 
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 * @param map Object Representation of the Map File.
	 * @return Any Errors if they exist otherwise empty String.
	 */	
	String checkDiscontinuity(Map map);
	
	/**
	 * This method receives the World Map Object from GUI and checks it for any discontinuities.
	 * If its valid then the map is converted to a FILE Object and forwarded to File Access Layer
	 * for saving to a physical MAP File.
	 * If its invalid then a boolean value False is returned to GUI. 
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 * @param map GUI Object Representation of the World Map.
	 * @return False If its an invalid Map otherwise True.
	 */	
	Boolean saveMap(com.risk.model.gui.Map map);
	
	/**
	 * This method retrieves an existing World Map File from local system and returns a 
	 * MAP Object that can be rendered to UI.
	 * If the loaded file is an invalid Map then it returns Null. 
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 * @param file_name Absolute Path of the  map file stored on local system.
	 * @return GUI Map object if its a valid map otherwise Null.
	 */	
	com.risk.model.gui.Map fetchMap(String file_name);

}
