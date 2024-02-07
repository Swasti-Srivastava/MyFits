package com.example.myfits;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DataStorage {
    private static final String PREF_NAME = "MyAppData";
    private static final String KEY_TOPS = "tops";
    private static final String KEY_BOTTOMS = "bottoms";
    private static final String KEY_OUTFITS = "outfits";

    private SharedPreferences sharedPreferences;
    private Gson gson;

    public DataStorage(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void saveTops(ArrayList<String> tops) {
        String topsJson = gson.toJson(tops);
        sharedPreferences.edit().putString(KEY_TOPS, topsJson).apply();
    }

    public ArrayList<String> loadTops() {
        String topsJson = sharedPreferences.getString(KEY_TOPS, null);
        if (topsJson != null) {
            Type type = new TypeToken<ArrayList<String>>() {}.getType();
            return gson.fromJson(topsJson, type);
        }
        return new ArrayList<>();
    }

    public void saveBottoms(ArrayList<String> bottoms) {
        String bottomsJson = gson.toJson(bottoms);
        sharedPreferences.edit().putString(KEY_BOTTOMS, bottomsJson).apply();
    }

    public ArrayList<String> loadBottoms() {
        String bottomsJson = sharedPreferences.getString(KEY_BOTTOMS, null);
        if (bottomsJson != null) {
            Type type = new TypeToken<ArrayList<String>>() {}.getType();
            return gson.fromJson(bottomsJson, type);
        }
        return new ArrayList<>();
    }

    public void saveOutfits(ArrayList<OutfitsSaved> outfits) {
        String outfitsJson = gson.toJson(outfits);
        sharedPreferences.edit().putString(KEY_OUTFITS, outfitsJson).apply();
    }

    public ArrayList<OutfitsSaved> loadOutfits() {
        String outfitsJson = sharedPreferences.getString(KEY_OUTFITS, null);
        if (outfitsJson != null) {
            Type type = new TypeToken<ArrayList<OutfitsSaved>>() {}.getType();
            return gson.fromJson(outfitsJson, type);
        }
        return new ArrayList<>();
    }
}

