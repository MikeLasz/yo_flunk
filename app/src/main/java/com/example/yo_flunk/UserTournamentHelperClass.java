// Helper Class for Player-Objects in Tournaments

package com.example.yo_flunk;

public class UserTournamentHelperClass {

    int numTreffer, numStrafbier, numStrafschluck;
    String teamName, name;

    public UserTournamentHelperClass() {
    }



    public UserTournamentHelperClass(String name, String teamName, int numTreffer, int numStrafbier, int numStrafschluck){
        this.teamName = teamName;
        this.name = name;
        this.numTreffer = numTreffer;
        this.numStrafbier = numStrafbier;
        this.numStrafschluck = numStrafschluck;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumTreffer() {
        return numTreffer;
    }

    public void setNumTreffer(int numTreffer) {
        this.numTreffer = numTreffer;
    }

    public int getNumStrafbier() {
        return numStrafbier;
    }

    public void setNumStrafbier(int numStrafbier) {
        this.numStrafbier = numStrafbier;
    }

    public int getNumStrafschluck() {
        return numStrafschluck;
    }

    public void setNumStrafschluck(int numStrafschluck) {
        this.numStrafschluck = numStrafschluck;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
