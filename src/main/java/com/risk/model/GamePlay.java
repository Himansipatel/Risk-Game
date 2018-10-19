package com.risk.model;

import java.util.List;

/**
* This class represents the Game Play state at any point of time.
* @author Zinnia Rana</a>
* @version 0.0.1
*/

public class GamePlay {
    
    private List<Player> game_state;
    private String file_name;
    private String game_phase;
    
    public List<Player> getGame_state() {
        return game_state;
    }
    public void setGame_state(List<Player> game_state) {
        this.game_state = game_state;
    }
    public String getFile_name() {
        return file_name;
    }
    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }
    public String getGame_phase() {
        return game_phase;
    }
    public void setGame_phase(String game_phase) {
        this.game_phase = game_phase;
    }
}