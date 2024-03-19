package Ottu.util;

import androidx.lifecycle.ViewModel;

import java.util.HashMap;

import Ottu.ui.otp.OtpViewModel;

/*
* DONT USE, ONLY FOR TEST
*
* */

public class DITest {

    private static final HashMap<String, ViewModel> viewModels = new HashMap<>();

    static {
        viewModels.put(OtpViewModel.class.getSimpleName(), new OtpViewModel());
    }

    public static ViewModel getViewModel(String key) {
        return viewModels.get(key);
    }

}
