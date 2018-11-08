package com.risk.business;

import com.risk.model.GamePlay;

public interface IManageDomination {

	/**
	 * This function is use to show updated domination view of each player in game
	 * i.e Total Armies player have in map,Map Coverage by Player
	 * 
	 * @param gameplay Current state of entire Game.
	 * @return Updated Game State Object.
	 */
	GamePlay dominationView(GamePlay game_play);
}
