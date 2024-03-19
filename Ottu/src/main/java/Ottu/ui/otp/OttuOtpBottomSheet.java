package Ottu.ui.otp;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import Ottu.R;
import Ottu.databinding.DialogOtpContainerBinding;
import Ottu.util.DITest;


public class OttuOtpBottomSheet extends BottomSheetDialogFragment {

    private DialogOtpContainerBinding binding;
    private OtpViewModel viewModel = (OtpViewModel) DITest.getViewModel(OtpViewModel.class.getSimpleName());

    private int maxPhoneNumberLength;

    public static void show(FragmentManager fragmentManager) {
        OttuOtpBottomSheet dialog = new OttuOtpBottomSheet();

        dialog.show(fragmentManager, OttuOtpBottomSheet.class.getSimpleName());
    }

    @Override
    public int getTheme() {
        return R.style.Ottu_BottomSheetDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogOtpContainerBinding.inflate(inflater, container, false);
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
        maxPhoneNumberLength = getResources().getInteger(R.integer.max_length_phone_number);
        setupViews();
        setupObservers();
    }

    private void setupObservers() {
        viewModel.getPhoneNumber().observe(getViewLifecycleOwner(), this::onPhoneChanged);
    }

    private void setupViews() {
        binding.btnSendOtp.tvText.setText(getString(R.string.text_send_otp));
        binding.btnSendOtp.container.setEnabled(viewModel.getPhoneNumberLength() == maxPhoneNumberLength);

        NavHostFragment navHostFragment = (NavHostFragment) getChildFragmentManager().findFragmentById(R.id.fragmentContainerView);
        NavController navController;

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
        } else {
            navController = null;
        }

        if (navController != null) {
            binding.btnSendOtp.getRoot().setOnClickListener(v -> navController.navigate(R.id.action_to_enterOtpFragment));
        }

    }

    private void onPhoneChanged(String phone) {
        if (binding != null) {
            binding.btnSendOtp.container.setEnabled(phone.length() == maxPhoneNumberLength);
        }
    }

}
