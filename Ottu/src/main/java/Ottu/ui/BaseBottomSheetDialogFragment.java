package Ottu.ui;

import android.app.Dialog;
import android.view.View;
import android.view.Window;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BaseBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private int windowAnimations = DIALOG_ENTER_ANIM_DISABLED;

    private static final int DIALOG_ENTER_ANIM_DISABLED = -1;

    @Override
    public void onResume() {
        super.onResume();
        View view = getView();
        if (view != null) {
            view.postOnAnimationDelayed(() -> {
                if (windowAnimations != DIALOG_ENTER_ANIM_DISABLED) {
                    setWindowAnimations(windowAnimations);
                }
            }, 300L);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        setWindowAnimations(DIALOG_ENTER_ANIM_DISABLED);
    }

    private void setWindowAnimations(int animations) {
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            if (window != null) {
                //Fix: repeating default animation after lifecycle`s changes
                if (animations == DIALOG_ENTER_ANIM_DISABLED) {
                    windowAnimations = window.getAttributes().windowAnimations;
                }
                window.setWindowAnimations(animations);
            }
        }
    }
}
