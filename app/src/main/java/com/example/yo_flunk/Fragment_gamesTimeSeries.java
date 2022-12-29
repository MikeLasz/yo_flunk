package com.example.yo_flunk;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_gamesTimeSeries#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_gamesTimeSeries extends Fragment {
    public Fragment_gamesTimeSeries() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_games_timeseries, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Intent mainIntent = getActivity().getIntent();
        String[] datesGames = mainIntent.getStringArrayListExtra("dates_games").toArray(new String[0]);

        LineChart chart = view.findViewById(R.id.timeseries_games);
        //chart.setViewPortOffsets(0, 0, 0, 0);
        //chart.setBackgroundColor(Color.rgb(104, 241, 175));

        // no description text
        chart.getDescription().setEnabled(false);

        // enable touch gestures
        chart.setTouchEnabled(true);

        // enable scaling and dragging
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);

        chart.setDrawGridBackground(false);
        chart.setMaxHighlightDistance(300);

        XAxis x = chart.getXAxis();
        x.setLabelCount(12,true);
        x.setDrawGridLines(false);
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setLabelRotationAngle(-70f);
        // x.setEnabled(false);

        YAxis y = chart.getAxisLeft();
        y.setTextColor(ContextCompat.getColor(getContext(), R.color.textcolor));
        y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        y.setDrawGridLines(false);
        y.setAxisLineColor(ContextCompat.getColor(getContext(), R.color.textcolor));
        y.setAxisMinimum(0f);
        y.setEnabled(false);

        chart.getAxisRight().setEnabled(false);

        Map<Integer, Integer> dict = ProcessDate(datesGames);

        List<Entry> entries = new ArrayList<Entry>();


        for(int month:dict.keySet()){
            int num_games = dict.get(month);
            entries.add(new Entry(month, num_games)); // month: start with January=0, February=1, etc.
        }



        // XAxis xAxis = chart.getXAxis();
        // xAxis.setValueFormatter(new IndexAxisValueFormatter(dates_games_str));

        LineDataSet dataset = new LineDataSet(entries, "label");
        dataset.setValueTextSize(15.0f);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "Nunito-SemiBold.ttf");
        dataset.setValueTypeface(tf);
        x.setTypeface(tf);
        x.setTextColor(ContextCompat.getColor(getContext(), R.color.textcolor));

        dataset.setDrawFilled(true);
        dataset.setFillColor(ContextCompat.getColor(getContext(), R.color.darkgreen));
        dataset.setColor(ContextCompat.getColor(getContext(), R.color.darkgreen));
        dataset.setValueFormatter(new DefaultValueFormatter(0));
        dataset.setValueTextColor(ContextCompat.getColor(getContext(), R.color.textcolor));
        dataset.setValueTextSize(15.0f);
        dataset.setCircleColor(ContextCompat.getColor(getContext(), R.color.darkgreen));

        LineData data = new LineData(dataset);
        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter( new String[] {"Januar", "Februar", "März", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Dezember"}));
        chart.setData(data);
        chart.getLegend().setEnabled(false);
        chart.setBorderColor(ContextCompat.getColor(getContext(), R.color.darkgreen));

        chart.animateXY(2000, 2000);

        // don't forget to refresh the drawing
        chart.invalidate();


    }

    public Map ProcessDate(String[] datesGames){
        /*
        Input: datesGames consists of the dataes in ''plain'' format, such as '1639088495963'
        Output: Dictionary filled with
                1. Month
                2. Number of Games played in this month

        */
        Map<Integer, Integer> dictionary = new HashMap<Integer, Integer>();
        String str_firstDate = datesGames[0];
        Date firstDate = new Date(Long.parseLong(str_firstDate));
        Integer currentMonth = Integer.parseInt(String.valueOf(firstDate.getMonth()));
        Integer numPlayed = 0;
        for(String date:datesGames){
            Date dateFormat_date = new Date(Long.parseLong(date));
            Integer month = Integer.parseInt(String.valueOf(dateFormat_date.getMonth()));
            if(month==currentMonth){ // increase numPlayed by one
                numPlayed = numPlayed + 1;
            }
            else{ // generate new key
                dictionary.put(currentMonth, numPlayed);
                numPlayed = 1;
                currentMonth = month;
            }
            dictionary.put(month, numPlayed);
        }

        // fill out the remaining months:
        for(int j=0; j<12; j++){
            //check if month is in dictionary
            if(!(dictionary.keySet().contains(j))){
                dictionary.put(j, 0);
            }
        }
        return(dictionary);
    }
}

