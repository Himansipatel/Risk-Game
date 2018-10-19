package com.risk.business;

import org.junit.Before;
import org.junit.Test;

import com.risk.business.impl.ManageGamePlay;
import com.risk.model.GamePlay;

/**
 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
 * @version 0.0.1
 */
public class ManageGamePlayTest {

	private static IManageGamePlay manageGamePlay;

	@Before
	public void initMapManager() {
		manageGamePlay = new ManageGamePlay();
	}

	/**
	 * Test to check if army stock allocation for reinforcement phase is working fine.
	 *  
	 * @see com.risk.business.IManageGamePlay#savePhase(com.risk.model.GamePlay)
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 */
	@Test
	public void testSavePhase() {
		//GamePlay game_state = new game_state();
		//manageGamePlay.savePhase(game_state);
	}

}