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

//        binding.container.setEnabled(false);
//        binding.container.setActivated(true);

    }

    public void setText(String text) {
        if (binding == null) return;

        binding.tvPaymentText.setText(text);
        binding.tvPaymentText.setVisibility(VISIBLE);
        binding.ivIcon.setVisibility(GONE);
    }

    public void setIcon(int icon) {
        if (binding == null) return;

        binding.container.setSelected(true);

        binding.tvPaymentText.setText(icon);
        binding.tvPaymentText.setVisibility(VISIBLE);
        binding.ivIcon.setVisibility(GONE);
    }

}
