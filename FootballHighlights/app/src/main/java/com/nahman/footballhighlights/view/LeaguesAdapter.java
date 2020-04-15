package com.nahman.footballhighlights.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.nahman.footballhighlights.R;
import com.nahman.footballhighlights.controller.HighlightsActivity;
import com.nahman.footballhighlights.model.Highlight;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class LeaguesAdapter extends RecyclerView.Adapter<LeaguesAdapter.LeaguesViewHolder> {

    List<Highlight> data;

    ArrayList<String> leaguesNames = new ArrayList<>();
    ArrayList<String> countryNames = new ArrayList<>();
    ArrayList<String> favoriteLeagues;
    ArrayList<String> favoriteCountry;
    SharedPreferences sharedPreferences;

    public LeaguesAdapter(List<Highlight> data, Context context) {
        this.data = data;
        leaguesNames.add("- All Leagues -");
        countryNames.add("- All Leagues -");

        sharedPreferences = context.getSharedPreferences("com.nahman.footballhighlights", Context.MODE_PRIVATE);

        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("favoriteLeagues", null);
        HashSet<String> set2 = (HashSet<String>) sharedPreferences.getStringSet("favoriteCountry", null);


        if (set != null && set2!=null) {
            favoriteLeagues = new ArrayList<>(set);
            favoriteCountry = new ArrayList<>(set2);
        } else {
            favoriteLeagues = new ArrayList<>();
            favoriteCountry = new ArrayList<>();
        }

        createLeaguesNamesList();



    }

    private void createLeaguesNamesList(){

        if (favoriteLeagues.size() > 0) {
            for (int i = 0; i < favoriteLeagues.size(); i++) {
                int flag = 0;
                if (leaguesNames.size() > 0)
                    for (int j = 0; j < leaguesNames.size(); j++) {
                        if (favoriteLeagues.get(i).equals(leaguesNames.get(j)))
                            flag = j;
                    }

                if (flag == 0) {
                    leaguesNames.add(favoriteLeagues.get(i));
                    countryNames.add(favoriteCountry.get(i));
                }
            }
        }


        for (int i = 0; i < data.size(); i++) {
            int flag = 0;
            if(leaguesNames.size()>0)
                for (int j = 0; j < leaguesNames.size(); j++) {
                    if (data.get(i).getCompetitionName().equals(leaguesNames.get(j)))
                        flag = 1;
                }
            if (flag == 0) {
                leaguesNames.add(data.get(i).getCompetitionName());
                countryNames.add(data.get(i).getCountryName());
            }
        }
    }

    @NonNull
    @Override
    public LeaguesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context ctx = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View v = inflater.inflate(R.layout.leagues_teams_item,parent,false);
        return new LeaguesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaguesViewHolder holder, int position) {

        String name = leaguesNames.get(position);
        holder.tvName.setText(name);

        if(name.equals("- All Leagues -")){
            holder.ivFavorite.setVisibility(View.GONE);
        }

        if (favoriteLeagues.contains(name)) {
            holder.ivFavorite.setImageResource(R.drawable.favorite);
        }

        int imageId = getLeagueImageUrl( countryNames.get(position)
                , holder.cl.getContext());
        if (imageId!=0){
            System.out.println(name);
            System.out.println(position);
            holder.ivImage.setImageResource(imageId);
        }else{
            holder.ivImage.setImageResource(R.drawable.football);
        }


        holder.cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), HighlightsActivity.class);
                intent.putParcelableArrayListExtra("highlights",createFilteredData(holder.tvName.getText().toString()));
                v.getContext().startActivity(intent);
            }
        });


        holder.ivFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.equals("- All Leagues -")){
                    return;
                }

                if (favoriteLeagues.contains(name)) {
                    int removeIndex = favoriteLeagues.indexOf(name);
                    favoriteLeagues.remove(removeIndex);
                    favoriteCountry.remove(removeIndex);
                    holder.ivFavorite.setImageResource(R.drawable.no_favorite);
                } else {
                    favoriteLeagues.add(name);
                    favoriteCountry.add(countryNames.get(position));
                    holder.ivFavorite.setImageResource(R.drawable.favorite);
                }
                HashSet<String> set = new HashSet<>(favoriteLeagues);
                HashSet<String> set2 = new HashSet<>(favoriteCountry);
                sharedPreferences.edit().putStringSet("favoriteLeagues", set).apply();
                sharedPreferences.edit().putStringSet("favoriteCountry", set2).apply();


            }
        });

    }

    private int getLeagueImageUrl(String leagueName, Context ctx){

        String newLeagueName = leagueName.replace(" ", "_").toLowerCase();
        int resId = ctx.getResources().getIdentifier(newLeagueName, "drawable", ctx.getPackageName());
        return resId;

    }

    private ArrayList<Highlight> createFilteredData(String leagueName){
        if (leagueName.equals("- All Leagues -")){
            return new ArrayList<>(data);
        }
        System.out.println("leagueName "+leagueName);
        ArrayList<Highlight> newData = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            String competition = data.get(i).getCompetitionName()+" "+data.get(i).getCountryName();
            if (competition.contains(leagueName)){
                newData.add(data.get(i));
            }
        }
        return newData;
    }

    @Override
    public int getItemCount() {
        return leaguesNames.size();
    }

    public class LeaguesViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout cl;
        TextView tvName;
        ImageView ivImage, ivFavorite;

        public LeaguesViewHolder(@NonNull View itemView) {
            super(itemView);

            cl = itemView.findViewById(R.id.leaguesTeams_constraintLayout);
            tvName = itemView.findViewById(R.id.leaguesTeams_tvName);
            ivImage = itemView.findViewById(R.id.leaguesTeams_ivImage);
            ivFavorite = itemView.findViewById(R.id.leaguesTeams_ivFavorite);


        }
    }


}
