package ottu.payment.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ottu.payment.R;
import ottu.payment.databinding.DialogDeleteBinding;
import ottu.payment.databinding.ItemSavedcardBinding;
import ottu.payment.model.DeleteCard.SendDeleteCard;
import ottu.payment.model.redirect.Card;
import ottu.payment.model.submitCHD.SubmitCHDToOttoPG;
import ottu.payment.ui.PaymentActivity;

import static ottu.payment.util.Constant.MerchantId;
import static ottu.payment.util.Constant.SessionId;
import static ottu.payment.util.Constant.lastSelected;
import static ottu.payment.util.Constant.savedCardSelected;
import static ottu.payment.util.Constant.selectedCardPosision;


public class SavedCardAdapter extends RecyclerView.Adapter<SavedCardAdapter.ViewHolder>{

    ArrayList<ottu.payment.model.redirect.Card> listCards;
    private ItemSavedcardBinding binding;
    private ItemSavedcardBinding bindingWithData;
    PaymentActivity activity;


    public SavedCardAdapter(PaymentActivity paymentActivity, ArrayList<ottu.payment.model.redirect.Card> cards) {
        listCards = cards;
        activity = paymentActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemSavedcardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.bindData(listCards.get(position),position);


    }

    @Override
    public int getItemCount() {
        return listCards.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {
        ItemSavedcardBinding itemBinding;
        public ViewHolder(ItemSavedcardBinding itemView) {
            super(itemView.getRoot());
            itemBinding = itemView;

        }

        public void bindData(Card card, int position) {
            if (selectedCardPosision == position){
                itemBinding.layoutCardData.setBackground(activity.getResources().getDrawable(R.drawable.item_bg_selected));
                itemBinding.checkIndicator.setImageResource(R.drawable.item_selected);
                itemBinding.deleteImage.setVisibility(View.VISIBLE);
            }else {
//                itemBinding.layoutCardData.setBackgroundColor(activity.getResources().getColor(R.color.white));
                itemBinding.layoutCardData.setBackground(activity.getResources().getDrawable(R.drawable.item_bg));
                itemBinding.checkIndicator.setImageResource(R.drawable.item_unselected);
                itemBinding.deleteImage.setVisibility(View.GONE);
            }

            itemBinding.cardNumber.setText(listCards.get(position).brand+" "+listCards.get(position).number);
            itemBinding.expireDate.setText(listCards.get(position).expiry_month+"/"+listCards.get(position).expiry_year);
            itemBinding.deleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogDeleteBinding deleteBinding = DialogDeleteBinding.inflate(activity.getLayoutInflater());
                    Dialog dialog = new Dialog(activity, R.style.MyDialog);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(true);
                    dialog.setContentView(deleteBinding.getRoot());


                    deleteBinding.deleteTitle.setText("Remove : "+listCards.get(position).number);
                    deleteBinding.no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    deleteBinding.yes.setOnClickListener(new View.OnClickListener() {
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
            itemBinding.layoutCardData.setOnClickListener(new View.OnClickListener() {
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
                        bindingWithData = itemBinding;
                        lastSelected = position;
                    }

                    notifyDataSetChanged();
                    activity.notifyPaymentMethodAdapter();

                }
            });



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
