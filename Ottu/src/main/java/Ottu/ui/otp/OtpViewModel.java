package Ottu.ui.otp;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OtpViewModel extends ViewModel {

    private final MutableLiveData<String> phoneNumberLiveData = new MutableLiveData<>();

    private final MutableLiveData<String> otpCodeLiveData = new MutableLiveData<>();

    public int getOtpCodeLength() {
        String otpCode = otpCodeLiveData.getValue();
        return otpCode == null ? 0 : otpCode.length();
    }

    public int getPhoneNumberLength() {
        String phone = phoneNumberLiveData.getValue();
        return phone == null ? 0 : phone.length();
    }

    public void setPhoneNumber(String phoneNumber) {
        phoneNumberLiveData.setValue(phoneNumber);
    }

    public MutableLiveData<String> getPhoneNumber() {
        return phoneNumberLiveData;
    }

    public void setOtpCode(String otpCode) {
        otpCodeLiveData.setValue(otpCode);
    }

    public MutableLiveData<String> getOtpCodeLiveData() {
        return otpCodeLiveData;
    }

}
