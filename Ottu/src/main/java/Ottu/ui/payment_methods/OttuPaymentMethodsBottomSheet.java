package Ottu.ui.payment_methods;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import Ottu.R;
import Ottu.databinding.DialogPaymentMethodsBinding;

public class OttuPaymentMethodsBottomSheet extends BottomSheetDialogFragment {

    private DialogPaymentMethodsBinding binding;

    public static void show(FragmentManager fragmentManager) {
        OttuPaymentMethodsBottomSheet dialog = new OttuPaymentMethodsBottomSheet();

        dialog.show(fragmentManager, OttuPaymentMethodsBottomSheet.class.getSimpleName());
    }

    @Override
    public int getTheme() {
        return R.style.Ottu_BottomSheetDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogPaymentMethodsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

}
