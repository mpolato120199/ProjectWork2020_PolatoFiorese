package com.example.projectx.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.projectx.Film.FilmTableHelper;
import com.example.projectx.R;

public class DetailActivity extends AppCompatActivity {

    TextView mTitle, mDescription, mRateNumber;
    Button mButtonRateFilm;
    ImageView mImageDetail, mImageStar;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setTitle("DETTAGLI DEL FILM");

        mTitle = findViewById(R.id.title);
        mDescription = findViewById(R.id.description);
        mImageDetail = findViewById(R.id.imageViewDetail);
        mImageStar = findViewById(R.id.star);
        mRateNumber = findViewById(R.id.rateNumber);
        mButtonRateFilm = findViewById(R.id.buttonRateFilm);

        mButtonRateFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                withRatingBar(view);
            }
        });

        if (getIntent().getExtras() != null) {
            final String title = getIntent().getExtras().getString(FilmTableHelper.TITOLO);
            final String description = getIntent().getExtras().getString(FilmTableHelper.DESCRIZIONE);
            final String imageDetail = getIntent().getExtras().getString(FilmTableHelper.IMMAGINE_DETTAGLIO);
            final String imageMain = getIntent().getExtras().getString(FilmTableHelper.IMMAGINE_COPERTINA);

            id = getIntent().getExtras().getString(FilmTableHelper.ID);

            if (imageDetail.equals(null) || imageDetail.equals("") || (TextUtils.isEmpty(imageDetail))) {
                Glide.with(DetailActivity.this)
                        .load("https://image.tmdb.org/t/p/w500/" + imageMain)
                        .into(mImageDetail);
            } else {
                Glide.with(DetailActivity.this)
                        .load("https://image.tmdb.org/t/p/w500/" + imageDetail)
                        .into(mImageDetail);
            }
            mTitle.setText(title);
            if (!description.equals(""))
                mDescription.setText(description);
            else {
                mDescription.setText("Per questo film non c'è nessuna descrizione disponibile");
            }
        } else {
            Toast.makeText(this, "Nessun dato ricevuto dal click", Toast.LENGTH_SHORT).show();
        }
    }

    public void withRatingBar(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        builder.setTitle(mTitle.getText());
        View dialogLayout = inflater.inflate(R.layout.rate_layout, null);
        final RatingBar ratingBar = dialogLayout.findViewById(R.id.ratingBar);
        builder.setView(dialogLayout);
        builder.setPositiveButton("VALUTA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mRateNumber.setText("Il tuo voto: " + ratingBar.getRating());
                mImageStar.setImageResource(R.drawable.star);
                Toast.makeText(getApplicationContext(), "Film valutato: " + ratingBar.getRating() + " stelle", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    /*public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }*/
}
