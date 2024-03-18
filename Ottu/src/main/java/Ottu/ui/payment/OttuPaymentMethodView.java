package Ottu.ui.payment;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.transition.ChangeBounds;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import Ottu.R;
import Ottu.databinding.DialogProcessingPaymentBinding;
import Ottu.databinding.LayoutOttuPaymentMethodBinding;
import Ottu.ui.otp.OttuAddNumberBottomSheet;
import Ottu.ui.payment_methods.OttuPaymentMethodsBottomSheet;


public class OttuPaymentMethodView extends FrameLayout {

    private LayoutOttuPaymentMethodBinding binding;

    private @Nullable OttuPaymentViewProvider viewProvider;

    private Type type;

    public OttuPaymentMethodView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public OttuPaymentMethodView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public OttuPaymentMethodView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(@NonNull Context context) {
        binding = LayoutOttuPaymentMethodBinding.inflate(LayoutInflater.from(context), this, true);

        binding.btnSelectedPayment.setListener(() -> {
            if (viewProvider != null) {
//                OttuPaymentMethodsBottomSheet.show(viewProvider.provideFragmentManager());
                OttuAddNumberBottomSheet.show(viewProvider.provideFragmentManager());

//                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context, R.style.ThemeOverlay_Ottu_MaterialAlertDialog);
//
//                DialogProcessingPaymentBinding processingPaymentBinding = DialogProcessingPaymentBinding
//                        .inflate(LayoutInflater.from(context), this, false);
//
//                builder.setView(processingPaymentBinding.getRoot());
//
//                builder.show();

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
