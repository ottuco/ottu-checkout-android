package Ottu.util;

import android.graphics.Bitmap;
import android.util.LruCache;

import java.util.ArrayList;
import java.util.HashMap;

public class BitmapCache extends LruCache<String, Bitmap> {
    public BitmapCache(int maxSize) {
        super(maxSize);
    }

    public static int getCacheSize() {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 2048);
        // use 1/8th of the available memory for this memory cache.
        return maxMemory / 8;
    }

    @Override
    protected int sizeOf( String key, Bitmap value ) {
        return value.getByteCount()/1024;
    }

    public Bitmap getBitmap(String key) {
        return this.get(key);
    }


    public void setBitmap(String key, Bitmap bitmap) {
        if (!hasBitmap(key)) {
            this.put(key, bitmap);
        }
    }

    public boolean hasBitmap(String key) {
        return getBitmap(key) != null;
    }

    HashMap<String,Bitmap> savedImg = new HashMap<>();

    public void saveImage(String key, Bitmap btm){
        if (!hasImageisHashmap(key)){
            savedImg.put(key,btm);
        }
    }

    public boolean hasImageisHashmap(String key){
        return  savedImg.get(key) != null;
    }
    public Bitmap getsavedBitmap(String key) {
        return savedImg.get(key);
    }

}