package com.risk.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.risk.model.Map;

@Controller
@RequestMapping("/maps")
public class MapController {

	@RequestMapping(value = "/map", method = RequestMethod.GET)
	@ResponseBody
	public Map printMap(HttpServletRequest request, HttpServletResponse response) {
		// ModelAndView mav = new ModelAndView("Hello");
		// mav.addObject("map", new Map());
		Map map = new Map();
		map.setCountries("India");
		return map;
	}

}
