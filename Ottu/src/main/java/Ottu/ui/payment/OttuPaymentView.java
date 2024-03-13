package Ottu.ui.payment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import Ottu.databinding.LayoutOttuPaymentBinding;
import Ottu.ui.payment_methods.OttuPaymentMethodsBottomSheet;


public class OttuPaymentView extends FrameLayout {

    private LayoutOttuPaymentBinding binding;

    private @Nullable OttuPaymentViewProvider viewProvider;

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

    public void setViewProvider(@NonNull OttuPaymentViewProvider provider) {
        this.viewProvider = provider;
    }

    private void init(@NonNull Context context) {
        binding = LayoutOttuPaymentBinding.inflate(LayoutInflater.from(context), this, true);

        binding.btnSelectedPayment.setListener(() -> {
            if (viewProvider != null) {
                OttuPaymentMethodsBottomSheet.show(viewProvider.provideFragmentManager());
            } else {
                throw new NullPointerException("OttuPaymentViewProvider == null");
            }
        });

    }


}
