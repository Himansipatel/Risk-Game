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
 * GamePlay Controller is a part of MVC Controller which handle the actions and
 * events on GUI side.According to risk game, this controller allows to
 * initialize the startup phase (each phases) and render game play webpage to
 * players.
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

	/**
	 * This function renders the gamePlay.jsp file on which players can start
	 * playing their game.
	 * 
	 * @author <a href="mailto:l_grew@encs.concordia.ca">Loveshant Grewal</a>
	 * @param request
	 * @param response
	 * @return Web Page of Game Play
	 * @throws Exception
	 */
	@RequestMapping(value = "/getPlayView", method = RequestMethod.GET)
	public ModelAndView getGamePlayView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView model = new ModelAndView("gamePlay");
		return model;
	}

	/**
	 * This function initializes the startup phase once the player selects no. of
	 * players and game play map.
	 * 
	 * @author <a href="mailto:l_grew@encs.concordia.ca">Loveshant Grewal</a>
	 * @param request
	 * @param response
	 * @param playersNo
	 * @param fileName
	 * @return List of Player Object
	 * @throws Exception
	 */
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