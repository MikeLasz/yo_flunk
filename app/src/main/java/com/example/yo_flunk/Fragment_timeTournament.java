package com.example.yo_flunk;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Fragment_timeTournament extends Fragment {

    private ImageView iv_stats;
    ArrayList<String> teamNames;
    int NUM_TEAMS_DISPLAYED = 5;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_time_tournament, container, false);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        teamNames= new ArrayList<>();
        DatabaseReference reference_teams = ((OverviewTournamentActivity)getActivity()).referenceTeams;
        DatabaseReference reference_games = ((OverviewTournamentActivity)getActivity()).referenceGames;
        Map<String, ArrayList<Long>> timeDict = new HashMap<>();
        reference_teams.orderByValue().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    TeamsHelperClass team = ds.getValue(TeamsHelperClass.class);
                    teamNames.add(team.getTeamName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference_games.orderByValue().addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dsGroup:dataSnapshot.getChildren()){
                    for(DataSnapshot dsGame:dsGroup.getChildren()){
                        GamesTournamentHelperClass game = dsGame.getValue(GamesTournamentHelperClass.class);
                        if(game.isPlayed()){
                            // find winner team
                            String winnerTeam = new String();
                            if(game.team1Win){
                                winnerTeam = game.getTeamNames().get(0);
                            }
                            else{
                                winnerTeam = game.getTeamNames().get(1);
                            }

                            // get time
                            long duration = game.getTimeEnd().getTime() - game.getTimeStart().getTime();
                            long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);

                            // add to dictionary
                            if(timeDict.keySet().contains(winnerTeam)){
                                ArrayList<Long> times = timeDict.get(winnerTeam);
                                times.add(diffInSeconds);
                            }else{
                                ArrayList<Long> times = new ArrayList<>();
                                times.add(diffInSeconds);
                                timeDict.put(winnerTeam, times);
                            }
                        }
                    }
                }
                createChart(timeDict);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void createChart(Map<String, ArrayList<Long>> timeDict){
        // Get Data:
        List<BarEntry> entries = new ArrayList<>();
        Integer index = 0;
        for(String teamName: teamNames){
            // compute avg win time
            ArrayList<Long> times = timeDict.get(teamName);
            if(times!=null){
                float avg_time = 0;
                for(int j=0; j<times.size(); j++){
                    avg_time = avg_time + times.get(j);
                }
                avg_time = avg_time/times.size();

                entries.add(new BarEntry( index, avg_time, teamName));
                index = index + 1;
            }

        }
        while(entries.size()>NUM_TEAMS_DISPLAYED){
            // find max
            float max= 0;
            int remove_index = 0;
            for(int j=0; j<entries.size(); j++){
                if(entries.get(j).getY() > max){
                    max = entries.get(j).getY();
                    remove_index = j;
                }
            }
            // remove it
            entries.remove(remove_index);
            timeDict.remove(remove_index);
        }
        for(int j=0; j<entries.size(); j++){
            if(j<entries.size()){
                entries.get(j).setX(j);
            }
        }

        BarDataSet barDataSet = new BarDataSet(entries, "Times");
        barDataSet.setColor(ContextCompat.getColor(getContext(), R.color.darkgreen));
        barDataSet.setValueFormatter(new DefaultValueFormatter(0));
        barDataSet.setValueTextColor(ContextCompat.getColor(getContext(), R.color.textcolor));
        barDataSet.setValueTextSize(15.0f);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "Nunito-SemiBold.ttf");

        barDataSet.setValueTypeface(tf);

        BarData data = new BarData(barDataSet);
        data.setValueTextSize(15.0f);
        data.setDrawValues(false);
        data.setValueTypeface(tf);


        HorizontalBarChart chart = getView().findViewById(R.id.chart);
        chart.setData(data);

        //chart.setOnChartValueSelectedListener((OnChartValueSelectedListener) );
        // chart.setHighlightEnabled(false);

        chart.setDrawBarShadow(false);

        chart.setDrawValueAboveBar(true);

        Description desc = chart.getDescription();
        desc.setEnabled(true);
        desc.setTextSize(10.0f);
        desc.setText("Durchschnittszeit in Sekunden");
        desc.setTypeface(tf);
        desc.setTextColor(ContextCompat.getColor(getContext(), R.color.textcolor));

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false);

        chart.setDrawGridBackground(false);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.TOP_INSIDE);


        XAxis xl = chart.getXAxis();
        //xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setTypeface(tf);
        xl.setTextSize(20.0f);
        xl.setTextColor(ContextCompat.getColor(getContext(), R.color.textcolor));
        xl.setValueFormatter(new IndexAxisValueFormatter(timeDict.keySet()));
        //xl.setTypeface(tfLight);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);
        xl.setGranularity(1f);

        YAxis yl = chart.getAxisLeft();
        yl.setTextColor(ContextCompat.getColor(getContext(), R.color.textcolor));
        yl.setTypeface(tf);
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(true);
        yl.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis yr = chart.getAxisRight();
        yr.setTextColor(ContextCompat.getColor(getContext(), R.color.textcolor));
        yr.setTypeface(tf);
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(false);
        yr.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        //chart.setFitBars(true);

        chart.animateY(2500);

        // setting data
        //seekBarY.setProgress(50);
        //seekBarX.setProgress(12);

        Legend l = chart.getLegend();
        l.setEnabled(false);
        /*
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setXEntrySpace(4f);

         */
    }
}