package com.example.yo_flunk;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_gamesPlayed#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_gamesPlayed extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_games_played, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Intent mainIntent = getActivity().getIntent();
        ArrayList<String> namePlayers = mainIntent.getStringArrayListExtra("name_players");
        ArrayList<Integer> winsPlayers = mainIntent.getIntegerArrayListExtra("wins_players");
        ArrayList<Integer> lossesPlayers = mainIntent.getIntegerArrayListExtra("losses_players");

        BarChart chart = (BarChart) view.findViewById(R.id.chart);

        List<BarEntry> entries = new ArrayList<>();
        Integer index = 0;
        for (String name : namePlayers) {
            entries.add(new BarEntry((float) index, (float) winsPlayers.get(index) + lossesPlayers.get(index)));
            index = index + 1;
        }
        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(namePlayers));
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setAxisMinimum(0f);
        xAxis.setDrawAxisLine(true);
        //xAxis.setLabelCount(8);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(ContextCompat.getColor(getContext(), R.color.textcolor));

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setEnabled(false);

        yAxis.setTextColor(ContextCompat.getColor(getContext(), R.color.textcolor));

        chart.getAxisRight().setEnabled(false);

        BarDataSet set = new BarDataSet(entries, " ");

        set.setColor(ContextCompat.getColor(getContext(), R.color.darkgreen));
        set.setValueFormatter(new DefaultValueFormatter(0));
        set.setValueTextColor(ContextCompat.getColor(getContext(), R.color.textcolor));
        set.setValueTextSize(15.0f);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "Nunito-SemiBold.ttf");
        set.setValueTypeface(tf);
        xAxis.setTypeface(tf);
        xAxis.setTextSize(13f);

        //set.setDrawValues(false);
        BarData data_chart = new BarData(set);
        chart.setData(data_chart);
        chart.setFitBars(false);
        chart.setExtraBottomOffset(10f);


        chart.getLegend().setEnabled(false);

        chart.getDescription().setEnabled(false);

        chart.setScaleYEnabled(false);

        //chart.zoom(1.5f, 1.0f, 200, 200);
        chart.animateXY(2000, 2000);

        chart.zoomAndCenterAnimated(4.5f, 1.0f, 200, 200, YAxis.AxisDependency.LEFT, 2000);
        chart.invalidate();
    }
}