package ottu.payment.interfaces;

public class SendPaymentCallback {
    OttuPaymentCallback paymentCallback;

    public SendPaymentCallback(OttuPaymentCallback paymentCallback) {
        this.paymentCallback = paymentCallback;
    }

    public void sendSuccess(String massage){

    }
    public void sendFail(String massage){

    }
}
