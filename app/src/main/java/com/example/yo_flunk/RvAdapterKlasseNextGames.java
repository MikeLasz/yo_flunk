// Adapter Class for the RecyclerView in Fragment_lastGames, which organizes the played and games to be played
// in a Tournament

package com.example.yo_flunk;

import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RvAdapterKlasseNextGames extends RecyclerView.Adapter<RvAdapterKlasseNextGames.ViewHolderKlasse> {

    public class ViewHolderKlasse extends RecyclerView.ViewHolder{

        TextView itemTeam1;
        TextView itemTeam2;

        LinearLayout layout;
        LinearLayout layoutTeam1;
        LinearLayout layoutTeam2;
        ImageView pokal1;
        ImageView pokal2;
        TextView group;

        public ViewHolderKlasse(@NonNull View itemView) {
            super(itemView);
            itemTeam1 = (TextView) itemView.findViewById(R.id.textTeam1);
            itemTeam2 = (TextView) itemView.findViewById(R.id.textTeam2);
            layout = itemView.findViewById(R.id.layout);
            pokal1 = (ImageView) itemView.findViewById(R.id.pokal_team1);
            pokal2 = (ImageView) itemView.findViewById(R.id.pokal_team2);

            layoutTeam1 = (LinearLayout) itemView.findViewById(R.id.layout_lg_team1);
            layoutTeam2 = (LinearLayout) itemView.findViewById(R.id.layout_lg_team2);
            group = itemView.findViewById(R.id.txtview_group);

        }
    }

    @NonNull
    @Override
    public ViewHolderKlasse onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View itemView1 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.next_game_tournament_item_layout, null);
        return new ViewHolderKlasse(itemView1);
    }





    @Override
    public void onBindViewHolder(@NonNull ViewHolderKlasse viewHolderKlasse, int position) {
        if(position < NextGamesTournamentActivity.NUM_SHOW_NEXT_GAMES && position < NextGamesTournamentActivity.itemTeam1Next.size()) {
            // i.e. that game is a game that hasn't been played yet

            // Next Games:
            viewHolderKlasse.itemTeam1.setText(NextGamesTournamentActivity.itemTeam1Next.get(position));
            //viewHolderKlasse.itemTeam1.setBackgroundResource(R.color.blue); // Todo Change colors
            viewHolderKlasse.itemTeam1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22);
            viewHolderKlasse.pokal1.setVisibility(View.INVISIBLE);
            viewHolderKlasse.layoutTeam1.setBackground(null);

            viewHolderKlasse.itemTeam2.setText(NextGamesTournamentActivity.itemTeam2Next.get(position));
            //viewHolderKlasse.itemTeam1.setBackgroundResource(R.color.blue); // Todo Change colors
            viewHolderKlasse.itemTeam2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22);
            viewHolderKlasse.pokal2.setVisibility(View.INVISIBLE);
            viewHolderKlasse.layoutTeam2.setBackground(null);
            int groupNr = NextGamesTournamentActivity.itemGroupNext.get(position) + 1;
            viewHolderKlasse.group.setText("Gruppe " + groupNr);

            viewHolderKlasse.layout.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    String team1 = ((TextView) v.findViewById(R.id.textTeam1)).getText().toString();
                    String team2 = ((TextView) v.findViewById(R.id.textTeam2)).getText().toString();
                    // 1. find game
                    // 2. set corresponding entry in allGamesPlayed to true
                    // restart this activity
                    // of course, in production Ill first start the playgame activity.

                    ArrayList<String> teamNames = new ArrayList<>();
                    teamNames.add(team1);
                    teamNames.add(team2);

                    ArrayList<String> playersTeam1 = Utility.findPlayersOfTeam(team1, NextGamesTournamentActivity.teamNames, NextGamesTournamentActivity.players1, NextGamesTournamentActivity.players2);
                    ArrayList<String> playersTeam2 = Utility.findPlayersOfTeam(team2, NextGamesTournamentActivity.teamNames, NextGamesTournamentActivity.players1, NextGamesTournamentActivity.players2);
                    // concatenate both String lists (I am sure that there is a way more efficient method than that...)
                    ArrayList<String> players = new ArrayList<>();
                    boolean[] teamPartition = new boolean[playersTeam1.size() + playersTeam2.size()];
                    int counter = 0;
                    for(String player:playersTeam1){
                        players.add(player);
                        teamPartition[counter] = true;
                        counter = counter + 1;
                    }
                    for(String player:playersTeam2){
                        players.add(player);
                        teamPartition[counter] = false;
                        counter = counter + 1;
                    }


                    Context context = v.getContext();
                    Intent next_i = new Intent(context, PlayGameActivity.class);
                    next_i.putStringArrayListExtra("players", ((ArrayList<String>) players));
                    next_i.putExtra("partition", teamPartition);
                    next_i.putStringArrayListExtra("teamnames", teamNames);
                    next_i.putExtra("track_geo", false);
                    next_i.putExtra("notification", false);
                    next_i.putExtra("league", "tournament_"+ NextGamesTournamentActivity.tournamentName);
                    context.startActivity(next_i);
                }
            });

        } else{
            // Games that already have been played:
            int shiftedPos;
            if(position>= NextGamesTournamentActivity.NUM_SHOW_NEXT_GAMES){
                shiftedPos = position - NextGamesTournamentActivity.NUM_SHOW_NEXT_GAMES;
            }
            else{
                shiftedPos = position - NextGamesTournamentActivity.itemTeam1Next.size();
            }
            viewHolderKlasse.itemTeam1.setText(NextGamesTournamentActivity.itemTeam1Finished.get(shiftedPos));
            viewHolderKlasse.itemTeam1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22);

            viewHolderKlasse.itemTeam2.setText(NextGamesTournamentActivity.itemTeam2Finished.get(shiftedPos));
            viewHolderKlasse.itemTeam2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22);
            if(NextGamesTournamentActivity.itemTeam1WinFinished.get(shiftedPos)){
                viewHolderKlasse.pokal2.setVisibility(View.INVISIBLE);
                viewHolderKlasse.pokal1.setVisibility(View.VISIBLE);

                viewHolderKlasse.layoutTeam2.setBackground(null);
                viewHolderKlasse.layoutTeam1.setBackgroundResource(R.drawable.button_winner_last_games);
            }else{
                viewHolderKlasse.pokal1.setVisibility(View.INVISIBLE);
                viewHolderKlasse.pokal2.setVisibility(View.VISIBLE);
                viewHolderKlasse.layoutTeam1.setBackground(null);
                viewHolderKlasse.layoutTeam2.setBackgroundResource(R.drawable.button_winner_last_games);
            }
            int groupNr = NextGamesTournamentActivity.itemGroupFinished.get(shiftedPos) + 1;
            viewHolderKlasse.group.setText("Gruppe " + groupNr);
        }

    }

    private final int limit = 50;
    @Override
    public int getItemCount() {
        return Math.min(NextGamesTournamentActivity.itemTeam1Next.size() + NextGamesTournamentActivity.itemTeam1Finished.size(), limit);
    }
}
