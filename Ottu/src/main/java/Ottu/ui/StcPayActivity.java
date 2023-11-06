package Ottu.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import Ottu.R;
import Ottu.databinding.ActivityPaymentBinding;
import Ottu.databinding.ActivityStcPayBinding;
import Ottu.databinding.DialogOtpBinding;
import Ottu.databinding.DialogStcMnumberBinding;
import Ottu.model.GenerateToken.CreatePaymentTransaction;
import Ottu.model.SocketData.SocketRespo;
import Ottu.model.StcPayMNumber.StcPayPayload;
import Ottu.model.StcPayMNumber.StcPayResponce;
import Ottu.model.StcPayOtp.StcOtpPayload;
import Ottu.model.StcPayOtp.StcOtpResponce;
import Ottu.model.fetchTxnDetail.PaymentMethod;
import Ottu.model.fetchTxnDetail.RespoFetchTxnDetail;
import Ottu.network.GetDataService;
import Ottu.network.RetrofitClientInstance;
import Ottu.util.StcPayButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static Ottu.network.RetrofitClientInstance.getRetrofitInstance;
import static Ottu.util.Constant.Amount;
import static Ottu.util.Constant.ApiId;
import static Ottu.util.Constant.CustomerId;
import static Ottu.util.Constant.CustomerPhone;
import static Ottu.util.Constant.LocalLan;
import static Ottu.util.Constant.MerchantId;
import static Ottu.util.Constant.SessionId;
import static Ottu.util.Constant.selectedCardPos;
import static Ottu.util.Util.isDeviceRooted;
import static Ottu.util.Util.isNetworkAvailable;
import Ottu.BuildConfig;


public class StcPayActivity extends AppCompatActivity {
    ActivityStcPayBinding binding;
    public ArrayList<PaymentMethod> listPaymentMethods;
    private CreatePaymentTransaction createPaymentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStcPayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, android.R.color.black));

        if (isDeviceRooted()) {
            finishPayment("Device is rooted");
        }


        Intent getIntent = getIntent();

        if (!getIntent().hasExtra("CreateTrasactionData")){
            finishPayment("Transaction detail is missing");
        }

        ApiId = getIntent.getStringExtra("ApiId");
        MerchantId = getIntent.getStringExtra("MerchantId");
        LocalLan = getIntent.getStringExtra("Local");
        createPaymentTransaction = (CreatePaymentTransaction)getIntent.getSerializableExtra("CreateTrasactionData");
        Amount = createPaymentTransaction.getAmount();
        CustomerId = createPaymentTransaction.getCustomer_id();


        createTrx(Amount);

    }

    public void createTrx(String amount) {



        Log.e("==========",createPaymentTransaction.type+createPaymentTransaction.pg_codes
                +createPaymentTransaction.amount+createPaymentTransaction.currency_code+createPaymentTransaction.disclosure_url
                +createPaymentTransaction.redirect_url+createPaymentTransaction.customer_id+createPaymentTransaction.expiration_time);


        if (isNetworkAvailable(this)) {
            
            binding.progressLayoutMain.setVisibility(View.VISIBLE);
            String baseUrlCreateTransaction = "https://"+MerchantId+"/b/";
            GetDataService apiendPoint = RetrofitClientInstance.getRetrofitPublicApi(baseUrlCreateTransaction);
            Call<RespoFetchTxnDetail> register = apiendPoint.createPaymentTxn(createPaymentTransaction);
            register.enqueue(new Callback<RespoFetchTxnDetail>() {
                @Override
                public void onResponse(Call<RespoFetchTxnDetail> call, Response<RespoFetchTxnDetail> response) {

                    if (response.isSuccessful() && response.body() != null) {

                        SessionId = response.body().session_id;

                        getTrnDetail();

                    }else {
//                        Toast.makeText(StcPayActivity.this, "Please try again!" +response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                        binding.progressLayoutMain.setVisibility(View.GONE);
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String error1 = jObjError.getJSONArray("pg_codes").get(0).toString();
                            finishPayment(error1);
                        } catch (JSONException | IOException e) {
                            finishPayment("Unable to create transaction");

                        }

                    }

                }

                @Override
                public void onFailure(Call<RespoFetchTxnDetail> call, Throwable t) {
                    binding.progressLayoutMain.setVisibility(View.GONE);
                    Toast.makeText(StcPayActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    private void getTrnDetail() {

        if (SessionId != null || !TextUtils.isEmpty(SessionId)) {

            setLocal(LocalLan);

            if (ApiId == null || ApiId.equals("")){
                finishPayment("ApiId is wrong.");
            }
            if (SessionId == null || SessionId.equals("")){
                finishPayment("SessionId is wrong.");
            }
            if (MerchantId == null || MerchantId.equals("")){
                finishPayment("MerchantId is wrong.");
            }
            if (Amount == null || Amount.equals("")  || Float.parseFloat(Amount) < 0.001){
                finishPayment("Amount is not valid");
            }


        } else {
            finishPayment("Session Id is wrong.");

        }


        if (isNetworkAvailable(StcPayActivity.this)) {
            binding.progressLayoutMain.setVisibility(View.VISIBLE);
            GetDataService apiendPoint = new RetrofitClientInstance().getRetrofitInstance();
//            Call<RespoFetchTxnDetail> register = apiendPoint.fetchTxnDetail(SessionId, true);
            String fetchTrxUrl = "https://"+ MerchantId+ BuildConfig.FetchTxnDetailUrlPart +SessionId+"?enableCHD=true";
            Call<RespoFetchTxnDetail> register = apiendPoint.fetchTxnDetail(fetchTrxUrl);
            register.enqueue(new Callback<RespoFetchTxnDetail>() {
                @Override
                public void onResponse(Call<RespoFetchTxnDetail> call, Response<RespoFetchTxnDetail> response) {


                    if (response.isSuccessful() && response.body() != null) {
                        binding.progressLayoutMain.setVisibility(View.GONE);

                        SessionId = response.body().session_id;
                        CustomerPhone = response.body().customer_phone;
                        listPaymentMethods = response.body().payment_methods;
                        for (int i = 0; i < response.body().payment_methods.size(); i++) {
                            if (response.body().payment_methods.get(i).code.equals("stc_pay")){
                                selectedCardPos = i;
                            }
                        }
                        StcPayPayload stcPayLoad = new StcPayPayload(response.body().payment_methods
                                .get(selectedCardPos).code,SessionId, CustomerPhone,false);
                        showSTCDialog(stcPayLoad,response.body().payment_methods.get(selectedCardPos).submit_url);
                    } else {
                        binding.progressLayoutMain.setVisibility(View.GONE);
//                        Toast.makeText(PaymentActivity.this,, "Please try again!" , Toast.LENGTH_SHORT).show();
                        SocketRespo finalResponse = new SocketRespo();
                        finalResponse.setStatus("failed");
                        finalResponse.setSession_id("");
                        finalResponse.setOrder_no("");
                        finalResponse.setOperation("");
                        finalResponse.setReference_number("");
                        finalResponse.setRedirect_url("");
                        finalResponse.setMerchant_id(MerchantId);
                        Intent intent = new Intent();
                        intent.putExtra("paymentResult", finalResponse);
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                }

                @Override
                public void onFailure(Call<RespoFetchTxnDetail> call, Throwable t) {
                    Toast.makeText(StcPayActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    finishPayment("Can't connect to server.");
                }

            });
        }
    }

    public void setLocal( String local1) {
        Locale locale = new Locale(local1);
        Locale.setDefault(locale);
        Configuration conf = getResources().getConfiguration();
        conf.setLocale(locale);
        conf.setLayoutDirection(locale);
        createConfigurationContext(conf);

        getResources().updateConfiguration(conf, getResources().getDisplayMetrics());

        onConfigurationChanged(conf);
    }

    private void showSTCDialog(StcPayPayload stcPayLoad, String url) {
        DialogStcMnumberBinding dialogBinding = DialogStcMnumberBinding.inflate(getLayoutInflater());
        Dialog dialog = new Dialog(this, R.style.MyDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(dialogBinding.getRoot());

        if (!LocalLan.equals("en")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                dialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        }

        if (listPaymentMethods.get(selectedCardPos).can_save_card){
            dialogBinding.saveCard.setVisibility(View.VISIBLE);
        }
        if (stcPayLoad.customer_phone != null && stcPayLoad.customer_phone.trim().length() > 0){
            dialogBinding.moNumberEtxt.setText(stcPayLoad.customer_phone);
        }
        dialogBinding.saveCard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                stcPayLoad.setSave_card(isChecked);
            }
        });
        dialogBinding.sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBinding.errorMsgText.setText("");
                dialogBinding.errorMsgText.setVisibility(View.GONE);
                String mNumber = dialogBinding.moNumberEtxt.getText().toString().trim();
                if (mNumber.length() < 8 || mNumber == null ||mNumber.equals("") ){
                    dialogBinding.errorMsgText.setText(getResources().getString(R.string.phone_number_notcorrect));
                    dialogBinding.errorMsgText.setVisibility(View.VISIBLE);
                    return;
                }
                stcPayLoad.setCustomer_phone(mNumber);
                if (isNetworkAvailable(StcPayActivity.this)) {

                    dialogBinding.progressLayout.setVisibility(View.VISIBLE);
                    GetDataService apiendPoint = getRetrofitInstance();
                    Call<StcPayResponce> register = apiendPoint.submitSTCPay(url,stcPayLoad);
                    register.enqueue(new Callback<StcPayResponce>() {
                        @Override
                        public void onResponse(Call<StcPayResponce> call, Response<StcPayResponce> response) {
                            dialogBinding.progressLayout.setVisibility(View.GONE);

                            if (response.isSuccessful() && response.code() == 200) {
                                showSTCOtpDialog( dialog);

                            } else if (response.code() == 400){
                                dialogBinding.errorMsgText.setVisibility(View.VISIBLE);
                                String jsonString = null;

                                try {
                                    jsonString = response.errorBody().string();
                                    JSONObject jsonObject = new JSONObject(jsonString);
                                    dialogBinding.errorMsgText.setText(jsonObject.getString("detail"));
                                } catch (JSONException | IOException e ) {
                                    e.printStackTrace();
                                    finishPayment(getResources().getString(R.string.cant_send_otp));
                                }

                            } else if (response.code() == 401){
                                dialogBinding.errorMsgText.setVisibility(View.VISIBLE);
                                String jsonString = response.errorBody().toString();
                                try {
                                    JSONObject jsonObject = new JSONObject(jsonString);
                                    dialogBinding.errorMsgText.setText(jsonObject.getString("detail"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    finishPayment(getResources().getString(R.string.cant_send_otp));
                                }
                            }else {
                                dialogBinding.errorMsgText.setVisibility(View.VISIBLE);
                                dialogBinding.errorMsgText.setText(getResources().getString(R.string.cant_send_otp));
                            }

                        }

                        @Override
                        public void onFailure(Call<StcPayResponce> call, Throwable t) {
                            dialogBinding.progressLayout.setVisibility(View.GONE);
                            finishPayment("Can't connect to server.");
                        }
                    });
                }else {
                    Toast.makeText(StcPayActivity.this, "Check internet connection", Toast.LENGTH_SHORT).show();
                    finishPayment("Can't connect to server.");
                }
            }
        });
        dialogBinding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finishPayment("Payment Canceled");
            }
        });

        dialog.show();
    }

    private void showSTCOtpDialog(Dialog dialogStcMoNumber) {
        DialogOtpBinding dialogBinding = DialogOtpBinding.inflate(getLayoutInflater());
        Dialog dialog = new Dialog(this, R.style.MyDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(dialogBinding.getRoot());
        if (!LocalLan.equals("en")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                dialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        }


        dialogBinding.pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBinding.errorMsgText.setText("");
                dialogBinding.errorMsgText.setVisibility(View.GONE);
                String otp = dialogBinding.moNumberEtxt.getText().toString().trim();
                if (otp.length() < 6 || otp == null ||otp.equals("") ){
                    dialogBinding.errorMsgText.setText(getResources().getString(R.string.incorrect_otp));
                    dialogBinding.errorMsgText.setVisibility(View.VISIBLE);
                    return;
                }
                if (isNetworkAvailable(StcPayActivity.this)) {
                    dialogBinding.progressLayout.setVisibility(View.VISIBLE);
                    GetDataService apiendPoint = getRetrofitInstance();
                    Call<StcOtpResponce> register = apiendPoint.sendStcOtp(listPaymentMethods.get(selectedCardPos).payment_url,
                            new StcOtpPayload(SessionId,otp));
                    register.enqueue(new Callback<StcOtpResponce>() {
                        @Override
                        public void onResponse(Call<StcOtpResponce> call, Response<StcOtpResponce> response) {
                            dialogBinding.progressLayout.setVisibility(View.GONE);

                            if (response.isSuccessful() && response.code() == 200) {
                                dialogBinding.errorMsgText.setText("");
                                dialogBinding.errorMsgText.setVisibility(View.GONE);
                                Gson gson = new Gson();
                                String jsonString = gson.toJson(response.body());
                                try {
                                    JSONObject jsonObject = new JSONObject(jsonString);
                                    finishPayment(jsonObject);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                dialog.dismiss();
                                dialogStcMoNumber.dismiss();

                            }else if (response.code() == 401){
                                dialogBinding.errorMsgText.setVisibility(View.VISIBLE);
                                dialogBinding.errorMsgText.setText(response.body().getDetail());
                            }else {
                                dialogBinding.errorMsgText.setVisibility(View.VISIBLE);
                                dialogBinding.errorMsgText.setText(getResources().getString(R.string.couldnt_verify_otp));
                            }
                        }

                        @Override
                        public void onFailure(Call<StcOtpResponce> call, Throwable t) {
                            dialogBinding.progressLayout.setVisibility(View.GONE);
                            dialogBinding.errorMsgText.setVisibility(View.VISIBLE);
                            dialogBinding.errorMsgText.setText(getResources().getString(R.string.couldnt_verify_otp));
                        }
                    });
                }else {
                    Toast.makeText(StcPayActivity.this, "Check internet connection", Toast.LENGTH_SHORT).show();
                    finishPayment("Can't connect to server.");
                }
            }
        });
        dialogBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void finishPayment(String message) {
        binding.progressLayoutMain.setVisibility(View.GONE);
        SocketRespo finalResponse = new SocketRespo();
        finalResponse.setStatus("failed");
        finalResponse.setSession_id(SessionId);
        finalResponse.setOrder_no(String.valueOf(""));
        finalResponse.setOperation("");
        finalResponse.setReference_number("");
        finalResponse.setRedirect_url("");
        finalResponse.setMessage(message);
        finalResponse.setMerchant_id(MerchantId);
        Intent intent = new Intent();
        intent.putExtra("paymentResult", finalResponse);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void finishPayment(JSONObject jsonObject) {
        binding.progressLayoutMain.setVisibility(View.GONE);

        SocketRespo finalResponse = new SocketRespo();
        try {
            finalResponse.setStatus(jsonObject.getString("status"));
            finalResponse.setSession_id(jsonObject.getString("session_id"));
            if (jsonObject.has("order_no")) {
                finalResponse.setOrder_no(jsonObject.getString("order_no"));
            }
            if (jsonObject.has("operation")) {
                finalResponse.setOperation(jsonObject.getString("operation"));
            }
            if (jsonObject.has("reference_number")) {
                finalResponse.setReference_number(jsonObject.getString("reference_number"));
            }
            if (jsonObject.has("redirect_url")) {
                finalResponse.setRedirect_url(jsonObject.getString("redirect_url"));
            }
            if (jsonObject.has("message")) {
                finalResponse.setMessage(jsonObject.getString("message"));
            }
            finalResponse.setMerchant_id(MerchantId);
            Intent intent = new Intent();
            intent.putExtra("paymentResult", finalResponse);
            setResult(RESULT_OK, intent);
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
            finalResponse.setStatus("failed");
            finalResponse.setMerchant_id(MerchantId);
            Intent intent = new Intent();
            intent.putExtra("paymentResult", finalResponse);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {

    }
}