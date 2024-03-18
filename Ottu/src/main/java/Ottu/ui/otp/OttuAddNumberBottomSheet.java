package Ottu.ui.otp;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import Ottu.R;
import Ottu.databinding.DialogAddNumberBinding;
import Ottu.util.TextWatcherAdapter;


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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews();
    }

    private void setupViews() {
        validatePhoneNumber();

        binding.viewSavePaymentMethod.tvSaveMethodTitle.setText(getString(R.string.title_save_stc_number));
        binding.viewSavePaymentMethod.tvSaveMethodDescription.setText(getString(R.string.text_save_stc_number_description));

        binding.viewSavePaymentMethod.getRoot().setOnClickListener(view -> {
            binding.viewSavePaymentMethod.switchSaveMethod.toggle();
        });

        binding.btnSendOtp.tvText.setText(getString(R.string.text_send_otp));
    }

    private void validatePhoneNumber() {
        int maxPhoneNumberLength = getResources().getInteger(R.integer.max_length_phone_number);

        binding.btnSendOtp.container.setEnabled(binding.etAddNumber.length() == maxPhoneNumberLength);

        binding.etAddNumber.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(@NonNull Editable s) {
                super.afterTextChanged(s);
                binding.btnSendOtp.container.setEnabled(s.length() == maxPhoneNumberLength);
            }
        });
    }

}
