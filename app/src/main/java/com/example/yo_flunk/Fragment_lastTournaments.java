package com.example.yo_flunk;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_lastTournaments#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_lastTournaments extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    RecyclerView recyclerView2;
    RecyclerView.Adapter<RvAdapterKlasseTabelle.ViewHolderKlasse> rvadapter2;
    RecyclerView.LayoutManager rvLayoutManager2;


    static ArrayList<String> itemPlayers, itemPlayers_unsorted;
    //static ArrayList<String> itemAvatar_id;
    static ArrayList<Integer> itemWins, itemLosses, itemTreffer, itemAvatar, itemElo, itemWins_unsorted, itemLosses_unsorted, itemTreffer_unsorted, itemAvatar_unsorted, headid_players, itemElo_unsorted;
    int id_latesthighlight = 0;
    String highlight;
    FirebaseDatabase rootNode;
    DatabaseReference reference_users, reference_games, reference_highlights;
    String league;
    String link_database;

    public Fragment_lastTournaments() {
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
    public static Fragment_lastTournaments newInstance(String param1, String param2) {
        Fragment_lastTournaments fragment = new Fragment_lastTournaments();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent main_intent = getActivity().getIntent();
        league = main_intent.getStringExtra("league");
        id_latesthighlight = main_intent.getIntExtra("id_highlight", 0);
        itemPlayers_unsorted = main_intent.getStringArrayListExtra("names_players");
        itemWins_unsorted = main_intent.getIntegerArrayListExtra("wins_players");
        itemLosses_unsorted = main_intent.getIntegerArrayListExtra("losses_players");
        itemTreffer_unsorted = main_intent.getIntegerArrayListExtra("treffer_players");
        headid_players = main_intent.getIntegerArrayListExtra("headid_players");
        itemElo_unsorted = main_intent.getIntegerArrayListExtra("elo_players");
        link_database = this.getResources().getString(R.string.firebase_link);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_last_tournaments, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}