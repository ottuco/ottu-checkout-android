package Ottu.ui.payment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import Ottu.databinding.LayoutOttuPaymentSelectionBinding;


public class OttuPaymentSelectionView extends FrameLayout {

    private LayoutOttuPaymentSelectionBinding binding;

    public OttuPaymentSelectionView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public OttuPaymentSelectionView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public OttuPaymentSelectionView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(@NonNull Context context) {
        binding = LayoutOttuPaymentSelectionBinding.inflate(LayoutInflater.from(context), this, true);
    }

    public enum Type {
        UNSELECTED,
        SELECTED,
        SELECTED_WITH_DESCRIPTION
    }

}
