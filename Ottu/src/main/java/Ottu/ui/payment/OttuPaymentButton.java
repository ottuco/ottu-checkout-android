package Ottu.ui.payment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import Ottu.databinding.ViewOttuPaymentButtonBinding;


public class OttuPaymentButton extends ConstraintLayout {

    private ViewOttuPaymentButtonBinding binding;

    public OttuPaymentButton(@NonNull Context context) {
        super(context);
        init(context);
    }

    public OttuPaymentButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public OttuPaymentButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(@NonNull Context context) {
        binding = ViewOttuPaymentButtonBinding.inflate(LayoutInflater.from(context), this, true);
    }

    public enum Type {
        Icon, Text
    }

}
