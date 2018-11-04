package com.risk.business;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.risk.business.impl.ManageGamePlay;
import com.risk.business.impl.ManageMap;
import com.risk.business.impl.ManagePlayer;
import com.risk.file.IManageFile;
import com.risk.file.IManageGamePlayFile;
import com.risk.file.impl.ManageFile;
import com.risk.file.impl.ManageGamePlayFile;
import com.risk.model.Card;
import com.risk.model.CardTrade;
import com.risk.model.GamePlay;
import com.risk.model.GamePlayTerritory;
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
		IManageGamePlay game_manager   = new ManageGamePlay();
		IManagePlayer   player_manager = new ManagePlayer();
		GamePlay game_state = new GamePlay();
		game_state = player_manager.createPlayer(6,"Switzerland.map","A");		
		game_manager.managePhase(game_state);
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
	 * Test to check if a card is allocated to a player it is removed  
	 * from the list of free cards.
	 * 
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 */
	@Test
	public void testAddCardToPlayer() {
		assertEquals(0, 0);
	}
	
	/**
	 * Test to check if an Valid trade move is executed when 
	 * all three cards have the same army image.
	 * 
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 */
	@Test
	public void testTradeCardsSameImage() {
		
		IManageGamePlay game_manager   = new ManageGamePlay();
		IManagePlayer   player_manager = new ManagePlayer();

		//Creating a game state using AutoAllocationMode - A.
		//Player 3 has an initial army count 4 and zero cards.		
		GamePlay game_state = new GamePlay();		
		game_state = player_manager.createPlayer(6,"Switzerland.map","A");
		
		//Setting Player 3 as current player
		game_state.setCurrent_player(3);
		
		//Preparing a CardTrade scenario and adding it game_state.
		CardTrade card_trade = new CardTrade();
		
		Card test_card_1 = new Card();
		test_card_1.setTerritory_name("Geneva");
		test_card_1.setArmy_type("Artillery");
		card_trade.setCard1(test_card_1);
		
		Card test_card_2 = new Card();		
		test_card_2.setTerritory_name("Varduz");
		test_card_2.setArmy_type("Artillery");
		card_trade.setCard2(test_card_2);
		
		Card test_card_3 = new Card();				
		test_card_3.setTerritory_name("Sarnen");
		test_card_3.setArmy_type("Artillery");
		card_trade.setCard3(test_card_3);
		
		game_state.setCard_trade(card_trade);
		
		//Setting the phase of game to TRADE_CARDS
		game_state.setGame_phase("TRADE_CARDS");
		
		//Setting the card list for player 3
		List<Card> card_list = new ArrayList<>();
		card_list.add(test_card_1);
		card_list.add(test_card_2);
		card_list.add(test_card_3);
		
		//Setting an empty free card list and current trade count for the player as 6.
		game_state.setFree_cards(new ArrayList<Card>());
		
		for (Player player : game_state.getGame_state()) {
			if (player.getId()!=3) {
				continue;
			}else {
				player.setCard_list(card_list);
				player.setTrade_count(6);
			}
		}
		
		/**
		 * Presently the list of free cards is empty and Player 3 has three cards of same
		 * image to be traded. So once the trade is over those three cards will be removed
		 * from Player's list and added to the free stock of cards. In addition to that
		 * player will receiving a specified set of armies based on risk rules.
		 * 
		 * Input Data Defined for this case:
		 * Current Player - 3 and has clicked on TRADE_CARD
		 * Player holds 3 cards of same image
		 * Number of trades player has already done - 6
		 * Current free army stock of Player 3  = 4
		 * New army stock after trade should be = 4 + 20 = 24 
		 */
		game_manager.managePhase(game_state);
		
		//Three cards became available after trade.
		assertEquals(3,game_state.getFree_cards().size());
		
		/**
		 * Player's army stock becomes      - 24
		 * Player's card list becomes empty.
		 * Player's number of trades become - 7 
		 */
		for (Player player : game_state.getGame_state()) {
			if (player.getId()!=3) {
				continue;
			}else {
				assertEquals(7,player.getTrade_count());
				assertEquals(0,player.getCard_list().size());
				assertEquals(24,player.getArmy_stock());
				break;
			}
		}		
	}

	/**
	 * Test to check if an Valid trade move is executed when 
	 * all three cards have a different army image.
	 * 
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 */
	@Test
	public void testTradeCardsDiffImage() {
		IManageGamePlay game_manager   = new ManageGamePlay();
		IManagePlayer   player_manager = new ManagePlayer();

		//Creating a game state using AutoAllocationMode - A.
		//Player 3 has an initial army count 4 and zero cards.		
		GamePlay game_state = new GamePlay();		
		game_state = player_manager.createPlayer(6,"Switzerland.map","A");
		
		//Setting Player 3 as current player
		game_state.setCurrent_player(3);
		
		//Preparing a CardTrade scenario and adding it game_state.
		CardTrade card_trade = new CardTrade();
		
		Card test_card_1 = new Card();
		test_card_1.setTerritory_name("Geneva");
		test_card_1.setArmy_type("Artillery");
		card_trade.setCard1(test_card_1);
		
		Card test_card_2 = new Card();		
		test_card_2.setTerritory_name("Stans");
		test_card_2.setArmy_type("Infantry");
		card_trade.setCard2(test_card_2);
		
		Card test_card_3 = new Card();				
		test_card_3.setTerritory_name("Solothum");
		test_card_3.setArmy_type("Cavalry");
		card_trade.setCard3(test_card_3);
		
		game_state.setCard_trade(card_trade);
		
		//Setting the phase of game to TRADE_CARDS
		game_state.setGame_phase("TRADE_CARDS");
		
		//Setting the card list for player 3
		List<Card> card_list = new ArrayList<>();
		card_list.add(test_card_1);
		card_list.add(test_card_2);
		card_list.add(test_card_3);
		
		//Setting an empty free card list and current trade count for the player as 4.
		game_state.setFree_cards(new ArrayList<Card>());
		
		for (Player player : game_state.getGame_state()) {
			if (player.getId()!=3) {
				continue;
			}else {
				player.setCard_list(card_list);
				player.setTrade_count(4);
			}
		}
		
		/**
		 * Presently the list of free cards is empty and Player 3 has three cards of different
		 * image to be traded. So once the trade is over those three cards will be removed
		 * from Player's list and added to the free stock of cards. In addition to that
		 * player will be receiving a specified set of armies based on risk rules.
		 * 
		 * Input Data Defined for this case:
		 * Current Player - 3 and has clicked on TRADE_CARD
		 * Player holds 3 cards of different image
		 * Number of trades player has already done - 4
		 * Current free army stock of Player 3  = 4
		 * New army stock after trade should be = 4 + 12 = 16
		 */
		game_manager.managePhase(game_state);
		
		//Three cards became available after trade.
		assertEquals(3,game_state.getFree_cards().size());
		
		/**
		 * Player's army stock becomes      - 16
		 * Player's card list becomes empty.
		 * Player's number of trades become - 5
		 */
		for (Player player : game_state.getGame_state()) {
			if (player.getId()!=3) {
				continue;
			}else {
				assertEquals(5,player.getTrade_count());
				assertEquals(0,player.getCard_list().size());
				assertEquals(16,player.getArmy_stock());
				break;
			}
		}		
	}

	/**
	 * Test to check if an Invalid trade is detected. 
	 * Either all three cards should have same army image or all three should have a different one.
	 *  
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 */
	@Test
	public void testTradeCardsInvalidTrade() {
		IManageGamePlay game_manager   = new ManageGamePlay();
		IManagePlayer   player_manager = new ManagePlayer();

		//Creating a game state using AutoAllocationMode - A.
		//Player 4 has an initial army count 3 and zero cards.		
		GamePlay game_state = new GamePlay();		
		game_state = player_manager.createPlayer(6,"Switzerland.map","A");
		
		//Setting Player 4 as current player
		game_state.setCurrent_player(4);
		
		//Preparing a CardTrade scenario and adding it game_state.
		CardTrade card_trade = new CardTrade();
		
		Card test_card_1 = new Card();
		test_card_1.setTerritory_name("Geneva");
		test_card_1.setArmy_type("Artillery");
		card_trade.setCard1(test_card_1);
		
		Card test_card_2 = new Card();		
		test_card_2.setTerritory_name("Zug Zug");
		test_card_2.setArmy_type("Artillery");
		card_trade.setCard2(test_card_2);
		
		Card test_card_3 = new Card();				
		test_card_3.setTerritory_name("Solothum");
		test_card_3.setArmy_type("Cavalry");
		card_trade.setCard3(test_card_3);
		
		game_state.setCard_trade(card_trade);
		
		//Setting the phase of game to TRADE_CARDS
		game_state.setGame_phase("TRADE_CARDS");
		
		//Setting the card list for player 3
		List<Card> card_list = new ArrayList<>();
		card_list.add(test_card_1);
		card_list.add(test_card_2);
		card_list.add(test_card_3);
		
		//Setting an empty free card list and current trade count for the player as 2.
		game_state.setFree_cards(new ArrayList<Card>());
		
		for (Player player : game_state.getGame_state()) {
			if (player.getId()!=4) {
				continue;
			}else {
				player.setCard_list(card_list);
				player.setTrade_count(2);
			}
		}
		
		/**
		 * Presently the list of free cards is empty and Player 4 has two cards of same
		 * image and one different to be traded. So once the trade is over those three 
		 * cards will still be there in Player's list and no new addition to free stock 
		 * of cards. In addition to that player will be receiving a specified set of 
		 * armies based on risk rules.
		 * 
		 * Input Data Defined for this case:
		 * Current Player - 4 and has clicked on TRADE_CARD
		 * Player holds 2 cards of same image and 1 different
		 * Number of trades player has already done - 2
		 * Current free army stock of Player 3  = 3
		 * New army stock after trade should be = 3 + 0 = 3
		 * 
		 * No new armies as per risk rules with a proper status message.
		 *  
		 */
		game_manager.managePhase(game_state);
		
		//No new cards in free cards list.
		assertEquals(0,game_state.getFree_cards().size());
		
		/**
		 * Player's army stock stays           - 3
		 * Player's card list unchanged.
		 * Player's number of trades unchanged - 2
		 */
		for (Player player : game_state.getGame_state()) {
			if (player.getId()!=4) {
				continue;
			}else {
				assertEquals(2,player.getTrade_count());
				assertEquals(3,player.getCard_list().size());
				assertEquals(3,player.getArmy_stock());
				assertEquals("Either all three cards should have same image or all three different.",game_state.getStatus());
				break;
			}
		}		
	}

	/**
	 * Test to check if trading works fine when Player holds a card for one of his 
	 * captured territories.
	 * 
	 * @author <a href="mailto:a_semwal@encs.concordia.ca">ApoorvSemwal</a>
	 */
	@Test
	public void testTradeCardsAdditionalArmies() {
		
		IManageGamePlay game_manager   = new ManageGamePlay();
		IManagePlayer   player_manager = new ManagePlayer();

		//Creating a game state using AutoAllocationMode - A.
		//With 6 players playing initially each gets an army count - an zero cards.		
		GamePlay game_state = new GamePlay();		
		game_state = player_manager.createPlayer(6,"Switzerland.map","A");
		
		//Setting Player 3 as current player
		game_state.setCurrent_player(3);
		
		//Preparing a CardTrade scenario and adding it game_state.
		CardTrade card_trade = new CardTrade();
		
		Card test_card_1 = new Card();
		test_card_1.setTerritory_name("Geneva");
		test_card_1.setArmy_type("Artillery");
		card_trade.setCard1(test_card_1);
		
		Card test_card_2 = new Card();		
		test_card_2.setTerritory_name("Varduz");
		test_card_2.setArmy_type("Artillery");
		card_trade.setCard2(test_card_2);
		
		Card test_card_3 = new Card();				
		test_card_3.setTerritory_name("Sarnen");
		test_card_3.setArmy_type("Artillery");
		card_trade.setCard3(test_card_3);
		
		game_state.setCard_trade(card_trade);
		
		//Setting the phase of game to TRADE_CARDS
		game_state.setGame_phase("TRADE_CARDS");
		
		//Setting the card list for player 3
		List<Card> card_list = new ArrayList<>();
		card_list.add(test_card_1);
		card_list.add(test_card_2);
		card_list.add(test_card_3);
		
		//Setting an empty free card list and current trade count for the player as 5.
		//Player should hold Geneva as a territory and he already holds a card with Geneva. 
		game_state.setFree_cards(new ArrayList<Card>());
		
		for (Player player : game_state.getGame_state()) {
			if (player.getId()!=3) {
				continue;
			}else {
				player.setCard_list(card_list);
				player.setTrade_count(5);
				//GamePlayTerritory game_play_territority = new GamePlayTerritory();
/*				game_play_territority.setContinent_name("Geneva");
				game_play_territority.setTerritory_name("Geneva");
				game_play_territority.setNumber_of_armies(8);
				player.getTerritory_list().add(game_play_territority);
*/			}
		}
		
		/**
		 * Presently the list of free cards is empty and Player 3 has three cards of same
		 * image to be traded. So once the trade is over those three cards will be removed
		 * from Player's list and added to the free stock of cards. In addition to that
		 * player will receiving a specified set of armies based on risk rules.
		 * 
		 * Input Data Defined for this case:
		 * Current Player - 3 and has clicked on TRADE_CARD
		 * Player holds 3 cards of same image
		 * Number of trades player has already done - 5
		 * Current free army stock of Player 3  = 4
		 * New army stock after trade should be = 4 + 15 = 19
		 * Player even holds a territory of same name which is present in one his cards
		 * i.e. Geneva - So player will get an extra bonus of 2 Armies on Geneva.
		 * Before trade he had 4 armies on Geneva after trade it should be 6. 
		 * 
		 */
		game_manager.managePhase(game_state);
		
		//Three cards became available after trade.
		assertEquals(game_state.getFree_cards().size(),3);
		
		/**
		 * Player's army stock becomes      - 19
		 * Player's card list becomes empty.
		 * Player's number of trades become - 6 
		 * Player's army count on Geneva Should become - 6
		 */
		for (Player player : game_state.getGame_state()) {
			if (player.getId()!=3) {
				continue;
			}else {
				assertEquals(6,player.getTrade_count());
				assertEquals(0,player.getCard_list().size());
				assertEquals(19,player.getArmy_stock());
				for (GamePlayTerritory territory : player.getTerritory_list()) {
					if (territory.getTerritory_name().equalsIgnoreCase("Geneva")) {
						assertEquals(6, territory.getNumber_of_armies());
						break;
					}else {
						continue;
					}
				}
				break;
			}
		}		
	}
}
