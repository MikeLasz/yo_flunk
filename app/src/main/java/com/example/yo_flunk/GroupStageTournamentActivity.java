package com.example.yo_flunk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.yo_flunk.ui.main.SectionsPagerAdapter_groupstage;
import com.google.android.material.tabs.TabLayout;

public class GroupStageTournamentActivity extends AppCompatActivity {

    ViewPager viewPager;
    String tournamentName;
    static int numGroups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupstage_tournament);
        Intent overview_intent = getIntent();
        tournamentName = overview_intent.getStringExtra("tournament_name");
        numGroups = overview_intent.getIntExtra("num_groups", 4);
        SectionsPagerAdapter_groupstage sectionsPagerAdapter = new SectionsPagerAdapter_groupstage(this, getSupportFragmentManager(), numGroups);
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }

    public void openOverview(View view) {
        Intent i = new Intent(this, OverviewTournamentActivity.class);
        i.putExtra("source", (String) "groups");
        i.putExtra("tournament_name", tournamentName);
        startActivity(i);
    }
}