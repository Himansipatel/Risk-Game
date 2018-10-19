package com.risk.business;

import com.risk.model.GamePlay; 

/**
 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
 * @version 0.0.1
 */
public interface IManageGamePlay {
	
	/**
	 * This method is used for saving the state of the game after a phase ends.
	 * 
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 * @param game_state State of Game at the end of startup phase.
	 * @return state of the game saved so far.
	 */
	GamePlay savePhase(GamePlay game_state);

	/**
	 * This method is used for preparing the game state after STARTUP phase ends 
	 * and before REINFORCE begins.
	 * 
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 * @param file_name name of the file holding the state of the game.
	 * @return state of the game saved so far.
	 */
	GamePlay loadPhase(GamePlay game_state);	

}