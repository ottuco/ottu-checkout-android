package Ottu.ui.payment_methods;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.ChangeBounds;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import Ottu.R;
import Ottu.databinding.DialogPaymentMethodsBinding;
import Ottu.ui.BaseBottomSheetDialogFragment;
import Ottu.util.PrototypeUtil;
import Ottu.util.SwipeToDeleteCallback;

public class OttuPaymentMethodsBottomSheet extends BaseBottomSheetDialogFragment {

    private final OnPaymentClickListener listener;
    private DialogPaymentMethodsBinding binding;

    public static void show(FragmentManager fragmentManager, OnPaymentClickListener listener) {
        OttuPaymentMethodsBottomSheet dialog = new OttuPaymentMethodsBottomSheet(listener);

        dialog.show(fragmentManager, OttuPaymentMethodsBottomSheet.class.getSimpleName());
    }

    private OttuPaymentMethodsBottomSheet(OnPaymentClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getTheme() {
        return R.style.Ottu_BottomSheetDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogPaymentMethodsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        dialog.setOnShowListener(dialog1 -> {
            dialog.getBehavior().setShouldRemoveExpandedCorners(false);
        });

        return dialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews();
    }

    private void setupViews() {
        PaymentMethodsAdapter adapter = new PaymentMethodsAdapter(PrototypeUtil.createPaymentMethods(), method -> {
            listener.onPaymentClicked(method);
            dismiss();
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(requireContext(), adapter));

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                beginDelayedTransition();
            }
        });

        itemTouchHelper.attachToRecyclerView(binding.rvPaymentMethods);
        binding.rvPaymentMethods.setAdapter(adapter);
    }



    private void beginDelayedTransition() {
        ViewGroup parent = (ViewGroup) binding.getRoot().getParent();

        final Transition transition = new ChangeBounds();
        transition.setDuration(300);

        TransitionManager.beginDelayedTransition(parent, transition);
    }

}
