// Adapter Class for the RecyclerView in Fragment_lastGames, which organizes
// the last regular games that had been played.
package com.example.yo_flunk;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.util.TypedValue;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RvAdapterKlasseLastGames extends RecyclerView.Adapter<RvAdapterKlasseLastGames.ViewHolderKlasse> {

    public class ViewHolderKlasse extends RecyclerView.ViewHolder{

        TextView itemTeam2Player1;
        TextView itemTeam2Player2;
        TextView itemTeam2Player3;
        TextView itemTeam2Player4;
        TextView itemTeam2Player5;

        TextView itemTeam1Player1;
        TextView itemTeam1Player2;
        TextView itemTeam1Player3;
        TextView itemTeam1Player4;
        TextView itemTeam1Player5;

        LinearLayout layout_team1;
        LinearLayout layout_team2;
        ImageView pokal1;
        ImageView pokal2;

        public ViewHolderKlasse(@NonNull View itemView) {
            super(itemView);
            itemTeam1Player1 = (TextView) itemView.findViewById(R.id.textTeam1Player1);
            itemTeam1Player2 = (TextView) itemView.findViewById(R.id.textTeam1Player2);
            itemTeam1Player3 = (TextView) itemView.findViewById(R.id.textTeam1Player3);
            itemTeam1Player4 = (TextView) itemView.findViewById(R.id.textTeam1Player4);
            itemTeam1Player5 = (TextView) itemView.findViewById(R.id.textTeam1Player5);


            itemTeam2Player1 = (TextView) itemView.findViewById(R.id.textTeam2Player1);
            itemTeam2Player2 = (TextView) itemView.findViewById(R.id.textTeam2Player2);
            itemTeam2Player3 = (TextView) itemView.findViewById(R.id.textTeam2Player3);
            itemTeam2Player4 = (TextView) itemView.findViewById(R.id.textTeam2Player4);
            itemTeam2Player5 = (TextView) itemView.findViewById(R.id.textTeam2Player5);
            layout_team1 = (LinearLayout) itemView.findViewById(R.id.layout_lg_team1);
            layout_team2 = (LinearLayout) itemView.findViewById(R.id.layout_lg_team2);
            pokal1 = (ImageView) itemView.findViewById(R.id.pokal_team1);
            pokal2 = (ImageView) itemView.findViewById(R.id.pokal_team2);
        }
    }

    @NonNull
    @Override
    public ViewHolderKlasse onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View itemView1 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.played_game_item_layout, null);
        return new ViewHolderKlasse(itemView1);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderKlasse viewHolderKlasse, int position) {

        viewHolderKlasse.itemTeam1Player1.setText(Fragment_lastGames.itemTeam1Spieler1.get(position));
        viewHolderKlasse.itemTeam1Player1.setTextSize(TypedValue.COMPLEX_UNIT_DIP,22);

        viewHolderKlasse.itemTeam2Player1.setText(Fragment_lastGames.itemTeam2Spieler1.get(position));
        viewHolderKlasse.itemTeam2Player1.setTextSize(TypedValue.COMPLEX_UNIT_DIP,22);

        //team1
        viewHolderKlasse.itemTeam1Player2.setText(Fragment_lastGames.itemTeam1Spieler2.get(position));

        if (TextUtils.isEmpty(Fragment_lastGames.itemTeam1Spieler2.get(position))) {
            viewHolderKlasse.itemTeam1Player2.setTextSize(TypedValue.COMPLEX_UNIT_DIP,0);
        }
        else{
            viewHolderKlasse.itemTeam1Player2.setTextSize(TypedValue.COMPLEX_UNIT_DIP,22);

        }

        viewHolderKlasse.itemTeam1Player3.setText(Fragment_lastGames.itemTeam1Spieler3.get(position));
        if (TextUtils.isEmpty(Fragment_lastGames.itemTeam1Spieler3.get(position))) {
            viewHolderKlasse.itemTeam1Player3.setTextSize(TypedValue.COMPLEX_UNIT_DIP,0);
        }
        else{
            viewHolderKlasse.itemTeam1Player3.setTextSize(TypedValue.COMPLEX_UNIT_DIP,22);
        }

        viewHolderKlasse.itemTeam1Player4.setText(Fragment_lastGames.itemTeam1Spieler4.get(position));
        if (TextUtils.isEmpty(Fragment_lastGames.itemTeam1Spieler4.get(position))) {
            viewHolderKlasse.itemTeam1Player4.setTextSize(TypedValue.COMPLEX_UNIT_DIP,0);
        }
        else{
            viewHolderKlasse.itemTeam1Player4.setTextSize(TypedValue.COMPLEX_UNIT_DIP,22);
        }

        viewHolderKlasse.itemTeam1Player5.setText(Fragment_lastGames.itemTeam1Spieler5.get(position));
        if (TextUtils.isEmpty(Fragment_lastGames.itemTeam1Spieler5.get(position))) {
            viewHolderKlasse.itemTeam1Player5.setTextSize(TypedValue.COMPLEX_UNIT_DIP,0);
        }
        else{
            viewHolderKlasse.itemTeam1Player2.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
            viewHolderKlasse.itemTeam1Player3.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
            viewHolderKlasse.itemTeam1Player4.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
            viewHolderKlasse.itemTeam1Player1.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
            viewHolderKlasse.itemTeam1Player5.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
        }

        //team2
        viewHolderKlasse.itemTeam2Player2.setText(Fragment_lastGames.itemTeam2Spieler2.get(position));
        if (TextUtils.isEmpty(Fragment_lastGames.itemTeam2Spieler2.get(position))) {
            viewHolderKlasse.itemTeam2Player2.setTextSize(TypedValue.COMPLEX_UNIT_DIP,0);
        }
        else{
            viewHolderKlasse.itemTeam2Player2.setTextSize(TypedValue.COMPLEX_UNIT_DIP,22);
        }

        viewHolderKlasse.itemTeam2Player3.setText(Fragment_lastGames.itemTeam2Spieler3.get(position));
        if (TextUtils.isEmpty(Fragment_lastGames.itemTeam2Spieler3.get(position))) {
            viewHolderKlasse.itemTeam2Player3.setTextSize(TypedValue.COMPLEX_UNIT_DIP,0);
        }
        else{
            viewHolderKlasse.itemTeam2Player3.setTextSize(TypedValue.COMPLEX_UNIT_DIP,22);
        }

        viewHolderKlasse.itemTeam2Player4.setText(Fragment_lastGames.itemTeam2Spieler4.get(position));
        if (TextUtils.isEmpty(Fragment_lastGames.itemTeam2Spieler4.get(position))) {
            viewHolderKlasse.itemTeam2Player4.setTextSize(TypedValue.COMPLEX_UNIT_DIP,0);
        }
        else{
            viewHolderKlasse.itemTeam2Player4.setTextSize(TypedValue.COMPLEX_UNIT_DIP,22);
        }

        viewHolderKlasse.itemTeam2Player5.setText(Fragment_lastGames.itemTeam2Spieler5.get(position));
        if (TextUtils.isEmpty(Fragment_lastGames.itemTeam2Spieler5.get(position))) {
            viewHolderKlasse.itemTeam2Player5.setTextSize(TypedValue.COMPLEX_UNIT_DIP,0);
        }
        else{
            viewHolderKlasse.itemTeam2Player2.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
            viewHolderKlasse.itemTeam2Player3.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
            viewHolderKlasse.itemTeam2Player4.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
            viewHolderKlasse.itemTeam2Player1.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
            viewHolderKlasse.itemTeam2Player5.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
        }


        if (TextUtils.isEmpty(Fragment_lastGames.itemWinTeam1.get(position))) {
            viewHolderKlasse.layout_team1.setBackground(null);
            viewHolderKlasse.layout_team2.setBackgroundResource(R.drawable.button_winner_last_games);
            viewHolderKlasse.pokal1.setVisibility(View.INVISIBLE);
            viewHolderKlasse.pokal2.setVisibility(View.VISIBLE);

        }
        else{
            viewHolderKlasse.layout_team2.setBackground(null);
            viewHolderKlasse.layout_team1.setBackgroundResource(R.drawable.button_winner_last_games);
            viewHolderKlasse.pokal2.setVisibility(View.INVISIBLE);
            viewHolderKlasse.pokal1.setVisibility(View.VISIBLE);
        }
    }

    private final int limit = 50;
    @Override
    public int getItemCount() {
        return Math.min(Fragment_lastGames.itemTeam1Spieler2.size(), limit);
    }
}