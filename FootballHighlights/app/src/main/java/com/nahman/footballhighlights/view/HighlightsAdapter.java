package com.nahman.footballhighlights.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.nahman.footballhighlights.R;
import com.nahman.footballhighlights.controller.VideoActivity;
import com.nahman.footballhighlights.model.Highlight;
import com.nahman.footballhighlights.model.IO;
import com.squareup.picasso.Picasso;

import java.util.List;


public class HighlightsAdapter extends RecyclerView.Adapter<HighlightsAdapter.HighlightsViewHolder> {

    List<Highlight> data;

    public HighlightsAdapter(List<Highlight> data) {
        this.data = data;
    }


    @NonNull
    @Override
    public HighlightsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context ctx = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View v = inflater.inflate(R.layout.highlight_item,parent,false);
        return new HighlightsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HighlightsViewHolder holder, int position) {

        Highlight highlight = data.get(position);
        holder.tvTitle.setText(highlight.getTitle());
        holder.tvComp.setText(highlight.getCompetition());
        holder.tvDate.setText(highlight.getDate());
        Picasso.get()
                .load(highlight.getImageUrl())
                .placeholder(R.drawable.blackrec)
                .error(R.drawable.blackrec)
                .into(holder.ivImage);

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (IO.haveNetworkConnection(v.getContext()) == true){
                    Intent intent = new Intent(v.getContext(), VideoActivity.class);
                    intent.putExtra("video",data.get(position));
                    v.getContext().startActivity(intent);
                }else{
                    new AlertDialog.Builder(v.getContext())
                            .setTitle("Info")
                            .setMessage("Internet not available, Cross check your internet connectivity and try again")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                }

            }
        });



    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class HighlightsViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout constraintLayout;
        TextView tvTitle;
        TextView tvComp;
        TextView tvDate;
        ImageView ivImage;


        public HighlightsViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.highlightItem_title);
            constraintLayout = itemView.findViewById(R.id.highlightItem_constraintLayout);
            tvComp = itemView.findViewById(R.id.highlightItem_competition);
            tvDate = itemView.findViewById(R.id.highlightItem_date);
            ivImage = itemView.findViewById(R.id.highlightItem_ivImage);

        }
    }
}
