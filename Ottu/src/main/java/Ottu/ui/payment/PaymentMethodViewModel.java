package Ottu.ui.payment;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import Ottu.model.fetchTxnDetail.PaymentMethod;

public class PaymentMethodViewModel extends ViewModel {

    private final MutableLiveData<PaymentMethod> selectedPaymentMethodLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> cvvCodeLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> otpCodeResultLiveData = new MutableLiveData<>();

    public void setSelectedPaymentMethod(PaymentMethod paymentMethod) {
        selectedPaymentMethodLiveData.setValue(paymentMethod);
    }

    public void setCvvCode(String cvvCode) {
        cvvCodeLiveData.setValue(cvvCode);
    }

    public void setOtpCodeResult(boolean isSuccess) {
        otpCodeResultLiveData.setValue(isSuccess);
    }

    public MutableLiveData<String> getCvvCodeLiveData() {
        return cvvCodeLiveData;
    }

    public MutableLiveData<PaymentMethod> getSelectedPaymentMethodLiveData() {
        return selectedPaymentMethodLiveData;
    }

    public MutableLiveData<Boolean> getOtpCodeResultLiveData() {
        return otpCodeResultLiveData;
    }

    public @Nullable PaymentMethod getSelectedPaymentMethod() {
        return selectedPaymentMethodLiveData.getValue();
    }

    public @Nullable String getCvvCode() {
        return cvvCodeLiveData.getValue();
    }

}
