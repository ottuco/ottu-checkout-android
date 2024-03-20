package Ottu.util;

import androidx.lifecycle.ViewModel;

import java.util.HashMap;

import Ottu.R;
import Ottu.ui.otp.OtpViewModel;

/*
* DONT USE, ONLY FOR TEST
*
* */

public class PrototypeUtil {

    private static final HashMap<String, ViewModel> viewModels = new HashMap<>();

    static {
        viewModels.put(OtpViewModel.class.getSimpleName(), new OtpViewModel());
    }

    public static ViewModel getViewModel(String key) {
        return viewModels.get(key);
    }

    public static int getPaymentIconByType(String type) {
        switch (type) {
            case "1":
                return R.drawable.ic_google_pay;
            case "2":
            case "3":
                return R.drawable.ic_stc_pay;
            case "4":
                return R.drawable.ic_saved_card;
            case "5":
                return R.drawable.ic_knet;
            case "6":
                return R.drawable.ic_benefitpay;
            case "7":
                return R.drawable.ic_ottu;
            default:
                return R.drawable.card_visa;
        }
    }

}
