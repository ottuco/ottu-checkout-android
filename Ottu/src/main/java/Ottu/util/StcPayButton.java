package Ottu.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Ottu.BuildConfig;
import Ottu.R;
import Ottu.model.GenerateToken.CreatePaymentTransaction;
import Ottu.model.SocketData.SocketRespo;
import Ottu.model.fetchTxnDetail.RespoFetchTxnDetail;
import Ottu.network.GetDataService;
import Ottu.network.RetrofitClientInstance;
import Ottu.ui.Ottu;
import Ottu.ui.PaymentActivity;
import Ottu.ui.StcPayActivity;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

import static Ottu.network.RetrofitClientInstance.getRetrofitInstance;
import static Ottu.util.Constant.Amount;
import static Ottu.util.Constant.ApiId;
import static Ottu.util.Constant.CustomerPhone;
import static Ottu.util.Constant.LocalLan;
import static Ottu.util.Constant.MerchantId;
import static Ottu.util.Constant.OttuPaymentResult;
import static Ottu.util.Constant.SessionId;
import static Ottu.util.Util.isNetworkAvailable;

public class StcPayButton extends androidx.appcompat.widget.AppCompatImageButton {

    private Intent intent;
    Context mContext;
    String apiId ;
    String merchantId ;
    String lanCode ;
    CreatePaymentTransaction createTrasaction;
    public String type = "e_commerce";

    public StcPayButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (merchantId == null || TextUtils.isEmpty(merchantId)){
                    Toast.makeText(context, "Merchant id is missing", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (apiId == null || TextUtils.isEmpty(apiId)){
                    Toast.makeText(context, "API Id id is missing", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (createTrasaction == null ){
                    Toast.makeText(context, "Transaction data is not provided", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    String amount = createTrasaction.getAmount();
                    String customerid = createTrasaction.getCustomer_id();
                    String mobileNumber = createTrasaction.getCustomer_phone();
                    String currencycode = createTrasaction.getCustomer_phone();

                    if (amount == null || TextUtils.isEmpty(amount) || Float.parseFloat(amount) <= 0.001){
                        Toast.makeText(context, "Amount is missing in transaction detail", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if ( Float.parseFloat(amount) < 0.001){
                        Toast.makeText(context, "Amount is not sufficient", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (customerid == null || TextUtils.isEmpty(customerid)){
                        Toast.makeText(context, "Customer id is missing in transaction detail", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (mobileNumber == null || TextUtils.isEmpty(mobileNumber)){
                        Toast.makeText(context, "Mobile number is missing in transaction detail", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (currencycode == null || TextUtils.isEmpty(currencycode)){
                        Toast.makeText(context, "Currency code is missing in transaction detail", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (lanCode == null || TextUtils.isEmpty(lanCode) || lanCode.equals("en") || lanCode.equals("ar")){
                    lanCode = "en";
                }
                ArrayList<String> listpg = new ArrayList<>();
                listpg.add("stc_pay");
                createTrasaction.setPg_codes(listpg);


                intent = new Intent(context, StcPayActivity.class);
                intent.putExtra("MerchantId",merchantId);
                intent.putExtra("ApiId",apiId);
                intent.putExtra("Local",lanCode);
                intent.putExtra("CreateTrasactionBaseUrl","https://"+merchantId+"/b/");
                intent.putExtra("CreateTrasactionData",createTrasaction);

                ((Activity)mContext).startActivityForResult(intent, OttuPaymentResult);
            }
        });

        setScaleType(ScaleType.FIT_XY);
        setBackgroundResource(R.drawable.stcpay);
        setElevation(5.0f);

        setLayoutParams(new LinearLayout.LayoutParams(100,50));


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int customHeight = MeasureSpec.makeMeasureSpec((int)(MeasureSpec.getSize(widthMeasureSpec) * 0.15f), MeasureSpec.EXACTLY);
//        int width = MeasureSpec.makeMeasureSpec((int)(MeasureSpec.getSize(widthMeasureSpec) * 0.6f), MeasureSpec.AT_MOST);
//        super.onMeasure(width, customHeight);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }



    public void setApiId(String apiId){

        this.apiId = apiId;
    }
    public void setMerchantId(String merchantId){
        this.merchantId = merchantId;
    }


    public void setLocal(String local){
        lanCode = local;
    }

    public void setCreateTrasaction(CreatePaymentTransaction createTrasaction) {
        this.createTrasaction = createTrasaction;
    }
    public void setCreateTransaction(String type,
                                     String amount,
                                     String currency_code,
                                     String disclosure_url,
                                     String redirect_url,
                                     String customer_id,
                                     String customer_phone,
                                     String expiration_time){

        createTrasaction = new CreatePaymentTransaction(type,amount,currency_code,disclosure_url,redirect_url,customer_id,customer_phone,expiration_time);
    }
}
