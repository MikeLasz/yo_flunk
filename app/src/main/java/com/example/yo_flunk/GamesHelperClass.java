// Helper Class for Game-Objects in regular Games

package com.example.yo_flunk;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Date;

public class GamesHelperClass {

    ArrayList<String> team1Players, team2Players;
    ArrayList<Integer> numTreffer1, numTreffer2, strafbier1, strafbier2, strafrunde1, strafrunde2;
    Boolean team1Win, knechtung;
    String geotag;
    Date timeStart, timeEnd;

    public GamesHelperClass() {
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public GamesHelperClass(ArrayList<String> team1Players, ArrayList<String> team2Players,
                            ArrayList<Integer> numTreffer1, ArrayList<Integer> numTreffer2,
                            Date timeStart, Date timeEnd) {

        this.team1Players = team1Players;
        this.team2Players = team2Players;
        int num_players1 = team1Players.size();
        int num_players2 = team2Players.size();
        this.numTreffer1 = numTreffer1;
        this.numTreffer2 = numTreffer2;
        this.strafbier1 = new ArrayList<Integer>();
        this.strafbier2 = new ArrayList<Integer>();
        this.strafrunde1 = new ArrayList<Integer>();
        this.strafrunde2 = new ArrayList<Integer>();
        this.team1Win = true;
        this.knechtung = false;
        this.geotag = "geotag";
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }

    public ArrayList<String> getTeam1Players() {
        return team1Players;
    }

    public void setTeam1Players(ArrayList<String> team1Players) {
        this.team1Players = team1Players;
    }

    public ArrayList<String> getTeam2Players() {
        return team2Players;
    }

    public void setTeam2Players(ArrayList<String> team2Players) {
        this.team2Players = team2Players;
    }

    public ArrayList<Integer> getNumTreffer1() {
        return numTreffer1;
    }

    public void setNumTreffer1(ArrayList<Integer> numTreffer1) {
        this.numTreffer1 = numTreffer1;
    }

    public ArrayList<Integer> getNumTreffer2() {
        return numTreffer2;
    }

    public void setNumTreffer2(ArrayList<Integer> numTreffer2) {
        this.numTreffer2 = numTreffer2;
    }

    public ArrayList<Integer> getStrafbier1() {
        return strafbier1;
    }

    public void setStrafbier1(ArrayList<Integer> strafbier1) {
        this.strafbier1 = strafbier1;
    }

    public ArrayList<Integer> getStrafbier2() {
        return strafbier2;
    }

    public void setStrafbier2(ArrayList<Integer> strafbier2) {
        this.strafbier2 = strafbier2;
    }

    public ArrayList<Integer> getStrafrunde1() {
        return strafrunde1;
    }

    public void setStrafrunde1(ArrayList<Integer> strafrunde1) {
        this.strafrunde1 = strafrunde1;
    }

    public ArrayList<Integer> getStrafrunde2() {
        return strafrunde2;
    }

    public void setStrafrunde2(ArrayList<Integer> strafrunde2) {
        this.strafrunde2 = strafrunde2;
    }

    public Boolean getTeam1Win() {
        return team1Win;
    }

    public void setTeam1Win(Boolean team1Win) {
        this.team1Win = team1Win;
    }

    public Boolean getKnechtung() {
        return knechtung;
    }

    public void setKnechtung(Boolean knechtung) {
        this.knechtung = knechtung;
    }

    public String getGeotag() {
        return geotag;
    }

    public void setGeotag(String geotag) {
        this.geotag = geotag;
    }

    public Date getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }
}