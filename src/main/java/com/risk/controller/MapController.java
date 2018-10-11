package com.risk.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.risk.business.IManageMap;
import com.risk.model.gui.Map;

/**
 * Model class for MapController
 * 
 * @author <a href="mailto:l_grew@encs.concordia.ca">Loveshant Grewal</a>
 * @version 0.0.1
 */
@Controller
@RequestMapping("/maps")
public class MapController {

	@Autowired
	IManageMap iManageMap;

	@RequestMapping(value = "/getMapView", method = RequestMethod.GET)
	public ModelAndView ModelAndView(HttpServletRequest request, HttpServletResponse response) throws Exception {

		ModelAndView model = new ModelAndView("map");
		com.risk.model.Map map = createMapObject();
		model.addObject("map", map);
		return model;
	}

	/**
	 * This method is an abstraction for the process of retrieving A Map Object from
	 * the file name given as Input.
	 */
	@RequestMapping(value = "/map", method = RequestMethod.GET)
	@ResponseBody
	public Map getFullMap(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "fileName", required = false) String fileName) throws Exception {
		Map map = prepareFileObject();
		return map;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public void submitQRList(HttpServletRequest request, HttpServletResponse response, @RequestBody Map map)
			throws Exception {
		System.out.println(map);
	}

	private com.risk.model.Map createMapObject() {
		com.risk.model.Map map = new com.risk.model.Map();
		map.setContinents(new HashMap<>());
		return map;
	}

	private Map prepareFileObject() {

		List<com.risk.model.gui.Continent> continents = new ArrayList<>();
		List<com.risk.model.gui.Territory> territories = new ArrayList<>();

		com.risk.model.gui.Continent continent = new com.risk.model.gui.Continent("Asia", "8");
		com.risk.model.gui.Territory territory = new com.risk.model.gui.Territory("India", "Asia", "Nepal;China");

		continents.add(continent);
		territories.add(territory);

		continent = new com.risk.model.gui.Continent("Europe", "8");
		territory = new com.risk.model.gui.Territory("France", "Europe", "Germany;Belgium");

		continents.add(continent);
		territories.add(territory);
		com.risk.model.gui.Map map = new com.risk.model.gui.Map(continents, territories);
		return map;

	}

}
