package com.example.projectx;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity {

    TextView mTitle, mDescription,mRateNumber;
    Button mRateFilm;
    ImageView mImage;
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
}
