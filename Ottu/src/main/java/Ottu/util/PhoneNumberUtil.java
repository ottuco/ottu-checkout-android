package Ottu.util;

public class PhoneNumberUtil {

    public static String formatPhoneNumber(String countryCode, String phoneNumber) {
        String firstPart = phoneNumber.substring(0, phoneNumber.length() / 2);
        String lastPart = phoneNumber.substring(phoneNumber.length() / 2);
        return String.format("%1$s %2$s %3$s", countryCode, firstPart, lastPart);
    }

    public static String getPhoneNumber(String phoneNumber) {
        return phoneNumber.replaceAll("\\s+","");
    }

}
