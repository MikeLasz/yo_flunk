package com.example.yo_flunk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class NextGamesTournamentActivity extends AppCompatActivity {

    RecyclerView recyclerView1;
    RecyclerView.Adapter<RvAdapterKlasseNextGames.ViewHolderKlasse> rvadapter1;
    RecyclerView.LayoutManager rvLayoutManager1;

    static ArrayList<String> itemTeam1Next, itemTeam2Next, itemTeam1Finished, itemTeam2Finished;
    static ArrayList<Boolean> itemTeam1WinFinished;
    static ArrayList<Integer> itemGroupFinished, itemGroupNext;
    public static String link_database, tournamentName;
    public static int NUM_SHOW_NEXT_GAMES = 3;
    public static ArrayList<String> teamNames, players1, players2;

    ArrayList<ArrayList> allGames, allGamesPlayed, team1win;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nextgames_tournament);
        link_database = this.getResources().getString(R.string.firebase_link);

        Intent main_intent = getIntent();
        allGames = (ArrayList<ArrayList>) main_intent.getSerializableExtra("all_games");
        allGamesPlayed = (ArrayList<ArrayList>) main_intent.getSerializableExtra("all_games_played");
        team1win = (ArrayList<ArrayList>) main_intent.getSerializableExtra("team1_win");
        tournamentName = main_intent.getStringExtra("tournament_name");
        teamNames = main_intent.getStringArrayListExtra("team_names");
        players1 = main_intent.getStringArrayListExtra("players1");
        players2 = main_intent.getStringArrayListExtra("players2");

        itemTeam1Next = new ArrayList<>();
        itemTeam2Next = new ArrayList<>();
        itemTeam1Finished = new ArrayList<>();
        itemTeam2Finished = new ArrayList<>();
        itemTeam1WinFinished = new ArrayList<>();
        itemGroupFinished = new ArrayList<>();
        itemGroupNext = new ArrayList<>();
        int numGames = Utility.computeNumGames(allGames);
        int[] groupCounter = new int[allGames.size()];
        for(int j=0; j<allGames.size(); j++){
            groupCounter[j] = 0;
        }


        int numShownNextGames = 0;
        int counter = 0;
        int numGamesToBePlayed = Utility.computeNumTobeplayed(allGamesPlayed);
        while((numShownNextGames<NUM_SHOW_NEXT_GAMES) && (counter<=numGames) && (numShownNextGames<numGamesToBePlayed)){
            int group = counter%allGames.size();
            // check whether this game has already been played
            if(groupCounter[group] < allGames.get(group).size()) { // check if there are remaining games in the group
                Bundle game = (Bundle) allGames.get(group).get(groupCounter[group]);
                if (((boolean) allGamesPlayed.get(group).get(groupCounter[group]))) {
                    // i.e. game has already been played
                    itemTeam1Finished.add(game.getString("team1"));
                    itemTeam2Finished.add(game.getString("team2"));

                    if((boolean) team1win.get(group).get(groupCounter[group])){
                        itemTeam1WinFinished.add(true);
                    }
                    else{
                        itemTeam1WinFinished.add(false);
                    }
                    itemGroupFinished.add(group);

                } else {
                    // i.e. game hasnt been played yet
                    itemTeam1Next.add(game.getString("team1"));
                    itemTeam2Next.add(game.getString("team2"));
                    itemGroupNext.add(group);
                    numShownNextGames = numShownNextGames + 1;
                }
            }
            // update counters
            groupCounter[group] = groupCounter[group] + 1;
            counter = counter + 1;
        }

        // This happens when all games have been played.
        if(numGamesToBePlayed==0){
            for(int counterFinished=0; counterFinished<numGames; counterFinished++) {
                int group = counterFinished % allGames.size();
                // check whether this game has already  been played
                if (groupCounter[group] < allGames.get(group).size()) { // check if there are remaining games in the group
                    Bundle game = (Bundle) allGames.get(group).get(groupCounter[group]);
                    // i.e. game has already been played
                    itemTeam1Finished.add(game.getString("team1"));
                    itemTeam2Finished.add(game.getString("team2"));

                    if ((boolean) team1win.get(group).get(groupCounter[group])) {
                        itemTeam1WinFinished.add(true);
                    } else {
                        itemTeam1WinFinished.add(false);
                    }
                    itemGroupFinished.add(group);
                }
                // update counters
                groupCounter[group] = groupCounter[group] + 1;
            }

            // -------------------------//
            // ToDo add Playoffs Games: //
            // -------------------------//
        }

        recyclerView1 = (RecyclerView) findViewById(R.id.recyclerview);
        rvLayoutManager1 = new LinearLayoutManager(NextGamesTournamentActivity.this);
        recyclerView1.setLayoutManager(rvLayoutManager1);

        rvadapter1 = new RvAdapterKlasseNextGames();
        recyclerView1.setAdapter(rvadapter1);
    }

    public void openOverview(View view) {
        Intent i = new Intent(this, OverviewTournamentActivity.class);
        i.putExtra("source", (String) "nextgames");
        i.putExtra("tournament_name", tournamentName);
        startActivity(i);
    }


}

