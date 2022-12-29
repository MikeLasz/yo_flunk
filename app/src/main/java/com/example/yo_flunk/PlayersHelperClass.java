package com.example.yo_flunk;

import java.util.ArrayList;

public class PlayersHelperClass {
    ArrayList<String> all_players = new ArrayList<String>();

    public ArrayList<String> GetAllPlayers() {
        return all_players;
    }

    public void AddPlayer(String player) {
        this.all_players.add(player);
    }

    public void RemoveAllPlayers(){
        this.all_players = new ArrayList<>();
    }
}
