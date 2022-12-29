// Helper Class for Player-Objects in regular Games

package com.example.yo_flunk;

public class UserHelperClass {

    String name, motto, favBeer;
    Integer numWins, numLoses, numKnechtungen, numWasKnechtet, numTreffer, numStrafbier, numStrafrunden, elo ;
    int headId, bodyId;

    public UserHelperClass() {
    }

    public UserHelperClass(String name, String motto, String favBeer, int head_id, int bodyId) {
        this.name = name;
        this.motto = motto;
        this.favBeer = favBeer;
        this.headId = head_id;
        this.bodyId = bodyId;
        this.numWins = 0;
        this.numLoses = 0;
        this.numKnechtungen = 0;
        this.numWasKnechtet = 0;
        this.numTreffer = 0;
        this.numStrafbier = 0;
        this.numStrafrunden = 0;
        this.elo = 250;
    }

    public Integer getNumStrafbier() {
        return numStrafbier;
    }

    public void setNumStrafbier(Integer numStrafbier) {
        this.numStrafbier = numStrafbier;
    }

    public Integer getNumStrafrunden() {
        return numStrafrunden;
    }

    public void setNumStrafrunden(Integer numStrafrunden) {
        this.numStrafrunden = numStrafrunden;
    }

    public int getHeadId() {
        return headId;
    }

    public void setHeadId(int headId) {
        this.headId = headId;
    }

    public int getBodyId() {
        return bodyId;
    }

    public void setBodyId(int bodyId) {
        this.bodyId = bodyId;
    }

    public Integer getNumTreffer() {
        return numTreffer;
    }

    public void setNumTreffer(Integer numTreffer) {
        this.numTreffer = numTreffer;
    }

    public Integer getNumWins() {
        return numWins;
    }

    public void setNumWins(Integer numWins) {
        this.numWins = numWins;
    }

    public Integer getNumLoses() {
        return numLoses;
    }

    public void setNumLoses(Integer numLoses) {
        this.numLoses = numLoses;
    }

    public Integer getNumKnechtungen() {
        return numKnechtungen;
    }

    public void setNumKnechtungen(Integer numKnechtungen) {
        this.numKnechtungen = numKnechtungen;
    }

    public Integer getNumWasKnechtet() {
        return numWasKnechtet;
    }

    public void setNumWasKnechtet(Integer numWasKnechtet) {
        this.numWasKnechtet = numWasKnechtet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    public String getFavbier() {
        return this.favBeer;
    }

    public void setFavbier(String favBeer) {
        this.favBeer = favBeer;
    }

    public void setElo(Integer elo) {
        this.elo = elo;
    }

    public Integer getElo(){
        return this.elo;
    }


}
