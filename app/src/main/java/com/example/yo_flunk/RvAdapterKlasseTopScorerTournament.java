// Adapter Class for the RecyclerView in Fragment_trefferTurnier

package com.example.yo_flunk;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RvAdapterKlasseTopScorerTournament extends RecyclerView.Adapter<RvAdapterKlasseTopScorerTournament.ViewHolderKlasse2> {

    public class ViewHolderKlasse2 extends RecyclerView.ViewHolder{
        TextView itemTeams;
        TextView itemWins;
        TextView itemLosses;
        TextView itemTreffer;
        TextView itemRanks;

        @SuppressLint("WrongViewCast")
        public ViewHolderKlasse2(@NonNull View itemView) {
            super(itemView);
            itemRanks = itemView.findViewById(R.id.textRank);
            itemTeams = (TextView) itemView.findViewById(R.id.textPlayers);
            itemWins = (TextView) itemView.findViewById(R.id.textWins);
            itemLosses = (TextView) itemView.findViewById(R.id.textLosses);
            itemTreffer = (TextView) itemView.findViewById(R.id.textTreffer);
        }
    }

    @NonNull
    @Override
    public ViewHolderKlasse2 onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView1 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.group_item_layout, null);
        return new ViewHolderKlasse2(itemView1);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderKlasse2 viewHolderKlasse, int position) {
        viewHolderKlasse.itemRanks.setText(String.valueOf(position + 1));
        viewHolderKlasse.itemTeams.setText(Fragment_trefferTournament.names.get(position));
        viewHolderKlasse.itemWins.setText(Fragment_trefferTournament.wins.get(position).toString());
        viewHolderKlasse.itemLosses.setText(Fragment_trefferTournament.loses.get(position).toString());
        viewHolderKlasse.itemTreffer.setText(Fragment_trefferTournament.hits.get(position).toString());
    }

    private final int limit=10;
    @Override
    public int getItemCount() {
        return Fragment_trefferTournament.names.size();
    }
}

