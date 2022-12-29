package com.example.yo_flunk;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.yo_flunk.ui.main.SectionsPagerAdapter_turnier;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ventura.bracketslib.model.ColomnData;
import com.ventura.bracketslib.model.CompetitorData;
import com.ventura.bracketslib.model.MatchData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class OverviewTournamentActivity extends AppCompatActivity {

    ViewPager viewPager;

    ArrayList<String> teamNames, teamBeers, player1, player2;
    ArrayList<Integer> teamInGroup, hitsPlayer1, hitsPlayer2, numWins, numGames;
    ArrayList<ArrayList> allGames, allGamesPlayed, team1win;
    String tournamentName, sourceActivity, linkDatabase;
    FirebaseDatabase rootNode;
    DatabaseReference referenceUsers, referenceTeams, referenceGames, referencePlayoffs, referenceTournament;
    TextView txtEndTournament;
    boolean loaded=false;
    ArrayList<CompetitorData> viertelFinale, halbFinale, Finale;
    int numGroups;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview_tournament);
        SectionsPagerAdapter_turnier sectionsPagerAdapter = new SectionsPagerAdapter_turnier(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);


        Intent mainIntent = getIntent();
        tournamentName = mainIntent.getStringExtra("tournament_name");
        TextView titleTxt = findViewById(R.id.title);
        titleTxt.setText("Turnierübersicht \n    " + tournamentName);

        linkDatabase = this.getResources().getString(R.string.firebase_link);
        rootNode = FirebaseDatabase.getInstance(linkDatabase);
        // later on this wont be needed since I upload everything to the firebase and download the arrays from there
        sourceActivity = mainIntent.getStringExtra("source");
        referenceTournament = rootNode.getReference("tournaments/" + tournamentName);
        referenceTournament.child("numGroups").addListenerForSingleValueEvent(
            new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    numGroups = dataSnapshot.getValue(int.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            }
        );
        referenceUsers = rootNode.getReference("tournaments/" + tournamentName + "/users");
        referenceTeams = rootNode.getReference("tournaments/" + tournamentName + "/teams");
        referenceGames = rootNode.getReference("tournaments/" + tournamentName + "/games");

        if(sourceActivity.equals("plantournament")){
            teamNames = mainIntent.getStringArrayListExtra("team_names");
            teamBeers = mainIntent.getStringArrayListExtra("team_beer");
            player1 = mainIntent.getStringArrayListExtra("player1");
            player2 = mainIntent.getStringArrayListExtra("player2");
            allGames = (ArrayList<ArrayList>) mainIntent.getSerializableExtra("all_games");
            allGamesPlayed = (ArrayList<ArrayList>) mainIntent.getSerializableExtra("all_games_played");
            teamInGroup = mainIntent.getIntegerArrayListExtra("groups");
            loaded = true;

            // TODO: convenience for debugging
            boolean debugging = false;
            if(debugging) {
                hitsPlayer1 = new ArrayList<>();
                hitsPlayer2 = new ArrayList<>();
                numWins = new ArrayList<>();
                numGames = new ArrayList<>();
                for (int j = 0; j < teamNames.size(); j++) {
                    hitsPlayer2.add(1);
                    hitsPlayer1.add(1);
                    numWins.add(j % 5);
                    numGames.add(j);
                }
            }else{
                hitsPlayer1 = new ArrayList<>();
                hitsPlayer2 = new ArrayList<>();
                numWins = new ArrayList<>();
                numGames = new ArrayList<>();
                for (int j = 0; j < teamNames.size(); j++) {
                    hitsPlayer2.add(0);
                    hitsPlayer1.add(0);
                    numWins.add(0);
                    numGames.add(0);
                }

            }
        }else{
            // source is whoWonActivity or mainActivity
            // read all data from firebase database
            numWins = new ArrayList<>();
            numGames = new ArrayList<>();
            teamNames = new ArrayList<>();
            teamInGroup = new ArrayList<>();
            team1win = new ArrayList<>();
            teamBeers = new ArrayList<>();
            player1 = new ArrayList<>();
            player2 = new ArrayList<>();
            hitsPlayer1 = new ArrayList<>();
            hitsPlayer2 = new ArrayList<>();
            referenceTeams.orderByValue().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                        TeamsHelperClass team = ds.getValue(TeamsHelperClass.class);
                        teamNames.add(team.getTeamName());
                        teamInGroup.add(team.getGroup());
                        teamBeers.add(team.getTeamBeer());
                        player1.add(team.getPlayer1());
                        player2.add(team.getPlayer2());
                        numWins.add(team.getWinsGroup());
                        numGames.add(team.getWinsGroup() + team.getDefeatsGroup());
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

            // update hits:
            referenceUsers.orderByChild("teamName").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                        UserTournamentHelperClass playerObj = ds.getValue(UserTournamentHelperClass.class);
                        String playerName = playerObj.getName();
                        if(player1.contains(playerName)){
                            hitsPlayer1.add(playerObj.getNumTreffer());
                        }else{
                            hitsPlayer2.add(playerObj.getNumTreffer());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

            // read Games:
            allGames = new ArrayList<>();
            allGamesPlayed = new ArrayList<>();
            referenceGames.orderByValue().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot dsGroup:dataSnapshot.getChildren()){
                        ArrayList<Bundle> gamesGroup = new ArrayList<>();
                        ArrayList<Boolean> gamesPlayedGroup = new ArrayList<>();
                        ArrayList<Boolean> team1winGroup = new ArrayList<>();
                        for(DataSnapshot dsGame:dsGroup.getChildren()){
                            GamesTournamentHelperClass game = dsGame.getValue(GamesTournamentHelperClass.class);
                            String teamName1 = game.getTeamNames().get(0);
                            String teamName2 = game.getTeamNames().get(1);
                            Boolean played = game.isPlayed();
                            Bundle bundleGame = new Bundle();
                            bundleGame.putString("team1", teamName1);
                            bundleGame.putString("team2", teamName2);
                            gamesGroup.add(bundleGame);
                            gamesPlayedGroup.add(played);
                            team1winGroup.add(game.getTeam1Win());
                        }
                        allGames.add(gamesGroup);
                        allGamesPlayed.add(gamesPlayedGroup);
                        team1win.add(team1winGroup);
                        loaded = true;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }

        // How i am organizing the groups and all hits/wins/losses/etc.
        // All teams are sorted by their teamname.
        // To obtain the name of player 1 in a specific team, we need to
        // a) find the teamname in teamnames
        // b) save the corresponding index j such that teamnames[j]=teamname
        // c) return player1[j]
        // I write a function that automates this procedure called Utility.findPlayerdsOfTeam
    }

        public void openPlayoffs(View view) {
        if(loaded){
            rootNode = FirebaseDatabase.getInstance(linkDatabase);
            referencePlayoffs = rootNode.getReference("tournaments/" + tournamentName + "/playoffs");

            viertelFinale = new ArrayList<>();
            halbFinale = new ArrayList<>();
            Finale = new ArrayList<>();


            referencePlayoffs.orderByValue().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                        for(DataSnapshot dsMatch:ds.getChildren()) {
                            GamesTournamentHelperClass game = dsMatch.getValue(GamesTournamentHelperClass.class);
                            String team1 = game.getTeamNames().get(0);
                            String team2 = game.getTeamNames().get(1);
                            String score1;
                            String score2;
                            if (game.isPlayed()) {
                                score1 = String.valueOf(game.getNumTreffer1().get(0) + game.getNumTreffer1().get(1));
                                score2 = String.valueOf(game.getNumTreffer2().get(0) + game.getNumTreffer2().get(1));
                            } else {
                                score1 = "";
                                score2 = "";
                            }
                            CompetitorData team1data = new CompetitorData(team1, score1);
                            CompetitorData team2data = new CompetitorData(team2, score2);
                            if (ds.getKey().equals("Viertelfinale")) {
                                viertelFinale.add(team1data);
                                viertelFinale.add(team2data);
                            } else {
                                if (ds.getKey().equals("Halbfinale")) {
                                    halbFinale.add(team1data);
                                    halbFinale.add(team2data);
                                } else {
                                    Finale.add(team1data);
                                    Finale.add(team2data);
                                }
                            }
                        }
                    }
                    MatchData match1ViertelFinale = new MatchData(viertelFinale.get(0), viertelFinale.get(1));
                    MatchData match2ViertelFinale = new MatchData(viertelFinale.get(2), viertelFinale.get(3));
                    MatchData match3ViertelFinale = new MatchData(viertelFinale.get(4), viertelFinale.get(5));
                    MatchData match4ViertelFinale = new MatchData(viertelFinale.get(6), viertelFinale.get(7));
                    ColomnData colomnViertelFinale = new ColomnData(Arrays.asList(match1ViertelFinale, match2ViertelFinale, match3ViertelFinale, match4ViertelFinale));

                    MatchData match1HalbFinale = new MatchData(halbFinale.get(0), halbFinale.get(1));
                    MatchData match2HalbFinale = new MatchData(halbFinale.get(2), halbFinale.get(3));
                    ColomnData colomnHalbFinale = new ColomnData(Arrays.asList(match1HalbFinale, match2HalbFinale));

                    MatchData matchFinale = new MatchData(Finale.get(0), Finale.get(1));
                    ColomnData colomnFinale = new ColomnData(Arrays.asList(matchFinale));

                    // start new activity
                    Intent i = new Intent(OverviewTournamentActivity.this, PlayoffsTournament.class);
                    i.putExtra("tournament_name", tournamentName);
                    i.putExtra("viertelfinale", colomnViertelFinale);
                    i.putExtra("halbfinale", colomnHalbFinale);
                    i.putExtra("finale", colomnFinale);
                    startActivity(i);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }

    public void openNextgames(View view) {
        if(loaded) {
            Intent i = new Intent(this, NextGamesTournamentActivity.class);
            i.putExtra("all_games", allGames);
            i.putExtra("all_games_played", allGamesPlayed);
            i.putExtra("tournament_name", tournamentName);
            i.putStringArrayListExtra("team_names", teamNames);
            i.putStringArrayListExtra("players1", player1);
            i.putStringArrayListExtra("players2", player2);
            i.putExtra("team1_win", team1win);
            startActivity(i);
        }
        else{
            Toast.makeText(this, "Die Datenbank wird geladen. Bitte probiere es gleich noch mal.", Toast.LENGTH_LONG).show();
        }
    }

    public void openGroups(View view) {
        // opens the overview of the groups stage
        Intent i = new Intent(this, GroupStageTournamentActivity.class);
        i.putStringArrayListExtra("team_names", teamNames);
        i.putIntegerArrayListExtra("groups", teamInGroup);
        i.putIntegerArrayListExtra("hits_player1", hitsPlayer1);
        i.putIntegerArrayListExtra("hits_player2", hitsPlayer2);
        i.putIntegerArrayListExtra("num_wins", numWins);
        i.putIntegerArrayListExtra("num_games", numGames);
        i.putExtra("num_groups", numGroups);
        i.putExtra("tournament_name", tournamentName);
        startActivity(i);
    }

    public void endTournament(View view) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View PopupView = getLayoutInflater().inflate(R.layout.popup_delete_team, null);

        txtEndTournament = PopupView.findViewById(R.id.txt_deleteteam);
        txtEndTournament.setText("Möchtest du das Turnier beenden?");

        Button btnEndTournament = PopupView.findViewById(R.id.btn_delete_team);
        btnEndTournament.setText("Turnier beenden");
        Button btnCancel = PopupView.findViewById(R.id.btn_cancel);

        dialogBuilder.setView(PopupView);
        AlertDialog dialog = dialogBuilder.create();

        // update timeEnd of the tournament object:
        btnEndTournament.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                DatabaseReference referenceTournament = rootNode.getReference("tournaments/" + tournamentName);
                Date endDate = new Date();
                referenceTournament.child("timeEnd").setValue(endDate);

                Intent i = new Intent(v.getContext(), MainActivity.class);
                startActivity(i);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}