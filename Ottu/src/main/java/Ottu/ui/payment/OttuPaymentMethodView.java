package Ottu.ui.payment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.transition.ChangeBounds;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import Ottu.R;
import Ottu.databinding.LayoutOttuPaymentMethodBinding;
import Ottu.model.fetchTxnDetail.PaymentMethod;
import Ottu.ui.otp.OttuOtpBottomSheet;
import Ottu.ui.payment_methods.OttuPaymentMethodsBottomSheet;
import Ottu.util.PrototypeUtil;
import Ottu.util.SingleClickListener;


public class OttuPaymentMethodView extends FrameLayout implements PaymentSelectionListener {

    private LayoutOttuPaymentMethodBinding binding;

    private @Nullable PaymentViewProvider viewProvider;

    private final PaymentMethodViewModel viewModel = (PaymentMethodViewModel) PrototypeUtil
            .getViewModel(PaymentMethodViewModel.class.getSimpleName());

    private Type type;

    private final Observer<PaymentMethod> selectedMethodObserver = method -> {
        invalidatePaymentButton();
    };

    private final Observer<String> cvvCodeObserver = cvvCode -> {
        checkPaymentButtonAccessibility();
    };

    private final Observer<Boolean> otpCodeResultObserver = isOtpResultSuccess -> {
        if (isOtpResultSuccess != null) {
            PrototypeUtil.showProcessingPaymentDialog(getContext(), this, () -> {
                viewModel.setSelectedPaymentMethod(null);
                Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
            });
        }
    };

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

        invalidatePaymentButton();

        binding.tvPaymentFeesCurrencyValue.setVisibility(INVISIBLE);
        binding.tvPaymentTotalCurrencyValue.setVisibility(INVISIBLE);

        binding.btnSelectedPayment.setListener(this);

        binding.btnPayment.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick() {
                makePayment();
            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        viewModel.getSelectedPaymentMethodLiveData().observeForever(selectedMethodObserver);
        viewModel.getCvvCodeLiveData().observeForever(cvvCodeObserver);
        viewModel.getOtpCodeResultLiveData().observeForever(otpCodeResultObserver);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        viewModel.getSelectedPaymentMethodLiveData().removeObserver(selectedMethodObserver);
        viewModel.getCvvCodeLiveData().removeObserver(cvvCodeObserver);
        viewModel.getOtpCodeResultLiveData().removeObserver(otpCodeResultObserver);
    }

    private void makePayment() {
        PaymentMethod paymentMethod = viewModel.getSelectedPaymentMethod();
        if (paymentMethod != null) {
            if (paymentMethod.type.equals("2")) {
                if (viewProvider != null) {
                    OttuOtpBottomSheet.show(viewProvider.provideFragmentManager());
                }
            } else {
                PrototypeUtil.showProcessingPaymentDialog(getContext(), this, () -> {
                    viewModel.setSelectedPaymentMethod(null);
                    Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                });
            }
        }
    }

    private void invalidateType() {
        if (binding == null) return;

        PaymentMethod method = viewModel.getSelectedPaymentMethod();

        int detailsVisibility = type == Type.COLLAPSED ? GONE : VISIBLE;

        if (method != null && method.cvv) {
            binding.containerPaymentDetails.setVisibility(detailsVisibility);
        } else {
            binding.groupWithoutButton.setVisibility(detailsVisibility);
        }

        beginDelayedTransition();
    }

    private void invalidatePaymentButton() {
        if (binding == null) return;
        checkPaymentButtonAccessibility();

        PaymentMethod paymentMethod = viewModel.getSelectedPaymentMethod();

        if (paymentMethod != null && PrototypeUtil.isBrandedPayment(paymentMethod.type)) {
            binding.btnPayment.setIcon(PrototypeUtil.getPaymentButtonIconByType(paymentMethod.type));
        } else {
            binding.btnPayment.setText(getContext().getString(R.string.text_pay_with_value, "(12.000 KWD)"));
        }
    }

    @Override
    public void onSelectPaymentClicked() {
        if (viewProvider != null) {
            OttuPaymentMethodsBottomSheet.show(viewProvider.provideFragmentManager(), viewModel::setSelectedPaymentMethod);
        } else {
            throw new NullPointerException("OttuPaymentViewProvider == null");
        }
    }

    private void checkPaymentButtonAccessibility() {
        boolean isEnabled = false;
        PaymentMethod paymentMethod = viewModel.getSelectedPaymentMethod();
        String cvvCode = viewModel.getCvvCode();
        int minCvvCodeLength = getResources().getInteger(R.integer.min_length_cvv_code);
        int maxCvvCodeLength = getResources().getInteger(R.integer.max_length_cvv_code);

        if (paymentMethod != null) {
            if (paymentMethod.cvv) {
                isEnabled = cvvCode != null && (cvvCode.length() >= minCvvCodeLength && cvvCode.length() <= maxCvvCodeLength);
            } else {
                isEnabled = true;
            }
        }

        binding.btnPayment.setEnabled(isEnabled);
    }

    private void beginDelayedTransition() {
        final Transition transition = new ChangeBounds();
        transition.setDuration(300);

        TransitionManager.beginDelayedTransition(binding.getRoot(), transition);
    }

    public void setViewProvider(@NonNull PaymentViewProvider provider) {
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
