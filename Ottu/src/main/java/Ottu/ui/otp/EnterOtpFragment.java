package Ottu.ui.otp;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import Ottu.R;
import Ottu.databinding.FragmentEnterOtpBinding;

public class EnterOtpFragment extends Fragment {

    private FragmentEnterOtpBinding binding;

    private String phoneNumber;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEnterOtpBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        phoneNumber = requireArguments().getString(getString(R.string.args_phone_number));

        setupViews();
    }

    private void setupViews() {
        binding.tvEnterOtpDescription.setText(getString(R.string.text_enter_otp_description, phoneNumber));

        binding.btnResendOtp.tvText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        binding.btnResendOtp.tvText.setText(getString(R.string.text_resend));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
