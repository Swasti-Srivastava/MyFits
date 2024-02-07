package com.example.myfits;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import com.example.myfits.OutfitsSaved;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_CODE = 23;
    private static final int CAMERA_REQUEST_CODE = 3;
    private static final int CAMERA_REQUEST_CODE_bottoms = 5;
    private static final int BUILD_OUTFIT_REQUEST_CODE = 1;

    private ArrayList<OutfitsSaved> outfitData;
    //int REQUEST_CODE_OUTFIT = 1;
    Button buildfit;
    ImageView topsFile;
    ImageView yourFitzFile;
    ImageView bottomsFile;
    public boolean topTrue;
    public boolean bottomTrue;
    private Bitmap capturedImage;
    private Bitmap CapturedImageBottoms;
    private String encodedImage;

    private DataStorage dataStorage;

    Bitmap photo;

    private static final int PICK_IMAGE_REQUEST_CODE = 100;
    private ArrayList<String> tops;
    private ArrayList<String> bottoms;
    private ArrayList<String> outfits;
    public ArrayList<OutfitsSaved> outfitCombinations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        outfitData = new ArrayList<>();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }

        dataStorage = new DataStorage(this);

        topTrue = false;
        bottomTrue = false;

        yourFitzFile = findViewById(R.id.Fitz);
        yourFitzFile.setImageResource(R.drawable.yourfitzz_image);
        yourFitzFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SavedFitzButton.class);
                intent.putExtra("outfitData", outfitData);
                startActivity(intent);
            }
        });


        bottomsFile = findViewById(R.id.Bottoms);
        bottomsFile.setImageResource(R.drawable.myfitzz_pants_image);
        bottoms = new ArrayList<>();

        bottomsFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomTrue = true;
//                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE_bottoms);
                openImageSourceDialog();
                //openGallery();
            }
        });

        tops = new ArrayList<>();

        topsFile = findViewById(R.id.tops);
        topsFile.setImageResource(R.drawable.myfitzz_tops_image);

        topsFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topTrue = true;
//                openGallery();
//                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//               startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
                openImageSourceDialog();
            }
        });

        //Building outfits with the images
        buildfit = findViewById(R.id.buildFitButton);

        buildfit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, buildingOutfit.class);
                intent.putStringArrayListExtra("topImage", tops);
                intent.putStringArrayListExtra("bottoms", bottoms);
                startActivity(intent);
            }
        });


        tops = dataStorage.loadTops();
        bottoms = dataStorage.loadBottoms();
        outfitCombinations = dataStorage.loadOutfits();





    }


    private void openImageSourceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Choose Image Source");
        builder.setItems(new CharSequence[]{"Camera", "Gallery"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        // Camera option selected
                        openCamera();
                        break;
                    case 1:
                        // Gallery option selected
                        openGallery();
                        break;
                }
            }
        });
        builder.show();
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyFitsData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Convert outfitCombinations ArrayList to JSON string using Gson
        Gson gson = new Gson();
        String outfitCombinationsJson = gson.toJson(outfitCombinations);

        editor.putString("outfitCombinations", outfitCombinationsJson);
        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyFitsData", MODE_PRIVATE);

        // Retrieve outfitCombinations JSON string from shared preferences
        String outfitCombinationsJson = sharedPreferences.getString("outfitCombinations", "");

        // Convert JSON string back to ArrayList using Gson
        Gson gson = new Gson();
        Type outfitCombinationsType = new TypeToken<ArrayList<OutfitsSaved>>() {}.getType();
        outfitCombinations = gson.fromJson(outfitCombinationsJson, outfitCombinationsType);

        if (outfitCombinations == null) {
            outfitCombinations = new ArrayList<>();
        }
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            if(topTrue) {
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
            }
            if(bottomTrue){
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE_bottoms);
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE);
    }

    private String convertBitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == BUILD_OUTFIT_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                outfitCombinations = data.getParcelableArrayListExtra("outfitCombinations");
                // Use the outfitCombinations ArrayList in your MainActivity as needed
            }
        }




        Log.d("TAG55", requestCode + " " + topTrue + " " + bottomTrue);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
//                capturedImage = (Bitmap) data.getExtras().get("data");
                if (topTrue) {
                    capturedImage = (Bitmap) data.getExtras().get("data");
                    encodedImage = convertBitmapToString(capturedImage);
                    tops.add(encodedImage);
                    //tops.add(capturedImage);
                    topTrue = false;
                }


                //topsFile.setImageBitmap(capturedImage);
            }
        }

        if (requestCode == CAMERA_REQUEST_CODE_bottoms && resultCode == RESULT_OK) {
            if (data != null) {
//                CapturedImageBottoms = (Bitmap) data.getExtras().get("data");

                if(bottomTrue){
                    CapturedImageBottoms = (Bitmap) data.getExtras().get("data");
                    encodedImage = convertBitmapToString(CapturedImageBottoms);
                    bottoms.add(encodedImage);
                    bottomTrue = false;
                }

                //topsFile.setImageBitmap(capturedImage);
            }
        }

//        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
//            if (data != null) {
//                Bitmap photo = (Bitmap) data.getExtras().get("data");
//                if (topTrue) {
//                    String encodedImage = convertBitmapToString(photo);
//                    tops.add(encodedImage);
//                    topTrue = false;
//                }
//            }
//        }


//        if(requestCode == CAMERA_PERMISSION_CODE){
//            photo = (Bitmap) data.getExtras().get("data");
//            topsFile.setImageBitmap(photo);
//        }

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                if (data.getClipData() != null) {
                    // Multiple images selected
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        String imageUri = data.getClipData().getItemAt(i).getUri().toString();
                        if(topTrue) {
                            tops.add(imageUri);
                            topTrue = false;
                        }
                        if(bottomTrue) {
                            bottoms.add(imageUri);
                            bottomTrue = false;
                        }
                    }
                } else if (data.getData() != null) {
                    // Single image selected
                    String imageUri = data.getData().toString();
                    if(topTrue) {
                        tops.add(imageUri);
                        topTrue = false;
                    }
                    if(bottomTrue) {
                        bottoms.add(imageUri);
                        bottomTrue = false;
                    }
                }

                // Log the selected image URIs
                for (String uri : tops) {
                    Log.d("Selected Image URI", uri);
                }
                for (String uri : bottoms) {
                    Log.d("Selected Image URI", uri);
                }
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Save the data before the app turns off
        dataStorage.saveTops(tops);
        dataStorage.saveBottoms(bottoms);
        dataStorage.saveOutfits(outfitCombinations);
    }


}