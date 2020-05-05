package com.example.projectx.activities;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.projectx.R;
import com.example.projectx.data.database.FilmTableHelper;

public class DetailActivity extends AppCompatActivity {

    TextView mTitle, mDescription, mRateNumber, mRateNumber2;
    Button mButtonRateFilm;
    ImageView mImageDetail, mImageStar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setTitle("Dettaglio del Film");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setLayout();

        displayFilmDetail();
    }

    public void setLayout() {
        mTitle = findViewById(R.id.title);
        mDescription = findViewById(R.id.description);
        mImageDetail = findViewById(R.id.imageViewDetail);
        mImageStar = findViewById(R.id.star);
        mRateNumber = findViewById(R.id.rateNumber);
        mRateNumber2 = findViewById(R.id.rateNumber2);
        mButtonRateFilm = findViewById(R.id.buttonRateFilm);
        mButtonRateFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                withRatingBar(view);
            }
        });
    }

    public void displayFilmDetail() {
        if (getIntent().getExtras() != null) {
            final String id = getIntent().getExtras().getString(FilmTableHelper._ID);
            final String title = getIntent().getExtras().getString(FilmTableHelper.TITOLO);
            final String description = getIntent().getExtras().getString(FilmTableHelper.DESCRIZIONE);
            final String imageDetail = getIntent().getExtras().getString(FilmTableHelper.IMMAGINE_DETTAGLIO);
            final String imageMain = getIntent().getExtras().getString(FilmTableHelper.IMMAGINE_COPERTINA);
            //final String valutazione = getIntent().getExtras().getString(FilmTableHelper.VALUTAZIONE);
            //scrollPosition = getIntent().getExtras().getInt("Posizione");

            //imposta il titolo
            if (title != null || !title.equals(""))
                mTitle.setText(title);
            else {
                mTitle.setText("**TITOLO NON DISPONIBILE**");
            }

            //imposta la descrizione
            if (description != null || !description.equals(""))
                mDescription.setText(description);
            else {
                mDescription.setText("**DESCRIZIONE NON DISPONIBILE**");
            }

            //imposta l'immagine
            if (imageDetail == null || imageDetail.equals(null) || imageDetail.equals("") || (TextUtils.isEmpty(imageDetail))) {
                Glide.with(DetailActivity.this)
                        .load("https://image.tmdb.org/t/p/w500/" + imageMain)
                        .placeholder(R.drawable.preview)
                        .into(mImageDetail);
            } else {
                Glide.with(DetailActivity.this)
                        .load("https://image.tmdb.org/t/p/w500/" + imageDetail)
                        .placeholder(R.drawable.preview)
                        .into(mImageDetail);
            }

            //mRateNumber.setText(valutazione);

        } else {
            Toast.makeText(this, "Nessun dato ricevuto dall'adapter'", Toast.LENGTH_SHORT).show();
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
                int orientation = getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    mRateNumber.setText("Il tuo voto: ");
                    mRateNumber2.setText("" + ratingBar.getRating());
                } else {
                    mRateNumber.setText("Il tuo voto: " + ratingBar.getRating());
                }
                mImageStar.setImageResource(R.drawable.star);
                Toast.makeText(getApplicationContext(), "Film valutato: " + ratingBar.getRating() + " stelle", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    //metodo per tornare indietro con la freccia in alto senza perdere la posizione di scroll
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
