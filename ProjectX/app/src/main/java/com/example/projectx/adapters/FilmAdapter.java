<<<<<<< HEAD:ProjectX/app/src/main/java/com/example/projectx/Film/FilmAdapter.java
package com.example.projectx.Film;
=======
package com.example.projectx.adapters;
>>>>>>> 476d4a477ee3181151111656f57efaeca0f825ca:ProjectX/app/src/main/java/com/example/projectx/adapters/FilmAdapter.java

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
<<<<<<< HEAD:ProjectX/app/src/main/java/com/example/projectx/Film/FilmAdapter.java
import com.example.projectx.DetailActivity;
import com.example.projectx.R;
=======
import com.example.projectx.R;
import com.example.projectx.activities.DetailActivity;
import com.example.projectx.activities.MainActivity;
>>>>>>> 476d4a477ee3181151111656f57efaeca0f825ca:ProjectX/app/src/main/java/com/example/projectx/adapters/FilmAdapter.java
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.film_cell, parent, false);
        FilmViewHolder holder = new FilmViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FilmViewHolder holder, int position) {
        //int i = Integer.parseInt(String.valueOf(R.drawable.ic_launcher_background));
        //holder.filmImage.setImageResource(i);
        Glide.with(context)
                .load(filmList.get(position).getPoster())
                .placeholder(R.drawable.loading_image)
                .into(holder.filmImage);
    }

    @Override
    public int getItemCount() {
        return filmList.size();
    }


    public class FilmViewHolder extends RecyclerView.ViewHolder {
        ImageView filmImage;

        public FilmViewHolder(@NonNull View itemView) {
            super(itemView);
            final int position = getAdapterPosition();
            filmImage = itemView.findViewById(R.id.imageViewFilm);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
<<<<<<< HEAD:ProjectX/app/src/main/java/com/example/projectx/Film/FilmAdapter.java
                   /*Film clickDataItem = filmList.get(position);
=======
                    //Film clickDataItem = filmList.get(position);
>>>>>>> 476d4a477ee3181151111656f57efaeca0f825ca:ProjectX/app/src/main/java/com/example/projectx/adapters/FilmAdapter.java
                    Intent intent = new Intent(context, DetailActivity.class);
                    /*intent.putExtra("film_ID", filmList.get(position).getImdbID());
                    intent.putExtra("film_title", filmList.get(position).getTitle());
                    intent.putExtra("film_poster", filmList.get(position).getPoster());*/
<<<<<<< HEAD:ProjectX/app/src/main/java/com/example/projectx/Film/FilmAdapter.java
                    context.startActivity(new Intent(context, DetailActivity.class));
=======
                    context.startActivity(intent);
>>>>>>> 476d4a477ee3181151111656f57efaeca0f825ca:ProjectX/app/src/main/java/com/example/projectx/adapters/FilmAdapter.java
                    //Toast.makeText(view.getContext(), "Hai cliccato " + clickDataItem.getTitle(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
