package Ottu.util;

import android.content.Context;

import java.util.Locale;

import Ottu.R;

public class PhoneNumberUtil {

    public static String formatPhoneNumber(Context context, String phoneNumber) {
        String firstPart = phoneNumber.substring(0, phoneNumber.length() / 2);
        String lastPart = phoneNumber.substring(phoneNumber.length() / 2);
        return context.getString(R.string.text_phone_format, firstPart, lastPart);
    }

    public static String getPhoneNumber(String phoneNumber) {
        return phoneNumber.replaceAll("\\s+","");
    }

}
