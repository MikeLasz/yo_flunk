package com.example.yo_flunk;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.Math.min;

public class Fragment_treffer extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hits, container, false);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Intent mainIntent = getActivity().getIntent();
        ArrayList<String> namePlayers = (ArrayList<String>) mainIntent.getStringArrayListExtra("name_players").clone();
        ArrayList<Integer> trefferPlayers = (ArrayList<Integer>) mainIntent.getIntegerArrayListExtra("treffer_players").clone();
        ArrayList<Integer> trefferPlayersUnsorted = (ArrayList<Integer>) trefferPlayers.clone();

        List<PieEntry> entries = new ArrayList<PieEntry>();

        // sort treffer players in a descending order
        Collections.sort(trefferPlayers);
        Collections.reverse(trefferPlayers);

        for( int i=0; i<min(trefferPlayers.size(), 6); i++){
            int numTreffer = trefferPlayers.get(i);
            // find player with numTreffer amounts of hits
            for(int j=0; j<namePlayers.size(); j++){
                int treffer = trefferPlayersUnsorted.get(j);
                if(treffer==numTreffer){
                    entries.add(new PieEntry(numTreffer, namePlayers.get(j)));
                    trefferPlayersUnsorted.remove(j); // remove the player to prevent the same player to be shown twice
                    namePlayers.remove(j); // remove the player to prevent the same player to be shown twice
                    break;
                }
            }
        }


        PieDataSet dataset = new PieDataSet(entries, "label");

        ArrayList<Integer> colors = new ArrayList<>();

        colors.add(ContextCompat.getColor(getContext(),R.color.darkgreen));
        colors.add(ContextCompat.getColor(getContext(),R.color.lightgreen));

        PieChart chart = (PieChart) view.findViewById(R.id.chart);

        dataset.setColors(colors);
        //dataset.setValueFormatter(new PercentFormatter(chart));
        dataset.setValueFormatter(new DefaultValueFormatter(0));
        dataset.setValueTextColor(ContextCompat.getColor(getContext(), R.color.textcolor));
        dataset.setValueTextSize(15.0f);

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "Nunito-SemiBold.ttf");
        dataset.setValueTypeface(tf);

        PieData data = new PieData(dataset);


        chart.setData(data);
        chart.getLegend().setEnabled(false);
        chart.setCenterText("Top 6");
        chart.setCenterTextColor(ContextCompat.getColor(getContext(),R.color.textcolor));
        chart.setCenterTextSize(35f);
        chart.setCenterTextTypeface(tf);


        chart.setUsePercentValues(false);
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

        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

        // add a selection listener
        //chart.setOnChartValueSelectedListener();


        chart.animateY(2000, Easing.EaseInOutQuad);
        //chart.spin(2000, 0, 360);

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