package com.risk.file.impl;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

import org.springframework.stereotype.Service;

import com.risk.file.IManageGamePlayFile;
import com.risk.model.file.PlayerFile;

/**
 * @author <a href="mailto:himansipatel1994@gmail.com">Himansi Patel</a>
 *
 */
@Service
public class ManageGamePlayFile implements IManageGamePlayFile {

	/**
	 * @author <a href="mailto:himansipatel1994@gmail.com">Himansi Patel</a>
	 * @see com.risk.file.IManageGamePlayFile#savePlayerInfoToDisk(java.util.List,
	 *      java.lang.String)
	 */
	public Boolean savePlayerInfoToDisk(List<PlayerFile> file_player_list, String file_name) {

		Boolean file_writer_message = false;
		try (PrintStream player_file_writer = new PrintStream(
				new BufferedOutputStream(new FileOutputStream("abc.txt")))) {
			// System.out.println("Map Name="+file_name);
			// player_file_writer.println("Map Name="+file_name);
			for (int i = 0; i < file_player_list.size(); i++) {
				player_file_writer.println("[Player]");
				player_file_writer.println("Id=" + file_player_list.get(i).getId());
				player_file_writer.println("Name=" + file_player_list.get(i).getName());
				player_file_writer.println("Armi Stock=" + file_player_list.get(i).getId());
				player_file_writer.println("[Territories]");
//				This for loop is used to write territories occupied by each player
				for (int j = 0; j < file_player_list.get(i).getTerritory_list().size(); j++)
					player_file_writer.println(file_player_list.get(i).getTerritory_list().get(j).getTerritory_name()
							+ "," + file_player_list.get(i).getTerritory_list().get(j).getContinent_name() + ","
							+ file_player_list.get(i).getTerritory_list().get(j).getNumber_of_armies());
				player_file_writer.println();
				player_file_writer.println("[Cards]");
//				This for loop is used to write cards occupied by each player
				for (int k = 0; k < file_player_list.get(i).getCard_list().size(); k++)
					player_file_writer.println(file_player_list.get(i).getCard_list().get(k).getTerritory_name() + ","
							+ file_player_list.get(i).getCard_list().get(k).getArmy_type());
				player_file_writer.println();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file_writer_message;
	}
}