package ottu.payment.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import ottu.payment.R;
import ottu.payment.adapter.PaymentMethodAdapter;
import ottu.payment.adapter.SavedCardAdapter;
import ottu.payment.databinding.ActivityPaymentBinding;
import ottu.payment.model.DeleteCard.SendDeleteCard;
import ottu.payment.model.RedirectUrl.CreateRedirectUrl;
import ottu.payment.model.RedirectUrl.RespoRedirectUrl;
import ottu.payment.model.redirect.ResponceFetchTxnDetail;
import ottu.payment.model.submitCHD.Card_SubmitCHD;
import ottu.payment.model.submitCHD.SubmitCHDToOttoPG;
import ottu.payment.network.GetDataService;
import ottu.payment.network.RetrofitClientInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static ottu.payment.network.RetrofitClientInstance.getRetrofitInstance;
import static ottu.payment.network.RetrofitClientInstance.getRetrofitInstancePg;
import static ottu.payment.util.Constant.Amount;
import static ottu.payment.util.Constant.ApiId;
import static ottu.payment.util.Constant.LocalLan;
import static ottu.payment.util.Constant.MerchantId;
import static ottu.payment.util.Constant.SessionId;
import static ottu.payment.util.Constant.savedCardSelected;
import static ottu.payment.util.Constant.selectedCardPos;
import static ottu.payment.util.Constant.selectedCardPosision;
import static ottu.payment.util.Constant.sessionId;
import static ottu.payment.util.Util.isNetworkAvailable;

public class PaymentActivity extends AppCompatActivity {

    ActivityPaymentBinding binding;
    private PaymentMethodAdapter adapterPaymentMethod;
    private SavedCardAdapter adapterSavedCard;

    private List<String> pg_codes;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        view();
        getTrnDetail();

    }


    private void view() {

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.gradiunt_blue));


        binding.rvSavedCards.setLayoutManager(new LinearLayoutManager(this));
        binding.rvPaymentMethod.setLayoutManager(new LinearLayoutManager(this));
        binding.payNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (savedCardSelected){
                    SubmitCHDToOttoPG cardDetail = adapterSavedCard.getCardDetail();
                    Log.e("=========",cardDetail.toString());
                    payNow(cardDetail);
                }else {
                    if (selectedCardPos == 0) {
                        Card_SubmitCHD submitCHD = adapterPaymentMethod.getCardData();
                        if (submitCHD == null) {
                            Toast.makeText(PaymentActivity.this, getResources().getString(R.string.enter_carddetail), Toast.LENGTH_SHORT).show();
                        } else {
                            if (sessionId.equals("")) {
                                Toast.makeText(PaymentActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                                return;
                            }

//                        CreatePaymentTransaction paymentTransaction = adapterPaymentMethod.getPaymentTrn(selectedCardPos);
//                        createTrx(paymentTransaction,paymentTransaction.getPg_codes().get(selectedCardPos));
                            SubmitCHDToOttoPG submitCHDToPG = new SubmitCHDToOttoPG(MerchantId, SessionId, "card", submitCHD);
                            payNow(submitCHDToPG);
                        }
                    } else if (selectedCardPos == 1) {
//                    CreatePaymentTransaction paymentTransaction = adapterPaymentMethod.getPaymentTrn(selectedCardPos);
//                    createTrx(paymentTransaction,paymentTransaction.getPg_codes().get(selectedCardPos));


                        CreateRedirectUrl redirectUrl = new CreateRedirectUrl(pg_codes.get(selectedCardPos), "mobile_sdk");
                        createRedirectUrl(redirectUrl, SessionId);
                    } else if (selectedCardPos == 2) {

                        CreateRedirectUrl redirectUrl = new CreateRedirectUrl(pg_codes.get(selectedCardPos), "mobile_sdk");
                        createRedirectUrl(redirectUrl, SessionId);
                    }
                }
            }
        });


    }

    public void setPayEnable(boolean isenble){
        binding.payNow.setBackground(getResources().getDrawable(R.drawable.payenable));
        binding.payNow.setEnabled(isenble);
        if (isenble){
            binding.payNow.setBackground(getResources().getDrawable(R.drawable.payenable));
            binding.payNow.setTextColor(getResources().getColor(R.color.white));
        }else {
            binding.payNow.setBackground(getResources().getDrawable(R.drawable.paydisable));
            binding.payNow.setTextColor(getResources().getColor(R.color.text_gray2));
        }
    }

    private void payNow(SubmitCHDToOttoPG submitCHDToPG) {
        if (isNetworkAvailable(PaymentActivity.this)) {
            showLoader(true);
            GetDataService apiendPoint = getRetrofitInstancePg();
            Call<JsonElement> register = apiendPoint.respoSubmitCHD(submitCHDToPG);
            register.enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                    showLoader(false);

                    if (response.isSuccessful()) {

                        try {
                            JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));


                            if (jsonObject.has("status")) {
                                // got success
                                String status = jsonObject.getString("status");
                                if (status.equals("success")){

                                    Toast.makeText(PaymentActivity.this, "Payment Successfull", Toast.LENGTH_SHORT).show();
                                }else if (status.equals("failed")){
                                    Toast.makeText(PaymentActivity.this, "Payment Failed", Toast.LENGTH_SHORT).show();
                                }else if (status.equals("error")){
                                    Toast.makeText(PaymentActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                }else if (status.equals("3DS")){
                                    startActivity(new Intent(PaymentActivity.this,WebPaymentActivity.class)
                                            .putExtra("is3DS",true)
                                    .putExtra("html",jsonObject.getString("html"))
                                    .putExtra("reference_number",jsonObject.getString("reference_number"))
                                            .putExtra("ws_url",jsonObject.getString("ws_url")));
                                    finish();
                                }

                            }else {
                                //got payment error

                                JSONObject cardFieldError = jsonObject.getJSONObject("card");
                                JSONArray cardGlobleError = jsonObject.getJSONArray("card");
                                JSONArray nonFieldErrors = jsonObject.getJSONArray("non_field_errors");
                                JSONArray merchantId = jsonObject.getJSONArray("merchant_id");
                                JSONArray payment_method = jsonObject.getJSONArray("payment_method");

                                
                                if (cardFieldError != null){
                                    Toast.makeText(PaymentActivity.this, "Card Filed Error", Toast.LENGTH_SHORT).show();
                                }
                                if (cardGlobleError != null){
                                    Toast.makeText(PaymentActivity.this, ""+cardGlobleError.get(0), Toast.LENGTH_SHORT).show();
                                }
                                if (nonFieldErrors != null){
                                    Toast.makeText(PaymentActivity.this, nonFieldErrors.getString(0), Toast.LENGTH_SHORT).show();
                                }
                                if (merchantId != null){
                                    Toast.makeText(PaymentActivity.this, merchantId.getString(0), Toast.LENGTH_SHORT).show();
                                }

                            }




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }else {
                        Toast.makeText(PaymentActivity.this, "Please try again!" , Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<JsonElement> call, Throwable t) {
                    showLoader(false);
                    Toast.makeText(PaymentActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void getTrnDetail() {
        String apiId = null;
        String amount = null;
        if (getIntent().hasExtra("SessionId")) {
             apiId = getIntent().getStringExtra("ApiId");
             Amount = getIntent().getStringExtra("Amount");
             MerchantId = getIntent().getStringExtra("MerchantId");
            SessionId = getIntent().getStringExtra("SessionId");
            LocalLan = getIntent().getStringExtra("LocalLan");
            setLocal(LocalLan);
             ApiId = apiId;
             binding.amountTextView.setText(Amount);
        }else {
            Toast.makeText(this, "No sessionid", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        if (isNetworkAvailable(PaymentActivity.this)) {
//            final ProgressDialog dialog = new ProgressDialog(PaymentActivity.this);
//            dialog.setMessage("Please wait for a moment. Fetching data.");
//            dialog.setCanceledOnTouchOutside(true);
//            dialog.show();
            showLoader(true);
            GetDataService apiendPoint = new RetrofitClientInstance().getRetrofitInstance();
            Call<ResponceFetchTxnDetail> register = apiendPoint.fetchTxnDetail(apiId,true);
            register.enqueue(new Callback<ResponceFetchTxnDetail>() {
                @Override
                public void onResponse(Call<ResponceFetchTxnDetail> call, Response<ResponceFetchTxnDetail> response) {
//                    dialog.dismiss();
                    showLoader(false);

                    if (response.isSuccessful() && response.body() != null) {
                        showData(response.body());
                        sessionId = response.body().session_id;
                        pg_codes = response.body().pg_codes;
                        Log.e("=======",response.body().toString());
                    }else {
                        Toast.makeText(PaymentActivity.this, "Please try again!" , Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<ResponceFetchTxnDetail> call, Throwable t) {
                    showLoader(false);
                    Toast.makeText(PaymentActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }

            });
        }
    }

    public void showData(ResponceFetchTxnDetail body) {
        if (body != null){
            binding.savescardTxt.setText(getResources().getString(R.string.total_bill));
            binding.titleSavedCard.setText(getResources().getString(R.string.saved_card));
            binding.subTitleSavedCard.setText(getResources().getString(R.string.list_card_saved));
            binding.txtpaymentMethod.setText(getResources().getString(R.string.payment_method));
            binding.txtpaymentMethodsub.setText(getResources().getString(R.string.list_gatway));

            binding.payNow.setText(Html.fromHtml("<b>" + getResources().getString(R.string.paynow) + "</b>"  +"("+body.amount + body.currency_code+")"));
            binding.currencyCode.setText(body.currency_code);
            if (body.cards != null) {
                if (body.cards.size() < 1){
                    binding.layoutSavedListTitle.setVisibility(View.GONE);
                }
                adapterSavedCard = new SavedCardAdapter(PaymentActivity.this,body.cards );
                binding.rvSavedCards.setAdapter(adapterSavedCard);
            }else {
                binding.layoutSavedListTitle.setVisibility(View.GONE);
            }
            if (body.payment_methods != null) {
               adapterPaymentMethod =  new PaymentMethodAdapter(this,body );
                binding.rvPaymentMethod.setAdapter(adapterPaymentMethod);
            }

        }
    }


    private void createRedirectUrl(CreateRedirectUrl redirectUrl, String session_id) {

        if (isNetworkAvailable(PaymentActivity.this)) {
            showLoader(true);
            GetDataService apiendPoint = getRetrofitInstance();
            Call<RespoRedirectUrl> register = apiendPoint.createRedirectUrl(session_id,redirectUrl);
            register.enqueue(new Callback<RespoRedirectUrl>() {
                @Override
                public void onResponse(Call<RespoRedirectUrl> call, Response<RespoRedirectUrl> response) {
                    showLoader(false);

                    if (response.isSuccessful() && response.body() != null) {

                        if (response.body().getRedirect_url() != null){
                            startActivity(new Intent(PaymentActivity.this,WebPaymentActivity.class)
                            .putExtra("RedirectUrl",response.body().getRedirect_url()));
                            finish();
                        }else {
                            Toast.makeText(PaymentActivity.this, response.body().getMessage() , Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        Log.e("=======",response.body().toString());
                    }else {
                        Toast.makeText(PaymentActivity.this, "Please try again!" , Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }

                @Override
                public void onFailure(Call<RespoRedirectUrl> call, Throwable t) {
                    showLoader(false);
                    Toast.makeText(PaymentActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    public void notifySavedCardAdapter(){
        if (adapterSavedCard != null){
            selectedCardPosision = -1;
            adapterSavedCard.notifyDataSetChanged();
        }
    }
    public void notifyPaymentMethodAdapter(){

        if (adapterPaymentMethod != null){
//            binding.rvPaymentMethod.setAdapter(adapterPaymentMethod);
            selectedCardPos = -1;
            adapterPaymentMethod.notifyDataSetChanged();
        }
    }

    public void manageKeyboard(InputConnection ic, int visible){
        if (visible == View.VISIBLE) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);
        }
        binding.keyboard.setInputConnection(ic);
        binding.keyboard.setVisibility(visible);
    }
    public void deleteCard(SendDeleteCard deleteCard, String token) {

        if (isNetworkAvailable(PaymentActivity.this)) {
            showLoader(true);
            GetDataService apiendPoint = getRetrofitInstance();
            Call<ResponseBody> register = apiendPoint.deleteCard(token,deleteCard.customer_id,deleteCard.type);
            register.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    showLoader(false);

                    Toast.makeText(PaymentActivity.this, "Card Deleted" , Toast.LENGTH_SHORT).show();

                    finish();
                    startActivity(getIntent());

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    showLoader(false);
                }
            });
        }

    }

    public void showLoader(boolean visibility){
        Glide.with(this).load(R.raw.loader).into(binding.loader);
        if (visibility) {
            binding.progressLayout.setVisibility(View.VISIBLE);
        }else {
            binding.progressLayout.setVisibility(View.GONE);
        }
    }
    public void setLocal(String local1){
        Locale locale = new Locale(local1);
        Locale.setDefault(locale);
        Configuration conf = getResources().getConfiguration();
        conf.setLocale(locale);
        conf.setLayoutDirection(locale);
        createConfigurationContext(conf);

//        DisplayMetrics metrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        getResources().updateConfiguration(conf,metrics);
//        Resources resources = new Resources(getAssets(), metrics, conf);
        getBaseContext().getResources().updateConfiguration(conf, getBaseContext().getResources().getDisplayMetrics());

    }
    @Override
    public void onBackPressed() {

        if (binding.keyboard.getVisibility() == View.VISIBLE){
            binding.keyboard.setVisibility(View.GONE);
            return;
        }
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectedCardPos = -1;
        selectedCardPosision = -1;
        notifySavedCardAdapter();
        notifyPaymentMethodAdapter();
    }
}