package com.risk.file.impl;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.risk.file.IManageGamePlayFile;
import com.risk.model.Card;
import com.risk.model.GamePlay;
import com.risk.model.GamePlayTerritory;
import com.risk.model.Player;

/**
 * This class is use to Manage Game Play Information including map name ,
 * players information and their state
 * 
 * @author <a href="mailto:himansipatel1994@gmail.com">Himansi Patel</a>
 * @author <a href="mailto:mayankjariwala1994@gmail.com">Mayank Jariwala</a> -
 *         Added Class Description , Logger and checkError Function
 * @version 0.0.1
 */
@Service
public class ManageGamePlayFile implements IManageGamePlayFile {

	private List<GamePlayTerritory> player_territory_info;
	private List<Card> player_card_info;
	private Player player_game_info;

	/**
	 * This function is use to store status of player during game.
	 * 
	 * @author <a href="mailto:himansipatel1994@gmail.com">Himansi Patel</a>
	 * @author <a href="mailto:mayankjariwala1994@gmail.com">Mayank Jariwala</a>
	 * @see com.risk.file.IManageGamePlayFile#savePlayerInfoToDisk(java.util.List,
	 *      java.lang.String)
	 */
	public Boolean saveGameStateToDisk(GamePlay gamePlay) {
		boolean file_writer_message = false;
		String file_name = gamePlay.getFile_name();
		String game_phase = gamePlay.getGame_phase();
		List<Player> player_list = gamePlay.getGame_state();
		try (PrintStream player_file_writer = new PrintStream(
				new BufferedOutputStream(new FileOutputStream("src/main/resource/gameplay/" + file_name + ".txt")))) {
			player_file_writer.println("Map name=" + file_name);
			player_file_writer.println("Phase=" + game_phase);
			player_file_writer.println();
//			Fetching all Current Players Object Playing in the Game
			for (int player_index = 0; player_index < player_list.size(); player_index++) {
				player_file_writer.println("[Player]");
				player_file_writer.println("Id=" + player_list.get(player_index).getId());
				player_file_writer.println("Name=" + player_list.get(player_index).getName());
				player_file_writer.println("Armies Stock=" + player_list.get(player_index).getId());
				player_file_writer.println("[Territories]");
//				Getting territories occupied by each player and writing it to file
				for (int j = 0; j < player_list.get(player_index).getTerritory_list().size(); j++)
					player_file_writer
							.println(player_list.get(player_index).getTerritory_list().get(j).getTerritory_name() + ","
									+ player_list.get(player_index).getTerritory_list().get(j).getContinent_name() + ","
									+ player_list.get(player_index).getTerritory_list().get(j).getNumber_of_armies());
				player_file_writer.println();
				player_file_writer.println("[Cards]");
//				Getting cards occupied by each player and writing it to file
				for (int k = 0; k < player_list.get(player_index).getCard_list().size(); k++)
					player_file_writer.println(player_list.get(player_index).getCard_list().get(k).getTerritory_name()
							+ "," + player_list.get(player_index).getCard_list().get(k).getArmy_type());
				player_file_writer.println();
			}
			if (player_file_writer.checkError()) {
				file_writer_message = false;
			} else {
				file_writer_message = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file_writer_message;
	}

	/**
	 * This function is use to load the state of ongoing and saved state of game.
	 * 
	 * @author <a href="mayankjariwala1994@gmail.com"> Mayank Jariwala </a>
	 * @see com.risk.file.IManageGamePlayFile#loadGameState(java.lang.String)
	 */
	public GamePlay fetchGameState(String file_name) {
		List<Player> players_list = new ArrayList<>();
		GamePlay game_play = new GamePlay();
		try (BufferedReader game_file_reader = new BufferedReader(
				new FileReader("src/main/resource/gameplay/" + file_name))) {
			// current_section : Single Player Game Play Information Section Identifier
			String file_line, current_section = "start";
			String map_name = game_file_reader.readLine();
			String game_phase = game_file_reader.readLine().split("=")[1];
			while ((file_line = game_file_reader.readLine()) != null) {
				if (file_line.length() > 0) {
					if (file_line.equalsIgnoreCase("[Player]")) {
						player_game_info = new Player();
						current_section = "player";
						file_line = game_file_reader.readLine();
					}
					if (file_line.equalsIgnoreCase("[Territories]")) {
						player_territory_info = new ArrayList<GamePlayTerritory>();
						current_section = "territories";
						file_line = game_file_reader.readLine();
					}
					if (file_line.equalsIgnoreCase("[Cards]")) {
						player_card_info = new ArrayList<Card>();
						current_section = "cards";
						file_line = game_file_reader.readLine();
					}
					if (current_section.equals("player")) {
						setPlayerBasicInfo(file_line);
					}
					if (current_section.equals("territories")) {
						setPlayerTerritoryInfo(player_game_info, file_line);
					}
					if (current_section.equals("cards")) {
						if (file_line.length() <= 0) {
							current_section = "";
							player_game_info.setTerritory_list(player_territory_info);
							player_game_info.setCard_list(player_card_info);
							players_list.add(player_game_info);
						}
					}
				}
			}

			game_play.setFile_name(map_name);
			game_play.setGame_state(players_list);
			game_play.setGame_phase(game_phase);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return game_play;
	}

	/**
	 * This function is use to set each player basic information such as id,name,no.
	 * of armies
	 * 
	 * @param player_game_info
	 * @param file_line
	 */
	private void setPlayerBasicInfo(String file_line) {
		String key = file_line.split("=")[0];
		String value = file_line.split("=")[1];
		if (key.equalsIgnoreCase("Id")) {
			player_game_info.setId(Integer.parseInt(value));
		} else if (key.equalsIgnoreCase("Name")) {
			player_game_info.setName(value);
		} else if (key.equalsIgnoreCase("Armies")) {
			player_game_info.setArmy_stock(Integer.parseInt(value));
		}
	}

	/**
	 * This function is use to set player territory information.
	 * 
	 * @param player_game_info
	 * @param file_line
	 */
	private void setPlayerTerritoryInfo(Player player_game_info, String file_line) {
		if (file_line.length() > 0) {
			GamePlayTerritory gamePlayTerritory = new GamePlayTerritory();
			String[] territory_info = file_line.split(",");
			gamePlayTerritory.setTerritory_name(territory_info[0]);
			gamePlayTerritory.setContinent_name(territory_info[1]);
			gamePlayTerritory.setNumber_of_armies(Integer.parseInt(territory_info[2]));
			player_territory_info.add(gamePlayTerritory);
		}
	}
}