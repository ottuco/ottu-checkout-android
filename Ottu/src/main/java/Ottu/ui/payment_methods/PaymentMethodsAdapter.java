package Ottu.ui.payment_methods;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import Ottu.R;
import Ottu.databinding.ItemPaymentMethodCompactBinding;
import Ottu.databinding.ItemPaymentMethodFullBinding;
import Ottu.model.fetchTxnDetail.PaymentMethod;

public class PaymentMethodsAdapter extends RecyclerView.Adapter<PaymentMethodsAdapter.ViewHolder> {

    private static final int COMPACT_VIEW_TYPE = 0;
    private static final int FULL_VIEW_TYPE = 1;

    private final List<PaymentMethod> data;

    public PaymentMethodsAdapter(List<PaymentMethod> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        switch (viewType) {
            case COMPACT_VIEW_TYPE:
                return new CompactViewHolder(ItemPaymentMethodCompactBinding.inflate(inflater, viewGroup, false));
            case FULL_VIEW_TYPE:
                return new FullViewHolder(ItemPaymentMethodFullBinding.inflate(inflater, viewGroup, false));
            default:
                throw new IllegalStateException("Unexpected value: " + viewType);
        }
    }

    @Override
    public int getItemViewType(int position) {
        PaymentMethod paymentMethod = data.get(position);
        switch (paymentMethod.type) {
            case "3":
            case "4":
                return FULL_VIEW_TYPE;
            default:
                return COMPACT_VIEW_TYPE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public abstract class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public abstract void bind(PaymentMethod paymentMethod);

        public int getIconByType(String type) {
            switch (type) {
                case "1":
                    return R.drawable.card_visa;
                case "2":
                    return R.drawable.card_visa;
                case "3":
                    return R.drawable.card_visa;
                case "4":
                    return R.drawable.card_visa;
                case "5":
                    return R.drawable.card_visa;
                default:
                    return R.drawable.card_visa;
            }
        }
    }

    private class CompactViewHolder extends ViewHolder {

        private final ItemPaymentMethodCompactBinding binding;

        public CompactViewHolder(@NonNull ItemPaymentMethodCompactBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        public void bind(PaymentMethod paymentMethod) {
            binding.ivPaymentMethod.setImageResource(getIconByType(paymentMethod.type));
            binding.tvPaymentTitle.setText(paymentMethod.name);
            binding.tvPaymentValue.setText("+1.000 KWD");
        }
    }

    private class FullViewHolder extends ViewHolder {

        private final ItemPaymentMethodFullBinding binding;

        public FullViewHolder(@NonNull ItemPaymentMethodFullBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        public void bind(PaymentMethod paymentMethod) {
            binding.ivPaymentMethod.setImageResource(getIconByType(paymentMethod.type));
            binding.tvPaymentTitle.setText(paymentMethod.name);
            binding.tvPaymentDescription.setText("*8282 | Expires on 01/39");
            binding.tvPaymentValue.setText("+1.000 KWD");
        }
    }


}
