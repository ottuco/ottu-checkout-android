package Ottu.ui.otp;

import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class OttuAddNumberBottomSheet extends BottomSheetDialogFragment {

    public static void show(FragmentManager fragmentManager) {
        OttuAddNumberBottomSheet dialog = new OttuAddNumberBottomSheet();

        dialog.show(fragmentManager, OttuAddNumberBottomSheet.class.getSimpleName());
    }

}
