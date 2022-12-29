package com.example.yo_flunk;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class Fragment_trefferTournament extends Fragment {

    private ImageView iv_stats;

    RecyclerView recyclerView2;
    RecyclerView.Adapter<RvAdapterKlasseTopScorerTournament.ViewHolderKlasse2> rvadapter2;
    RecyclerView.LayoutManager rvLayoutManager2;

    static ArrayList<String> names, player1, player2;
    static ArrayList<Integer> wins, loses, hits, hitsPlayer1, hitsPlayer2, winsGroup, gamesGroup, losesGroup;

    DatabaseReference referenceUsers;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hits_tournament, container, false);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        referenceUsers = ((OverviewTournamentActivity)getActivity()).referenceUsers;
        player1 = ((OverviewTournamentActivity)getActivity()).player1;
        player2 = ((OverviewTournamentActivity)getActivity()).player2;
        hitsPlayer1 = ((OverviewTournamentActivity)getActivity()).hitsPlayer1;
        hitsPlayer2 = ((OverviewTournamentActivity)getActivity()).hitsPlayer2;
        winsGroup = ((OverviewTournamentActivity)getActivity()).numWins;
        gamesGroup = ((OverviewTournamentActivity)getActivity()).numGames;
        losesGroup = new ArrayList<>();
        for(int j=0; j<winsGroup.size(); j++){
            losesGroup.add(gamesGroup.get(j) - winsGroup.get(j));
        }

        names = new ArrayList<>();
        wins = new ArrayList();
        loses = new ArrayList();
        hits = new ArrayList();

        for(int i=0; i<player1.size(); i++){
            names.add(player1.get(i));
            hits.add(hitsPlayer1.get(i));
            wins.add(winsGroup.get(i));
            loses.add(losesGroup.get(i));
            names.add(player2.get(i));
            hits.add(hitsPlayer2.get(i));
            wins.add(winsGroup.get(i));
            loses.add(losesGroup.get(i));
        }

        // sort all lists based on number of hits
        int numPlayers = names.size();
        int[] helperSort = new int[names.size()];
        for(int j=0; j<names.size(); j++){
            int hit = hits.get(j);
            helperSort[j] = hit;
        }

        int[] sortedIndices = IntStream.range(0, helperSort.length)
                .boxed().sorted((i, j) -> helperSort[j] - helperSort[i])
                .mapToInt(ele -> ele).toArray();
        ArrayList<String> namesSorted = new ArrayList<>(numPlayers);
        ArrayList<Integer> winsSorted = new ArrayList<>(numPlayers);
        ArrayList<Integer> losesSorted = new ArrayList<>(numPlayers);
        ArrayList<Integer> hitsSorted = new ArrayList<>(numPlayers);
        for(int j=0; j<numPlayers; j++){
            int index = sortedIndices[j];
            namesSorted.add(j, names.get(index));
            winsSorted.add(j, wins.get(index));
            losesSorted.add(j, loses.get(index));
            hitsSorted.add(j, hits.get(index));
        }
        names= namesSorted;
        wins = winsSorted;
        loses = losesSorted;
        hits = hitsSorted;

        recyclerView2 = (RecyclerView) view.findViewById(R.id.recyclerview2);
        rvLayoutManager2 = new LinearLayoutManager(getContext());
        recyclerView2.setLayoutManager(rvLayoutManager2);
        rvadapter2 = new RvAdapterKlasseTopScorerTournament();
        recyclerView2.setAdapter(rvadapter2);
    }
}