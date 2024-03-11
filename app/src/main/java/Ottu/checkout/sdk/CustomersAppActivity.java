package Ottu.checkout.sdk;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import Ottu.payment.sdk.databinding.ActivityCustomersAppBinding;

public class CustomersAppActivity extends AppCompatActivity {

    private ActivityCustomersAppBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomersAppBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}