package Ottu.ui.otp;

import android.os.Bundle;
import android.text.Editable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import Ottu.R;
import Ottu.databinding.FragmentEnterOtpBinding;
import Ottu.util.PrototypeUtil;
import Ottu.util.TextWatcherAdapter;

public class EnterOtpFragment extends Fragment {

    private FragmentEnterOtpBinding binding;

    private String phoneNumber;

    private final OtpViewModel viewModel = (OtpViewModel) PrototypeUtil.getViewModel(OtpViewModel.class.getSimpleName());

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
        binding.tvEnterOtpNumberText.setText(phoneNumber);

        binding.btnResendOtp.tvText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        binding.btnResendOtp.tvText.setText(getString(R.string.text_resend));
        binding.btnResendOtp.getRoot().setOnClickListener(view -> resend());

        binding.etOtpCode.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(@NonNull Editable s) {
                super.afterTextChanged(s);
                viewModel.setOtpCode(s.toString());
            }
        });
    }

    private void resend() {
        Toast.makeText(requireContext(), "Resend", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
