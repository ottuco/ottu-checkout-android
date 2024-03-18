package Ottu.ui.otp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import Ottu.R;
import Ottu.databinding.DialogAddNumberBinding;
import Ottu.databinding.DialogPaymentMethodsBinding;


public class OttuAddNumberBottomSheet extends BottomSheetDialogFragment {

    private DialogAddNumberBinding binding;

    public static void show(FragmentManager fragmentManager) {
        OttuAddNumberBottomSheet dialog = new OttuAddNumberBottomSheet();

        dialog.show(fragmentManager, OttuAddNumberBottomSheet.class.getSimpleName());
    }

    @Override
    public int getTheme() {
        return R.style.Ottu_BottomSheetDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogAddNumberBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        dialog.setOnShowListener(dialog1 -> {
            dialog.getBehavior().setPeekHeight(binding.getRoot().getHeight(), true);
        });

        return dialog;
    }
}
