package com.example.myfits;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.appcompat.app.AppCompatActivity;

public class OutfitsSaved implements Parcelable {

    private Bitmap topImage;
    private Bitmap bottomImage;


    public OutfitsSaved(Bitmap topImage, Bitmap bottomImage) {
        this.topImage = topImage;
        this.bottomImage = bottomImage;
    }

    public Bitmap getTopImage() {
        return topImage;
    }

    public Bitmap getBottomImage() {
        return bottomImage;
    }
    protected OutfitsSaved(Parcel in) {
        topImage = in.readParcelable(Bitmap.class.getClassLoader());
        bottomImage = in.readParcelable(Bitmap.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(topImage, flags);
        dest.writeParcelable(bottomImage, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<OutfitsSaved> CREATOR = new Parcelable.Creator<OutfitsSaved>() {
        @Override
        public OutfitsSaved createFromParcel(Parcel in) {
            return new OutfitsSaved(in);
        }

        @Override
        public OutfitsSaved[] newArray(int size) {
            return new OutfitsSaved[size];
        }
    };


}
