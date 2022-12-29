// Adapter Class for the RecyclerView in planTournamentActivity, which organizes the teams

package com.example.yo_flunk;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class RvAdapterKlasseTeams extends RecyclerView.Adapter<RvAdapterKlasseTeams.ViewHolderKlasse> {

    public static AlertDialog dialog;

    public class ViewHolderKlasse extends RecyclerView.ViewHolder{

        TextView itemPlayer1, itemPlayer2, itemTeam;
        ConstraintLayout layoutTeam;


        public ViewHolderKlasse(@NonNull View itemView) {
            super(itemView);
            itemPlayer1 = (TextView) itemView.findViewById(R.id.txt_player1);
            itemPlayer2 = (TextView) itemView.findViewById(R.id.txt_player2);
            itemTeam = (TextView) itemView.findViewById(R.id.txt_teamname);
            layoutTeam = itemView.findViewById(R.id.teambox);
        }
    }

    @NonNull
    @Override
    public ViewHolderKlasse onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View itemView1 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.team_item_layout, null);
        return new ViewHolderKlasse(itemView1);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolderKlasse viewHolderKlasse, int position) {
        viewHolderKlasse.itemTeam.setText(PlanTournamentActivity.itemTeamNames.get(position));

        viewHolderKlasse.itemPlayer1.setText(PlanTournamentActivity.itemPlayer1.get(position));
        viewHolderKlasse.itemPlayer2.setText(PlanTournamentActivity.itemPlayer2.get(position));

        viewHolderKlasse.layoutTeam.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // delete the team onLongClick
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(v.getContext());

                final View popupDeleteTeam = LayoutInflater.from(v.getContext()).inflate(R.layout.popup_delete_team, null);

                String teamName = ((TextView) v.findViewById(R.id.txt_teamname)).getText().toString();
                TextView txtInfo = popupDeleteTeam.findViewById(R.id.txt_deleteteam);
                String msg = "Möchtest du Team " + teamName + " wirklich löschen?";
                txtInfo.setText(msg);

                Button btnDelete = popupDeleteTeam.findViewById(R.id.btn_delete_team);
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String msg = ((TextView) popupDeleteTeam.findViewById(R.id.txt_deleteteam)).getText().toString();
                        msg = msg.replace("Möchtest du Team ", "");
                        msg = msg.replace(" wirklich löschen?", "");
                        String teamname = msg;

                        int index = 0;
                        // find index of team via teamname:
                        for(int j = 0; j< PlanTournamentActivity.itemTeamNames.size(); j++){
                            if(PlanTournamentActivity.itemTeamNames.get(j).equals(teamname)){
                                index = j;
                            }
                        }
                        // delete all entries at index j
                        PlanTournamentActivity.itemTeamNames.remove(index);
                        PlanTournamentActivity.itemPlayer1.remove(index);
                        PlanTournamentActivity.itemPlayer2.remove(index);

                        v.setVisibility(View.GONE);

                        // redraw recycler:
                        RecyclerView recyclerView1 = (RecyclerView) PlanTournamentActivity.recyclerView1;
                        recyclerView1.setLayoutManager(PlanTournamentActivity.rvLayoutManager1);
                        RecyclerView.Adapter rvadapter1 = new RvAdapterKlasseTeams();
                        recyclerView1.setAdapter(rvadapter1);

                        dialog.dismiss();

                    }
                });

                Button btnCancel = popupDeleteTeam.findViewById(R.id.btn_cancel);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialogBuilder.setView(popupDeleteTeam);
                dialog = dialogBuilder.create();
                dialog.show();
                return false;
            }
        });

    }

    private final int limit = 50;
    @Override
    public int getItemCount() {
        return Math.min(PlanTournamentActivity.itemPlayer1.size(), limit);
    }
}
