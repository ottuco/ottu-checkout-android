package ottu.payment.ui;

import android.app.Activity;
import android.content.Intent;

public class OttoPaymentSdk {

    static {
        System.loadLibrary("native-lib");
    }

    Intent intent;
    Activity activity;
    public OttoPaymentSdk(Activity activity) {

        this.activity = activity;
        intent = new Intent(activity,PaymentActivity.class);


    }

    public void setAmount(String amount) {

        intent.putExtra("Amount",amount);
    }

    public void build() {
        activity.startActivity(intent);
    }


    public void setApiId(String apiId){

        intent.putExtra("ApiId",apiId);
    }
    public void setMerchantId(String merchantId){
        intent.putExtra("MerchantId",merchantId);
    }
    public void setSessionId(String sessionId){
        intent.putExtra("SessionId",sessionId);
    }
}
