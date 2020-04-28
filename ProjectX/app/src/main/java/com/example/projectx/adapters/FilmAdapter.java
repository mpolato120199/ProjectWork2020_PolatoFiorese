package com.example.projectx.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projectx.Film.FilmTableHelper;
import com.example.projectx.R;
import com.example.projectx.activities.DetailActivity;
import com.example.projectx.data.models.FilmResponse;

import java.util.ArrayList;
import java.util.List;

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.FilmViewHolder> implements Filterable {

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<FilmResponse.SingleFilmResult> filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(filmToSearch);
            } else {
                String filteredStyle = charSequence.toString().toLowerCase().trim();

                for (FilmResponse.SingleFilmResult item : filmToSearch) {
                    if (item.getTitle().toLowerCase().contains(filteredStyle)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            filmList.clear();
            filmList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public List<FilmResponse.SingleFilmResult> filmList;
    public List<FilmResponse.SingleFilmResult> filmToSearch;
    public Context context;

    public FilmAdapter(List<FilmResponse.SingleFilmResult> filmList, Context context) {
        this.context = context;
        this.filmList = filmList;
        filmToSearch = new ArrayList<>(filmList);
    }

    @NonNull
    @Override
    public FilmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.film_cell, parent, false);
        FilmViewHolder holder = new FilmViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final FilmViewHolder holder, final int position) {
        final ImageView imageView = holder.filmImage;
        final CardView cardView = holder.itemView.findViewById(R.id.myCardView);


        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w500" + filmList.get(position).getPosterPath())
                .placeholder(R.drawable.loading)
                .into(imageView);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, holder.getAdapterPosition() + "", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, DetailActivity.class);
                Bundle bundle = new Bundle();
                int id = filmList.get(position).getId();
                //Toast.makeText(context, "id film --> " + id, Toast.LENGTH_SHORT).show();
                bundle.putString(FilmTableHelper.ID, Integer.toString(id));
                bundle.putString(FilmTableHelper.TITOLO, filmList.get(position).getTitle());
                bundle.putString(FilmTableHelper.DESCRIZIONE, filmList.get(position).getOverview());
                bundle.putString(FilmTableHelper.IMMAGINE_COPERTINA, filmList.get(position).getPosterPath());
                bundle.putString(FilmTableHelper.IMMAGINE_DETTAGLIO, filmList.get(position).getBackdropPath());
                //bundle.putInt("Posizione", holder.getAdapterPosition());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filmList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    public class FilmViewHolder extends RecyclerView.ViewHolder {
        ImageView filmImage;

        public FilmViewHolder(@NonNull View itemView) {
            super(itemView);
            filmImage = itemView.findViewById(R.id.imageFilm);
        }
    }
}
