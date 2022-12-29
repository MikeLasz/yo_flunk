// Adapter Class for the RecyclerView in TabelleActivity

package com.example.yo_flunk;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RvAdapterKlasseTabelle extends RecyclerView.Adapter<RvAdapterKlasseTabelle.ViewHolderKlasse> {

    public class ViewHolderKlasse extends RecyclerView.ViewHolder{
        TextView itemPlayers;
        ImageView itemAvatar;
        ImageView itemImageView;
        TextView itemWins;
        TextView itemLosses;
        TextView itemTreffer;
        TextView itemRanks;
        TextView itemElo;

        @SuppressLint("WrongViewCast")
        public ViewHolderKlasse(@NonNull View itemView) {
            super(itemView);
            itemRanks = itemView.findViewById(R.id.textRank);
            itemPlayers = (TextView) itemView.findViewById(R.id.textPlayers);
            itemImageView = (ImageView) itemView.findViewById(R.id.imageViewItem);
            itemAvatar = (ImageView) itemView.findViewById(R.id.imageAvatar);
            itemWins = (TextView) itemView.findViewById(R.id.textWins);
            itemLosses = (TextView) itemView.findViewById(R.id.textLosses);
            itemTreffer = (TextView) itemView.findViewById(R.id.textTreffer);
            itemElo = (TextView) itemView.findViewById(R.id.textElo);
        }
    }

    @NonNull
    @Override
    public ViewHolderKlasse onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView1 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.table_item_layout, null);
        return new ViewHolderKlasse(itemView1);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderKlasse viewHolderKlasse, int position) {
        viewHolderKlasse.itemRanks.setText(String.valueOf(position + 1));
        viewHolderKlasse.itemPlayers.setText(Fragment_scoreboard.itemPlayers.get(position));
        viewHolderKlasse.itemElo.setText(String.valueOf(Fragment_scoreboard.itemElo.get(position)));
        viewHolderKlasse.itemAvatar.setImageResource(Fragment_scoreboard.itemAvatar.get(position));
        viewHolderKlasse.itemWins.setText(Fragment_scoreboard.itemWins.get(position).toString());
        viewHolderKlasse.itemLosses.setText(Fragment_scoreboard.itemLosses.get(position).toString());
        viewHolderKlasse.itemTreffer.setText(Fragment_scoreboard.itemTreffer.get(position).toString());
        viewHolderKlasse.itemElo.setText(Fragment_scoreboard.itemElo.get(position).toString());
    }

    private final int limit=10;
    @Override
    public int getItemCount() {
        return Fragment_scoreboard.itemPlayers.size();
    }
}

