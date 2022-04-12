package ottu.payment.ui;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Arrays;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import ottu.payment.R;
import ottu.payment.databinding.ActivityWebPaymentBinding;
import ottu.payment.model.GenerateToken.CreatePaymentTransaction;
import ottu.payment.model.RedirectUrl.CreateRedirectUrl;
import ottu.payment.model.redirect.ResponceFetchTxnDetail;
import ottu.payment.model.submitCHD.Card_SubmitCHD;
import ottu.payment.model.submitCHD.SubmitCHDToOttoPG;
import ottu.payment.network.GetDataService;
import ottu.payment.network.RetrofitClientInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static ottu.payment.adapter.PaymentMethodAdapter.selectedCardPos;
import static ottu.payment.network.RetrofitClientInstance.getRetrofitInstance;
import static ottu.payment.ui.PaymentActivity.Amount;
import static ottu.payment.ui.PaymentActivity.ApiId;
import static ottu.payment.util.Util.isNetworkAvailable;

public class WebPaymentActivity extends AppCompatActivity {

    private ActivityWebPaymentBinding binding;
    private Socket mSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWebPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        WebView webView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewOverrideUrl());
        webView.setWebChromeClient(new MyWebViewClient());
        webView.getSettings().setDomStorageEnabled(true);

        String url = null;
        if (getIntent().hasExtra("RedirectUrl")){
            url = getIntent().getStringExtra("RedirectUrl");
            webView.loadUrl(url);
        }else if (getIntent().hasExtra("is3DS")){
            if (getIntent().hasExtra("is3DS")){
                url = getIntent().getStringExtra("html");
                webView.loadData(url, "text/html; charset=utf-8", "UTF-8");
                String socketUrl = getIntent().getStringExtra("ws_url");
                try {
                    mSocket = IO.socket(socketUrl);
                } catch (URISyntaxException e) {

                }
                mSocket.connect();
                mSocket.on("new message", socketListner);


            }
        }


    }

    private class WebViewOverrideUrl extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            proceedUrl(view, Uri.parse(url));
            return true;
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            proceedUrl(view, request.getUrl());
            if (request.getUrl().toString().contains("mobile-sdk-redirect")){


                getTrnDetail();
            }
            return true;
        }

        private void proceedUrl(WebView view, Uri uri) {
            try {
                view.loadUrl(uri.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private class MyWebViewClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress >= 100) {

            }
            super.onProgressChanged(view, newProgress);
        }
    }


    private void getTrnDetail() {


        if (isNetworkAvailable(WebPaymentActivity.this)) {
            final ProgressDialog dialog = new ProgressDialog(WebPaymentActivity.this);
            dialog.setMessage("Please wait for a moment. Fetching data.");
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
            GetDataService apiendPoint = new RetrofitClientInstance().getRetrofitInstance();
            Call<ResponceFetchTxnDetail> register = apiendPoint.fetchTxnDetail(ApiId,true);
            register.enqueue(new Callback<ResponceFetchTxnDetail>() {
                @Override
                public void onResponse(Call<ResponceFetchTxnDetail> call, Response<ResponceFetchTxnDetail> response) {
                    dialog.dismiss();

                    if (response.isSuccessful() && response.body() != null) {
                        Intent intent = new Intent(WebPaymentActivity.this,PaymentResultActivity.class);

                        String state = response.body().state;
                        if (state.equals("expired")){
                            intent.putExtra("Result",false);
                        }else if (state.equals("")){
                            intent.putExtra("Result",true);
                        }else {
                            intent.putExtra("Result",false);
                        }
                        intent.putExtra("name",response.body().customer_first_name + " " +response.body().customer_last_name);
                        intent.putExtra("amount",response.body().amount);
                        intent.putExtra("status",response.body().state);
                        intent.putExtra("gateway",response.body().mode);
                        startActivity(intent);
                        finish();
                        Log.e("=======",response.body().toString());
                    }else {
                        Toast.makeText(WebPaymentActivity.this, "Please try again!" , Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }

                @Override
                public void onFailure(Call<ResponceFetchTxnDetail> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(WebPaymentActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mSocket.disconnect();
        mSocket.off("new message", socketListner);
    }

    private Emitter.Listener socketListner = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    Toast.makeText(WebPaymentActivity.this, ""+data.toString(), Toast.LENGTH_SHORT).show();



                }
            });
        }



    };
}