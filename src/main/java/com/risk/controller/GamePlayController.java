package com.risk.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.risk.business.IManageMap;
import com.risk.business.IManagePlayer;
import com.risk.model.GamePlay;
import com.risk.model.Player;

/**
 * Model class for GamePlayController
 * 
 * @author <a href="mailto:l_grew@encs.concordia.ca">Loveshant Grewal</a>
 * @version 0.0.1
 */
@Controller
@RequestMapping("/gamePlay")
public class GamePlayController {

	@Autowired
	IManageMap iManageMap;

	@Autowired
	IManagePlayer iManagePlayer;

	@RequestMapping(value = "/getPlayView", method = RequestMethod.GET)
	public ModelAndView getGamePlayView(HttpServletRequest request, HttpServletResponse response) throws Exception {

		ModelAndView model = new ModelAndView("gamePlay");
		return model;
	}

	@RequestMapping(value = "/initStartUpPhase", method = RequestMethod.GET)
	@ResponseBody
	public List<Player> initStartUpPhase(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "playersNo", required = false) String playersNo,
			@RequestParam(value = "fileName", required = false) String fileName) throws Exception {
		
		GamePlay game_state = null;
		game_state = iManagePlayer.createPlayer(Integer.parseInt(playersNo), fileName);
		List<Player> players = game_state.getGame_state();
		return players;
	}
}