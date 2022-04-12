package ottu.payment.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ottu.payment.R;
import ottu.payment.databinding.ItemSavedcardBinding;
import ottu.payment.model.DeleteCard.SendDeleteCard;
import ottu.payment.model.fetchTxnDetail.Card;
import ottu.payment.model.submitCHD.SubmitCHDToOttoPG;
import ottu.payment.ui.PaymentActivity;

import static ottu.payment.ui.PaymentActivity.MerchantId;
import static ottu.payment.ui.PaymentActivity.SessionId;
import static ottu.payment.ui.PaymentActivity.savedCardSelected;

public class SavedCardAdapter extends RecyclerView.Adapter<SavedCardAdapter.ViewHolder>{

    ArrayList<ottu.payment.model.redirect.Card> listCards;
    private ItemSavedcardBinding binding;
    private ItemSavedcardBinding bindingWithData;
    PaymentActivity activity;
    public static int selectedCardPosision = -1;
    public static int lastSelected = -1 ;

    public SavedCardAdapter(PaymentActivity paymentActivity, ArrayList<ottu.payment.model.redirect.Card> cards) {
        listCards = cards;
        activity = paymentActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemSavedcardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        if (selectedCardPosision == position){
            binding.layoutCardData.setBackgroundColor(activity.getResources().getColor(R.color.text_gray7));
        }else {
            binding.layoutCardData.setBackgroundColor(activity.getResources().getColor(R.color.white));
        }

        binding.cardNumber.setText(listCards.get(position).brand+" "+listCards.get(position).number);
        binding.expireDate.setText("Expired on "+listCards.get(position).expiry_month+"/"+listCards.get(position).expiry_year);
        binding.deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Dialog dialog = new Dialog(activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_delete);

                Button no = (Button) dialog.findViewById(R.id.btnNo);
                Button yes = (Button) dialog.findViewById(R.id.btnYes);

                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SendDeleteCard card = new SendDeleteCard("sandbox",listCards.get(position).customer_id);
                        activity.deleteCard(card,listCards.get(position).token);

                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
        binding.layoutCardData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savedCardSelected = true;
                activity.setPayEnable(true);
                selectedCardPosision = position;
                if (lastSelected == position){
                    selectedCardPosision = -1;
                    lastSelected = -1;
                    bindingWithData = null;
                    activity.setPayEnable(false);
                    savedCardSelected = false;
                }else {
                    bindingWithData = binding;

                    lastSelected = position;
                }

                notifyDataSetChanged();
                activity.notifyPaymentMethodAdapter();

            }
        });


    }

    @Override
    public int getItemCount() {
        return listCards.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);

        }
    }

    public SubmitCHDToOttoPG getCardDetail(){
        SubmitCHDToOttoPG submitCHDToOttoPG = null;

        if (bindingWithData == null || selectedCardPosision == -1){
            return submitCHDToOttoPG;
        }

        submitCHDToOttoPG = new SubmitCHDToOttoPG(MerchantId,SessionId,"token",listCards.get(selectedCardPosision).token);

        return submitCHDToOttoPG;
    }
}
