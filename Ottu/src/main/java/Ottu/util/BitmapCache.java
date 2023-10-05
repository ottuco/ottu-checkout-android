package Ottu.util;

import android.graphics.Bitmap;
import android.util.LruCache;

public class BitmapCache extends LruCache<String, Bitmap> {
    public BitmapCache(int maxSize) {
        super(maxSize);
    }

    public static int getCacheSize() {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
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
}