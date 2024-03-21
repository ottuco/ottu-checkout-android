package Ottu.ui.payment;

import android.content.Context;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;

import Ottu.databinding.LayoutOttuPaymentSelectionBinding;
import Ottu.model.fetchTxnDetail.PaymentMethod;
import Ottu.util.PrototypeUtil;
import Ottu.util.TextWatcherAdapter;


public class OttuPaymentSelectionView extends LinearLayoutCompat {

    private LayoutOttuPaymentSelectionBinding binding;
    private PaymentSelectionListener listener;

    public OttuPaymentSelectionView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public OttuPaymentSelectionView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public OttuPaymentSelectionView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(@NonNull Context context) {
        binding = LayoutOttuPaymentSelectionBinding.inflate(LayoutInflater.from(context), this, true);

        binding.btnSelectedPayment.setOnClickListener(view -> {
            if (listener != null) {
                listener.onPaymentSelectionClick();
            }
        });

    }

    public void select(@Nullable PaymentMethod paymentMethod) {
        if (paymentMethod == null) {
            showUnselectedPayment();
        } else {
            showSelectedPayment(paymentMethod);
        }
    }

    private void showUnselectedPayment() {
        binding.viewSelectedPayment.getRoot().setVisibility(GONE);
        binding.viewUnselectedPayment.getRoot().setVisibility(VISIBLE);
    }

    private void showSelectedPayment(PaymentMethod paymentMethod) {
        binding.viewSelectedPayment.getRoot().setVisibility(VISIBLE);
        binding.viewUnselectedPayment.getRoot().setVisibility(GONE);

        //Test
        if (paymentMethod.cardNumber != null) {
            binding.viewSelectedPayment.tvSelectedPaymentDescription.setVisibility(VISIBLE);
            binding.viewSelectedPayment.tvSelectedPaymentDescription.setText(paymentMethod.cardNumber);
        } else {
            binding.viewSelectedPayment.tvSelectedPaymentDescription.setVisibility(GONE);
        }

        binding.tilCvvCode.setVisibility(paymentMethod.cvv ? VISIBLE : GONE);
        binding.etCvvCode.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(@NonNull Editable s) {
                super.afterTextChanged(s);
                listener.onCvvCodeChanged(s.toString());
            }
        });

        binding.viewSelectedPayment.tvSelectedPayment.setText(paymentMethod.name);

        binding.viewSelectedPayment.ivPaymentMethod.setImageResource(PrototypeUtil.getPaymentIconByType(paymentMethod.type));
    }

    public void setListener(PaymentSelectionListener listener) {
        this.listener = listener;
    }

}
