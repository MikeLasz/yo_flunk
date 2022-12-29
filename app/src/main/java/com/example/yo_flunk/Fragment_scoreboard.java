package com.example.yo_flunk;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.stream.IntStream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_scoreboard#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_scoreboard extends Fragment {

    // ignore those
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    RecyclerView recyclerView;
    RecyclerView.Adapter<RvAdapterKlasseTabelle.ViewHolderKlasse> rvAdapter;
    RecyclerView.LayoutManager rvLayoutManager;

    static ArrayList<String> itemPlayers, itemPlayersUnsorted;
    static ArrayList<Integer> itemWins, itemLosses, itemTreffer, itemAvatar, itemElo, itemWinsUnsorted, itemLossesUnsorted, itemTrefferUnsorted, headIdPlayers, itemEloUnsorted;
    int idLatestHighlight = 0;
    String highlight;
    FirebaseDatabase rootNode;
    DatabaseReference referenceHighlights;
    String league;
    String linkDatabase;

    public Fragment_scoreboard() {
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
    public static Fragment_scoreboard newInstance(String param1, String param2) {
        Fragment_scoreboard fragment = new Fragment_scoreboard();
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
        idLatestHighlight = main_intent.getIntExtra("id_highlight", 0);
        itemPlayersUnsorted = main_intent.getStringArrayListExtra("names_players");
        itemWinsUnsorted = main_intent.getIntegerArrayListExtra("wins_players");
        itemLossesUnsorted = main_intent.getIntegerArrayListExtra("losses_players");
        itemTrefferUnsorted = main_intent.getIntegerArrayListExtra("treffer_players");
        headIdPlayers = main_intent.getIntegerArrayListExtra("headid_players");
        itemEloUnsorted = main_intent.getIntegerArrayListExtra("elo_players");
        linkDatabase = this.getResources().getString(R.string.firebase_link);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scoreboard, container, false);
        // Edit league title
        String leagueName = this.getResources().getString(R.string.league_name);
        TextView txtViewLeague = view.findViewById(R.id.txtView_league);
        txtViewLeague.setText(leagueName);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView txtView_League = view.findViewById(R.id.txtView_league);

        int numPlayers = itemPlayersUnsorted.size();

        ArrayList<Integer> itemAvatarUnsorted = new ArrayList<Integer>(numPlayers);
        TypedArray heads = getResources().obtainTypedArray(R.array.heads);

        for(int j=0; j<numPlayers; j++){
            itemAvatarUnsorted.add(j, heads.getResourceId(headIdPlayers.get(j),0));
        }

        //---------------------------------------------------------
        // sort all lists based on elo

        // I clone itemEloUnsorted as int[] because int[] is applicable to IntStream....
        int[] intList_elo = new int[numPlayers];
        for(int j=0; j<numPlayers; j++){
            int elo = itemEloUnsorted.get(j);
            intList_elo[j] = elo;
        }
        int[] sortedIndices = IntStream.range(0, intList_elo.length)
                .boxed().sorted((i, j) -> intList_elo[j] - intList_elo[i])
                .mapToInt(ele -> ele).toArray();
        ArrayList<String> itemPlayersSorted = new ArrayList<>(numPlayers);
        ArrayList<Integer> itemWinsSorted = new ArrayList<>(numPlayers);
        ArrayList<Integer> itemLossesSorted = new ArrayList<>(numPlayers);
        ArrayList<Integer> itemTrefferSorted = new ArrayList<>(numPlayers);
        ArrayList<Integer> itemAvatarSorted = new ArrayList<>(numPlayers);
        ArrayList<Integer> itemEloSorted = new ArrayList<>(numPlayers);
        for(int j=0; j<numPlayers; j++){
            int index = sortedIndices[j];
            itemPlayersSorted.add(j, itemPlayersUnsorted.get(index));
            itemWinsSorted.add(j, itemWinsUnsorted.get(index));
            itemLossesSorted.add(j, itemLossesUnsorted.get(index));
            itemTrefferSorted.add(j, itemTrefferUnsorted.get(index));
            itemAvatarSorted.add(j, itemAvatarUnsorted.get(index));
            itemEloSorted.add(j, itemEloUnsorted.get(index));
        }
        // save those values to the static variables
        itemPlayers = itemPlayersSorted;
        itemWins = itemWinsSorted;
        itemLosses = itemLossesSorted;
        itemTreffer = itemTrefferSorted;
        itemAvatar = itemAvatarSorted;
        itemElo = itemEloSorted;

        // read firebase database
        rootNode = FirebaseDatabase.getInstance(linkDatabase);
        referenceHighlights = rootNode.getReference(league + "/highlights");

        // get highlight of the month
        referenceHighlights.child(String.valueOf(idLatestHighlight)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                highlight = (String) dataSnapshot.getValue();
                // set textview to this value
                TextView txtViewHighlight = view.findViewById(R.id.txtview_highlight);
                txtViewHighlight.setMovementMethod(new ScrollingMovementMethod());
                txtViewHighlight.setText(highlight);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview2);
        rvLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(rvLayoutManager);
        rvAdapter = new RvAdapterKlasseTabelle();
        recyclerView.setAdapter(rvAdapter);
    }
}