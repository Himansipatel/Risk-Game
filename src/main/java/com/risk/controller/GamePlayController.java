package com.risk.controller;

import java.util.Observer;

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

import com.risk.business.IManageGamePlay;
import com.risk.business.IManageMap;
import com.risk.business.IManagePlayer;
import com.risk.model.GamePlay;

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

	@Autowired
	IManageGamePlay iManageGamePlay;

	static GamePlay gamePlay;

	/**
	 * This function renders the gamePlay.jsp file on which players can start
	 * playing their game.
	 * 
	 * @author <a href="mailto:l_grew@encs.concordia.ca">Loveshant Grewal</a>
	 * @param request  Request Payload
	 * @param response An object to assist a servlet in sending a response to the
	 *                 client
	 * @return Web Page of Game Play
	 * @throws Exception NullPointerException when model object is null
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
	 * @param request        Request Payload
	 * @param response       An object to assist a servlet in sending a response to
	 *                       the client
	 * @param playersNo      Number of Playing Players
	 * @param fileName       GamePlay Map to be loaded
	 * @param allocationType Deciding factor for allocation of armies(M-Manual or
	 *                       A-Automatic) on territory
	 * @return List of Player Object
	 * @throws Exception NullPointerException when game state object is null
	 */
	@RequestMapping(value = "/initStartUpPhase", method = RequestMethod.GET)
	@ResponseBody
	public GamePlay initStartUpPhase(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "playersNo", required = true) String playersNo,
			@RequestParam(value = "fileName", required = true) String fileName,
			@RequestParam(value = "allocationType", required = true) String allocationType) throws Exception {

		gamePlay = iManagePlayer.createPlayer(Integer.parseInt(playersNo), fileName, allocationType);
		gamePlay.addObserver((Observer) iManageGamePlay);
		return gamePlay;
	}

	/**
	 * This function will save the map state and return the map state with next
	 * phase and next player turn.
	 * 
	 * @author <a href="mailto:l_grew@encs.concordia.ca">Loveshant Grewal</a>
	 * @param request          Request Payload
	 * @param response         An object to assist a servlet in sending a response
	 *                         to the client
	 * @param gamePlayFromView game state to save
	 * @return game play state after update with next phase and player
	 * @throws Exception NullPointerException when game state object is null
	 */
	@RequestMapping(value = "/saveGameState", method = RequestMethod.POST)
	@ResponseBody
	public GamePlay submitGameState(HttpServletRequest request, HttpServletResponse response,
			@RequestBody GamePlay gamePlayFromView) throws Exception {
		// gamePlay = iManageGamePlay.savePhase(gamePlay);
		abstractView(gamePlayFromView);
		return gamePlay;
	}

	/**
	 * This function provides the abstract view for observer design pattern.
	 * 
	 * @author <a href="mailto:l_grew@encs.concordia.ca">Loveshant Grewal</a>
	 * @param gamePlay game state to save
	 */
	private void abstractView(GamePlay gamePlayFromView) {
		gamePlay.setMap(gamePlayFromView.getMap());
		gamePlay.setCard_trade(gamePlayFromView.getCard_trade());
		gamePlay.setCurrent_player(gamePlayFromView.getCurrent_player());
		gamePlay.setFile_name(gamePlayFromView.getFile_name());
		gamePlay.setFree_cards(gamePlayFromView.getFree_cards());
		gamePlay.setGame_phase(gamePlayFromView.getGame_phase());
		gamePlay.setStatus(gamePlayFromView.getStatus());
		gamePlay.setAttack(gamePlayFromView.getAttack());
		gamePlay.setFortification(gamePlayFromView.getFortification());
		gamePlay.setArmy_move(gamePlayFromView.getArmy_move());
		gamePlay.setGui_map(gamePlayFromView.getGui_map());
		gamePlay.setDomination(gamePlayFromView.getDomination());
		gamePlay.setGame_state(gamePlayFromView.getGame_state());
	}
}