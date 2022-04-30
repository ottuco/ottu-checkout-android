package ottu.payment.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import ottu.payment.R;
import ottu.payment.databinding.ActivityPaymetResultBinding;
import ottu.payment.interfaces.OttuPaymentCallback;
import ottu.payment.interfaces.SendPaymentCallback;

public class PaymentResultActivity extends AppCompatActivity {

    ActivityPaymetResultBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymetResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        view();

    }

    private void view() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.gradiunt_blue));



        if (getIntent().hasExtra("Result")) {
            boolean result = getIntent().getBooleanExtra("Result", false);
            String name = getIntent().getStringExtra("name");
            String amount = getIntent().getStringExtra("amount");
            String status = getIntent().getStringExtra("status");
            String gateway = getIntent().getStringExtra("gateway");
            String referanceNo = getIntent().getStringExtra("referanceNo");

            binding.referanceNo.setText(referanceNo);
            binding.amount.setText(amount);
            binding.status.setText(status);
            binding.gateway.setText(gateway);


            if (getIntent().getBooleanExtra("Result", false)) {
                binding.resultImg.setImageResource(R.drawable.sucess);
                binding.resultText.setText(getResources().getString(R.string.transaction_successfull));
            } else {
                binding.resultImg.setImageResource(R.drawable.fail);
                binding.resultText.setText(getResources().getString(R.string.transaction_fail));
            }


            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

//                    setResult(001,new Intent().putExtra("Result","Fail"));
                    finish();


                }
            }, 3000);
        }
    }



    @Override
    public void onBackPressed() {
        startActivity(new Intent(PaymentResultActivity.this, PaymentActivity.class));
        finish();
    }
}