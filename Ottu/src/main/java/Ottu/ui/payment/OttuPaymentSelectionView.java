package Ottu.ui.payment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;

import Ottu.databinding.LayoutOttuPaymentSelectionBinding;
import Ottu.ui.payment_methods.OttuPaymentMethodsBottomSheet;


public class OttuPaymentSelectionView extends LinearLayoutCompat {

    private LayoutOttuPaymentSelectionBinding binding;
    private OttuPaymentSelectionListener listener;

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

        binding.btnSelectedPayment.getRoot().setOnClickListener(view -> {
            if (listener != null) {
                listener.onSelectedPaymentClick();
            }
        });

    }

    public void setListener(OttuPaymentSelectionListener listener) {
        this.listener = listener;
    }

    public enum Type {
        UNSELECTED,
        SELECTED,
        SELECTED_WITH_DESCRIPTION
    }

}
