package com.example.yo_flunk;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.yo_flunk.Utility.dp2pix;

public class PrepareFlunkActivity extends AppCompatActivity implements MultiSpinner.MultiSpinnerListener {

    private static final int PERMISSIONS_FINE_LOCATION = 99;
    static PlayersHelperClass playersRound = new PlayersHelperClass();
    static PlayersHelperClass playersClass = new PlayersHelperClass();
    String league;
    String linkDatabase;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    public LocationManager locationManager;
    String lat, lon;
    Switch geoTracking;

    // Class responsible for GPS tracking
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare_flunk);
        linkDatabase = this.getResources().getString(R.string.firebase_link);

        Intent mainIntent = getIntent();
        List<String> listPlayers = mainIntent.getStringArrayListExtra("allPlayers");
        league = mainIntent.getStringExtra("league");

        rootNode = FirebaseDatabase.getInstance(linkDatabase);
        reference = rootNode.getReference(league + "/users");


        // I define this class to make the list of players globally accessible
        playersClass.RemoveAllPlayers();
        for (String player : listPlayers) {
            playersClass.AddPlayer(player);
        }

        com.example.yo_flunk.MultiSpinner multispinner = findViewById(R.id.multispinner_players);

        // multispinner needs a CharSequence as an input for setItems instead of a List<String>
        CharSequence[] charSeqPlayers = listPlayers.toArray(new CharSequence[listPlayers.size()]);
        multispinner.setItems(charSeqPlayers, "Spieler:in hinzufügen", this);
        multispinner.setMultiSpinnerListener(this);

        geoTracking = (Switch) findViewById(R.id.switch_heatmap);

        // find linear layouts that contain players avatars
        // Layout: [11 12|vs.|22 21]
        LinearLayout layoutTeam11 = findViewById(R.id.layout_team11); // first team, first column
        LinearLayout layoutTeam12 = findViewById(R.id.layout_team12); // first team, second column
        LinearLayout layoutTeam21 = findViewById(R.id.layout_team21); // second team, first column (from right)
        LinearLayout layoutTeam22 = findViewById(R.id.layout_team22); // second team, second column (from right)
        layoutTeam11.setId(100);
        layoutTeam12.setId(200);
        layoutTeam21.setId(300);
        layoutTeam22.setId(400);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //if(locationManager.isProviderEnabled(locationManager.GPS_PROVIDER)){
                    GetGeo();
                    //}
                } else {
                    geoTracking.setChecked(false);
                    Toast.makeText(this, "Um das Spiel auf der Heatmap anzuzeigen, brauchen wir GPS-Berechtigungen.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;

        }
    }


    public void CreateTeams(View v) {
        // if geo location is activated and geo location is tracked:
        if(geoTracking.isChecked()){
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            GetGeo();
        }

        // create the team partition:
        // Layout: [11 12|vs.|22 21]
        LinearLayout layoutTeam11 = findViewById(100);
        LinearLayout layoutTeam12 = findViewById(200);
        LinearLayout layoutTeam21 = findViewById(300);
        LinearLayout layoutTeam22 = findViewById(400);

        // teamPartition has size of number of players. True or False in entry j := j-th player is in team 1 or 2, respectively
        boolean[] teamPartition = new boolean[playersRound.GetAllPlayers().size()];
        ArrayList<String> players = new ArrayList<String>();

        // for all player on the left hand side: add player and set team_partition to true
        // team 1, first column:
        int numPlayers11 = layoutTeam11.getChildCount();
        for (int j = 0; j < numPlayers11; j++) {
            players.add((String) layoutTeam11.getChildAt(j).getTag());
            teamPartition[j] = true;
        }
        // team1, second_column:
        int numPlayers12 = layoutTeam12.getChildCount();
        for (int j = 0; j < numPlayers12; j++) {
            players.add((String) layoutTeam12.getChildAt(j).getTag());
            teamPartition[j + numPlayers11] = true;
        }
        // do the same for the right hand side, i.e. team2
        // team2, first column:
        int numPlayers21 = layoutTeam21.getChildCount();
        for (int j = 0; j < numPlayers21; j++) {
            players.add((String) layoutTeam21.getChildAt(j).getTag());
            teamPartition[j + numPlayers11 + numPlayers12] = false;
        }
        // team2, second column:
        int numPlayers22 = layoutTeam22.getChildCount();
        for (int j = 0; j < numPlayers22; j++) {
            players.add((String) layoutTeam22.getChildAt(j).getTag());
            teamPartition[j + numPlayers11 + numPlayers12 + numPlayers21] = false;
        }

        if (numPlayers11 == 0 || numPlayers21 ==0){
            Toast.makeText(this, "Zum Flunken wird mindestens ein Spieler je Team benötigt!", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent i = new Intent(this, PlayGameActivity.class);
        i.putStringArrayListExtra("players", players);
        i.putExtra("partition", teamPartition);
        i.putExtra("league", league);
        i.putExtra("lat", lat);
        i.putExtra("lon", lon);
        Switch switchNotification = findViewById(R.id.switch_pushUp);
        boolean notification = switchNotification.isChecked();
        i.putExtra("notification", notification);
        if (geoTracking.isChecked()) {
            String geotag = lat + "," + lon;
            i.putExtra("track_geo", true);
            // note: Meistens is geotag einfach "null, null", weil das Laden der GPS-Location zu lange dauert.
            // Etwa 10 Sec und der standard user wird das Spiel schon eher starten.
            // In summary: Das ist nur relevant, wenn GPS bereits aktiviert ist.
            i.putExtra("geotag", geotag);
        } else {
            i.putExtra("track_geo", false);
        }
        startActivity(i);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onItemsSelected(boolean[] selected) {
        // Avatars will be drag-able:
        View.OnDragListener dragListen = new myDragEventListener();

        // find linear layouts that contain players avatars
        LinearLayout containerTeam11 = findViewById(100);
        LinearLayout containerTeam12 = findViewById(200);
        LinearLayout containerTeam21 = findViewById(300);
        LinearLayout containerTeam22 = findViewById(400);

        // delete all current views from the layouts:
        containerTeam11.removeAllViews();
        containerTeam12.removeAllViews();
        containerTeam21.removeAllViews();
        containerTeam22.removeAllViews();
        containerTeam11.invalidate();
        containerTeam12.invalidate();
        containerTeam21.invalidate();
        containerTeam22.invalidate();

        // enable drag&drop for all linear layouts
        containerTeam11.setOnDragListener(dragListen);
        containerTeam12.setOnDragListener(dragListen);
        containerTeam21.setOnDragListener(dragListen);
        containerTeam22.setOnDragListener(dragListen);


        boolean isTeam1 = true;
        List<String> listPlayers = playersClass.GetAllPlayers();
        playersRound.RemoveAllPlayers();

        TypedArray heads = getResources().obtainTypedArray(R.array.heads);
        int j = 0;
        for (String player : listPlayers) {
            if (selected[j]) { // if player is selected
                playersRound.AddPlayer(player);
                ImageView avatarPlayer = new ImageView(this);
                avatarPlayer.setTag((String) player); // tag gets read at the end to determine the team partition

                // visualize avatar images
                reference.orderByChild("name").equalTo(player).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            // read headId and set image according to headId
                            UserHelperClass playerObj = ds.getValue(UserHelperClass.class);
                            int headId = playerObj.getHeadId();
                            avatarPlayer.setBackgroundResource(heads.getResourceId(headId, 0));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                avatarPlayer.setOnLongClickListener(new View.OnLongClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    public boolean onLongClick(View v) {
                        // on long click we want to drag the avatar and possibly move to the other team
                        LayoutInflater inflater = getLayoutInflater();
                        View toastView = inflater.inflate(R.layout.toast,
                                (ViewGroup) findViewById(R.id.custom_toast_layout));

                        TextView text = (TextView) toastView.findViewById(R.id.toast_text);
                        text.setText("Zuordnung von " + ((String) v.getTag()));

                        Toast toast = new Toast(getApplicationContext());
                        toast.setView(toastView);
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                        ClipData.Item item = new ClipData.Item((String) v.getTag());

                        ClipData dragData = new ClipData((CharSequence) v.getTag(),
                                new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
                                item);

                        View.DragShadowBuilder myShadow = new DragShadowBuilder(avatarPlayer);

                        v.startDragAndDrop(dragData, myShadow, v, 0); //
                        v.setVisibility(View.INVISIBLE);
                        return true;
                    }
                });
                if (isTeam1) {
                    // add new view to linear layout:
                    int numViews = containerTeam11.getChildCount() + containerTeam12.getChildCount();
                    avatarPlayer.setId(View.generateViewId());
                    // if less than 3 players: add view to first column, else to second column
                    if (numViews < 3) {
                        containerTeam11.addView(avatarPlayer);
                    } else {
                        containerTeam12.addView(avatarPlayer);
                    }
                    isTeam1 = false; // iterate through team1 and team2
                } else {
                    // add new view to linear layout:
                    int numViews = containerTeam21.getChildCount() + containerTeam22.getChildCount();
                    avatarPlayer.setId(View.generateViewId());
                    // if less than 3 players: add view to first column, else to second column
                    if (numViews < 3) {
                        containerTeam21.addView(avatarPlayer);
                    } else {
                        containerTeam22.addView(avatarPlayer);
                    }

                    isTeam1 = true;
                }
                int dp_avatar = 40;
                avatarPlayer.getLayoutParams().height = dp2pix(dp_avatar, getApplicationContext());
                avatarPlayer.getLayoutParams().width = dp2pix(dp_avatar, getApplicationContext());
            }
            j = j + 1;
        }
    }

    public void GetGeo(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(PrepareFlunkActivity.this);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // we got permissions
                    if(location!=null) {
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
}

class myDragEventListener implements View.OnDragListener {
    // This is the method that the system calls when it dispatches a drag event to the
    // listener.

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public boolean onDrag(View v, DragEvent event) {
        // Defines a variable to store the action type for the incoming event
        final int action = event.getAction();
        boolean ownerIsTeam1, destinationIsTeam1;

        // Handles each of the expected events
        switch(action) {
            case DragEvent.ACTION_DRAG_STARTED:
                // Determines if this View can accept the dragged data
                if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {

                    // As an example of what your application might do,
                    // applies a blue color tint to the View to indicate that it can accept
                    // data.
                    //v.setColorFilter(Color.BLUE);

                    // Invalidate the view to force a redraw in the new tint
                    // v.invalidate();

                    // returns true to indicate that the View can accept the dragged data.
                    return true;

                }

                // Returns false. During the current drag and drop operation, this View will
                // not receive events again until ACTION_DRAG_ENDED is sent.
                return false;

            case DragEvent.ACTION_DRAG_ENTERED: // is the case at the beginning of a drag action
                return true;

            case DragEvent.ACTION_DRAG_LOCATION:
                // Ignore the event
                return true;

            case DragEvent.ACTION_DRAG_EXITED: //if drag is outside of drag viewers
                // Wenn ich das richtig verstehe: Sobald ich mit dem Drag außerhalb eines Areals
                // bin (und noch nicht gedroppt habe)
                return true;

            case DragEvent.ACTION_DROP: // after dropping into a suitable field
                int idTeam11 = 100;
                int idTeam12 = 200;

                View state = (View) event.getLocalState();
                LinearLayout owner = (LinearLayout) state.getParent();
                LinearLayout destination = (LinearLayout) v;

                if(owner.getId()==idTeam11 || owner.getId()==idTeam12){
                    ownerIsTeam1 = true;
                }
                else{
                    ownerIsTeam1 = false;
                }
                if(destination.getId()==idTeam11 || destination.getId()==idTeam12){
                    destinationIsTeam1 = true;
                }
                else{
                    destinationIsTeam1 = false;
                }
                // find all LinearLayouts:
                LinearLayout layoutBothTeams = (LinearLayout) owner.getParent();
                LinearLayout layoutTeam11 = (LinearLayout) layoutBothTeams.getChildAt(0);
                LinearLayout layoutTeam12 = (LinearLayout) layoutBothTeams.getChildAt(1);
                LinearLayout layoutTeam21 = (LinearLayout) layoutBothTeams.getChildAt(4);
                LinearLayout layoutTeam22 = (LinearLayout) layoutBothTeams.getChildAt(3);

                // 1. Adjust source of the view
                owner.removeView(state);
                if (ownerIsTeam1) {
                    // check whether column1 has 2 entries but column2 has 1 entry
                    if (layoutTeam11.getChildCount() == 2 && layoutTeam12.getChildCount() > 0) {
                        // move the first entry from column 2 to column 1
                        View viewTomove = layoutTeam12.getChildAt(0);
                        layoutTeam12.removeView(viewTomove);
                        layoutTeam11.addView(viewTomove);
                    }
                } else {
                    if (layoutTeam21.getChildCount() == 2 && layoutTeam22.getChildCount() > 0) {
                        // move the first entry from column 2 to column 1
                        View viewToMove = layoutTeam22.getChildAt(0);
                        layoutTeam22.removeView(viewToMove);
                        layoutTeam21.addView(viewToMove);
                    }
                }

                // define avatar size
                Resources res = v.getResources();
                int dp_avatar = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        40,
                        res.getDisplayMetrics()
                );
                state.getLayoutParams().height = dp_avatar;
                state.getLayoutParams().width = dp_avatar;

                // 2. Add View to destination
                if (destinationIsTeam1) {
                    //check whether column 1 already has 3 Views
                    if (layoutTeam11.getChildCount() == 3) {
                        //add view to column 2 (if it has 0 or 1 views so far)
                        if (layoutTeam12.getChildCount() < 2) {
                            layoutTeam12.addView(state);
                        }
                        else{//add view to team2 again
                            Toast.makeText(v.getContext(), "Team 1 ist bereits voll.", Toast.LENGTH_LONG).show();
                            if(layoutTeam21.getChildCount()==3){
                                layoutTeam22.addView(state);
                            }
                            else{
                                layoutTeam21.addView(state);
                            }
                        }
                    } else {
                        layoutTeam11.addView(state);
                    }
                }
                else{
                    //check whether column 1 already has 3 Views
                    if (layoutTeam21.getChildCount() == 3) {
                        //add view to column 2 (if it has 0 or 1 views so far)
                        if (layoutTeam22.getChildCount() < 2) {
                            layoutTeam22.addView(state);
                        }
                        else{//add view to team1 again
                            Toast.makeText(v.getContext(), "Team 2 ist bereits voll.", Toast.LENGTH_LONG).show();
                            if(layoutTeam11.getChildCount()==3){
                                layoutTeam12.addView(state);
                            }
                            else{
                                layoutTeam11.addView(state);
                            }
                        }
                    } else {
                        layoutTeam21.addView(state);
                    }
                }

                state.invalidate();
                // Returns true. DragEvent.getResult() will return true.
                return true;

            case DragEvent.ACTION_DRAG_ENDED:  //after each drag action
                View state2 = (View) event.getLocalState();
                state2.setVisibility(View.VISIBLE);
                return true;

            // An unknown action type was received.
            default:
                Log.e("DragDrop Example","Unknown action type received by OnDragListener.");
                break;
        }
        return false;
    }
}


