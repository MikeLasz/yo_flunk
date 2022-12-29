package com.example.yo_flunk;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Date;

import static com.example.yo_flunk.Utility.dp2pix;
import static java.lang.Math.max;

public class PlayGameActivity extends AppCompatActivity{
    public static boolean[] teamPartition;
    Long longStart;
    Date dateStart = new Date();
    boolean notification, trackGeo;
    ArrayList<String> players, teamNames;
    String league, geotag, lat, lon;
    LinearLayout team1Treffer, team1Strafschluck, team1Strafbier, team2Treffer, team2Strafschluck, team2Strafbier;

    // Class and constants responsible for GPS tracking
    FusedLocationProviderClient fusedLocationProviderClient;
    Task<LocationSettingsResponse> task;

    private static final int PERMISSIONS_FINE_LOCATION = 99;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    final static int REQUEST_LOCATION = 1;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);

        // get data from last intent: players that play, team partitions, and so on.
        Intent i = getIntent();
        players = i.getStringArrayListExtra("players");
        teamPartition = i.getBooleanArrayExtra("partition");
        notification = i.getBooleanExtra("notification", true);
        league = i.getStringExtra("league");
        if(league.startsWith("tournament_")){
            teamNames = i.getStringArrayListExtra("teamnames");
        }
        trackGeo = i.getBooleanExtra("track_geo", true);
        if(trackGeo){
            setupGeoTracking(); // activate GPS and/or ask for permissions
            getGeo(); // get lon and lat
            geotag = i.getStringExtra("geotag");
        }

        // count players in team1 and team2
        int numPlayers1 = 0;
        int numPlayers2 = 0;
        for(boolean assignment: teamPartition){
            if(assignment==true){
                numPlayers1 = numPlayers1 + 1;
            }
            else{
                numPlayers2 = numPlayers2 + 1;
            }
        }
        longStart = dateStart.getTime();

        // find all views:
        team1Treffer = findViewById(R.id.Container_team1Treffer);
        team1Strafschluck = findViewById(R.id.Container_team1Strafschluck);
        team1Strafbier = findViewById(R.id.Container_team1Strafbier);
        team2Treffer = findViewById(R.id.Container_team2Treffer);
        team2Strafschluck = findViewById(R.id.Container_team2Strafschluck);
        team2Strafbier = findViewById(R.id.Container_team2Strafbier);

        LinearLayout layoutTeam1Player1 = findViewById(R.id.layout_team1_player1);
        LinearLayout layoutTeam1Player2 = findViewById(R.id.layout_team1_player2);
        LinearLayout layoutTeam1Player3 = findViewById(R.id.layout_team1_player3);
        LinearLayout layoutTeam1Player4 = findViewById(R.id.layout_team1_player4);
        LinearLayout layoutTeam1Player5 = findViewById(R.id.layout_team1_player5);
        LinearLayout layoutTeam2Player1 = findViewById(R.id.layout_team2_player1);
        LinearLayout layoutTeam2Player2 = findViewById(R.id.layout_team2_player2);
        LinearLayout layoutTeam2Player3 = findViewById(R.id.layout_team2_player3);
        LinearLayout layoutTeam2Player4 = findViewById(R.id.layout_team2_player4);
        LinearLayout layoutTeam2Player5 = findViewById(R.id.layout_team2_player5);

        LinearLayout layoutTeam1Player1Strafschluck = findViewById(R.id.layout_team1_player1_strafschluck);
        LinearLayout layoutTeam1Player2Strafschluck = findViewById(R.id.layout_team1_player2_strafschluck);
        LinearLayout layoutTeam1Player3Strafschluck = findViewById(R.id.layout_team1_player3_strafschluck);
        LinearLayout layoutTeam1Player4Strafschluck = findViewById(R.id.layout_team1_player4_strafschluck);
        LinearLayout layoutTeam1Player5Strafschluck = findViewById(R.id.layout_team1_player5_strafschluck);
        LinearLayout layoutTeam2Player1Strafschluck = findViewById(R.id.layout_team2_player1_strafschluck);
        LinearLayout layoutTeam2Player2Strafschluck = findViewById(R.id.layout_team2_player2_strafschluck);
        LinearLayout layoutTeam2Player3Strafschluck = findViewById(R.id.layout_team2_player3_strafschluck);
        LinearLayout layoutTeam2Player4Strafschluck = findViewById(R.id.layout_team2_player4_strafschluck);
        LinearLayout layoutTeam2Player5Strafschluck = findViewById(R.id.layout_team2_player5_strafschluck);

        LinearLayout layoutTeam1Player1Strafbier = findViewById(R.id.layout_team1_player1_strafbier);
        LinearLayout layoutTeam1Player2Strafbier = findViewById(R.id.layout_team1_player2_strafbier);
        LinearLayout layoutTeam1Player3Strafbier = findViewById(R.id.layout_team1_player3_strafbier);
        LinearLayout layoutTeam1Player4Strafbier = findViewById(R.id.layout_team1_player4_strafbier);
        LinearLayout layoutTeam1Player5Strafbier = findViewById(R.id.layout_team1_player5_strafbier);
        LinearLayout layoutTeam2Player1Strafbier = findViewById(R.id.layout_team2_player1_strafbier);
        LinearLayout layoutTeam2Player2Strafbier = findViewById(R.id.layout_team2_player2_strafbier);
        LinearLayout layoutTeam2Player3Strafbier = findViewById(R.id.layout_team2_player3_strafbier);
        LinearLayout layoutTeam2Player4Strafbier = findViewById(R.id.layout_team2_player4_strafbier);
        LinearLayout layoutTeam2Player5Strafbier = findViewById(R.id.layout_team2_player5_strafbier);


        // Define a list to iterate through all views
        LinearLayout[] layoutTeam1 = new LinearLayout[5];
        layoutTeam1[0] = layoutTeam1Player1;
        layoutTeam1[1] = layoutTeam1Player2;
        layoutTeam1[2] = layoutTeam1Player3;
        layoutTeam1[3] = layoutTeam1Player4;
        layoutTeam1[4] = layoutTeam1Player5;
        LinearLayout[] layoutTeam2 = new LinearLayout[5];
        layoutTeam2[0] = layoutTeam2Player1;
        layoutTeam2[1] = layoutTeam2Player2;
        layoutTeam2[2] = layoutTeam2Player3;
        layoutTeam2[3] = layoutTeam2Player4;
        layoutTeam2[4] = layoutTeam2Player5;

        LinearLayout[] layoutTeam1Strafschluck = new LinearLayout[5];
        layoutTeam1Strafschluck[0] = layoutTeam1Player1Strafschluck;
        layoutTeam1Strafschluck[1] = layoutTeam1Player2Strafschluck;
        layoutTeam1Strafschluck[2] = layoutTeam1Player3Strafschluck;
        layoutTeam1Strafschluck[3] = layoutTeam1Player4Strafschluck;
        layoutTeam1Strafschluck[4] = layoutTeam1Player5Strafschluck;
        LinearLayout[] layoutTeam2Strafschluck = new LinearLayout[5];
        layoutTeam2Strafschluck[0] = layoutTeam2Player1Strafschluck;
        layoutTeam2Strafschluck[1] = layoutTeam2Player2Strafschluck;
        layoutTeam2Strafschluck[2] = layoutTeam2Player3Strafschluck;
        layoutTeam2Strafschluck[3] = layoutTeam2Player4Strafschluck;
        layoutTeam2Strafschluck[4] = layoutTeam2Player5Strafschluck;

        LinearLayout[] layoutTeam1Strafbier = new LinearLayout[5];
        layoutTeam1Strafbier[0] = layoutTeam1Player1Strafbier;
        layoutTeam1Strafbier[1] = layoutTeam1Player2Strafbier;
        layoutTeam1Strafbier[2] = layoutTeam1Player3Strafbier;
        layoutTeam1Strafbier[3] = layoutTeam1Player4Strafbier;
        layoutTeam1Strafbier[4] = layoutTeam1Player5Strafbier;
        LinearLayout[] layoutTeam2Strafbier = new LinearLayout[5];
        layoutTeam2Strafbier[0] = layoutTeam2Player1Strafbier;
        layoutTeam2Strafbier[1] = layoutTeam2Player2Strafbier;
        layoutTeam2Strafbier[2] = layoutTeam2Player3Strafbier;
        layoutTeam2Strafbier[3] = layoutTeam2Player4Strafbier;
        layoutTeam2Strafbier[4] = layoutTeam2Player5Strafbier;


        // Initialize the buttons to define onLongClickListener
        TextView[] buttonsTeam1 = new TextView[5];
        TextView[] buttonsTeam1Strafschluck = new TextView[5];
        TextView[] buttonsTeam1Strafbier = new TextView[5];
        int counterPlayersTeam1 = 0;
        // collect all player names
        for(LinearLayout layoutPlayer:layoutTeam1){
            // The textview is either at index 0 or at index 1
            TextView namePlayer = new TextView(this);
            namePlayer = (TextView) layoutPlayer.getChildAt(1);
            buttonsTeam1[counterPlayersTeam1] = namePlayer;
            buttonsTeam1Strafschluck[counterPlayersTeam1] = namePlayer;
            buttonsTeam1Strafbier[counterPlayersTeam1] = namePlayer;
            counterPlayersTeam1 = counterPlayersTeam1 + 1;
        }

        TextView[] buttonsTeam2 = new TextView[5];
        TextView[] buttonsTeam2Strafschluck = new TextView[5];
        TextView[] buttonsTeam2Strafbier = new TextView[5];
        // collect all player names
        int counterPlayersTeam2 = 0;
        for(LinearLayout layoutPlayer:layoutTeam2){
            TextView namePlayer = new TextView(this);
            namePlayer = (TextView) layoutPlayer.getChildAt(1);
            buttonsTeam2[counterPlayersTeam2] = namePlayer;
            buttonsTeam2Strafschluck[counterPlayersTeam2] = namePlayer;
            buttonsTeam2Strafbier[counterPlayersTeam2] = namePlayer;
            counterPlayersTeam2 = counterPlayersTeam2 + 1;
        }

        // define onLongClickListeners for each player (decrease score on long click)
        for(TextView namePlayer:buttonsTeam1){
            namePlayer.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // read current score, and decrease by 1.
                    LinearLayout layoutPlayer = (LinearLayout) v.getParent();
                    TextView txtViewScorePlayer;
                    // layoutPlayer has either children button (with name), view (for spacing), txtview (with score)   [Ordering 1]
                    // or reversed, i.e. txtview (with score), view (for spacing), button (with name)      [Ordering 2]
                    if(((TextView) layoutPlayer.getChildAt(0)).getText().equals("placeholder")){
                        // Ordering 1
                        txtViewScorePlayer = (TextView) layoutPlayer.getChildAt(2);
                    }
                    else{
                        // Ordering 2
                        txtViewScorePlayer = (TextView) layoutPlayer.getChildAt(0);
                    }
                    int scorePlayer = Integer.parseInt((String) txtViewScorePlayer.getText());
                    scorePlayer = max(scorePlayer - 1, 0);
                    txtViewScorePlayer.setText(Integer.toString(scorePlayer));
                    return true;
                }
            });
        }
        for(TextView namePlayer:buttonsTeam2){
            namePlayer.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // read current score, and decrease by 1.
                    LinearLayout layoutPlayer = (LinearLayout) v.getParent();
                    TextView txtViewScorePlayer;
                    // layoutPlayer has either children button (with name), view (for spacing), txtview (with score)   [Ordering 1]
                    // or reversed, i.e. txtview (with score), view (for spacing), button (with name)      [Ordering 2]
                    if(((TextView) layoutPlayer.getChildAt(0)).getText().equals("placeholder")){
                        // Ordering 1
                        txtViewScorePlayer = (TextView) layoutPlayer.getChildAt(2);
                    }
                    else{
                        // Ordering 2
                        txtViewScorePlayer = (TextView) layoutPlayer.getChildAt(0);
                    }
                    int scorePlayer = Integer.parseInt((String) txtViewScorePlayer.getText());
                    scorePlayer = max(scorePlayer - 1, 0);
                    txtViewScorePlayer.setText(Integer.toString(scorePlayer));
                    return true;
                }
            });
        }

        // define onLongClickListeners for each player (strafschluck) (decrease score on long click)
        for(TextView namePlayer:buttonsTeam1Strafschluck){
            namePlayer.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    LinearLayout layoutPlayer = (LinearLayout) v.getParent();
                    TextView txtViewStrafschluckPlayer;
                    // layoutPlayer has either children button (with name), view (for spacing), txtview (with score)   [Ordering 1]
                    // or reversed, i.e. txtview (with score), view (for spacing), button (with name)      [Ordering 2]
                    if(((TextView) layoutPlayer.getChildAt(0)).getText().equals("placeholder")){
                        // Ordering 1
                        txtViewStrafschluckPlayer = (TextView) layoutPlayer.getChildAt(2);
                    }
                    else{
                        // Ordering 2
                        txtViewStrafschluckPlayer = (TextView) layoutPlayer.getChildAt(0);
                    }
                    int strafschluckPlayer = Integer.parseInt((String) txtViewStrafschluckPlayer.getText());
                    strafschluckPlayer = max(strafschluckPlayer - 1, 0);
                    txtViewStrafschluckPlayer.setText(Integer.toString(strafschluckPlayer));
                    return true;
                }
            });
        }
        for(TextView namePlayer:buttonsTeam2Strafschluck){
            namePlayer.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    LinearLayout layoutPlayer = (LinearLayout) v.getParent();
                    TextView txtViewStrafschluckPlayer;
                    // layoutPlayer has either children button (with name), view (for spacing), txtview (with score)   [Ordering 1]
                    // or reversed, i.e. txtview (with score), view (for spacing), button (with name)      [Ordering 2]
                    if(((TextView) layoutPlayer.getChildAt(0)).getText().equals("placeholder")){
                        // Ordering 1
                        txtViewStrafschluckPlayer = (TextView) layoutPlayer.getChildAt(2);
                    }
                    else{
                        // Ordering 2
                        txtViewStrafschluckPlayer = (TextView) layoutPlayer.getChildAt(0);
                    }
                    int strafschluckPlayer = Integer.parseInt((String) txtViewStrafschluckPlayer.getText());
                    strafschluckPlayer = max(strafschluckPlayer - 1, 0);
                    txtViewStrafschluckPlayer.setText(Integer.toString(strafschluckPlayer));
                    return true;
                }
            });
        }
        // define onLongClickListeners for each player (strafbier) (decrease score on long click)
        for(TextView namePlayer:buttonsTeam1Strafbier){
            namePlayer.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    LinearLayout layoutPlayer = (LinearLayout) v.getParent();
                    TextView txtViewStrafbierPlayer;
                    // layoutPlayer has either children button (with name), view (for spacing), txtview (with score)   [Ordering 1]
                    // or reversed, i.e. txtview (with score), view (for spacing), button (with name)      [Ordering 2]
                    if(((TextView) layoutPlayer.getChildAt(0)).getText().equals("placeholder")){
                        // Ordering 1
                        txtViewStrafbierPlayer = (TextView) layoutPlayer.getChildAt(2);
                    }
                    else{
                        // Ordering 2
                        txtViewStrafbierPlayer = (TextView) layoutPlayer.getChildAt(0);
                    }
                    int strafbierPlayer = Integer.parseInt((String) txtViewStrafbierPlayer.getText());
                    strafbierPlayer = max(strafbierPlayer - 1, 0);
                    txtViewStrafbierPlayer.setText(Integer.toString(strafbierPlayer));
                    return true;
                }
            });
        }
        for(TextView namePlayer:buttonsTeam2Strafbier){
            namePlayer.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    LinearLayout layoutPlayer = (LinearLayout) v.getParent();
                    TextView txtViewStrafbierPlayer;
                    // layoutPlayer has either children button (with name), view (for spacing), txtview (with score)   [Ordering 1]
                    // or reversed, i.e. txtview (with score), view (for spacing), button (with name)      [Ordering 2]
                    if(((TextView) layoutPlayer.getChildAt(0)).getText().equals("placeholder")){
                        // Ordering 1
                        txtViewStrafbierPlayer = (TextView) layoutPlayer.getChildAt(2);
                    }
                    else{
                        // Ordering 2
                        txtViewStrafbierPlayer = (TextView) layoutPlayer.getChildAt(0);
                    }
                    int strafbierPlayer = Integer.parseInt((String) txtViewStrafbierPlayer.getText());
                    strafbierPlayer = max(strafbierPlayer - 1, 0);
                    txtViewStrafbierPlayer.setText(Integer.toString(strafbierPlayer));
                    return true;
                }
            });
        }

        // set up all sizes, margins, and texts
        // for the scores:
        int counterPlayer = 0;
        int counterPositionTeam1 = 0;
        int counterPositionTeam2 = 0;
        for(String namePlayer: players){
            if(teamPartition[counterPlayer] == true){
                // Team 1
                TextView txtViewPlayer = new TextView(this);
                txtViewPlayer = (TextView) layoutTeam1[counterPositionTeam1].getChildAt(1);
                txtViewPlayer.setText(namePlayer);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layoutTeam1[counterPositionTeam1].getLayoutParams();
                layoutParams.weight = 0.2f;
                // adjust margins:
                layoutParams.leftMargin = dp2pix(-4f, getApplicationContext());
                layoutParams.rightMargin = dp2pix(-4f, getApplicationContext());
                if(counterPositionTeam1 == 0){ // first namePlayer has 8dp margin left and -4dp margin right
                    layoutParams.leftMargin = dp2pix(8f, getApplicationContext());
                }
                if(counterPositionTeam1==(numPlayers1 - 1)){ // last namePlayer has 8dp right margin
                    layoutParams.rightMargin = dp2pix(8f, getApplicationContext());
                }
                layoutTeam1[counterPositionTeam1].setLayoutParams(layoutParams);
                counterPositionTeam1 = counterPositionTeam1 + 1;
            }
            else{
                // Team 2
                TextView txtViewPlayer = new TextView(this);
                txtViewPlayer = (TextView) layoutTeam2[counterPositionTeam2].getChildAt(1);
                txtViewPlayer.setText(namePlayer);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layoutTeam2[counterPositionTeam2].getLayoutParams();
                layoutParams.weight = 0.2f;
                // adjust margins:
                layoutParams.leftMargin = dp2pix(-4f, getApplicationContext());
                layoutParams.rightMargin = dp2pix(-4f, getApplicationContext());
                if(counterPositionTeam2 == 0){ //first namePlayer has 8dp left margin
                    layoutParams.leftMargin = dp2pix(8f, getApplicationContext());
                }
                if(counterPositionTeam2 == (numPlayers2 - 1)){ //... and last namePlayer has 8dp right margin
                    layoutParams.rightMargin = dp2pix(8f, getApplicationContext());
                }
                layoutTeam2[counterPositionTeam2].setLayoutParams(layoutParams);
                counterPositionTeam2 = counterPositionTeam2 + 1;
            }
            counterPlayer = counterPlayer + 1;
        }

        // set up all sizes, margins, and texts
        // for strafschluck:
        counterPlayer = 0;
        counterPositionTeam1 = 0;
        counterPositionTeam2 = 0;
        for(String namePlayer: players){
            if(teamPartition[counterPlayer] == true){
                // Team 1
                TextView txtViewPlayer = new TextView(this);
                txtViewPlayer = (TextView) layoutTeam1Strafschluck[counterPositionTeam1].getChildAt(1);
                txtViewPlayer.setText(namePlayer);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layoutTeam1Strafschluck[counterPositionTeam1].getLayoutParams();
                layoutParams.weight = 0.2f;
                // adjust margins:
                layoutParams.leftMargin = dp2pix(-4f, getApplicationContext());
                layoutParams.rightMargin = dp2pix(-4f, getApplicationContext());
                if(counterPositionTeam1 == 0){ // first namePlayer has 8dp margin left and -4dp margin right
                    layoutParams.leftMargin = dp2pix(8f, getApplicationContext());
                }
                if(counterPositionTeam1==(numPlayers1 - 1)){ // last namePlayer has 8dp right margin
                    layoutParams.rightMargin = dp2pix(8f, getApplicationContext());
                }
                layoutTeam1Strafschluck[counterPositionTeam1].setLayoutParams(layoutParams);
                counterPositionTeam1 = counterPositionTeam1 + 1;
            }
            else{
                // Team 2
                TextView txtViewPlayer = new TextView(this);
                txtViewPlayer = (TextView) layoutTeam2Strafschluck[counterPositionTeam2].getChildAt(1);
                txtViewPlayer.setText(namePlayer);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layoutTeam2Strafschluck[counterPositionTeam2].getLayoutParams();
                layoutParams.weight = 0.2f;
                // adjust margins:
                layoutParams.leftMargin = dp2pix(-4f, getApplicationContext());
                layoutParams.rightMargin = dp2pix(-4f, getApplicationContext());
                if(counterPositionTeam2 == 0){ //first namePlayer has 8dp left margin
                    layoutParams.leftMargin = dp2pix(8f, getApplicationContext());
                }
                if(counterPositionTeam2 == (numPlayers2 - 1)){ //... and last namePlayer has 8dp right margin
                    layoutParams.rightMargin = dp2pix(8f, getApplicationContext());
                }
                layoutTeam2Strafschluck[counterPositionTeam2].setLayoutParams(layoutParams);
                counterPositionTeam2 = counterPositionTeam2 + 1;
            }
            counterPlayer = counterPlayer + 1;
        }
        // set up all sizes, margins, and texts
        // for strafbier:
        counterPlayer = 0;
        counterPositionTeam1 = 0;
        counterPositionTeam2 = 0;
        for(String namePlayer: players){
            if(teamPartition[counterPlayer] == true){
                // Team 1
                TextView txtViewPlayer = new TextView(this);
                txtViewPlayer = (TextView) layoutTeam1Strafbier[counterPositionTeam1].getChildAt(1);
                txtViewPlayer.setText(namePlayer);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layoutTeam1Strafbier[counterPositionTeam1].getLayoutParams();
                layoutParams.weight = 0.2f;
                // adjust margins:
                layoutParams.leftMargin = dp2pix(-4f, getApplicationContext());
                layoutParams.rightMargin = dp2pix(-4f, getApplicationContext());
                if(counterPositionTeam1 == 0){ // first namePlayer has 8dp margin left and -4dp margin right
                    layoutParams.leftMargin = dp2pix(8f, getApplicationContext());
                }
                if(counterPositionTeam1==(numPlayers1 - 1)){ // last namePlayer has 8dp right margin
                    layoutParams.rightMargin = dp2pix(8f, getApplicationContext());
                }
                layoutTeam1Strafbier[counterPositionTeam1].setLayoutParams(layoutParams);
                counterPositionTeam1 = counterPositionTeam1 + 1;
            }
            else{
                // Team 2
                TextView txtViewPlayer = new TextView(this);
                txtViewPlayer = (TextView) layoutTeam2Strafbier[counterPositionTeam2].getChildAt(1);
                txtViewPlayer.setText(namePlayer);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layoutTeam2Strafbier[counterPositionTeam2].getLayoutParams();
                layoutParams.weight = 0.2f;
                // adjust margins:
                layoutParams.leftMargin = dp2pix(-4f, getApplicationContext());
                layoutParams.rightMargin = dp2pix(-4f, getApplicationContext());
                if(counterPositionTeam2 == 0){ //first namePlayer has 8dp left margin
                    layoutParams.leftMargin = dp2pix(8f, getApplicationContext());
                }
                if(counterPositionTeam2 == (numPlayers2 - 1)){ //... and last namePlayer has 8dp right margin
                    layoutParams.rightMargin = dp2pix(8f, getApplicationContext());
                }
                layoutTeam2Strafbier[counterPositionTeam2].setLayoutParams(layoutParams);
                counterPositionTeam2 = counterPositionTeam2 + 1;
            }
            counterPlayer = counterPlayer + 1;
        }


    }

    public void setupGeoTracking() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        task = client.checkLocationSettings(builder.build());
        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(PlayGameActivity.this,
                                REQUEST_CHECK_SETTINGS);

                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }


                }
            }
        });
    }

    public void getGeo(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(PlayGameActivity.this);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // we got permissions
                    if(location!=null){
                        lat = String.valueOf(location.getLatitude());
                        lon = String.valueOf(location.getLongitude());
                    }

                }
            });
        }
        else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATION);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(this, "Du hast die GPS-Erlaubnis erteilt.", Toast.LENGTH_SHORT).show();
                    getGeo();
                    //}
                } else {
                    Toast.makeText(this, "Um das Spiel auf der Heatmap anzuzeigen, brauchen wir deine GPS-Berechtigungen.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;

        }
    }

    // This function is called after the permission request.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("onActivityResult()", Integer.toString(resultCode));

        //final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_OK: {
                        break;
                    }
                    case Activity.RESULT_CANCELED: {
                        // The user was asked to change settings, but chose not to
                        Toast.makeText(PlayGameActivity.this, "Geo-tracking wurde nicht akzeptiert.", Toast.LENGTH_LONG).show();
                        break;
                    }
                    default: {
                        break;
                    }
                }
                break;
        }
    }




    public void gameIsOver(View view) {
        // 1. Safe all scores
        // 1.1. Get players
        // Intent i = getIntent();
        // ArrayList<String> players_list = i.getStringArrayListExtra("players");

        // 1.2. Get scores
        ArrayList<Integer> scores = new ArrayList<Integer>();
        ArrayList<Integer> strafbier = new ArrayList<Integer>();
        ArrayList<Integer> strafrunde = new ArrayList<Integer>();

        int counterTeam1 = 0; // counter increments when player from team 1 is iterated
        int counterTeam2 = 0; // counter increments when player from team 2 is iterated
        int counterPlayers = 0; // counter increments after each iteration
        for(String player: players){
            int score, scoreStrafrunde, scoreStrafbier;
            if(teamPartition[counterPlayers] == true){ // player is in team1
                // find score of this player
                // treffer:
                LinearLayout layoutPlayer = (LinearLayout) team1Treffer.getChildAt(counterTeam1);
                // Note that the position of the score is alternating.
                // Hence we need to check which of the views corresponds to the txtview showing the score.
                // I implemented it by checking whether the first child of the layout has a text equal to "placeholder".
                // If so, then I know that the txtview showing the score must be the third child, and vice versa.
                if(((TextView) layoutPlayer.getChildAt(0)).getText().equals("placeholder")) {
                    score = Integer.parseInt((String) ((TextView) layoutPlayer.getChildAt(2)).getText());
                }
                else{
                    score = Integer.parseInt((String) ((TextView) layoutPlayer.getChildAt(0)).getText());
                }
                scores.add(score);
                // strafrunde:
                layoutPlayer = (LinearLayout) team1Strafschluck.getChildAt(counterTeam1);
                if(((TextView) layoutPlayer.getChildAt(0)).getText().equals("placeholder")) {
                    scoreStrafrunde = Integer.parseInt((String) ((TextView) layoutPlayer.getChildAt(2)).getText());
                }
                else{
                    scoreStrafrunde = Integer.parseInt((String) ((TextView) layoutPlayer.getChildAt(0)).getText());
                }
                strafrunde.add(scoreStrafrunde);
                // strafbier:
                layoutPlayer = (LinearLayout) team1Strafbier.getChildAt(counterTeam1);
                if(((TextView) layoutPlayer.getChildAt(0)).getText().equals("placeholder")) {
                    scoreStrafbier = Integer.parseInt((String) ((TextView) layoutPlayer.getChildAt(2)).getText());
                }
                else{
                    scoreStrafbier = Integer.parseInt((String) ((TextView) layoutPlayer.getChildAt(0)).getText());
                }
                strafbier.add(scoreStrafbier);

                counterPlayers = counterPlayers + 1;
                counterTeam1 = counterTeam1 + 1;
            }
            else{ // player is in team2
                // find score of this player
                // treffer:
                LinearLayout layoutPlayer = (LinearLayout) team2Treffer.getChildAt(counterTeam2);
                if(((TextView) layoutPlayer.getChildAt(0)).getText().equals("placeholder")) {
                    score = Integer.parseInt((String) ((TextView) layoutPlayer.getChildAt(2)).getText());
                }
                else{
                    score = Integer.parseInt((String) ((TextView) layoutPlayer.getChildAt(0)).getText());
                }
                scores.add(score);
                // strafrunde:
                layoutPlayer = (LinearLayout) team2Strafschluck.getChildAt(counterTeam2);
                if(((TextView) layoutPlayer.getChildAt(0)).getText().equals("placeholder")) {
                    scoreStrafrunde = Integer.parseInt((String) ((TextView) layoutPlayer.getChildAt(2)).getText());
                }
                else{
                    scoreStrafrunde = Integer.parseInt((String) ((TextView) layoutPlayer.getChildAt(0)).getText());
                }
                strafrunde.add(scoreStrafrunde);
                // strafbier:
                layoutPlayer = (LinearLayout) team2Strafbier.getChildAt(counterTeam2);
                if(((TextView) layoutPlayer.getChildAt(0)).getText().equals("placeholder")) {
                    scoreStrafbier = Integer.parseInt((String) ((TextView) layoutPlayer.getChildAt(2)).getText());
                }
                else{
                    scoreStrafbier = Integer.parseInt((String) ((TextView) layoutPlayer.getChildAt(0)).getText());
                }
                strafbier.add(scoreStrafbier);

                counterPlayers = counterPlayers + 1;
                counterTeam2 = counterTeam2 + 1;
            }
        }

        Intent nextIntent = new Intent(this, WhoWonActivity.class);
        nextIntent.putStringArrayListExtra("players", players);
        nextIntent.putIntegerArrayListExtra("scores", scores);
        nextIntent.putIntegerArrayListExtra("strafbier", strafbier);
        nextIntent.putIntegerArrayListExtra("strafrunde", strafrunde);
        nextIntent.putExtra("time_start", longStart);
        nextIntent.putExtra("partition", teamPartition);
        nextIntent.putExtra("league", league);
        nextIntent.putExtra("notification", notification);
        nextIntent.putExtra("track_geo", trackGeo);
        if(trackGeo){
            String geotag = lat + "," + lon;
            nextIntent.putExtra("geotag", geotag);
        }
        if(league.startsWith("tournament_")){
            nextIntent.putExtra("time_start_tournament", dateStart);
            nextIntent.putStringArrayListExtra("team_names", teamNames);
        }
        startActivity(nextIntent);
    }

    public void trefferAnzeigen(View view) {
        // set the image highlighted
        LinearLayout layoutStrafbier = findViewById(R.id.bt_strafbier);
        LinearLayout layoutTreffer = findViewById(R.id.bt_treffer);
        LinearLayout layoutStrafschluck = findViewById(R.id.bt_strafschluck);


        layoutStrafbier.setBackground(null);
        layoutStrafschluck.setBackground(null);
        layoutTreffer.setBackgroundResource(R.drawable.view_withoutframe_foot); //

        // find all containers
        LinearLayout containerTreffer1 = findViewById(R.id.Container_team1Treffer);
        LinearLayout containerTreffer2 = findViewById(R.id.Container_team2Treffer);
        LinearLayout containerStrafschluck1 = findViewById(R.id.Container_team1Strafschluck);
        LinearLayout containerStrafschluck2 = findViewById(R.id.Container_team2Strafschluck);
        LinearLayout containerStrafbier1 = findViewById(R.id.Container_team1Strafbier);
        LinearLayout containerStrafbier2 = findViewById(R.id.Container_team2Strafbier);

        // set the visibility
        containerTreffer1.setVisibility(View.VISIBLE);
        containerTreffer2.setVisibility(View.VISIBLE);
        containerStrafbier1.setVisibility(View.INVISIBLE);
        containerStrafbier2.setVisibility(View.INVISIBLE);
        containerStrafschluck1.setVisibility(View.INVISIBLE);
        containerStrafschluck2.setVisibility(View.INVISIBLE);

        // change the weights
        // i.e. set weight of treffer to 0.3
        LinearLayout.LayoutParams containerTreffer1LayoutParams = (LinearLayout.LayoutParams) containerTreffer1.getLayoutParams();
        containerTreffer1LayoutParams.weight = 0.3f;
        containerTreffer1.setLayoutParams(containerTreffer1LayoutParams);
        LinearLayout.LayoutParams containerTreffer2LayoutParams = (LinearLayout.LayoutParams) containerTreffer2.getLayoutParams();
        containerTreffer2LayoutParams.weight = 0.3f;
        containerTreffer2.setLayoutParams(containerTreffer2LayoutParams);
        // ... and the others to 0
        LinearLayout.LayoutParams containerStrafbier1Params = (LinearLayout.LayoutParams) containerStrafbier1.getLayoutParams();
        containerStrafbier1Params.weight = 0.0f;
        containerStrafbier1.setLayoutParams(containerStrafbier1Params);
        LinearLayout.LayoutParams containerStrafbier2LayoutParams = (LinearLayout.LayoutParams) containerStrafbier2.getLayoutParams();
        containerStrafbier2LayoutParams.weight = 0.0f;
        containerStrafbier2.setLayoutParams(containerStrafbier2LayoutParams);
        LinearLayout.LayoutParams containerStrafschluck1LayoutParams = (LinearLayout.LayoutParams) containerStrafschluck1.getLayoutParams();
        containerStrafschluck1LayoutParams.weight = 0.0f;
        containerStrafschluck1.setLayoutParams(containerStrafschluck1LayoutParams);
        LinearLayout.LayoutParams containerStrafschluck2LayoutParams = (LinearLayout.LayoutParams) containerStrafschluck2.getLayoutParams();
        containerStrafschluck2LayoutParams.weight = 0.0f;
        containerStrafschluck2.setLayoutParams(containerStrafschluck2LayoutParams);
    }

    public void strafbierAnzeigen(View view) {
        // set the image highlighted
        LinearLayout layoutStrafbier = findViewById(R.id.bt_strafbier);
        LinearLayout layoutTreffer = findViewById(R.id.bt_treffer);
        LinearLayout layoutStrafschluck = findViewById(R.id.bt_strafschluck);

        layoutStrafschluck.setBackground(null);
        layoutTreffer.setBackground(null);
        layoutStrafbier.setBackgroundResource(R.drawable.view_withoutframe_foot);

        // find all containers
        LinearLayout containerTreffer1 = findViewById(R.id.Container_team1Treffer);
        LinearLayout containerTreffer2 = findViewById(R.id.Container_team2Treffer);
        LinearLayout containerStrafschluck1 = findViewById(R.id.Container_team1Strafschluck);
        LinearLayout containerStrafschluck2 = findViewById(R.id.Container_team2Strafschluck);
        LinearLayout containerStrafbier1 = findViewById(R.id.Container_team1Strafbier);
        LinearLayout containerStrafbier2 = findViewById(R.id.Container_team2Strafbier);

        // set the visibility
        containerTreffer1.setVisibility(View.INVISIBLE);
        containerTreffer2.setVisibility(View.INVISIBLE);
        containerStrafbier1.setVisibility(View.VISIBLE);
        containerStrafbier2.setVisibility(View.VISIBLE);
        containerStrafschluck1.setVisibility(View.INVISIBLE);
        containerStrafschluck2.setVisibility(View.INVISIBLE);

        // change the weights
        // i.e. set weight of strafbier to 0.3
        LinearLayout.LayoutParams containerStrafbier1Params = (LinearLayout.LayoutParams) containerStrafbier1.getLayoutParams();
        containerStrafbier1Params.weight = 0.3f;
        containerStrafbier1.setLayoutParams(containerStrafbier1Params);
        LinearLayout.LayoutParams containerStrafbier2LayoutParams = (LinearLayout.LayoutParams) containerStrafbier2.getLayoutParams();
        containerStrafbier2LayoutParams.weight = 0.3f;
        containerStrafbier2.setLayoutParams(containerStrafbier2LayoutParams);
        // ... and the others to 0
        LinearLayout.LayoutParams containerTreffer1LayoutParams = (LinearLayout.LayoutParams) containerTreffer1.getLayoutParams();
        containerTreffer1LayoutParams.weight = 0.0f;
        containerTreffer1.setLayoutParams(containerTreffer1LayoutParams);
        LinearLayout.LayoutParams containerTreffer2LayoutParams = (LinearLayout.LayoutParams) containerTreffer2.getLayoutParams();
        containerTreffer2LayoutParams.weight = 0.0f;
        containerTreffer2.setLayoutParams(containerTreffer2LayoutParams);
        LinearLayout.LayoutParams containerStrafschluck1LayoutParams = (LinearLayout.LayoutParams) containerStrafschluck1.getLayoutParams();
        containerStrafschluck1LayoutParams.weight = 0.0f;
        containerStrafschluck1.setLayoutParams(containerStrafschluck1LayoutParams);
        LinearLayout.LayoutParams containerStrafschluck2LayoutParams = (LinearLayout.LayoutParams) containerStrafschluck2.getLayoutParams();
        containerStrafschluck2LayoutParams.weight = 0.0f;
        containerStrafschluck2.setLayoutParams(containerStrafschluck2LayoutParams);
    }

    public void strafschluckAnzeigen(View view) {
        // set the image highlighted
        LinearLayout layoutStrafbier = findViewById(R.id.bt_strafbier);
        LinearLayout layoutTreffer = findViewById(R.id.bt_treffer);
        LinearLayout layoutStrafschluck = findViewById(R.id.bt_strafschluck);


        layoutStrafbier.setBackground(null);
        layoutTreffer.setBackground(null);
        layoutStrafschluck.setBackgroundResource(R.drawable.view_withoutframe_foot);



        // find all containers
        LinearLayout containerTreffer1 = findViewById(R.id.Container_team1Treffer);
        LinearLayout containerTreffer2 = findViewById(R.id.Container_team2Treffer);
        LinearLayout containerStrafschluck1 = findViewById(R.id.Container_team1Strafschluck);
        LinearLayout containerStrafschluck2 = findViewById(R.id.Container_team2Strafschluck);
        LinearLayout containerStrafbier1 = findViewById(R.id.Container_team1Strafbier);
        LinearLayout containerStrafbier2 = findViewById(R.id.Container_team2Strafbier);

        // set the visibility
        containerTreffer1.setVisibility(View.INVISIBLE);
        containerTreffer2.setVisibility(View.INVISIBLE);
        containerStrafbier1.setVisibility(View.INVISIBLE);
        containerStrafbier2.setVisibility(View.INVISIBLE);
        containerStrafschluck1.setVisibility(View.VISIBLE);
        containerStrafschluck2.setVisibility(View.VISIBLE);

        // change the weights
        // i.e. set weight of strafbier to 0.3
        LinearLayout.LayoutParams containerStrafschluck1LayoutParams = (LinearLayout.LayoutParams) containerStrafschluck1.getLayoutParams();
        containerStrafschluck1LayoutParams.weight = 0.3f;
        containerStrafschluck1.setLayoutParams(containerStrafschluck1LayoutParams);
        LinearLayout.LayoutParams containerStrafschluck2LayoutParams = (LinearLayout.LayoutParams) containerStrafschluck2.getLayoutParams();
        containerStrafschluck2LayoutParams.weight = 0.3f;
        containerStrafschluck2.setLayoutParams(containerStrafschluck2LayoutParams);
        // ... and the others to 0
        LinearLayout.LayoutParams containerTreffer1LayoutParams = (LinearLayout.LayoutParams) containerTreffer1.getLayoutParams();
        containerTreffer1LayoutParams.weight = 0.0f;
        containerTreffer1.setLayoutParams(containerTreffer1LayoutParams);
        LinearLayout.LayoutParams containerTreffer2LayoutParams = (LinearLayout.LayoutParams) containerTreffer2.getLayoutParams();
        containerTreffer2LayoutParams.weight = 0.0f;
        containerTreffer2.setLayoutParams(containerTreffer2LayoutParams);
        LinearLayout.LayoutParams containerStrafbier1LayoutParams = (LinearLayout.LayoutParams) containerStrafbier1.getLayoutParams();
        containerStrafbier1LayoutParams.weight = 0.0f;
        containerStrafbier1.setLayoutParams(containerStrafbier1LayoutParams);
        LinearLayout.LayoutParams containerStrafbier2LayoutParams = (LinearLayout.LayoutParams) containerStrafbier2.getLayoutParams();
        containerStrafbier2LayoutParams.weight = 0.0f;
        containerStrafbier2.setLayoutParams(containerStrafbier2LayoutParams);
    }

    public void increaseScore(View view) {
        LinearLayout layoutPlayer = (LinearLayout) view.getParent();
        TextView txtviewScorePlayer;
        // Note that the position of the score is alternating.
        // Hence we need to check which of the views corresponds to the txtview showing the score.
        // I implemented it by checking whether the first child of the layout has a text equal to "placeholder".
        // If so, then I know that the txtview showing the score must be the third child, and vice versa.
        if(((TextView)layoutPlayer.getChildAt(0)).getText().equals("placeholder")){
            txtviewScorePlayer = (TextView) layoutPlayer.getChildAt(2);
        }
        else{
            txtviewScorePlayer = (TextView) layoutPlayer.getChildAt(0);
        }
        int scorePlayer = Integer.parseInt((String) txtviewScorePlayer.getText());
        scorePlayer = scorePlayer + 1;
        txtviewScorePlayer.setText(Integer.toString(scorePlayer));
        getGeo();
    }
}