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
	
	//Test Cases for Reinforcement Phase.
	/**
	 * Test to check if automatic allocation in reinforcement phase is working fine.
	 * 
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 */
	@Test
	public void testReinforceAutoAllocate() {
		assertEquals(0, 0);
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

	//Test Cases for Trading Cards.
	/**
	 * Test to check if an Valid trade move is executed when 
	 * all three cards have the same army image.
	 * 
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 */
	@Test
	public void testTradeCardsSameImage() {
		assertEquals(0,0);
	}

	/**
	 * Test to check if an Valid trade move is executed when 
	 * all three cards have a different army image.
	 * 
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 */
	@Test
	public void testTradeCardsDiffImage() {
		assertEquals(0,0);
	}

	/**
	 * Test to check if an Invalid trade is detected. 
	 * Either all three cards should have same army image or all three should have a different one.
	 *  
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 */
	@Test
	public void testTradeCardsInvalidTrade() {
		assertEquals(0,0);
	}

	/**
	 * Test to check if trading works fine when Player holds a card for one of his 
	 * captured territories.
	 * 
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 */
	@Test
	public void testTradeCardsAdditionalArmies() {
		assertEquals(0,0);
	}

}
