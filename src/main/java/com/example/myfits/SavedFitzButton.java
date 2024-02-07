package com.example.myfits;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
public class SavedFitzButton extends AppCompatActivity {

    private ArrayList<OutfitsSaved> outfitData;
    private ImageView topsImageView;
    private ImageView bottomsImageView;
    private Button nextButton;
    private int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_fitz_button);

        topsImageView = findViewById(R.id.TopsImage_SavedFitz);
        bottomsImageView = findViewById(R.id.bottomsImage_SavedFitz);
        nextButton = findViewById(R.id.next);

        Intent intent = getIntent();
        outfitData = intent.getParcelableArrayListExtra("outfitData");

        if (outfitData != null && !outfitData.isEmpty()) {
            displayOutfitCombination(currentIndex);

            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    currentIndex++;
                    if (currentIndex >= outfitData.size()) {
                        currentIndex = 0;
                    }

                    displayOutfitCombination(currentIndex);
                }
            });
        }
    }

    private void displayOutfitCombination(int index) {
        OutfitsSaved outfit = outfitData.get(index);
        Bitmap topImage = outfit.getTopImage();
        Bitmap bottomImage = outfit.getBottomImage();

        topsImageView.setImageBitmap(topImage);
        bottomsImageView.setImageBitmap(bottomImage);
    }

}
