package com.example.yo_flunk;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

import java.util.ArrayList;
import java.util.List;

public class Utility {

    public static int computeNumGames(ArrayList<ArrayList> allGames){
        int numGames = 0;
        for(int j=0; j<allGames.size(); j++){
            numGames = numGames + allGames.get(j).size();
        }
        return numGames;
    }

    public static ArrayList<String> findPlayersOfTeam(String teamName, ArrayList<String> teamNames, ArrayList<String> players1, ArrayList<String> players2){
        for(int index=0; index<teamNames.size(); index++){
            if(teamNames.get(index).equals(teamName)){
                ArrayList<String> playersInTeam = new ArrayList<>();
                playersInTeam.add(players1.get(index));
                playersInTeam.add(players2.get(index));
                return playersInTeam;
            }
        }
        return null;
    }
    public static boolean checkIfValid(String str, ArrayList<String> players1, ArrayList<String> players2){
        String[] forbiddenStrings = {".", "$", "[", "]", "#", "/"};
        for (String forbiddenString : forbiddenStrings){
            if(str.contains(forbiddenString)){
                return false;
            }
        }

        // check whether this name is already used
        if(players1.contains(str)){
            return false;
        }
        if(players2.contains(str)){
            return false;
        }
        return true;
    }
    public static int computeNumTobeplayed(ArrayList<ArrayList> allGamesPlayed) {
        // Computes the total number of games in a Tournament
        int num = 0;
        for(ArrayList<Boolean> played_group:allGamesPlayed){
            for(Boolean played:played_group){
                if(played==false){
                    num = num + 1;
                }
            }
        }
        return num;
    }


    public static int dp2pix(float dp, Context context) {
        // context: getApplicationContext()
        Resources r = context.getResources();
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );
        return (px);
    }


    public static ArrayList<Integer> computeElo(Integer eloTeam1, Integer eloTeam2, boolean team1win, boolean knechtung){
        int K = 16;
        int updateElo1;
        int updateElo2;
        float expectation = (float) (1 / ( 1 + Math.pow(10, (eloTeam2 - eloTeam1) / 100)));
        if(team1win){
            if(knechtung){
                updateElo1 = (int) Math.round(K * (1.5 - expectation));
                updateElo2 = (int) Math.round(K * (-1.5*(1-expectation)));
            }
            else{
                updateElo1 = (int) Math.round(K * (1. - expectation));
                updateElo2 = (int) Math.round(K * (-1*(1-expectation)));
            }
        }
        else{
            if(knechtung){
                updateElo2 = (int) Math.round(K * (1.5 -(1-expectation)));
                updateElo1 = (int) Math.round(K * (-1.5*expectation));
            }
            else{
                updateElo2 = (int) Math.round(K * (1. - (1-expectation)));
                updateElo1 = (int) Math.round(K * (-1*expectation));
            }
        }
        List<Integer> eloUpdates = new ArrayList<Integer>();
        eloUpdates.add(0, updateElo1);
        eloUpdates.add(1, updateElo2);
        return (ArrayList<Integer>) eloUpdates;
    }
}
