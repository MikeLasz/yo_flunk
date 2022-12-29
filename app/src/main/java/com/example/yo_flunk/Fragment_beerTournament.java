package com.example.yo_flunk;

import android.content.Intent;
import android.graphics.Color;
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

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fragment_beerTournament extends Fragment {

    private ImageView iv_stats;
    ArrayList<String> teamBeer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bier, container, false);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //initialize your view here for use view.findViewById("your view id")
        teamBeer = new ArrayList<>();
        DatabaseReference referenceTeams = ((OverviewTournamentActivity) getActivity()).referenceTeams;
        referenceTeams.orderByValue().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    TeamsHelperClass team = ds.getValue(TeamsHelperClass.class);
                    teamBeer.add(team.getTeamBeer());
                }
                createChart(teamBeer);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void createChart(ArrayList<String> TeamBeer) {
        Map<String, Integer> beerDict = new HashMap<>();
        List<PieEntry> entries = new ArrayList<PieEntry>();
        for (int i = 0; i < teamBeer.size(); i++) {
            if (beerDict.keySet().contains(teamBeer.get(i))) {
                int old_num = beerDict.get(teamBeer.get(i));
                beerDict.put(teamBeer.get(i), old_num + 1);
            } else {
                beerDict.put(teamBeer.get(i), 1);
            }
        }
        // find most prominent beer:
        for (int j = 0; j < 5; j++) {
            if (!beerDict.isEmpty()) {
                String beer = Collections.max(beerDict.entrySet(), Map.Entry.comparingByValue()).getKey();
                int numBeer = beerDict.get(beer);
                beerDict.remove(beer);
                entries.add(new PieEntry(numBeer, beer));
            }
        }

        PieDataSet dataset = new PieDataSet(entries, "label");

        ArrayList<Integer> colors = new ArrayList<>();

        colors.add(ContextCompat.getColor(getContext(), R.color.darkgreen));
        colors.add(ContextCompat.getColor(getContext(), R.color.lightgreen));

        PieChart chart = (PieChart) getView().findViewById(R.id.chart);

        dataset.setColors(colors);
        dataset.setValueFormatter(new PercentFormatter(chart));
        dataset.setValueTextColor(ContextCompat.getColor(getContext(), R.color.textcolor));
        dataset.setValueTextSize(15.0f);

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "Nunito-SemiBold.ttf");
        dataset.setValueTypeface(tf);

        PieData data = new PieData(dataset);


        chart.setData(data);
        chart.getLegend().setEnabled(false);
        chart.setCenterText("Top 5");
        chart.setCenterTextColor(ContextCompat.getColor(getContext(), R.color.textcolor));
        chart.setCenterTextSize(35f);
        chart.setCenterTextTypeface(tf);


        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);

        chart.setDragDecelerationFrictionCoef(0.95f);

        //chart.setCenterTextTypeface(R.font.nunito_semibold);
        //chart.setCenterText(generateCenterSpannableText());

        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);

        chart.setDrawCenterText(true);

        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);

        chart.animateY(2000, Easing.EaseInOutQuad);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        chart.setEntryLabelColor(Color.WHITE);
        //chart.setEntryLabelTypeface(R.font.nunito_semibold);
        chart.setEntryLabelTextSize(12f);

        chart.invalidate();
    }
}