package com.example.projectx.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projectx.Film.FilmTableHelper;
import com.example.projectx.R;
import com.example.projectx.R;
import com.example.projectx.activities.DetailActivity;
import com.example.projectx.activities.MainActivity;

import com.example.projectx.data.models.Film;

import java.util.List;

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.FilmViewHolder> {

    public List<Film> filmList;
    public Context context;

    public FilmAdapter(List<Film> filmList, Context context) {
        this.context = context;
        this.filmList = filmList;
    }

    @NonNull
    @Override
    public FilmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.film_cell, parent, false);
        //FilmViewHolder holder = new FilmViewHolder(view);
        return new FilmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilmViewHolder holder, final int position) {
        //int i = Integer.parseInt(String.valueOf(R.drawable.ic_launcher_background));
        //holder.filmImage.setImageResource(i);
        final ImageView img = holder.cellView.findViewById(R.id.imageFilm);
        final CardView card = holder.cellView.findViewById(R.id.myCardView);
        Glide.with(context)
                .load(filmList.get(position).getPoster()) //bisogna mettere l'api
                .into(img);

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                Bundle bundle = new Bundle();
                int id = filmList.get(position).getId();
                bundle.putString(FilmTableHelper.ID, Integer.toString(id));
                bundle.putString(FilmTableHelper.TITOLO, filmList.get(position).getTitle());
                bundle.putString(FilmTableHelper.DESCRIZIONE, filmList.get(position).getDesc());
                bundle.putString(FilmTableHelper.IMMAGINE_COPERTINA, filmList.get(position).getPoster());
                bundle.putString(FilmTableHelper.IMMAGINE_DETTAGLIO, filmList.get(position).getBackDrop());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filmList.size();
    }


    public class FilmViewHolder extends RecyclerView.ViewHolder {
        View cellView;

        public FilmViewHolder(@NonNull View itemView) {
            super(itemView);
            this.cellView = itemView;
            };
        }
}
  /*  final int position = getAdapterPosition();
            filmImage = itemView.findViewById(R.id.imageViewFilm);

                    itemView.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
                   /*Film clickDataItem = filmList.get(position);
                    //Film clickDataItem = filmList.get(position);
                    Intent intent = new Intent(context, DetailActivity.class);
                    /*intent.putExtra("film_ID", filmList.get(position).getImdbID());
                    intent.putExtra("film_title", filmList.get(position).getTitle());
                    intent.putExtra("film_poster", filmList.get(position).getPoster());
        context.startActivity(new Intent(context, DetailActivity.class));
        //Toast.makeText(view.getContext(), "Hai cliccato " + clickDataItem.getTitle(), Toast.LENGTH_SHORT).show();
        }
        */