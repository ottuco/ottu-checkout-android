package Ottu.ui.otp;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import Ottu.R;
import Ottu.databinding.FragmentAddNumberOtpBinding;
import Ottu.util.DITest;
import Ottu.util.TextWatcherAdapter;

public class AddNumberFragment extends Fragment {

    private FragmentAddNumberOtpBinding binding;

    private OtpViewModel viewModel = (OtpViewModel) DITest.getViewModel(OtpViewModel.class.getSimpleName());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddNumberOtpBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews();
    }

    private void setupViews() {
        validatePhoneNumber();

//        binding.etAddNumber.setOnFocusChangeListener((view, hasFocus) -> {
//            if (hasFocus) {
//                binding.etAddNumber.setHint(R.string.placeholder_phone_number);
//            } else {
//                binding.etAddNumber.setHint("");
//            }
//        });


        binding.viewSavePaymentMethod.tvSaveMethodTitle.setText(getString(R.string.title_save_stc_number));
        binding.viewSavePaymentMethod.tvSaveMethodDescription.setText(getString(R.string.text_save_stc_number_description));

        binding.viewSavePaymentMethod.getRoot().setOnClickListener(view -> {
            binding.viewSavePaymentMethod.switchSaveMethod.toggle();
        });
    }

    private void validatePhoneNumber() {
        binding.etAddNumber.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(@NonNull Editable s) {
                super.afterTextChanged(s);
                viewModel.setPhoneNumber(s.toString());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
