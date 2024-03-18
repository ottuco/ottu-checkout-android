package Ottu.checkout.sdk;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;

import Ottu.payment.sdk.databinding.ActivityCustomersAppBinding;
import Ottu.ui.payment.OttuPaymentMethodView;

public class CustomersAppActivity extends AppCompatActivity {

    private ActivityCustomersAppBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomersAppBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.ottuPaymentView.setViewProvider(this::getSupportFragmentManager);

        binding.switchUiMode.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        binding.switchContentType.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            binding.ottuPaymentView.setType(isChecked ? OttuPaymentMethodView.Type.COLLAPSED : OttuPaymentMethodView.Type.EXPANDED);
        });

        binding.switchLanguage.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            String lang = isChecked ? "ar" : "en";
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(lang));
        });
    }
}