package Ottu.ui.payment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.transition.ChangeBounds;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import Ottu.databinding.LayoutOttuPaymentBinding;
import Ottu.ui.payment_methods.OttuPaymentMethodsBottomSheet;


public class OttuPaymentView extends FrameLayout {

    private LayoutOttuPaymentBinding binding;

    private @Nullable OttuPaymentViewProvider viewProvider;

    private Type type;

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

        binding.btnSelectedPayment.setListener(() -> {
            if (viewProvider != null) {
                OttuPaymentMethodsBottomSheet.show(viewProvider.provideFragmentManager());
            } else {
                throw new NullPointerException("OttuPaymentViewProvider == null");
            }
        });
    }

    private void invalidateType() {
        if (binding == null) return;

        switch (type) {
            case COLLAPSED:
                binding.groupViewType.setVisibility(GONE);
                beginDelayedTransition();
                break;
            case EXPANDED:
                binding.groupViewType.setVisibility(VISIBLE);
                beginDelayedTransition();
                break;
        }
    }

    private void beginDelayedTransition() {
        final Transition transition = new ChangeBounds();
        transition.setDuration(300);

        TransitionManager.beginDelayedTransition(binding.getRoot(), transition);
    }

    public void setViewProvider(@NonNull OttuPaymentViewProvider provider) {
        this.viewProvider = provider;
    }

    public void setType(Type type) {
        this.type = type;
        invalidateType();
    }

    public enum Type {
        COLLAPSED, EXPANDED
    }


}
