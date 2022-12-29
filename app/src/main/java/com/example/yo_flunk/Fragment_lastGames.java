package com.example.yo_flunk;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_lastGames#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_lastGames extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    RecyclerView recyclerView;
    RecyclerView.Adapter<RvAdapterKlasseLastGames.ViewHolderKlasse> rvadapter;
    RecyclerView.LayoutManager rvLayoutManager;

    static ArrayList<String> itemTeam1Spieler1, itemTeam2Spieler1, itemWinTeam1;
    static ArrayList<String> itemTeam2Spieler2, itemTeam2Spieler3, itemTeam2Spieler4, itemTeam2Spieler5;
    static ArrayList<String> itemTeam1Spieler2, itemTeam1Spieler3, itemTeam1Spieler4, itemTeam1Spieler5;
    static ArrayList<Integer> itemFotoIds, itemEloUnsorted;
    static ArrayList<String> itemPlayers;
    static ArrayList<Integer> itemWins, itemLosses, itemTreffer, headidPlayers;
    int idLatestHighlight = 0;
    FirebaseDatabase rootNode;
    DatabaseReference referenceGames;
    String league;
    String linkDatabase;

    public Fragment_lastGames() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_lastgames.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_lastGames newInstance(String param1, String param2) {
        Fragment_lastGames fragment = new Fragment_lastGames();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        linkDatabase = this.getResources().getString(R.string.firebase_link);

        Intent main_intent = getActivity().getIntent();
        idLatestHighlight = main_intent.getIntExtra("id_highlight", 0);
        league = main_intent.getStringExtra("league");
        itemPlayers = main_intent.getStringArrayListExtra("names_players");
        itemWins = main_intent.getIntegerArrayListExtra("wins_players");
        itemLosses= main_intent.getIntegerArrayListExtra("losses_players");
        itemTreffer= main_intent.getIntegerArrayListExtra("treffer_players");
        headidPlayers = main_intent.getIntegerArrayListExtra("headid_players");
        itemEloUnsorted = main_intent.getIntegerArrayListExtra("elo_players");

        // note for each game, we add one entry in each of those arrays.
        // For instance if team1 has only 4 players, we fill itemTeam1Spieler 5 with a null
        itemFotoIds = new ArrayList<>();
        itemWinTeam1 = new ArrayList<>();
        itemTeam1Spieler1 = new ArrayList<>();
        itemTeam2Spieler2 = new ArrayList<>();
        itemTeam2Spieler3 = new ArrayList<>();
        itemTeam2Spieler4 = new ArrayList<>();
        itemTeam2Spieler5 = new ArrayList<>();

        itemTeam2Spieler1 = new ArrayList<>();
        itemTeam1Spieler2 = new ArrayList<>();
        itemTeam1Spieler3 = new ArrayList<>();
        itemTeam1Spieler4 = new ArrayList<>();
        itemTeam1Spieler5 = new ArrayList<>();


        rootNode = FirebaseDatabase.getInstance(linkDatabase);
        referenceGames = rootNode.getReference(league + "/games");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_last_games, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        referenceGames.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    GamesHelperClass game = ds.getValue(GamesHelperClass.class);
                    // get winner team
                    boolean winTeam1 = game.team1Win;
                    if (winTeam1) {
                        itemWinTeam1.add("1");
                    } else {
                        itemWinTeam1.add(null);
                    }
                    // get player lists and fill with nulls
                    ArrayList team1 = game.getTeam1Players();
                    int counterTeam1 = team1.size();
                    while (counterTeam1 < 5) {
                        team1.add(null);
                        counterTeam1 = counterTeam1 + 1;
                    }
                    ArrayList team2 = game.getTeam2Players();
                    int counterTeam2 = team2.size();
                    while (counterTeam2 < 5) {
                        team2.add(null);
                        counterTeam2 = counterTeam2 + 1;
                    }

                    // fill the static arrays
                    itemTeam1Spieler1.add((String) team1.get(0));
                    itemTeam1Spieler2.add((String) team1.get(1));
                    itemTeam1Spieler3.add((String) team1.get(2));
                    itemTeam1Spieler4.add((String) team1.get(3));
                    itemTeam1Spieler5.add((String) team1.get(4));

                    itemTeam2Spieler1.add((String) team2.get(0));
                    itemTeam2Spieler2.add((String) team2.get(1));
                    itemTeam2Spieler3.add((String) team2.get(2));
                    itemTeam2Spieler4.add((String) team2.get(3));
                    itemTeam2Spieler5.add((String) team2.get(4));
                }
                // show the lastest games on top, i.e. reverse all arrays
                Collections.reverse(itemTeam1Spieler1);
                Collections.reverse(itemTeam1Spieler2);
                Collections.reverse(itemTeam1Spieler3);
                Collections.reverse(itemTeam1Spieler4);
                Collections.reverse(itemTeam1Spieler5);
                Collections.reverse(itemTeam2Spieler1);
                Collections.reverse(itemTeam2Spieler2);
                Collections.reverse(itemTeam2Spieler3);
                Collections.reverse(itemTeam2Spieler4);
                Collections.reverse(itemTeam2Spieler5);
                Collections.reverse(itemWinTeam1);


                recyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerview);
                rvLayoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(rvLayoutManager);

                rvadapter = new RvAdapterKlasseLastGames();
                recyclerView.setAdapter(rvadapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}