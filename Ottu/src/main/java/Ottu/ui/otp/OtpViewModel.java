package Ottu.ui.otp;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OtpViewModel extends ViewModel {

    private final MutableLiveData<String> phoneNumberLiveData = new MutableLiveData<String>();

    public void setPhoneNumber(String phoneNumber) {
        phoneNumberLiveData.setValue(phoneNumber);
    }

    public MutableLiveData<String> getPhoneNumber() {
        return phoneNumberLiveData;
    }

    public int getPhoneNumberLength() {
        String phone = phoneNumberLiveData.getValue();
        return phone == null ? 0 : phone.length();
    }
}
