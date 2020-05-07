package com.example.projectx.activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.example.projectx.data.database.FilmContentProvider;
import com.example.projectx.data.database.FilmTableHelper;
import com.example.projectx.data.database.RateTableHelper;
import com.example.projectx.data.models.FilmResponse;

import java.util.Calendar;

public class DetailActivity extends AppCompatActivity {

    TextView mTitle, mDescription, mRateNumber, mRateNumber2;
    Button mButtonRateFilm;
    ImageView mImageDetail, mImageStar;
    private String id = "";
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
            id = getIntent().getExtras().getString(FilmTableHelper._ID);
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

            setRateNumbers(id);
            //mRateNumber.setText(valutazione);

        } else {
            Toast.makeText(this, "Nessun dato ricevuto dall'adapter'", Toast.LENGTH_SHORT).show();
        }
    }

    public void withRatingBar(View view) {
        final Cursor vCursor = getContentResolver().query(FilmContentProvider.RATE_URI, null,RateTableHelper._ID +" = "+id,null,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        builder.setTitle(mTitle.getText());
        View dialogLayout = inflater.inflate(R.layout.rate_layout, null);
        final RatingBar ratingBar = dialogLayout.findViewById(R.id.ratingBar);
        builder.setView(dialogLayout);
        builder.setPositiveButton("VALUTA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(vCursor.getCount() == 0)
                {
                    insertToDB(id, ratingBar.getRating());
                    setRateNumbers(id);
                }else
                {
                    updateToDB(id, ratingBar.getRating());
                    setRateNumbers(id);
                }
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

    public void setRateNumbers(String id)
    {
        String[] valutazione = {RateTableHelper.VALUTAZIONE};
        Cursor vCursor = getContentResolver().query(FilmContentProvider.RATE_URI, valutazione,RateTableHelper._ID +" = "+id,null,null);
        vCursor.moveToNext();
        int orientation = getResources().getConfiguration().orientation;

        if(vCursor.getCount() == 1) {
            float rating = vCursor.getFloat(vCursor.getColumnIndex(RateTableHelper.VALUTAZIONE));
            Log.d("TAG", rating+"");
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                mRateNumber.setText("Il tuo voto: ");
                mRateNumber2.setText("" + rating);
            } else {
                Log.d("TAG", "" + rating);
                mRateNumber.setText("Il tuo voto: " + rating);

            }
            mImageStar.setImageResource(R.drawable.star);
        }
        else
            mRateNumber.setTextColor(Color.BLUE);
            mRateNumber.setText("Ancora da valutare");
    }
    public void insertToDB(String id_movie, float rating)
    {
        ContentValues cv = new ContentValues();
        cv.put(RateTableHelper._ID, id_movie);
        cv.put(RateTableHelper.VALUTAZIONE, rating);
        cv.put(RateTableHelper.DATA_VALUTAZIONE, ""+Calendar.getInstance().getTime());
        getContentResolver().insert(FilmContentProvider.RATE_URI, cv);
    }
    public void updateToDB(String id_movie, float rating)
    {
        ContentValues cv = new ContentValues();
        cv.put(RateTableHelper._ID, id_movie);
        cv.put(RateTableHelper.VALUTAZIONE, rating);
        cv.put(RateTableHelper.DATA_VALUTAZIONE, ""+Calendar.getInstance().getTime());
        getContentResolver().update(FilmContentProvider.RATE_URI,cv,null,null);
    }
}
