package com.example.yo_flunk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    FirebaseDatabase rootNode;
    DatabaseReference referenceUsers, referenceHighlights, referenceGames, referenceAllTournaments;
    String league, linkDatabase, activeTournamentName;
    ArrayList<String> namePlayers, datesGames, geoGames;
    ArrayList<Integer> hitsPlayers, lossesPlayers, winsPlayers, headIdPlayers, eloPlayers;
    int maxIdHighlights, numPlayers, numGames, activeTournamentNumGroups;
    boolean activeTournament = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linkDatabase = this.getResources().getString(R.string.firebase_link);


        boolean dev_database = this.getResources().getBoolean(R.bool.dev_version);
        if (dev_database){
            league = this.getResources().getString(R.string.league);
        }
        else{
            league = this.getResources().getString(R.string.league_prod);
        }

        TextView txtViewLeague = findViewById(R.id.txtView_league);
        String leagueName = this.getResources().getString(R.string.league_name);
        txtViewLeague.setText(leagueName);

        rootNode = FirebaseDatabase.getInstance(linkDatabase);
        referenceUsers = rootNode.getReference(league + "/users");
        referenceHighlights = rootNode.getReference(league + "/highlights");
        referenceGames = rootNode.getReference(league + "/games");
        // get id of latest highlight
        referenceHighlights.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    maxIdHighlights = (int) dataSnapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        // Populate Arrays with data of all the players in the data base
        referenceUsers.orderByChild("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                numPlayers = (int) dataSnapshot.getChildrenCount();
                ArrayList<String> namePlayersTmp = new ArrayList<String>(numPlayers);
                ArrayList<Integer> winsPlayersTmp = new ArrayList<Integer>(numPlayers);
                ArrayList<Integer> lossesPlayersTmp = new ArrayList<Integer>(numPlayers);
                ArrayList<Integer> hitsPlayersTmp = new ArrayList<Integer>(numPlayers);
                ArrayList<Integer> headIdPlayersTmp = new ArrayList<Integer>(numPlayers);
                ArrayList<Integer> eloPlayersTmp = new ArrayList<Integer>(numPlayers);
                int j = 0;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    UserHelperClass user = ds.getValue(UserHelperClass.class);
                    String name = user.getName();
                    Integer wins = user.getNumWins();
                    Integer losses = user.getNumLoses();
                    Integer treffer = user.getNumTreffer();
                    Integer headid = user.getHeadId();
                    Integer elo = user.getElo();

                    namePlayersTmp.add(j, name);
                    winsPlayersTmp.add(j, wins);
                    lossesPlayersTmp.add(j, losses);
                    hitsPlayersTmp.add(j, treffer);
                    headIdPlayersTmp.add(j, headid);
                    eloPlayersTmp.add(j, elo);
                    j = j + 1;
                }
                namePlayers = namePlayersTmp;
                winsPlayers = winsPlayersTmp;
                lossesPlayers = lossesPlayersTmp;
                hitsPlayers = hitsPlayersTmp;
                headIdPlayers = headIdPlayersTmp;
                eloPlayers = eloPlayersTmp;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // read all games
        referenceGames.orderByChild("timeStart").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                numGames = (int) dataSnapshot.getChildrenCount();
                ArrayList<String> datesGamesTmp = new ArrayList<String>(numGames);
                ArrayList<String> geoGamesTmp = new ArrayList<String>();
                int j = 0;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    GamesHelperClass game = ds.getValue(GamesHelperClass.class);
                    long startDate = game.getTimeStart().getTime();
                    datesGamesTmp.add(j, startDate+"");//this way we can cast long to string
                    String geotag = game.getGeotag();
                    geoGamesTmp.add(geotag);
                    j = j + 1;
                }
                datesGames = datesGamesTmp;
                geoGames = geoGamesTmp;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // read tournaments
        referenceAllTournaments = rootNode.getReference("tournaments");
        referenceAllTournaments.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    // if timeStart = timeEnd, this means that the tournament is still active
                    Date timeStart = ds.child("timeEnd").getValue(Date.class);
                    Date timeEnd = ds.child("timeStart").getValue(Date.class);
                    if(timeStart.equals(timeEnd)){
                        activeTournament = true;
                        activeTournamentName = ds.getKey();
                        activeTournamentNumGroups = ds.child("numGroups").getValue(int.class);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void CreatePlayer(View view) {
        // starts CreatePlayerActivity
        try{
            namePlayers.isEmpty(); // this works only if data base has been read
            Intent i = new Intent(this, CreatePlayerActivity.class);
            i.putExtra("league", league);
            startActivity(i);
        } catch (Exception e){
            Toast.makeText(this, "Die Datenbank wird geladen. Bitte probiere es gleich noch mal.", Toast.LENGTH_LONG).show();
        }
    }

    public void StartFlunk(View view) {
        // starts prepareFlunkActivity
        try{
            namePlayers.isEmpty(); // this works only if data base has been read
            Intent i = new Intent(this, PrepareFlunkActivity.class);
            i.putStringArrayListExtra("allPlayers", namePlayers);
            i.putExtra("league", league);
            startActivity(i);
        } catch (Exception e){
            Toast.makeText(this, "Die Datenbank wird geladen. Bitte probiere es gleich noch mal.", Toast.LENGTH_LONG).show();
        }
    }
    public void ShowScoreboard(View view) {
        // starts ScoreboardActivity
        try{
            datesGames.isEmpty(); // this works only if data base has been read
            Intent i = new Intent(this, ScoreboardActivity.class);
            i.putExtra("id_highlight", maxIdHighlights);
            i.putStringArrayListExtra("names_players", namePlayers);
            i.putIntegerArrayListExtra("losses_players", lossesPlayers);
            i.putIntegerArrayListExtra("wins_players", winsPlayers);
            i.putIntegerArrayListExtra("treffer_players", hitsPlayers);
            i.putIntegerArrayListExtra("headid_players", headIdPlayers);
            i.putIntegerArrayListExtra("elo_players", eloPlayers);
            i.putExtra("league", league);
            startActivity(i);
        } catch (Exception e){
            Toast.makeText(this, "Die Datenbank wird geladen. Bitte probiere es gleich noch mal.", Toast.LENGTH_LONG).show();
        }
    }

    public void OpenStats(View view) {
        try{
            datesGames.isEmpty(); // this works only if data base has been read
            Intent i = new Intent(this, StatsActivity.class);
            i.putStringArrayListExtra("name_players", namePlayers);
            i.putIntegerArrayListExtra("losses_players", lossesPlayers);
            i.putIntegerArrayListExtra("wins_players", winsPlayers);
            i.putIntegerArrayListExtra("treffer_players", hitsPlayers);
            i.putStringArrayListExtra("dates_games", datesGames);
            i.putExtra("league", league);
            startActivity(i);
        } catch (Exception e){
            Toast.makeText(this, "Die Datenbank wird geladen. Bitte probiere es gleich noch mal.", Toast.LENGTH_LONG).show();
        }
    }

    public void OpenRules(View view) {
        Intent i = new Intent(this, RulesActivity.class);
        startActivity(i);
    }

    public void PlanTournament(View view){
        if(activeTournament){
            // open tournament overview hub
            Intent i = new Intent(this, OverviewTournamentActivity.class);
            i.putExtra("tournament_name", activeTournamentName);
            i.putExtra("num_groups", activeTournamentNumGroups);
            i.putExtra("source", "main");
            startActivity(i);
        }
        else{
            // create new tournament
            Intent i = new Intent(this, PlanTournamentActivity.class);
            startActivity(i);
        }

    }

    public void OpenHeatmap(View view) {
        try {
            geoGames.isEmpty(); // Proxy for whether data has been loaded from database
            Intent i = new Intent(this, HeatmapActivity.class);
            i.putStringArrayListExtra("geotags", geoGames);
            i.putStringArrayListExtra("dates_games", datesGames);
            startActivity(i);
        } catch (Exception e){
            Toast.makeText(this, "Die Datenbank wird geladen. Bitte probiere es gleich noch mal.", Toast.LENGTH_LONG).show();
        }
    }
}