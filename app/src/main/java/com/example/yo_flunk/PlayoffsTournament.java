package com.example.yo_flunk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ventura.bracketslib.BracketsView;
import com.ventura.bracketslib.model.ColomnData;

import java.util.Arrays;

public class PlayoffsTournament extends AppCompatActivity {

    DatabaseReference reference_playoffs;
    FirebaseDatabase rootNode;
    String link_database, tournamentName;
    ColomnData viertelFinale, halbFinale, Finale;
    BracketsView bracketsView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playoffs);

        Intent i = getIntent();
        link_database = this.getResources().getString(R.string.firebase_link);
        tournamentName = i.getStringExtra("tournament_name");

        bracketsView = findViewById(R.id.bracket_view);


        viertelFinale = (ColomnData) i.getSerializableExtra("viertelfinale");
        halbFinale =  (ColomnData) i.getSerializableExtra("halbfinale");
        Finale = (ColomnData) i.getSerializableExtra("finale");

        bracketsView.setBracketsData(Arrays.asList(viertelFinale, halbFinale, Finale));



        rootNode = FirebaseDatabase.getInstance(link_database);
        reference_playoffs = rootNode.getReference("tournaments/" + tournamentName + "/playoffs");
    }

    public void openOverview(View view) {
        Intent i = new Intent(this, OverviewTournamentActivity.class);
        i.putExtra("source", (String) "playoffs");
        i.putExtra("tournament_name", tournamentName);
        startActivity(i);
    }
}