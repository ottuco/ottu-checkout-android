package ottu.payment.ui;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;


import ottu.payment.R;
import ottu.payment.databinding.ActivityWebPaymentBinding;
import ottu.payment.model.SocketData.SendToSocket;
import ottu.payment.model.SocketData.SocketRespo;
import ottu.payment.model.redirect.ResponceFetchTxnDetail;
import ottu.payment.network.GetDataService;
import ottu.payment.network.RetrofitClientInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static ottu.payment.util.Constant.Amount;
import static ottu.payment.util.Constant.ApiId;
import static ottu.payment.util.Constant.MerchantId;
import static ottu.payment.util.Util.isNetworkAvailable;

public class WebPaymentActivity extends AppCompatActivity {

    private ActivityWebPaymentBinding binding;
    private WebSocketClient mWebSocketClient;
    private String referenceNo = "";

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
        if (getIntent().hasExtra("RedirectUrl")) {
            url = getIntent().getStringExtra("RedirectUrl");
            webView.loadUrl(url);
        } else if (getIntent().hasExtra("is3DS")) {
            if (getIntent().hasExtra("is3DS")) {
                url = getIntent().getStringExtra("html");
                webView.loadData(url, "text/html; charset=utf-8", "UTF-8");
                String socketUrl = getIntent().getStringExtra("ws_url");
                connectWebSocket(socketUrl);


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
            if (request.getUrl().toString().contains("mobile-sdk-redirect")) {


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
            Call<ResponceFetchTxnDetail> register = apiendPoint.fetchTxnDetail(ApiId, false);
            register.enqueue(new Callback<ResponceFetchTxnDetail>() {
                @Override
                public void onResponse(Call<ResponceFetchTxnDetail> call, Response<ResponceFetchTxnDetail> response) {
                    dialog.dismiss();

                    if (response.isSuccessful() && response.body() != null) {
                        Intent intent = new Intent(WebPaymentActivity.this, PaymentResultActivity.class);

                        String state = response.body().state;
                        if (state.equals("expired")) {
                            intent.putExtra("Result", false);
                        } else if (state.equals("success")) {
                            intent.putExtra("Result", true);
                        } else {
                            intent.putExtra("Result", false);
                        }
                        intent.putExtra("name", response.body().customer_first_name + " " + response.body().customer_last_name);
                        intent.putExtra("amount", response.body().amount);
                        intent.putExtra("status", response.body().state);
                        intent.putExtra("gateway", response.body().mode);
                        intent.putExtra("referanceNo", referenceNo);
                        startActivity(intent);
                        finish();
                        Log.e("=======", response.body().toString());
                    } else {
                        Toast.makeText(WebPaymentActivity.this, "Please try again!", Toast.LENGTH_SHORT).show();
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

        if (mWebSocketClient != null) {
            mWebSocketClient.close();
        }
    }


    private void connectWebSocket(String socketUrl) {
        URI uri;
        try {
            uri = new URI(socketUrl);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.i("Websocket", "Opened");
                String jsonString = new com.google.gson.Gson().toJson(
                        new SendToSocket("sdk"
                                , getIntent().getStringExtra("reference_number")
                                , MerchantId));
                mWebSocketClient.send(jsonString);

            }

            @Override
            public void onMessage(String s) {
                SocketRespo response = null;

                response = new Gson().fromJson(s, SocketRespo.class);
                Log.e("Websocket", "onMessage " + s);


                SocketRespo finalResponse = response;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (finalResponse != null) {
                            Intent intent = new Intent(WebPaymentActivity.this, PaymentResultActivity.class);

                            String state = finalResponse.getStatus();
                            if (state.equals("expired")) {
                                intent.putExtra("Result", false);
                            } else if (state.equals("success")) {
                                intent.putExtra("Result", true);
                            } else {
                                intent.putExtra("Result", false);
                            }
                            intent.putExtra("name", "");
                            intent.putExtra("referanceNo", finalResponse.getReference_number());
                            intent.putExtra("amount", Amount);
                            intent.putExtra("status", finalResponse.getStatus());
                            intent.putExtra("gateway", finalResponse.getOperation());
                            startActivity(intent);
                            finish();

//                            referenceNo = finalResponse.getReference_number();
//                            getTrnDetail();

                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                Log.e("Websocket", "onMessage " + jsonObject.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    }
                });
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.e("Websocket", "Closed " + s);
            }

            @Override
            public void onError(Exception e) {
                Log.e("Websocket", "Error " + e.getMessage());
            }
        };
        mWebSocketClient.connect();
    }


    @Override
    public void onBackPressed() {
        finish();
    }
}