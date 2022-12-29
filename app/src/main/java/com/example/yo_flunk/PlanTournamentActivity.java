package com.example.yo_flunk;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import static com.example.yo_flunk.Utility.checkIfValid;

public class PlanTournamentActivity extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText teamName, player1, player2, teamBeer, editTxtTournamentName;
    private Button btnCancel, btnAddTeam, btnStartTournament;

    static ArrayList<String> itemPlayer1, itemPlayer2, itemTeamNames, itemTeamBeer;
    static ArrayList<Integer> teamInGroup;
    static String tournamentName;

    static RecyclerView recyclerView1;
    static RecyclerView.Adapter<RvAdapterKlasseTeams.ViewHolderKlasse> rvAdapter1;
    static RecyclerView.LayoutManager rvLayoutManager1;

    int numGroups = 4;
    FirebaseDatabase rootNode;
    DatabaseReference referenceUsers, referenceTeams, referenceGames, referenceTournament, referencePlayoffs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_tournament);

        itemPlayer1 = new ArrayList<>();
        itemPlayer2 = new ArrayList<>();
        itemTeamNames = new ArrayList<>();
        itemTeamBeer = new ArrayList<>();

    }

    public void startTournamentPopup(View v){
        dialogBuilder = new AlertDialog.Builder(this);
        final View createteamPopupView = getLayoutInflater().inflate(R.layout.popup_start_tournament, null);

        editTxtTournamentName = createteamPopupView.findViewById(R.id.editTxt_tournamentName);
        btnStartTournament = createteamPopupView.findViewById(R.id.btn_start);
        btnCancel = createteamPopupView.findViewById(R.id.btn_cancel);
        RadioButton radio2Groups = createteamPopupView.findViewById(R.id.radio_2groups);
        RadioButton radio4Groups = createteamPopupView.findViewById(R.id.radio_4groups);



        dialogBuilder.setView(createteamPopupView);
        dialog = dialogBuilder.create();

        // Next: On click listener for btn_startTournament to start the tournament.
        btnStartTournament.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                tournamentName = editTxtTournamentName.getText().toString();

                if(tournamentName.isEmpty()){
                    Toast.makeText(PlanTournamentActivity.this, "Bitte trage einen Turniernamen ein.", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(radio2Groups.isChecked()){
                        numGroups = 2;
                        startTournament(v);
                    }
                    else {
                        if (radio4Groups.isChecked()) {
                            numGroups = 4;
                            startTournament(v);
                        } else {
                            Toast.makeText(PlanTournamentActivity.this, "Bitte trage die Anzahl der Gruppen ein.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
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


    public void createNewTeam(View v){
        dialogBuilder = new AlertDialog.Builder(this);
        final View createteamPopupView = getLayoutInflater().inflate(R.layout.popup_add_team, null);

        teamName = createteamPopupView.findViewById(R.id.edittxt_teamname);
        teamBeer = createteamPopupView.findViewById(R.id.edittxt_teambier);
        player1 = createteamPopupView.findViewById(R.id.edittxt_player1);
        player2 = createteamPopupView.findViewById(R.id.edittxt_player2);
        btnCancel = createteamPopupView.findViewById(R.id.btn_cancel);
        btnAddTeam = createteamPopupView.findViewById(R.id.btn_create);

        dialogBuilder.setView(createteamPopupView);
        dialog = dialogBuilder.create();

        btnAddTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String team_name = ((EditText) createteamPopupView.findViewById(R.id.edittxt_teamname)).getText().toString();
                String team_beer = ((EditText) createteamPopupView.findViewById(R.id.edittxt_teambier)).getText().toString();
                team_beer = team_beer.substring(0, 1).toUpperCase() + team_beer.substring(1);
                String player1_name = ((EditText) createteamPopupView.findViewById(R.id.edittxt_player1)).getText().toString();
                String player2_name = ((EditText) createteamPopupView.findViewById(R.id.edittxt_player2)).getText().toString();


                if(checkIfValid(team_name, new ArrayList<>(), new ArrayList<>()) && checkIfValid(player1_name, itemPlayer1, itemPlayer2) && checkIfValid(player2_name, itemPlayer1, itemPlayer2)) {
                    itemTeamNames.add(team_name);
                    itemTeamBeer.add(team_beer);
                    itemPlayer1.add(player1_name);
                    itemPlayer2.add(player2_name);

                    recyclerView1 = (RecyclerView) findViewById(R.id.recyclerview);
                    rvLayoutManager1 = new LinearLayoutManager(PlanTournamentActivity.this);
                    recyclerView1.setLayoutManager(rvLayoutManager1);

                    rvAdapter1 = new RvAdapterKlasseTeams();
                    recyclerView1.setAdapter(rvAdapter1);

                    dialog.dismiss();
                }
                else{
                    Toast.makeText(PlanTournamentActivity.this, "Die folgenden Zeichen sind nicht zulässig: . $ [ ] # /", Toast.LENGTH_LONG).show();
                }
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void startTournament(View v){
        Boolean debugging = this.getResources().getBoolean(R.bool.dev_version);
        if(debugging) {
            // use those placeholders for more convenient debugging
            createDummies();
        }
        teamInGroup = new ArrayList<>();
        for(int i = 0; i<= itemTeamNames.size() - 1; i++){
            teamInGroup.add(i% numGroups);
        }
        Collections.shuffle(teamInGroup);

        ArrayList<ArrayList> allGames = createGames(itemTeamNames, teamInGroup);
        ArrayList<ArrayList> allGamesPlayed = new ArrayList<>();
        for(int j = 0; j< numGroups; j++){
            ArrayList<Boolean> groupGamesPlayed = new ArrayList<>();
            for(int game_num=0; game_num<allGames.get(j).size(); game_num++){
                groupGamesPlayed.add(false);
            }
            allGamesPlayed.add(groupGamesPlayed);
        }

        // ----------------------
        // Update Firebase:
        // get all references:
        String link_database = this.getResources().getString(R.string.firebase_link);
        rootNode = FirebaseDatabase.getInstance(link_database);
        referenceTournament = rootNode.getReference("tournaments/" + tournamentName);
        referenceUsers = rootNode.getReference("tournaments/" + tournamentName + "/users");
        referenceTeams = rootNode.getReference("tournaments/" + tournamentName + "/teams");
        referenceGames = rootNode.getReference("tournaments/" + tournamentName + "/games");
        referencePlayoffs = rootNode.getReference("tournaments/" + tournamentName + "/playoffs");

        // update users:
        // register users
        for(int j = 0; j< itemPlayer1.size(); j++){
            UserTournamentHelperClass user1 = new UserTournamentHelperClass(itemPlayer1.get(j), itemTeamNames.get(j), 0, 0, 0);
            referenceUsers.child(itemPlayer1.get(j)).setValue(user1);

            UserTournamentHelperClass user2 = new UserTournamentHelperClass(itemPlayer2.get(j), itemTeamNames.get(j), 0, 0, 0);
            referenceUsers.child(itemPlayer2.get(j)).setValue(user2);
        }

        // register teams
        for(int j = 0; j< itemTeamNames.size(); j++) {
            TeamsHelperClass team = new TeamsHelperClass(itemTeamNames.get(j), itemPlayer1.get(j), itemPlayer2.get(j), itemTeamBeer.get(j), teamInGroup.get(j), 0, 0);
            referenceTeams.child(itemTeamNames.get(j)).setValue(team);
        }

        // register games
        for(int group=0; group<allGames.size(); group++){
            for(int idGame=0; idGame<allGames.get(group).size(); idGame++){
                Bundle teams = (Bundle) allGames.get(group).get(idGame);
                String team1 = teams.getString("team1");
                String team2 = teams.getString("team2");
                ArrayList<String> teamnamesGame = new ArrayList<>();
                teamnamesGame.add(team1);
                teamnamesGame.add(team2);
                ArrayList<String> team1Players = Utility.findPlayersOfTeam(team1, itemTeamNames, itemPlayer1, itemPlayer2);
                ArrayList<String> team2Players = Utility.findPlayersOfTeam(team2, itemTeamNames, itemPlayer1, itemPlayer2);
                ArrayList<Integer> team1Treffer = new ArrayList<>();
                ArrayList<Integer> team2Treffer = new ArrayList<>();
                team1Treffer.add(0);
                team1Treffer.add(0);
                team2Treffer.add(0);
                team2Treffer.add(0);
                Date startDate = new Date();
                Date endDate = new Date();

                GamesTournamentHelperClass game = new GamesTournamentHelperClass(teamnamesGame, team1Players, team2Players, team1Treffer, team2Treffer,
                        startDate, endDate);

                referenceGames.child(String.valueOf(group)).child(String.valueOf(idGame)).setValue(game);
            }

        }

        // register playoffs
        // quarterfinals
        for(int j=0; j<4; j++){
            ArrayList<String> teamnamesGame = new ArrayList<>();
            teamnamesGame.add("TBD");
            teamnamesGame.add("TBD");
            ArrayList<String> team1Players = new ArrayList<>();
            ArrayList<String> team2Players = new ArrayList<>();
            team1Players.add("TBD");
            team1Players.add("TBD");
            team2Players.add("TBD");
            team2Players.add("TBD");
            ArrayList<Integer> team1Treffer = new ArrayList<>();
            ArrayList<Integer> team2Treffer = new ArrayList<>();
            team1Treffer.add(0);
            team1Treffer.add(0);
            team2Treffer.add(0);
            team2Treffer.add(0);
            Date startDate = new Date();
            Date endDate = new Date();

            GamesTournamentHelperClass game = new GamesTournamentHelperClass(teamnamesGame, team1Players, team2Players, team1Treffer, team2Treffer,
                    startDate, endDate);

            referencePlayoffs.child("Viertelfinale").child(String.valueOf(j)).setValue(game);
        }
        // semifinals
        for(int j=0; j<2; j++){
            ArrayList<String> teamnamesGame = new ArrayList<>();
            teamnamesGame.add("TBD");
            teamnamesGame.add("TBD");
            ArrayList<String> team1Players = new ArrayList<>();
            ArrayList<String> team2Players = new ArrayList<>();
            team1Players.add("TBD");
            team1Players.add("TBD");
            team2Players.add("TBD");
            team2Players.add("TBD");
            ArrayList<Integer> team1Treffer = new ArrayList<>();
            ArrayList<Integer> team2Treffer = new ArrayList<>();
            team1Treffer.add(0);
            team1Treffer.add(0);
            team2Treffer.add(0);
            team2Treffer.add(0);
            Date startDate = new Date();
            Date endDate = new Date();

            GamesTournamentHelperClass game = new GamesTournamentHelperClass(teamnamesGame, team1Players, team2Players, team1Treffer, team2Treffer,
                    startDate, endDate);

            referencePlayoffs.child("Halbfinale").child(String.valueOf(j)).setValue(game);
        }
        // finals
        ArrayList<String> teamnamesGame = new ArrayList<>();
        teamnamesGame.add("TBD");
        teamnamesGame.add("TBD");
        ArrayList<String> team1Players = new ArrayList<>();
        ArrayList<String> team2Players = new ArrayList<>();
        team1Players.add("TBD");
        team1Players.add("TBD");
        team2Players.add("TBD");
        team2Players.add("TBD");
        ArrayList<Integer> team1Treffer = new ArrayList<>();
        ArrayList<Integer> team2Treffer = new ArrayList<>();
        team1Treffer.add(0);
        team1Treffer.add(0);
        team2Treffer.add(0);
        team2Treffer.add(0);
        Date startDate = new Date();
        Date endDate = new Date();

        GamesTournamentHelperClass game = new GamesTournamentHelperClass(teamnamesGame, team1Players, team2Players, team1Treffer, team2Treffer,
                startDate, endDate);

        referencePlayoffs.child("Finale").child(String.valueOf(0)).setValue(game);


        // register tournament time
        Date start_date = new Date();
        referenceTournament.child("timeStart").setValue(start_date);
        referenceTournament.child("timeEnd").setValue(start_date);
        referenceTournament.child("numGroups").setValue(numGroups);

        // --------------------------

        // Infos für den neuen Intent:
        // Alle Spieler + teamnamen + teambier + Gruppenzuordnung
        Intent i = new Intent(this, OverviewTournamentActivity.class);
        i.putStringArrayListExtra("team_names", itemTeamNames);
        i.putStringArrayListExtra("team_beer", itemTeamBeer);
        i.putIntegerArrayListExtra("groups", teamInGroup);
        i.putStringArrayListExtra("player1", itemPlayer1);
        i.putStringArrayListExtra("player2", itemPlayer2);
        i.putExtra("all_games_played", allGamesPlayed);
        i.putExtra("all_games", allGames);
        i.putExtra("tournament_name", tournamentName);
        i.putExtra("num_groups", numGroups);
        i.putExtra("source", "plantournament");
        startActivity(i);

    }

    public void createDummies(){
        //placeholders: TODO use those placeholders for more convenient debugging
        itemTeamNames = new ArrayList<>();
        itemTeamNames.add("Team1");
        itemTeamNames.add("Team2");
        itemTeamNames.add("Team3");
        itemTeamNames.add("Team4");
        itemTeamNames.add("Team5");
        itemTeamNames.add("Team6");
        itemTeamNames.add("Team7");
        itemTeamNames.add("Team8");
        itemTeamNames.add("Team9");
        itemTeamNames.add("Team10");
        itemTeamNames.add("Team11");

        itemPlayer1.add("Mike");
        itemPlayer1.add("b");
        itemPlayer1.add("c");
        itemPlayer1.add("d");
        itemPlayer1.add("e");
        itemPlayer1.add("f");
        itemPlayer1.add("g");
        itemPlayer1.add("h");
        itemPlayer1.add("i");
        itemPlayer1.add("j");
        itemPlayer1.add("k");

        itemPlayer2.add("l");
        itemPlayer2.add("m");
        itemPlayer2.add("n");
        itemPlayer2.add("o");
        itemPlayer2.add("p");
        itemPlayer2.add("q");
        itemPlayer2.add("r");
        itemPlayer2.add("s");
        itemPlayer2.add("t");
        itemPlayer2.add("u");
        itemPlayer2.add("v");

        itemTeamBeer.add("Hansa");
        itemTeamBeer.add("Hansa");
        itemTeamBeer.add("Hansa");
        itemTeamBeer.add("Hansa");
        itemTeamBeer.add("Hansa");
        itemTeamBeer.add("Hansa");
        itemTeamBeer.add("Fiege");
        itemTeamBeer.add("Fiege");
        itemTeamBeer.add("Fiege");
        itemTeamBeer.add("Stauder");
        itemTeamBeer.add("Stauder");

    }

    public ArrayList<ArrayList> createGames(ArrayList<String> teamnames, ArrayList<Integer> teamInGroup){
        ArrayList<ArrayList> allGames = new ArrayList<ArrayList>();

        int numGroups = (int) Collections.max(teamInGroup);
        for(int idGroup=0; idGroup<=numGroups; idGroup++){
            // get all teams in group idGroup
            ArrayList<String> teamnamesGroup = new ArrayList<>();
            for(int j=0; j<teamnames.size(); j++){
                if(idGroup == teamInGroup.get(j)){
                    teamnamesGroup.add(teamnames.get(j));
                }
            }

            // compute all possible games
            ArrayList<Bundle> gamesGroup = new ArrayList<>();
            for(int j=0; j<teamnamesGroup.size() - 1; j++){
                String team1 = teamnamesGroup.get(j);
                for(int i=j+1; i<teamnamesGroup.size(); i++){
                    String team2 = teamnamesGroup.get(i);
                    Bundle game = new Bundle();
                    if(j+i%2==0){
                        game.putString("team1", team1);
                        game.putString("team2", team2);
                    }
                    else{
                        game.putString("team1", team2);
                        game.putString("team2", team1);
                    }
                    gamesGroup.add(game);
                }
            }

            // prioritize games to avoid something like team1 has 2 games in a row
            ArrayList<Integer> priorities = new ArrayList<>();
            for(int j=0; j<gamesGroup.size(); j++){
                priorities.add(0); //initialize all priorities by zero
            }
            ArrayList<Bundle> gamesGroupPrioritized = new ArrayList<>();
            while(!gamesGroup.isEmpty()){
                // find game with lowest priority (somewhat missleading... Here I set priority=amount of games of team1 and team2)
                int index = priorities.indexOf(Collections.min(priorities));
                priorities.remove(index);
                Bundle addedGame = gamesGroup.get(index);
                gamesGroupPrioritized.add(addedGame);
                gamesGroup.remove(index);
                // update priorities
                String team1 = addedGame.getString("team1");
                String team2 = addedGame.getString("team2");
                for(int j=0; j<gamesGroup.size(); j++){
                    if(((Bundle)gamesGroup.get(j)).getString("team1") == team1 ||  ((Bundle)gamesGroup.get(j)).getString("team2") == team1){
                        int oldPriority = priorities.get(j);
                        priorities.set(j, oldPriority + 1);
                    }
                    if(((Bundle)gamesGroup.get(j)).getString("team1") == team2 ||  ((Bundle)gamesGroup.get(j)).getString("team2") == team2){
                        int old_priority = priorities.get(j);
                        priorities.set(j, old_priority + 1);
                    }
                }
            }
            allGames.add(gamesGroupPrioritized);
        }
        return allGames;
    }
}