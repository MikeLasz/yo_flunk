package com.example.yo_flunk;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.res.ResourcesCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Date;

import static com.example.yo_flunk.Utility.computeElo;

public class WhoWonActivity extends AppCompatActivity {

    public static boolean team1Win;
    public static boolean[] teamPartition;
    public static ArrayList<String> players = new ArrayList<String>();
    public static ArrayList<Integer> scores = new ArrayList<Integer>();
    public static ArrayList<Integer> strafbier = new ArrayList<Integer>();
    public static ArrayList<Integer> strafrunde = new ArrayList<Integer>();
    ArrayList<String> teamNames;
    long maxId = 0;
    int headId;
    int indexPlayerTeam1 = 0;
    int indexPlayerTeam2 = 0;
    int numPlayersTeam1 = 0;
    int numPlayersTeam2 = 0;
    int sumEloTeam1 = 0;
    int sumEloTeam2 = 0;
    boolean notification, trackGeo;
    boolean winnerDetermined = false;
    boolean knechtung;
    String geotag;
    Date timeStart = new Date();
    FirebaseDatabase rootNode;
    DatabaseReference referenceUsers, referenceGames, referenceTeams;
    String league;
    String linkDatabase;
    String tournamentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_who_won);
        linkDatabase = this.getResources().getString(R.string.firebase_link);

        // read values from playGameActivity
        Intent i = getIntent();
        players = i.getStringArrayListExtra("players");
        scores = i.getIntegerArrayListExtra("scores");
        strafbier = i.getIntegerArrayListExtra("strafbier");
        strafrunde = i.getIntegerArrayListExtra("strafrunde");
        teamPartition = i.getBooleanArrayExtra("partition");
        timeStart.setTime(i.getLongExtra("time_start", -1));
        league = i.getStringExtra("league");
        notification = i.getBooleanExtra("notification", true);
        trackGeo = i.getBooleanExtra("track_geo", true);
        if(trackGeo){
            geotag = i.getStringExtra("geotag");
        }

        rootNode = FirebaseDatabase.getInstance(linkDatabase);

        // find all Views //
        ////////////////////
        // Team 1:
        ConstraintLayout containerTeam1 = findViewById(R.id.layout_team1);
        //        Player 1:
        ConstraintLayout containerTeam1Player1 = findViewById(R.id.layout_team1_player1);
        ImageView avatarTeam1Player1 = findViewById(R.id.avatar_team1_player1);
        TextView txtviewTeam1Player1 = findViewById(R.id.name_team1_player1);
        //        Player 2:
        ConstraintLayout containerTeam1Player2 = findViewById(R.id.layout_team1_player2);
        ImageView avatarTeam1Player2 = findViewById(R.id.avatar_team1_player2);
        TextView txtviewTeam1Player2 = findViewById(R.id.name_team1_player2);
        //        Player 3:
        ConstraintLayout containerTeam1Player3 = findViewById(R.id.layout_team1_player3);
        ImageView avatarTeam1Player3 = findViewById(R.id.avatar_team1_player3);
        TextView txtviewTeam1Player3 = findViewById(R.id.name_team1_player3);
        //        Player 4:
        ConstraintLayout containerTeam1Player4 = findViewById(R.id.layout_team1_player4);
        ImageView avatarTeam1Player4 = findViewById(R.id.avatar_team1_player4);
        TextView txtviewTeam1Player4 = findViewById(R.id.name_team1_player4);
        //        Player 5:
        ConstraintLayout containerTeam1Player5 = findViewById(R.id.layout_team1_player5);
        ImageView avatarTeam1Player5 = findViewById(R.id.avatar_team1_player5);
        TextView txtviewTeam1Player5 = findViewById(R.id.name_team1_player5);

        // Team 2:
        ConstraintLayout containerTeam2 = findViewById(R.id.layout_team2);
        //        Player 1:
        ConstraintLayout containerTeam2Player1 = findViewById(R.id.layout_team2_player1);
        ImageView avatarTeam2Player1 = findViewById(R.id.avatar_team2_player1);
        TextView txtviewTeam2Player1 = findViewById(R.id.name_team2_player1);
        //        Player 2:
        ConstraintLayout containerTeam2Player2 = findViewById(R.id.layout_team2_player2);
        ImageView avatarTeam2Player2 = findViewById(R.id.avatar_team2_player2);
        TextView txtviewTeam2Player2 = findViewById(R.id.name_team2_player2);
        //        Player 3:
        ConstraintLayout containerTeam2Player3 = findViewById(R.id.layout_team2_player3);
        ImageView avatarTeam2Player3 = findViewById(R.id.avatar_team2_player3);
        TextView txtviewTeam2Player3 = findViewById(R.id.name_team2_player3);
        //        Player 4:
        ConstraintLayout containerTeam2Player4 = findViewById(R.id.layout_team2_player4);
        ImageView avatarTeam2Player4 = findViewById(R.id.avatar_team2_player4);
        TextView txtviewTeam2Player4 = findViewById(R.id.name_team2_player4);
        //        Player 5:
        ConstraintLayout containerTeam2Player5 = findViewById(R.id.layout_team2_player5);
        ImageView avatarTeam2Player5 = findViewById(R.id.avatar_team2_player5);
        TextView txtviewTeam2Player5 = findViewById(R.id.name_team2_player5);


        // To iterate through the image, textviews, and the layouts, we organize them in a list:
        ImageView[] avatarsTeam1 = new ImageView[5]; // 6 is the maximum number of players per team
        avatarsTeam1[0] = avatarTeam1Player1;
        avatarsTeam1[1] = avatarTeam1Player2;
        avatarsTeam1[2] = avatarTeam1Player3;
        avatarsTeam1[3] = avatarTeam1Player4;
        avatarsTeam1[4] = avatarTeam1Player5;

        TextView[] namesTeam1 = new TextView[5];
        namesTeam1[0] = txtviewTeam1Player1;
        namesTeam1[1] = txtviewTeam1Player2;
        namesTeam1[2] = txtviewTeam1Player3;
        namesTeam1[3] = txtviewTeam1Player4;
        namesTeam1[4] = txtviewTeam1Player5;

        ConstraintLayout[] layoutsTeam1 = new ConstraintLayout[5];
        layoutsTeam1[0] = containerTeam1Player1;
        layoutsTeam1[1] = containerTeam1Player2;
        layoutsTeam1[2] = containerTeam1Player3;
        layoutsTeam1[3] = containerTeam1Player4;
        layoutsTeam1[4] = containerTeam1Player5;

        ImageView[] avatarsTeam2 = new ImageView[5];
        avatarsTeam2[0] = avatarTeam2Player1;
        avatarsTeam2[1] = avatarTeam2Player2;
        avatarsTeam2[2] = avatarTeam2Player3;
        avatarsTeam2[3] = avatarTeam2Player4;
        avatarsTeam2[4] = avatarTeam2Player5;

        TextView[] namesTeam2 = new TextView[5];
        namesTeam2[0] = txtviewTeam2Player1;
        namesTeam2[1] = txtviewTeam2Player2;
        namesTeam2[2] = txtviewTeam2Player3;
        namesTeam2[3] = txtviewTeam2Player4;
        namesTeam2[4] = txtviewTeam2Player5;

        ConstraintLayout[] layoutsTeam2 = new ConstraintLayout[5];
        layoutsTeam2[0] = containerTeam2Player1;
        layoutsTeam2[1] = containerTeam2Player2;
        layoutsTeam2[2] = containerTeam2Player3;
        layoutsTeam2[3] = containerTeam2Player4;
        layoutsTeam2[4] = containerTeam2Player5;


        // initialize list with player names in each team
        ArrayList<String> playersTeam1 = new ArrayList<String>();
        ArrayList<String> playersTeam2 = new ArrayList<String>();
        // counter numPlayers in each team:
        int numPlayers = players.size();
        for(int j=0;j<numPlayers;j++) {
            if(teamPartition[j]){
                numPlayersTeam1 = numPlayersTeam1 + 1;
            }
            else{
                numPlayersTeam2 = numPlayersTeam2 + 1;
            }
        }

        if(league.startsWith("tournament_")){
            // it is a tournament game:
            tournamentName = league.substring(11);
            teamNames = i.getStringArrayListExtra("team_names");

            // display no image views when doing a tournament game:
            avatarTeam1Player1.setVisibility(View.INVISIBLE);
            avatarTeam1Player2.setVisibility(View.INVISIBLE);
            avatarTeam1Player3.setVisibility(View.INVISIBLE);
            avatarTeam1Player4.setVisibility(View.INVISIBLE);
            avatarTeam1Player5.setVisibility(View.INVISIBLE);
            avatarTeam2Player1.setVisibility(View.INVISIBLE);
            avatarTeam2Player2.setVisibility(View.INVISIBLE);
            avatarTeam2Player3.setVisibility(View.INVISIBLE);
            avatarTeam2Player4.setVisibility(View.INVISIBLE);
            avatarTeam2Player5.setVisibility(View.INVISIBLE);

            // configure text views
            // 1. player names
            Typeface tfPlayernames = Typeface.createFromAsset(this.getAssets(), "Nunito-SemiBold.ttf");
            namesTeam1[3].setText(players.get(0));
            namesTeam1[3].setTypeface(tfPlayernames);
            namesTeam1[4].setText(players.get(1));
            namesTeam1[4].setTypeface(tfPlayernames);
            namesTeam2[3].setText(players.get(2));
            namesTeam2[3].setTypeface(tfPlayernames);
            namesTeam2[4].setText(players.get(3));
            namesTeam2[4].setTypeface(tfPlayernames);

            // 2. team names
            namesTeam1[1].setText(teamNames.get(0));
            namesTeam1[1].setTextSize(20f);
            Typeface tf = ResourcesCompat.getFont(this, R.font.nunito_bold);//Typeface.createFromAsset(this.getAssets(), "Nunito-Bold.ttf");
            namesTeam1[1].setTypeface(tf);
            namesTeam2[1].setText(teamNames.get(1));
            namesTeam2[1].setTypeface(tf);
            namesTeam2[1].setTextSize(20f);



            referenceUsers = rootNode.getReference("tournaments/" + tournamentName + "/users");

            referenceTeams = rootNode.getReference("tournaments/" + tournamentName + "/teams");
            referenceGames = rootNode.getReference("tournaments/" + tournamentName + "/games");
        }
        else{
            // normal game:
            if (numPlayersTeam1 <=3){
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(containerTeam1);
                constraintSet.connect(R.id.layout_team1_player1, ConstraintSet.BOTTOM, R.id.layout_team1, ConstraintSet.BOTTOM, 0);
                constraintSet.applyTo(containerTeam1);
            }

            if (numPlayersTeam2 <=3){
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(containerTeam2);
                constraintSet.connect(R.id.layout_team2_player1, ConstraintSet.BOTTOM, R.id.layout_team2, ConstraintSet.BOTTOM, 0);
                constraintSet.applyTo(containerTeam2);
            }

            referenceGames = rootNode.getReference(league + "/games");
            referenceUsers = rootNode.getReference(league + "/users");

            // count existing games to update maxid
            referenceGames.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        maxId = (dataSnapshot.getChildrenCount()) + 1;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            // iterate through all players
            for (int j=0; j<numPlayers; j++) {
                String player = players.get(j);
                // add player to team 1 or team 2
                if( teamPartition[j]) {
                    playersTeam1.add(player);
                }
                else {
                    playersTeam2.add(player);
                }

                TypedArray heads = getResources().obtainTypedArray(R.array.heads);
                // get avatar, name, and elo of the player:
                referenceUsers.orderByChild("name").equalTo(player).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            UserHelperClass playerObj = ds.getValue(UserHelperClass.class);
                            String player = playerObj.getName();

                            headId = playerObj.getHeadId();

                            // Configure the corresponding Layout: Image, Text, Parameters
                            if (playersTeam1.contains(player)) {
                                namesTeam1[indexPlayerTeam1].setText(player);

                                avatarsTeam1[indexPlayerTeam1].setImageResource(heads.getResourceId(headId, 0));

                                if (indexPlayerTeam1 ==numPlayersTeam1 - 1){ // i.e. this is the last player
                                    if (indexPlayerTeam1 !=4) { //new constraints aren't necessary for the 5th player
                                        // set right constraint to parent
                                        ConstraintSet constraintSet = new ConstraintSet();
                                        constraintSet.clone(containerTeam1);
                                        constraintSet.connect(layoutsTeam1[indexPlayerTeam1].getId(), ConstraintSet.RIGHT, containerTeam1.getId(), ConstraintSet.RIGHT, 0);
                                        if (indexPlayerTeam1 ==3){
                                            // additionally, set left constraint differently:
                                            constraintSet.connect(layoutsTeam1[3].getId(), ConstraintSet.LEFT, layoutsTeam1[1].getId(), ConstraintSet.LEFT, 0);
                                            constraintSet.connect(layoutsTeam1[3].getId(), ConstraintSet.RIGHT, layoutsTeam1[1].getId(), ConstraintSet.RIGHT, 0);
                                        }
                                        constraintSet.applyTo(containerTeam1);

                                        // ...and then set the others to invisble
                                        for (int j = numPlayersTeam1; j < 5; j++) {
                                            layoutsTeam1[j].setVisibility(View.INVISIBLE);
                                        }
                                    }
                                }
                                indexPlayerTeam1 = indexPlayerTeam1 + 1;
                                sumEloTeam1 = sumEloTeam1 + playerObj.getElo();
                            }
                            else {
                                namesTeam2[indexPlayerTeam2].setText(player);

                                avatarsTeam2[indexPlayerTeam2].setImageResource(heads.getResourceId(headId, 0));

                                if (indexPlayerTeam2 == numPlayersTeam2 - 1){ // i.e. this is the last player
                                    if (indexPlayerTeam2 !=4) { //new constraints aren't necessary for the 5th player
                                        // set right constraint to parent
                                        ConstraintSet constraintSet = new ConstraintSet();
                                        constraintSet.clone(containerTeam2);
                                        constraintSet.connect(layoutsTeam2[indexPlayerTeam2].getId(), ConstraintSet.RIGHT, containerTeam2.getId(), ConstraintSet.RIGHT, 0);
                                        if (indexPlayerTeam2 ==3){
                                            // additionally, set left constraint differently:
                                            constraintSet.connect(layoutsTeam2[3].getId(), ConstraintSet.LEFT, layoutsTeam2[1].getId(), ConstraintSet.LEFT, 0);
                                            constraintSet.connect(layoutsTeam2[3].getId(), ConstraintSet.RIGHT, layoutsTeam2[1].getId(), ConstraintSet.RIGHT, 0);

                                        }
                                        constraintSet.applyTo(containerTeam2);
                                        // ...and then set the others to invisble
                                        for (int j = numPlayersTeam2; j < 5; j++) {
                                            layoutsTeam2[j].setVisibility(View.INVISIBLE);
                                        }
                                    }
                                }
                                indexPlayerTeam2 = indexPlayerTeam2 + 1;
                                sumEloTeam2 = sumEloTeam2 + playerObj.getElo();
                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }

    }

    public void selectWinner(View view) {
        winnerDetermined = true;
        ConstraintLayout first_team_button = findViewById(R.id.layout_team1);
        ConstraintLayout second_team_button = findViewById(R.id.layout_team2);
        first_team_button.setBackgroundResource(R.drawable.button_chooseselection);
        second_team_button.setBackgroundResource(R.drawable.button_chooseselection);
        view.setBackgroundResource(R.drawable.button_winnerteam);

        Integer id = view.getId();
        team1Win = id == first_team_button.getId();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void endGame(View view) {
        // if no winner is chosen, do nothing
        if(winnerDetermined ==false){
            Toast.makeText(this, "Wähle einen Gewinner aus.", Toast.LENGTH_SHORT).show();
        }
        else {
            // Knechtung?
            CheckBox checkbox_knechtung = findViewById(R.id.checkbox_knechtung);
            knechtung = checkbox_knechtung.isChecked();

            // Update the data base
            rootNode = FirebaseDatabase.getInstance(linkDatabase);
            if(league.startsWith("tournament_")) {
                Intent i = new Intent(this, OverviewTournamentActivity.class);
                i.putExtra("source", "whoWon");
                i.putExtra("team1win", team1Win);
                i.putExtra("teamNames", teamNames);
                i.putStringArrayListExtra("players", players);


                // Update users:
                for(String player: players){
                    referenceUsers.child(player).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            //for(DataSnapshot ds:dataSnapshot.getChildren()){
                            UserTournamentHelperClass playerObj = dataSnapshot.getValue(UserTournamentHelperClass.class);
                            // 1. find player in playerslist
                            int counter = 0;
                            int pos=0;
                            for(String name_player: players){
                                if(name_player.equals(player)){
                                    pos = counter;
                                }
                                counter = counter + 1;
                            }
                            // 2.find scorings of player
                            int numTreffer = scores.get(pos);
                            int numStrafbier = strafbier.get(pos);
                            int numStrafrunde = strafrunde.get(pos);
                            // 3. get old scorings
                            int oldNumTreffer = playerObj.getNumTreffer();
                            int oldNumStrafbier = playerObj.getNumStrafbier();
                            int oldNumStrafrunde = playerObj.getNumStrafschluck();
                            // 4. update all scorings
                            playerObj.setNumTreffer(oldNumTreffer + numTreffer);
                            playerObj.setNumStrafbier(oldNumStrafbier + numStrafbier);
                            playerObj.setNumStrafschluck(oldNumStrafrunde + numStrafrunde);
                            //}

                            referenceUsers.child(dataSnapshot.getKey()).setValue(playerObj);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                // Update games:
                for(int group=0; group<4; group++){ // This version only supports 4 groups
                    referenceGames.child(String.valueOf(group)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot ds:dataSnapshot.getChildren()){
                                GamesTournamentHelperClass game = ds.getValue(GamesTournamentHelperClass.class);
                                if(game.getTeamNames().get(0).equals(teamNames.get(0))) {
                                    if (game.getTeamNames().get(1).equals(teamNames.get(1))) {
                                        // i.e. we have found the correct game

                                        // fill numTreffer1 and numTreffer2
                                        ArrayList<String> namesTeam1 = game.getTeam1Players();
                                        ArrayList<Integer> numTreffer1 = new ArrayList<>();
                                        for(String name:namesTeam1){
                                            for(int index = 0; index < players.size(); index++){
                                                if(name.equals(players.get(index))){
                                                    numTreffer1.add(scores.get(index));
                                                }
                                            }
                                        }
                                        ArrayList<String> namesTeam2 = game.getTeam2Players();
                                        ArrayList<Integer> numTreffer2 = new ArrayList<>();
                                        for(String name:namesTeam2){
                                            for(int index = 0; index < players.size(); index++){
                                                if(name.equals(players.get(index))){
                                                    numTreffer2.add(scores.get(index));
                                                }
                                            }
                                        }

                                        game.setNumTreffer1(numTreffer1);
                                        game.setNumTreffer2(numTreffer2);
                                        game.setPlayed(true);
                                        game.setKnechtung(knechtung);
                                        game.setTimeEnd(new Date());
                                        game.setTeam1Win(team1Win);
                                        game.setTimeStart(timeStart);

                                        referenceGames.child(String.valueOf(dataSnapshot.getKey())).child(ds.getKey()).setValue(game);
                                    }
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }


                // Update teams:
                referenceTeams.child(teamNames.get(0)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        TeamsHelperClass team = dataSnapshot.getValue(TeamsHelperClass.class);
                        if(team1Win){
                            int numWins = team.getWinsGroup();
                            team.setWinsGroup(numWins + 1);
                        }else{
                            int numDefeats = team.getDefeatsGroup();
                            team.setDefeatsGroup(numDefeats + 1);
                        }
                        referenceTeams.child(dataSnapshot.getKey()).setValue(team);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                referenceTeams.child(teamNames.get(1)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        TeamsHelperClass team = dataSnapshot.getValue(TeamsHelperClass.class);
                        if(team1Win){
                            int numDefeats = team.getDefeatsGroup();
                            team.setDefeatsGroup(numDefeats + 1);
                        }else{
                            int numWins = team.getWinsGroup();
                            team.setWinsGroup(numWins + 1);
                        }
                        referenceTeams.child(dataSnapshot.getKey()).setValue(team);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                i.putExtra("tournament_name", tournamentName);
                startActivity(i);
            }
            else{
                Intent next_i = new Intent(this, MainActivity.class);
                referenceUsers = rootNode.getReference(league + "/users");

                // 1. users
                ArrayList<Integer> eloUpdates = computeElo(sumEloTeam1 / numPlayersTeam1, sumEloTeam2 / numPlayersTeam2, team1Win, knechtung);

                int scoreCounter = 0;
                for (String player : players) {
                    int scoreOfPlayer = scores.get(scoreCounter);
                    int strafbierOfPlayer = strafbier.get(scoreCounter);
                    int strafrundeOfPlayer = strafrunde.get(scoreCounter);
                    int finalScoreCounter = scoreCounter;
                    referenceUsers.orderByChild("name").equalTo(player).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                UserHelperClass playerObj = ds.getValue(UserHelperClass.class);
                                int oldNumTreffer = playerObj.getNumTreffer();
                                int oldNumStrafrunden = playerObj.getNumStrafrunden();
                                int oldNumStrafbier = playerObj.getNumStrafbier();
                                Integer oldElo = playerObj.getElo();

                                // update num_siege, num_niederlagen, num_knechtungen, num_wurdegeknechtet
                                if (team1Win) {
                                    if (teamPartition[finalScoreCounter]) { // player is in team 1
                                        int oldNumWins = playerObj.getNumWins();
                                        playerObj.setNumWins(oldNumWins + 1);
                                        if (knechtung) {
                                            int oldNumKnechtungen = playerObj.getNumKnechtungen();
                                            playerObj.setNumKnechtungen(oldNumKnechtungen + 1);
                                        }
                                        playerObj.setElo(oldElo + eloUpdates.get(0));
                                    } else { // player is in team 2
                                        int oldNumNiederlagen = playerObj.getNumLoses();
                                        playerObj.setNumLoses(oldNumNiederlagen + 1);
                                        if (knechtung) {
                                            int oldNumWasKnechtet = playerObj.getNumWasKnechtet();
                                            playerObj.setNumWasKnechtet(oldNumWasKnechtet + 1);
                                        }
                                        playerObj.setElo(oldElo + eloUpdates.get(1));
                                    }
                                } else {
                                    if (teamPartition[finalScoreCounter]) {
                                        int oldNumLoses = playerObj.getNumLoses();
                                        playerObj.setNumLoses(oldNumLoses + 1);
                                        if (knechtung) {
                                            int oldNumWasKnechtet = playerObj.getNumWasKnechtet();
                                            playerObj.setNumWasKnechtet(oldNumWasKnechtet + 1);
                                        }
                                        playerObj.setElo(oldElo + eloUpdates.get(0));
                                    } else {
                                        int oldNumWins = playerObj.getNumWins();
                                        playerObj.setNumWins(oldNumWins + 1);
                                        if (knechtung) {
                                            int oldNumKnechtungen = playerObj.getNumKnechtungen();
                                            playerObj.setNumKnechtungen(oldNumKnechtungen + 1);
                                        }
                                        playerObj.setElo(oldElo + eloUpdates.get(1));
                                    }
                                }

                                playerObj.setNumTreffer(oldNumTreffer + scoreOfPlayer);
                                playerObj.setNumStrafbier(oldNumStrafbier + strafbierOfPlayer);
                                playerObj.setNumStrafrunden(oldNumStrafrunden + strafrundeOfPlayer);
                                referenceUsers.child(player).setValue(playerObj);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    scoreCounter = scoreCounter + 1;
                }

                // 2. games
                ArrayList<String> team1Players = new ArrayList<String>();
                ArrayList<String> team2Players = new ArrayList<String>();
                ArrayList<Integer> numTreffer1 = new ArrayList<Integer>();
                ArrayList<Integer> numTreffer2 = new ArrayList<Integer>();
                ArrayList<Integer> numStrafbier1 = new ArrayList<Integer>();
                ArrayList<Integer> numStrafbier2 = new ArrayList<Integer>();
                ArrayList<Integer> numStrafrunde1 = new ArrayList<Integer>();
                ArrayList<Integer> numStrafrunde2 = new ArrayList<Integer>();


                for (int i = 0; i < players.size(); i++) {
                    String player = players.get(i);
                    int scorePlayer = scores.get(i);
                    int strafbierPlayer = strafbier.get(i);
                    int strafrundePlayer = strafrunde.get(i);
                    if (teamPartition[i] == true) {
                        team1Players.add(player);
                        numTreffer1.add(scorePlayer);
                        numStrafbier1.add(strafbierPlayer);
                        numStrafrunde1.add(strafrundePlayer);
                    } else {
                        team2Players.add(player);
                        numTreffer2.add(scorePlayer);
                        numStrafbier2.add(strafbierPlayer);
                        numStrafrunde2.add(strafrundePlayer);
                    }
                }
                Date timeEnd = new Date();
                GamesHelperClass gamesClass = new GamesHelperClass(team1Players, team2Players, numTreffer1, numTreffer2, timeStart, timeEnd);
                gamesClass.setStrafbier1(numStrafbier1);
                gamesClass.setStrafbier2(numStrafbier2);
                gamesClass.setStrafrunde1(numStrafrunde1);
                gamesClass.setStrafrunde2(numStrafrunde2);
                gamesClass.setKnechtung(knechtung);
                if (trackGeo) {
                    gamesClass.setGeotag(geotag);
                }
                gamesClass.setTeam1Win(team1Win);


                referenceGames = rootNode.getReference(league + "/games");
                referenceGames.child(String.valueOf(maxId)).setValue(gamesClass);

                // Notifications:
                boolean dev_version = this.getResources().getBoolean(R.bool.dev_version);
                // make notifications only in the production version
                if(!dev_version) {
                    FirebaseMessaging.getInstance().subscribeToTopic("all");
                    String messageTitle;
                    String messageBody;
                    if (knechtung) {
                        messageTitle = "Knechtsirene!";
                        if (team1Win) {
                            if (team2Players.size() == 1) {
                                messageBody = team2Players.get(0) + " wurde geknechtet!";
                            } else {
                                messageBody = team2Players.get(0) + " und Co. wurden geknechtet!";
                            }
                        } else {
                            if (team1Players.size() == 1) {
                                messageBody = team1Players.get(0) + " wurde geknechtet!";
                            } else {
                                messageBody = team1Players.get(0) + " und Co. wurden geknechtet!";
                            }
                        }
                    } else {
                        messageTitle = "Flunkereien!";
                        messageBody = team1Players.get(0) + " und Co. haben soeben geflunkt!";
                    }
                    FcmNotificationsSender notificationsSender = new FcmNotificationsSender("/topics/all",
                            messageTitle,
                            messageBody,
                            getApplicationContext(),
                            WhoWonActivity.this);
                    notificationsSender.SendNotifications();
                }

                startActivity(next_i);
            }

        }
    }
}