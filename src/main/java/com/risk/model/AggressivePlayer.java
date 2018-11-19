package com.risk.model;

import com.risk.business.AbstractPlayer;
import com.risk.business.impl.ManagePlayer;

/**
 * This Player Model represents a Human Player in terms of Strategy-Pattern implementation,
 * during our GamePlay.
 * 
 * @author <a href="mailto:apoorv.semwal20@gmail.com">Apoorv Semwal</a>
 * @version 0.0.1
 */
public class AggressivePlayer extends AbstractPlayer {
	/**
	 * Reinforcement of an Aggressive Player.
	 * @see com.risk.business.IAbstractPLayer#reinforce(GamePlay)
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 */
	public GamePlay reinforce(GamePlay game_play) {
		ManagePlayer manage_player = new ManagePlayer();
		game_play = manage_player.reinforce(game_play);
		return game_play;
	}

	/**
	 * Attack of an Aggressive Player.
	 * @see com.risk.business.IAbstractPLayer#attack(GamePlay)
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 */
	public GamePlay attack(GamePlay game_play) {
		ManagePlayer manage_player = new ManagePlayer();
		game_play = manage_player.attack(game_play);
		return game_play;
	}

	/**
	 * Fortify of an Aggressive Player.
	 * @see com.risk.business.IAbstractPLayer#fortify(GamePlay)
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 */
	public GamePlay fortify(GamePlay game_play) {
		ManagePlayer manage_player = new ManagePlayer();
		game_play = manage_player.fortify(game_play);
		return game_play;
	}
}
