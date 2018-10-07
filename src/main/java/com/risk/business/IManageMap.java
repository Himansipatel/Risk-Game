package com.risk.business;

import com.risk.model.Map;

/**
 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
 * @version 0.0.1
 */
public interface IManageMap {
	/**
	 * This method is an abstraction for the process of retrieving A Map Object
	 * from the Map File given as Input.
	 * 
	 * @param fileName : Fully Qualified Map File Name on local disk.
	 *  				 This file will be rendered for Playing as well as for Editing Map.
	 * @return Map : This is entire Map Object which will be rendered
	 */
	Map getFullMap(String fileName);
}
