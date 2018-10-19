package com.risk.business;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.risk.business.impl.ManageGamePlay;
import com.risk.business.impl.ManageMap;
import com.risk.file.IManageFile;
import com.risk.file.IManageGamePlayFile;
import com.risk.file.impl.ManageFile;
import com.risk.file.impl.ManageGamePlayFile;
import com.risk.model.GamePlay;
import com.risk.model.Map;
import com.risk.model.Player;
import com.risk.model.file.File;

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
	 * Test to check if army stock allocation for reinforcement phase is working
	 * fine.
	 * 
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 */
	@Test
	public void testCalculateArmiesReinforce() {

		IManageGamePlayFile game_file = new ManageGamePlayFile();
		GamePlay game_state = new GamePlay();
		game_state = game_file.fetchGameState("India_Reinforce_Test.txt");

		IManageMap map_manager = new ManageMap();
		String[] file_name_construct = game_state.getFile_name().split("_");
		String[] file_name = file_name_construct[0].split("=");
		IManageFile file_manager = new ManageFile(file_name[1].concat(".map"));
		File file = file_manager.retreiveFileObject();
		Map map = map_manager.convertFileToMap(file);

		List<Player> game_state_new = manageGamePlay.calculateArmiesReinforce(game_state.getGame_state(), map);

		// Player 1 captures entire "southern states" Continent - Score = 5.
		// Initial army stock 20 - New Stock after calculation = 35.
		assertEquals(35, game_state_new.get(0).getArmy_stock());

		// Player 2 captures entire "north east" Continent - Score = 5.
		// Initial army stock 10 - New Stock after calculation = 19.
		assertEquals(19, game_state_new.get(1).getArmy_stock());
	}

}
