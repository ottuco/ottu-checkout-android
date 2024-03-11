package Ottu.ui.payment;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import Ottu.databinding.LayoutOttuPaymentBinding;


public class OttuPaymentView extends FrameLayout {

    private LayoutOttuPaymentBinding binding;

    public OttuPaymentView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public OttuPaymentView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public OttuPaymentView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(@NonNull Context context) {
        binding = LayoutOttuPaymentBinding.inflate(LayoutInflater.from(context), this, true);
    }


}
