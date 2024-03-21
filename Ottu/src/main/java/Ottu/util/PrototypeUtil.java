package Ottu.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModel;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.HashMap;

import Ottu.R;
import Ottu.databinding.DialogPaymentResultBinding;
import Ottu.databinding.DialogProcessingPaymentBinding;
import Ottu.ui.otp.OtpViewModel;
import Ottu.ui.payment.PaymentMethodViewModel;

/*
 * DONT USE, ONLY FOR TEST
 *
 * */

public class PrototypeUtil {

    private static final HashMap<String, ViewModel> viewModels = new HashMap<>();

    static {
        viewModels.put(OtpViewModel.class.getSimpleName(), new OtpViewModel());
        viewModels.put(PaymentMethodViewModel.class.getSimpleName(), new PaymentMethodViewModel());
    }

    public static ViewModel getViewModel(String key) {
        return viewModels.get(key);
    }

    public static int getPaymentIconByType(String type) {
        switch (type) {
            case "1":
                return R.drawable.ic_google_pay;
            case "2":
            case "3":
                return R.drawable.ic_stc_pay;
            case "4":
                return R.drawable.ic_saved_card;
            case "5":
                return R.drawable.ic_knet;
            case "6":
                return R.drawable.ic_benefitpay;
            case "7":
                return R.drawable.ic_ottu;
            default:
                return R.drawable.card_visa;
        }
    }

    public static boolean isBrandedPayment(String type) {
        switch (type) {
            case "1":
            case "2":
            case "3":
                return true;
            default:
                return false;
        }
    }

    public static void showProcessingPaymentDialog(Context context, ViewGroup root, OnDialogListener listener) {
        MaterialAlertDialogBuilder loadingBuilder = new MaterialAlertDialogBuilder(context, R.style.ThemeOverlay_Ottu_MaterialAlertDialog);

        DialogProcessingPaymentBinding loadingProcessingPaymentBinding = DialogProcessingPaymentBinding
                .inflate(LayoutInflater.from(context), root, false);

        loadingProcessingPaymentBinding.tvDialogTitle.setText("Processing Payment");
        loadingProcessingPaymentBinding.tvDialogMessage.setText("Please wait while we process your payment");

        loadingBuilder.setView(loadingProcessingPaymentBinding.getRoot());
        Dialog loadingDialog = loadingBuilder.show();

        new Handler().postDelayed(() -> {
            loadingDialog.dismiss();
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context, R.style.ThemeOverlay_Ottu_MaterialAlertDialog);

            DialogPaymentResultBinding processingPaymentBinding = DialogPaymentResultBinding
                    .inflate(LayoutInflater.from(context), root, false);

            processingPaymentBinding.ivIconResult.setImageResource(R.drawable.ic_success);

            builder.setView(processingPaymentBinding.getRoot());

            builder.setPositiveButton(R.string.dialog_action_close,(dialog, which) -> {
                listener.onButtonClick();
                dialog.dismiss();
            });

            builder.show();
        }, 2500);
    }

    public interface OnDialogListener {
        void onButtonClick();
    }

}
