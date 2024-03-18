package Ottu.ui.otp;

import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class OttuOtpBottomSheet extends BottomSheetDialogFragment {

    public static void show(FragmentManager fragmentManager) {
        OttuOtpBottomSheet dialog = new OttuOtpBottomSheet();

        dialog.show(fragmentManager, OttuOtpBottomSheet.class.getSimpleName());
    }

}
