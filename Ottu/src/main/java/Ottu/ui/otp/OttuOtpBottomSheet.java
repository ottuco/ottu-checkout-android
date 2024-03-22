package Ottu.ui.otp;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import Ottu.R;
import Ottu.databinding.DialogOtpContainerBinding;
import Ottu.ui.BaseBottomSheetDialogFragment;
import Ottu.ui.payment.PaymentMethodViewModel;
import Ottu.util.PhoneNumberUtil;
import Ottu.util.PrototypeUtil;


public class OttuOtpBottomSheet extends BaseBottomSheetDialogFragment {

    private DialogOtpContainerBinding binding;
    private final OtpViewModel viewModel = (OtpViewModel) PrototypeUtil.getViewModel(OtpViewModel.class.getSimpleName());
    private final PaymentMethodViewModel sharedViewModel = (PaymentMethodViewModel) PrototypeUtil.getViewModel(PaymentMethodViewModel.class.getSimpleName());

    private int maxPhoneNumberLength;
    private int maxOtpCodeLength;

    private State state = State.ADD_NUMBER;

    public static void show(@NonNull FragmentManager fragmentManager) {
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
            dialog.getBehavior().setShouldRemoveExpandedCorners(false);
            dialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
        });

        return dialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        maxPhoneNumberLength = getResources().getInteger(R.integer.max_length_phone_number);
        maxOtpCodeLength = getResources().getInteger(R.integer.max_length_otp_code);
        setupViews();
        setupObservers();
    }

    private void setupObservers() {
        viewModel.getPhoneNumber().observe(getViewLifecycleOwner(), this::onPhoneChanged);
        viewModel.getOtpCodeLiveData().observe(getViewLifecycleOwner(), this::onOtpCodeChanged);
    }

    private void setupViews() {
        changeState(State.ADD_NUMBER);

        NavHostFragment navHostFragment = (NavHostFragment) getChildFragmentManager().findFragmentById(R.id.fragmentContainerView);
        NavController navController;

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
        } else {
            navController = null;
        }

        if (navController != null) {
            binding.btnBack.setOnClickListener(v -> navController.navigateUp());

            navController.addOnDestinationChangedListener((navController1, navDestination, bundle) -> {
                if (AddNumberFragment.class.getSimpleName().equals(navDestination.getLabel())) {
                    changeState(State.ADD_NUMBER);
                } else if (EnterOtpFragment.class.getSimpleName().equals(navDestination.getLabel())) {
                    changeState(State.ENTER_OTP);
                }
            });

            binding.btnOtpActions.getRoot().setOnClickListener(v -> {
                switch (state) {
                    case ADD_NUMBER:
                        navController.navigate(R.id.action_to_enterOtpFragment, getPhoneNumberBundle());
                        break;
                    case ENTER_OTP:
                        sharedViewModel.setOtpCodeResult(true);
                        dismiss();
                        break;
                }
            });
        }

    }

    private @Nullable Bundle getPhoneNumberBundle() {
        String phoneNumber = viewModel.getPhoneNumber().getValue();

        if (phoneNumber == null) return null;

        String formattedPhoneNumber = PhoneNumberUtil.formatPhoneNumber(requireContext(), phoneNumber);
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.args_phone_number), formattedPhoneNumber);

        return bundle;
    }

    private void changeState(State state) {
        this.state = state;

        switch (state) {
            case ADD_NUMBER:
                binding.tvTitle.setText(getString(R.string.title_add_stc_number));
                binding.btnOtpActions.tvText.setText(getString(R.string.text_send_otp));
                binding.btnBack.setVisibility(View.GONE);
                binding.btnOtpActions.container.setEnabled(viewModel.getPhoneNumberLength() == maxPhoneNumberLength);
                break;
            case ENTER_OTP:
                binding.tvTitle.setText(getString(R.string.enter_otp));
                binding.btnOtpActions.tvText.setText(getString(R.string.confirm));
                binding.btnBack.setVisibility(View.VISIBLE);
                binding.btnOtpActions.container.setEnabled(viewModel.getOtpCodeLength() == maxOtpCodeLength);
                break;
        }
    }

    private void onPhoneChanged(String phone) {
        if (binding != null) {
            binding.btnOtpActions.container.setEnabled(phone.length() == maxPhoneNumberLength);
        }
    }

    private void onOtpCodeChanged(String otpCode) {
        if (binding != null) {
            binding.btnOtpActions.container.setEnabled(otpCode.length() == maxOtpCodeLength);
        }
    }

    private enum State {
        ADD_NUMBER, ENTER_OTP
    }

}
