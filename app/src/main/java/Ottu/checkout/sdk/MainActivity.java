package Ottu.checkout.sdk;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import Ottu.model.GenerateToken.CreatePaymentTransaction;
import Ottu.model.SocketData.SocketRespo;
import Ottu.model.fetchTxnDetail.RespoFetchTxnDetail;

import Ottu.checkout.sdk.network.GetDataService;

import Ottu.payment.sdk.BuildConfig;
import Ottu.ui.Ottu;
import Ottu.payment.sdk.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static Ottu.checkout.sdk.network.RetrofitClientInstance.getRetrofitInstance;
import static Ottu.util.Constant.OttuPaymentResult;


public class MainActivity extends AppCompatActivity  {

    private EditText etLocalLan;
    public ArrayList<String> listpg = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isStoragePermissionGranted();
        EditText text = findViewById(R.id.etAmount);
        EditText localLan = findViewById(R.id.localLan);
        RadioButton rbOttupg = findViewById(R.id.rbOttupg);
        RadioButton rbKnet = findViewById(R.id.rbKnet);
        RadioButton rbMpgs = findViewById(R.id.rbMpgs);
        AppCompatButton pay = findViewById(R.id.pay);
        etLocalLan = findViewById(R.id.localLan);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String amount = text.getText().toString().trim();
                String language = localLan.getText().toString().trim();

                listpg.clear();
                if (rbOttupg.isChecked()){
                    listpg.add("ottu_pg_kwd_tkn");
                }
                if (rbKnet.isChecked()){
                    listpg.add("knet");
                }
                if (rbMpgs.isChecked()){
                    listpg.add("mpgs");
                }
                if (listpg.size() < 1){
                    listpg.add("ottu_pg_kwd_tkn");
                }



                if (language.equals("en") || language.equals("ar")){
                    createTrx(Float.parseFloat(amount));
                    rbOttupg.setChecked(false);
                    rbKnet.setChecked(false);
                    rbMpgs.setChecked(false);
                }else {
                    Toast.makeText(MainActivity.this, "Enter supported launguage", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {

                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    public void createTrx(float amount) {

//        String[] listpg  = {"ottu_pg_kwd_tkn", "knet-test", "mpgs"};
        CreatePaymentTransaction paymentTransaction = new CreatePaymentTransaction("e_commerce"
                , listpg
                ,String.valueOf(amount)
                ,"KWD"
                ,"https://postapp.knpay.net/disclose_ok/"
                ,"https://postapp.knpay.net/redirected/"
                ,"mani"
                ,"300");

        Log.e("==========",paymentTransaction.type+paymentTransaction.pg_codes+paymentTransaction.amount+paymentTransaction.currency_code+paymentTransaction.disclosure_url
        +paymentTransaction.redirect_url+paymentTransaction.customer_id+paymentTransaction.expiration_time);


        if (isNetworkAvailable(MainActivity.this)) {
            final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage("Please wait for a moment. Fetching data.");
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
            GetDataService apiendPoint = getRetrofitInstance();
            Call<RespoFetchTxnDetail> register = apiendPoint.createPaymentTxn(paymentTransaction);
            register.enqueue(new Callback<RespoFetchTxnDetail>() {
                @Override
                public void onResponse(Call<RespoFetchTxnDetail> call, Response<RespoFetchTxnDetail> response) {
                    dialog.dismiss();

                    if (response.isSuccessful() && response.body() != null) {
                        Ottu ottuPaymentSdk = new Ottu(MainActivity.this);
                        ottuPaymentSdk.setApiId("KaaWIoPG.AZGowZsIiXATe9QVYBiEdnfbheb3sGPj");  // L0Fc5f81.dLqByodGesaD9pJdzoKpo6rP1FQBkVzR
                        ottuPaymentSdk.setMerchantId("hotfix4.ottu.dev"); //ksa.ottu.dev
                        ottuPaymentSdk.setSessionId(response.body().session_id);
                        ottuPaymentSdk.setAmount(response.body().amount);
                        ottuPaymentSdk.setLocal(etLocalLan.getText().toString().trim());
                        ottuPaymentSdk.build();



                    }else {
                        Log.i("========",response.errorBody().toString());
//                        Toast.makeText(MainActivity.this, "Please try again!" +response.errorBody().toString(), Toast.LENGTH_SHORT).show();

                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            Toast.makeText(MainActivity.this, jObjError.getJSONArray("pg_codes").get(0).toString(), Toast.LENGTH_LONG).show();
                        } catch (JSONException | IOException e) {


                        }

                    }

                }

                @Override
                public void onFailure(Call<RespoFetchTxnDetail> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(MainActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    public static boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager manager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            // Network is present and connected
            isAvailable = true;
        }
        return isAvailable;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK ){
            if (requestCode == OttuPaymentResult ){
                SocketRespo paymentResult = (SocketRespo) data.getSerializableExtra("paymentResult");
                StringBuilder sb = new StringBuilder("");
                sb.append("Status : "+paymentResult.getStatus()+"\n");
                sb.append("Message : "+paymentResult.getMessage()+"\n");
                sb.append("Session id : "+paymentResult.getSession_id()+"\n");
                sb.append("Order no : "+paymentResult.getOrder_no()+"\n");
                sb.append("operation : "+paymentResult.getOperation()+"\n");
                sb.append("Reference number : "+paymentResult.getReference_number()+"\n");
                sb.append("Redirect url : "+paymentResult.getRedirect_url()+"\n");
                sb.append("Merchant id : "+paymentResult.getMerchant_id());



                new AlertDialog.Builder(this)
                        .setMessage(sb)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        listpg.clear();
    }
}