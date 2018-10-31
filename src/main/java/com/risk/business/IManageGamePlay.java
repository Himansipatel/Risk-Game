package com.risk.business;

import java.util.List;
import com.risk.model.GamePlay;
import com.risk.model.Map;
import com.risk.model.Player;

/**
 * This Interface handles the GamePlay as per RiskRules and manages game state
 * flow between UI and Data Access Layer.
 * 
 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
 * @version 0.0.1
 */
public interface IManageGamePlay {

	/**
	 * This method is used for saving the state of the game across multiple game
	 * phases.
	 * 
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 * @param game_state State of Game during any phase
	 * @return state of the game saved so far.
	 */
	GamePlay savePhase(GamePlay game_state);

	/**
	 * This method is used for preparing the game state after STARTUP phase ends and
	 * before REINFORCE begins.
	 * 
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 * @param game_state State of Game during any phase
	 * @return state of the game updated so far.
	 * 
	 * This method here acts as our Game Manager method. This method decides the
	 * entire flow of the game. Updates the phase of the game as per inputs
	 * received from GUI and returns a new Game State back to GUI.
	 * It executes the action corresponding the given phase, update the state of the 
	 * game and decides the upcoming phases as per risk rules.
	 */
	GamePlay managePhase(GamePlay game_state);

	/**
	 * This method calculates the number of Armies to be allocated to each player at
	 * the start of their Reinforcement Phase, based on Risk Rules . 
	 * Rule 1 : Number of armies = max(floor(Total captured Territories / 3),3)
	 * Rule 2 : Number of extra armies added = continent score if Player has captured 
	 * the entire continent.
	 * @param gameplay Current state of entire Game.
	 * @param map      Current map on which game is being played.
	 * @return State of the game in form of a List of Players.
	 */
	List<Player> calculateArmiesReinforce(List<Player> gameplay, Map map);
}