package com.risk.file.impl;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

import org.springframework.stereotype.Service;

import com.risk.file.IManageGamePlayFile;
import com.risk.model.file.PlayerFile;

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


	/**
	 * @author <a href="mailto:himansipatel1994@gmail.com">Himansi Patel</a>
	 * @author <a href="mailto:mayankjariwala1994@gmail.com">Mayank Jariwala</a>
	 * @see com.risk.file.IManageGamePlayFile#savePlayerInfoToDisk(java.util.List,
	 *      java.lang.String)
	 */
	public Boolean savePlayerInfoToDisk(List<PlayerFile> file_player_list, String file_name) {

		boolean file_writer_message = false;
		file_name = (file_name.endsWith(".map") ? file_name.split("\\.")[0] : file_name) + "_"
				+ String.valueOf(System.currentTimeMillis());
		try (PrintStream player_file_writer = new PrintStream(
				new BufferedOutputStream(new FileOutputStream("src/main/resource/gameplay/" + file_name + ".txt")))) {
			player_file_writer.println("Map name=" + file_name);
			player_file_writer.println();
//			Fetching all Current Players Object Playing in the Game
			for (int player_index = 0; player_index < file_player_list.size(); player_index++) {
				player_file_writer.println("[Player]");
				player_file_writer.println("Id=" + file_player_list.get(player_index).getId());
				player_file_writer.println("Name=" + file_player_list.get(player_index).getName());
				player_file_writer.println("Armies Stock=" + file_player_list.get(player_index).getId());
				player_file_writer.println("[Territories]");
//				Getting territories occupied by each player and writing it to file
				for (int j = 0; j < file_player_list.get(player_index).getTerritory_list().size(); j++)
					player_file_writer.println(file_player_list.get(player_index).getTerritory_list().get(j)
							.getTerritory_name() + ","
							+ file_player_list.get(player_index).getTerritory_list().get(j).getContinent_name() + ","
							+ file_player_list.get(player_index).getTerritory_list().get(j).getNumber_of_armies());
				player_file_writer.println();
				player_file_writer.println("[Cards]");
//				Getting cards occupied by each player and writing it to file
				for (int k = 0; k < file_player_list.get(player_index).getCard_list().size(); k++)
					player_file_writer
							.println(file_player_list.get(player_index).getCard_list().get(k).getTerritory_name() + ","
									+ file_player_list.get(player_index).getCard_list().get(k).getArmy_type());
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
}