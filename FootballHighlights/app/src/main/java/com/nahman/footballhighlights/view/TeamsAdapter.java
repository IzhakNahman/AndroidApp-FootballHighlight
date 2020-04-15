package com.nahman.footballhighlights.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.nahman.footballhighlights.R;
import com.nahman.footballhighlights.controller.HighlightsActivity;
import com.nahman.footballhighlights.controller.TeamsFragment;
import com.nahman.footballhighlights.model.Highlight;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class TeamsAdapter extends RecyclerView.Adapter<TeamsAdapter.TeamsViewHolder> {

    List<Highlight> data;
    ArrayList<String> teamsNames = new ArrayList<>();
    ArrayList<String> favoriteTeams;
    SharedPreferences sharedPreferences;

    public TeamsAdapter(List<Highlight> data, String teamFilter, Context context) {
        this.data = data;
        teamsNames.add("- All Teams -");

        sharedPreferences = context.getSharedPreferences("com.nahman.footballhighlights", Context.MODE_PRIVATE);

        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("favoriteTeams", null);


        if (set != null) {
            favoriteTeams = new ArrayList<>(set);
        } else {
            favoriteTeams = new ArrayList<>();
        }

        createLeaguesNamesList(teamFilter);

    }

    private void createLeaguesNamesList(String teamFilter) {


        if (teamFilter == null) {
            teamFilter = "";
        }

        if (favoriteTeams.size() > 0) {
            for (int i = 0; i < favoriteTeams.size(); i++) {
                int flag = 0;
                if (teamsNames.size() > 0)
                    for (int j = 0; j < teamsNames.size(); j++) {
                        if (favoriteTeams.get(i).equals(teamsNames.get(j)))
                            flag = j;
                    }

                if (flag == 0 && favoriteTeams.get(i).toUpperCase().contains(teamFilter.toUpperCase()))
                    teamsNames.add(favoriteTeams.get(i));
            }
        }


        for (int i = 0; i < data.size(); i++) {
            int flagTeam1 = 0;
            int flagTeam2 = 0;
            if (teamsNames.size() > 0)
                for (int j = 0; j < teamsNames.size(); j++) {
                    if (data.get(i).getTeam1().equals(teamsNames.get(j)))
                        flagTeam1 = j;
                    if (data.get(i).getTeam2().equals(teamsNames.get(j)))
                        flagTeam2 = j;

                }

            if (flagTeam1 == 0 && data.get(i).getTeam1().toUpperCase().contains(teamFilter.toUpperCase()))
                teamsNames.add(data.get(i).getTeam1());
            if (flagTeam2 == 0 && data.get(i).getTeam2().toUpperCase().contains(teamFilter.toUpperCase()))
                teamsNames.add(data.get(i).getTeam2());
        }
    }

    @NonNull
    @Override
    public TeamsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context ctx = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View v = inflater.inflate(R.layout.leagues_teams_item, parent, false);
        return new TeamsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamsViewHolder holder, int position) {

        String name = teamsNames.get(position);
        holder.tvName.setText(name);

        if(name.equals("- All Teams -")){
            holder.ivFavorite.setVisibility(View.GONE);
        }

        if (favoriteTeams.contains(name)) {
            holder.ivFavorite.setImageResource(R.drawable.favorite);
        }


        int imageId = getTeamImageUrl(holder.tvName.getText().toString()
                , holder.cl.getContext());
        if (imageId != 0) {
            System.out.println(name);
            System.out.println(position);
            holder.ivImage.setImageResource(imageId);
        } else {
            holder.ivImage.setImageResource(R.drawable.football);
        }

        holder.cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<Highlight> filteredData = createFilteredData(holder.tvName.getText().toString());
                if (filteredData.size() > 0) {
                    Intent intent = new Intent(v.getContext(), HighlightsActivity.class);
                    intent.putParcelableArrayListExtra("highlights", filteredData);
                    v.getContext().startActivity(intent);
                } else {
                    Toast.makeText(v.getContext(), "No Highlights For this Team", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.ivFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.equals("- All Teams -")){
                    return;
                }

                if (favoriteTeams.contains(name)) {
                    favoriteTeams.remove(name);
                    holder.ivFavorite.setImageResource(R.drawable.no_favorite);
                } else {
                    favoriteTeams.add(name);
                    holder.ivFavorite.setImageResource(R.drawable.favorite);
                }
                HashSet<String> set = new HashSet<>(favoriteTeams);
                sharedPreferences.edit().putStringSet("favoriteTeams", set).apply();
            }
        });

    }

    private int getTeamImageUrl(String teamName, Context ctx) {

        String newTeamName = teamName.replace(" ", "_").toLowerCase();
        int resId = ctx.getResources().getIdentifier(newTeamName, "drawable", ctx.getPackageName());
        return resId;

    }

    private ArrayList<Highlight> createFilteredData(String teamName) {

        if (teamName.equals("- All Teams -")) {
            return new ArrayList<>(data);
        }

        //System.out.println("teamName "+teamName);
        ArrayList<Highlight> newData = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getTeam1().contains(teamName) || data.get(i).getTeam2().contains(teamName)) {
                newData.add(data.get(i));
            }
        }
        return newData;
    }

    @Override
    public int getItemCount() {
        return teamsNames.size();
    }

    public class TeamsViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout cl;
        TextView tvName;
        ImageView ivImage, ivFavorite;


        public TeamsViewHolder(@NonNull View itemView) {
            super(itemView);

            cl = itemView.findViewById(R.id.leaguesTeams_constraintLayout);
            tvName = itemView.findViewById(R.id.leaguesTeams_tvName);
            ivImage = itemView.findViewById(R.id.leaguesTeams_ivImage);
            ivFavorite = itemView.findViewById(R.id.leaguesTeams_ivFavorite);

        }
    }
}
