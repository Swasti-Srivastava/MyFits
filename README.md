# MyFits

A closet app for Android. You take pictures of your tops and bottoms, then flip through them to put outfits together.

## Features

- Add clothes from the camera or from your photo gallery, sorted into tops and bottoms.
- On the build screen, swipe through your tops and bottoms separately to mix and match.
- Save combinations you like.
- Look through your saved outfits one at a time on the "Your Fitz" screen.

## What's in here

- `OpeningScene.java` is the start screen.
- `MainActivity.java` is the main closet screen. It handles camera and gallery input.
- `buildingOutfit.java` is the outfit builder. It uses two `ViewPager2`s, one for tops and one for bottoms.
- `SavedFitzButton.java` lets you browse saved outfits.
- `DataStorage.java` saves your clothes locally with `SharedPreferences` and Gson.
- `OutfitsSaved.java` and `OutfitData.java` hold the outfit data.

## Running it

Open the project in Android Studio and run it on a phone or an emulator. The first time it opens, it will ask for camera permission.
