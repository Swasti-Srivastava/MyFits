package com.example.myfits;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.myfits.OutfitsSaved;
import com.example.myfits.R;

import java.io.IOException;
import java.util.ArrayList;

public class buildingOutfit extends AppCompatActivity {

    ViewPager2 topViewPager;
    ViewPager2 bottomViewPager;
    Button saveFits;
    Button back;
    ArrayList<String> tops;
    ArrayList<String> bottoms;
    ArrayList<OutfitsSaved> outfitCombinations;
    private OutfitData outfitData;

    int currentTopItem = 0;
    int currentBottomItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_outfit);

        outfitData = OutfitData.getInstance();


        topViewPager = findViewById(R.id.currentTopPager);
        bottomViewPager = findViewById(R.id.currentBottomPager);
        saveFits = findViewById(R.id.SaveFit);
        back = findViewById(R.id.backToMain);

        tops = getIntent().getStringArrayListExtra("topImage");
        bottoms = getIntent().getStringArrayListExtra("bottoms");

        outfitCombinations = new ArrayList<>();

        // Setup top ViewPager2 and Adapter
        TopImageAdapter topImageAdapter = new TopImageAdapter();
        topViewPager.setAdapter(topImageAdapter);

        // Setup bottom ViewPager2 and Adapter
        BottomImageAdapter bottomImageAdapter = new BottomImageAdapter();
        bottomViewPager.setAdapter(bottomImageAdapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("outfitCombinations", outfitCombinations);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        saveFits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap topBitmap = getBitmapFromPosition(currentTopItem, tops, true);
                Bitmap bottomBitmap = getBitmapFromPosition(currentBottomItem, bottoms, false);

                OutfitsSaved newOutfit = new OutfitsSaved(topBitmap, bottomBitmap);
                outfitData.addOutfitCombination(newOutfit);
            }
        });






        topViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                currentTopItem = position;
            }
        });

        bottomViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                currentBottomItem = position;
            }
        });
    }

    private Bitmap getBitmapFromPosition(int position, ArrayList<String> images, boolean isTops) {
        if (position >= 0 && position < images.size()) {
            String encodedImage = images.get(position);
            if (encodedImage.startsWith("content://")) {
                try {
                    Uri imageUri = Uri.parse(encodedImage);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    return bitmap;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                byte[] decodedBytes = Base64.decode(encodedImage, Base64.DEFAULT);
                return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            }
        }
        return null;
    }



//    private Bitmap getBitmapFromPositionBottoms(int position) {
//
//        if (position >= 0 && position < bottoms.size()) {
//            String encodedImage = bottoms.get(position);
//            if (encodedImage.startsWith("content://")) {
//                try {
//                    Log.d("hello","Getting bottom");
//                    Uri imageUri = Uri.parse(encodedImage);
//                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
//                    return bitmap;
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                byte[] decodedBytes = Base64.decode(encodedImage, Base64.DEFAULT);
//                return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
//            }
//        }
//        return null;
//    }
//
//    private Bitmap getBitmapFromPositionTops(int position) {
//        if (position >= 0 && position < tops.size()) {
//            String encodedImage = tops.get(position);
//            if (encodedImage.startsWith("content://")) {
//                try {
//                    Log.d("hello","Getting tops");
//                    Uri imageUri = Uri.parse(encodedImage);
//                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
//                    return bitmap;
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                byte[] decodedBytes = Base64.decode(encodedImage, Base64.DEFAULT);
//                return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
//            }
//        }
//
//        return null;
//    }


    private class TopImageAdapter extends RecyclerView.Adapter<TopImageAdapter.TopImageViewHolder> {

        @NonNull
        @Override
        public TopImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_top_image, parent, false);
            return new TopImageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TopImageViewHolder holder, int position) {
            Bitmap topBitmap = getBitmapFromPosition(position, tops,true);
            holder.topImage.setImageBitmap(topBitmap);

        }

        @Override
        public int getItemCount() {
            return tops.size();
        }

        class TopImageViewHolder extends RecyclerView.ViewHolder {
            ImageView topImage;

            TopImageViewHolder(@NonNull View itemView) {
                super(itemView);
                topImage = itemView.findViewById(R.id.topImageView);
            }
        }
    }

    private class BottomImageAdapter extends RecyclerView.Adapter<BottomImageAdapter.BottomImageViewHolder> {

        @NonNull
        @Override
        public BottomImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bottom_image, parent, false);
            return new BottomImageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull BottomImageViewHolder holder, int position) {
            Bitmap bottomBitmap = getBitmapFromPosition(position, bottoms,false);
            holder.bottomImage.setImageBitmap(bottomBitmap);
        }

        @Override
        public int getItemCount() {
            return bottoms.size();
        }

        class BottomImageViewHolder extends RecyclerView.ViewHolder {
            ImageView bottomImage;

            BottomImageViewHolder(@NonNull View itemView) {
                super(itemView);
                bottomImage = itemView.findViewById(R.id.bottomImageView);
            }
        }
    }
}



/*
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;

public class buildingOutfit extends AppCompatActivity {
    ImageView currentTop;
    ImageView currentBottoms;
    private ArrayList<String> tops;
    private ArrayList<String> bottoms;
    private ArrayList<OutfitsSaved> outfitCombinations;


    Button saveFits;
    Button back;

    private int topIndex = 0;
    private int bottomIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_outfit);
        updateTopImage();
        updateBottomsImage();

        back = findViewById(R.id.backToMain);


        outfitCombinations = new ArrayList<>();

        saveFits = findViewById(R.id.SaveFit);
        saveFits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OutfitsSaved newOutfit = new OutfitsSaved(getBitmapFromImageView(currentTop), getBitmapFromImageView(currentBottoms));

                outfitCombinations.add(newOutfit);

                for (OutfitsSaved outfit : outfitCombinations) {
                    Log.d("Outfit Combination", "Top: " + outfit.getTopImage() + ", Bottom: " + outfit.getBottomImage());
                }


            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("outfitCombinations", outfitCombinations);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });





        currentTop = findViewById(R.id.currentTop);
        tops = getIntent().getStringArrayListExtra("topImage");
        //Bitmap topImage = convertStringToBitmap(tops.get(0));
        currentTop.setImageBitmap(convertStringToBitmap(tops.get(0)));


        currentBottoms = findViewById(R.id.currentBottom);
        bottoms = getIntent().getStringArrayListExtra("bottoms");
        //Bitmap bottomImage = convertStringToBitmap(bottoms.get(0));
        currentBottoms.setImageBitmap(convertStringToBitmap(bottoms.get(0)));

        updateTopImage();
        updateBottomsImage();

        currentTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topIndex = (topIndex + 1) % tops.size();
                updateTopImage();
            }
        });


        currentBottoms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    bottomIndex = (bottomIndex + 1) % bottoms.size();
                    updateBottomsImage();

            }
        });



    }

    private Bitmap getBitmapFromImageView(ImageView imageView) {
        Drawable drawable = imageView.getDrawable();
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else {
            return null;
        }
    }
    private Bitmap convertStringToBitmap(String encodedImage) {
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    private void updateTopImage() {
        if (tops != null && tops.size() > 0) {
            Bitmap topImage = convertStringToBitmap(tops.get(topIndex));
            currentTop.setImageBitmap(topImage);
        }
    }


    private void updateBottomsImage() {
        if (bottoms != null && bottoms.size() > 0) {
            Bitmap bottomImage = convertStringToBitmap(bottoms.get(bottomIndex));
            currentBottoms.setImageBitmap(bottomImage);
            //String bottomImageUri = bottoms.get(bottomIndex);
            // Set the bottom image to the currentBottoms ImageView
           // currentBottoms.setImageURI(Uri.parse(bottomImageUri));
        }
    }
}*/


