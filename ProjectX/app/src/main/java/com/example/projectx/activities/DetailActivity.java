package com.example.projectx.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.media.Image;
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

import com.bumptech.glide.Glide;
import com.example.projectx.Film.FilmTableHelper;
import com.example.projectx.R;

public class DetailActivity extends AppCompatActivity {

    TextView mTitle, mDescription,mRateNumber;
    Button mRateFilm;
    ImageView mImage;
    Cursor mCursor;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mTitle = findViewById(R.id.title);
        mDescription = findViewById(R.id.description);
        mImage = findViewById(R.id.imageView);
        mRateNumber = findViewById(R.id.rateNumber);
        mRateFilm = findViewById(R.id.rateFilm);
        mRateFilm.setOnClickListener(new View.OnClickListener() {
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
                        .load("https://image.tmdb.org/t/p/w500/" + imageDetail)
                        .into(mImage);
            } else {
                Glide.with(DetailActivity.this)
                        .load("https://image.tmdb.org/t/p/w500/" + imageMain)
                        .into(mImage);
            }
            mTitle.setText(title);
            if (description != "")
                mDescription.setText(description);
            else
                mDescription.setText("Per questo film non c'è nessuna descrizione disponibile");
        }
    }
    public void withRatingBar(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        builder.setTitle(mTitle.getText());
        View dialogLayout = inflater.inflate(R.layout.rate_layout, null);
        final RatingBar ratingBar = dialogLayout.findViewById(R.id.ratingBar);
        builder.setView(dialogLayout);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mRateNumber.setText("Valutato da te: " + ratingBar.getRating());
                //Toast.makeText(getApplicationContext(), "Valutato: " + ratingBar.getRating(), Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
