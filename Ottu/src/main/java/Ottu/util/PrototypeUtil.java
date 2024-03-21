package Ottu.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModel;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Ottu.R;
import Ottu.databinding.DialogPaymentResultBinding;
import Ottu.databinding.DialogProcessingPaymentBinding;
import Ottu.model.fetchTxnDetail.PaymentMethod;
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

    public static List<PaymentMethod> createPaymentMethods() {
        ArrayList<PaymentMethod> data = new ArrayList<>();

        data.add(createPaymentMethod("Google Pay", "1"));
        data.add(createPaymentMethod("STC Pay", "2"));
        data.add(createPaymentMethod("Credit Card", "3"));
        data.add(createPaymentMethod("Credit Card", "4"));
        data.add(createPaymentMethod("KNET", "5"));
        data.add(createPaymentMethod("Benefit Pay", "6"));
        data.add(createPaymentMethod("Ottu PG", "7"));

        return data;
    }

    private static PaymentMethod createPaymentMethod(String name, String type) {
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.name = name;
        paymentMethod.type = type;

        if (type.equals("3") ) {
            paymentMethod.desc = "*8282 | Expires on 01/39";
            paymentMethod.cardNumber = "****8282";
            paymentMethod.cvv = true;
        }

        if (type.equals("4") ) {
            paymentMethod.desc = "*8282 | Expires on 01/39";
            paymentMethod.cardNumber = "****8282";
        }

        return paymentMethod;
    }

    public static int getPaymentIconByType(String type) {
        switch (type) {
            case "1":
                return R.drawable.ic_google;
            case "2":
                return R.drawable.ic_stc_pay;
            case "3":
            case "4":
                return R.drawable.ic_saved_card;
            case "5":
                return R.drawable.ic_knet;
            case "6":
                return R.drawable.ic_benefitpay;
            case "7":
                return R.drawable.ic_ottu;
            default:
                return 0;
        }
    }

    public static int getPaymentButtonIconByType(String type) {
        switch (type) {
            case "1":
                return R.drawable.ic_google_pay;
            case "2":
                return R.drawable.ic_stc_pay_large;
            default:
                return 0;
        }
    }

    public static boolean isBrandedPayment(String type) {
        switch (type) {
            case "1":
            case "2":
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
