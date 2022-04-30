package ottu.payment.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.SparseArray;

import java.util.Locale;
import java.util.regex.Pattern;

import ottu.payment.R;

public class Util {

    public static boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager manager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            // Network is present and connected
            isAvailable = true;
        }
        return isAvailable;
    }

    public static SparseArray<Pattern> listCardPatter() {
         SparseArray<Pattern> mCCPatterns = new SparseArray<>();
            // With spaces for credit card masking
            mCCPatterns.put(R.drawable.card_visa, Pattern.compile("^4[0-9]{2,12}(?:[0-9]{3})?$"));
            mCCPatterns.put(R.drawable.card_master, Pattern.compile("^5[1-5][0-9]{1,14}$"));
            mCCPatterns.put(R.drawable.card_knet, Pattern.compile("^3[47][0-9]{1,13}$"));
            mCCPatterns.put(R.drawable.card_knet, Pattern.compile("^3(?:0[0-5]|[68][0-9])[0-9]{11}$"));//dinner
            mCCPatterns.put(R.drawable.card_knet, Pattern.compile("^^6(?:011|5[0-9]{2})[0-9]{12}$")); //discover
            mCCPatterns.put(R.drawable.card_knet, Pattern.compile("^(?:2131|1800|35\\\\d{3})\\\\d{11}$")); //jcb

        return mCCPatterns;
    }



}
