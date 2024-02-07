package com.example.myfits;

import java.util.ArrayList;

public class OutfitData {
    private static OutfitData instance;
    private ArrayList<OutfitsSaved> outfitCombinations;

    private OutfitData() {
        outfitCombinations = new ArrayList<>();
    }

    public static synchronized OutfitData getInstance() {
        if (instance == null) {
            instance = new OutfitData();
        }
        return instance;
    }

    public ArrayList<OutfitsSaved> getOutfitCombinations() {
        return outfitCombinations;
    }

    public void addOutfitCombination(OutfitsSaved outfit) {
        outfitCombinations.add(outfit);
    }
}

