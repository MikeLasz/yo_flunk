package com.example.yo_flunk;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.stream.IntStream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_groupsTournament#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_groupsTournament extends Fragment {

    static final String ARG_PARAM1 = "group_index";
    static final String ARG_PARAM2 = "param2";

    RecyclerView recyclerView2;
    RecyclerView.Adapter<RvAdapterKlasseGroupsTournament.ViewHolderKlasse2> rvadapter2;
    RecyclerView.LayoutManager rvLayoutManager2;

    static ArrayList<String> teamNamesGroup;
    static ArrayList<Integer> winsGroup, losesGroup, hitsGroup;
    static ArrayList<ArrayList<String>> allTeamNamesGroup;
    static ArrayList<ArrayList<Integer>> allWinsGroup, allLosesGroup, allHitsGroup;
    int idGroup;


    private ArrayList<String> teamNames;
    private ArrayList<Integer> numGames, numWins, teamInGroup, hitsPlayer1, hitsPlayer2;

    public Fragment_groupsTournament() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_tabelle.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_groupsTournament newInstance(int param1, String param2) {
        Fragment_groupsTournament fragment = new Fragment_groupsTournament();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        idGroup = getArguments().getInt("group_index");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_groups_tournament, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Intent main_intent = getActivity().getIntent();
        teamNames = main_intent.getStringArrayListExtra("team_names");
        numWins = main_intent.getIntegerArrayListExtra("num_wins");
        numGames = main_intent.getIntegerArrayListExtra("num_games");
        hitsPlayer1 = main_intent.getIntegerArrayListExtra("hits_player1");
        hitsPlayer2 = main_intent.getIntegerArrayListExtra("hits_player2");
        teamInGroup = main_intent.getIntegerArrayListExtra("groups");
        allTeamNamesGroup = new ArrayList<>();
        allWinsGroup = new ArrayList<>();
        allLosesGroup = new ArrayList<>();
        allHitsGroup = new ArrayList<>();

        int numGroups = GroupStageTournamentActivity.numGroups;
        for(int group=0; group<numGroups; group++){
            teamNamesGroup = new ArrayList<>();
            winsGroup = new ArrayList();
            losesGroup = new ArrayList();
            hitsGroup = new ArrayList();

            for(int j = 0; j< teamNames.size(); j++){
                if(teamInGroup.get(j)==group){
                    teamNamesGroup.add(teamNames.get(j));
                    winsGroup.add(numWins.get(j));
                    losesGroup.add(numGames.get(j) - numWins.get(j));
                    hitsGroup.add(hitsPlayer1.get(j) + hitsPlayer2.get(j));
                }
            }

            int numPlayers = teamNamesGroup.size();

            //---------------------------------------------------------
            // sort all lists based on number of wins
            int[] helperSort = new int[numPlayers];
            for(int j=0; j<numPlayers; j++){
                int wins = winsGroup.get(j);
                helperSort[j] = wins;
            }

            int[] sortedIndices = IntStream.range(0, helperSort.length)
                    .boxed().sorted((i, j) -> helperSort[j] - helperSort[i])
                    .mapToInt(ele -> ele).toArray();
            ArrayList<String> teamnamesSorted = new ArrayList<>(numPlayers);
            ArrayList<Integer> winsSorted = new ArrayList<>(numPlayers);
            ArrayList<Integer> losesSorted = new ArrayList<>(numPlayers);
            ArrayList<Integer> hitsSorted = new ArrayList<>(numPlayers);
            for(int j=0; j<numPlayers; j++){
                int index = sortedIndices[j];
                teamnamesSorted.add(j, teamNamesGroup.get(index));
                winsSorted.add(j, winsGroup.get(index));
                losesSorted.add(j, losesGroup.get(index));
                hitsSorted.add(j, hitsGroup.get(index));
            }

            teamNamesGroup = teamnamesSorted;
            winsGroup = winsSorted;
            losesGroup = losesSorted;
            hitsGroup = hitsSorted;

            allTeamNamesGroup.add(teamNamesGroup);
            allWinsGroup.add(winsGroup);
            allLosesGroup.add(losesGroup);
            allHitsGroup.add(hitsGroup);
        }

        recyclerView2 = (RecyclerView) view.findViewById(R.id.recyclerview2);
        rvLayoutManager2 = new LinearLayoutManager(getContext());
        recyclerView2.setLayoutManager(rvLayoutManager2);
        rvadapter2 = new RvAdapterKlasseGroupsTournament(idGroup);
        recyclerView2.setAdapter(rvadapter2);
    }
}