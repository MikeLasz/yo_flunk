// Helper Class for Team-Objects in Tournaments

package com.example.yo_flunk;

public class TeamsHelperClass {

    String teamName, player1, player2, teamBeer;
    int group, winsGroup, defeatsGroup;

    public TeamsHelperClass(){
    }

    public TeamsHelperClass(String teamName, String player1, String player2, String teamBeer, int group, int winsGroup, int defeatsGroup ){
        this.teamName = teamName;
        this.player1 = player1;
        this.player2 = player2;
        this.teamBeer = teamBeer;
        this.group = group;
        this.winsGroup = winsGroup;
        this.defeatsGroup = defeatsGroup;
    }


    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public String getTeamBeer() {
        return teamBeer;
    }

    public void setTeamBeer(String teamBeer) {
        this.teamBeer = teamBeer;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public int getWinsGroup() {
        return winsGroup;
    }

    public void setWinsGroup(int winsGroup) {
        this.winsGroup = winsGroup;
    }

    public int getDefeatsGroup() {
        return defeatsGroup;
    }

    public void setDefeatsGroup(int defeatsGroup) {
        this.defeatsGroup = defeatsGroup;
    }
}
