package Ottu.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import Ottu.R;
import Ottu.databinding.DialogDeleteBinding;
import Ottu.databinding.ItemSavedcardBinding;
import Ottu.model.DeleteCard.SendDeleteCard;
import Ottu.model.fetchTxnDetail.Card;
import Ottu.model.submitCHD.SubmitCHDToOttoPG;
import Ottu.ui.PaymentActivity;

import static Ottu.util.Constant.MerchantId;
import static Ottu.util.Constant.SessionId;
import static Ottu.util.Constant.savedCardSelected;
import static Ottu.util.Constant.selectedCardPosision;


public class SavedCardAdapter extends RecyclerView.Adapter<SavedCardAdapter.ViewHolder>{

    ArrayList<Card> listCards;
    private ItemSavedcardBinding binding;
    private ItemSavedcardBinding bindingWithData;
    PaymentActivity activity;


    public SavedCardAdapter(PaymentActivity paymentActivity, ArrayList<Card> cards) {
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
                if (listCards.get(position).cvv_required){
                    itemBinding.layoutCVV.setVisibility(View.VISIBLE);
                    checkIfcardDetailfill(itemBinding, true);
                }else {
                    activity.setPayEnable(true);
                }
                bindingWithData = itemBinding;
            }else {
//              itemBinding.layoutCardData.setBackgroundColor(activity.getResources().getColor(R.color.white));
                itemBinding.layoutCardData.setBackground(activity.getResources().getDrawable(R.drawable.item_bg));
                itemBinding.checkIndicator.setImageResource(R.drawable.item_unselected);
                itemBinding.deleteImage.setVisibility(View.GONE);
                itemBinding.layoutCVV.setVisibility(View.GONE);
            }

            itemBinding.cardImage.setImageResource(getcard(listCards.get(position).brand));
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
                            activity.deleteCard(card,listCards.get(position).delete_url,position,listCards);
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
                    selectedCardPosision = position;
//                    if (lastSelected == position){
//                        selectedCardPosision = -1;
//                        lastSelected = -1;
//                        bindingWithData = null;
//                        activity.setPayEnable(false);
//                        savedCardSelected = false;
//                    }else {
//                        bindingWithData = itemBinding;
//                        lastSelected = position;
//                    }
                    bindingWithData = null;
                    activity.setSavedCardFee(listCards.get(position).pg_code);
//                    if (listCards.get(position).cvv_required){
//                        itemBinding.layoutCVV.setVisibility(View.VISIBLE);
//                    }else {
//                    }
//                    activity.setPayEnable(true);


                    notifyDataSetChanged();
                    activity.notifyPaymentMethodAdapter();

                }
            });


            InputConnection ic1 = itemBinding.cvvTextView.onCreateInputConnection(new EditorInfo());
            itemBinding.cvvTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (hasFocus) {
                        itemBinding.cvvTextView.setRawInputType(InputType.TYPE_CLASS_TEXT);
                        itemBinding.cvvTextView.setTextIsSelectable(true);
                        itemBinding.cvvTextView.setShowSoftInputOnFocus(false);
                        activity.manageKeyboard(ic1, View.VISIBLE);
                    }else {
                        activity.manageKeyboard(ic1, View.GONE);
                    }
                }
            });
            itemBinding.cvvTextView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (activity.getKeyboardvisibility() == View.GONE) {
                        activity.manageKeyboard(ic1, View.VISIBLE);
                    }
                    return false;
                }
            });

            itemBinding.cvvTextView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String s = editable.toString();
                    if (s.length() > 2) {
                        checkIfcardDetailfill(itemBinding, true);
                    }else {
                        checkIfcardDetailfill(itemBinding, false);
                    }

                }
            });

        }
    }

    private int getcard(String brand) {

        if (brand.toLowerCase().equals("visa")){
            return R.drawable.icon_visa;
        }else if (brand.toLowerCase().equals("mastercard")){
            return R.drawable.icon_mastercard;
        } else if (brand.toLowerCase().equals("americanexpress")){
            return R.drawable.icon_amex;
        }else if (brand.toLowerCase().equals("dinner")){
            return R.drawable.icon_diner;
        }else if (brand.toLowerCase().equals("discover")){
            return R.drawable.icon_diner;                   // set true card icon
        }else if (brand.toLowerCase().equals("jcb")){
            return R.drawable.icon_jcb;
        }else if (brand.toLowerCase().equals("maestro")){
            return R.drawable.icon_maestro;
        }else if (brand.toLowerCase().equals("rupay")){
            return R.drawable.icon_rupay;
        } else if (brand.toLowerCase().equals("unionpay")){
            return R.drawable.icon_unionpay;
        }

        return 0;
    }

    public SubmitCHDToOttoPG getCardDetail(){
        SubmitCHDToOttoPG submitCHDToOttoPG = null;

        if (bindingWithData == null || selectedCardPosision == -1){
            return submitCHDToOttoPG;
        }

        String cvv = bindingWithData.cvvTextView.getText().toString().trim();
        if (bindingWithData.layoutCVV.getVisibility() == View.VISIBLE) {
            if (cvv.equals("") || cvv.length() < 3) {
                bindingWithData.expiredateErrorTextView.setText(activity.getResources().getString(R.string.enter_valid_cvv));
                return submitCHDToOttoPG;
            }
        }

        submitCHDToOttoPG = new SubmitCHDToOttoPG(MerchantId,SessionId,"token",listCards.get(selectedCardPosision).token,cvv);

        return submitCHDToOttoPG;
    }

    private void checkIfcardDetailfill(ItemSavedcardBinding itemBinding1, boolean b) {
        boolean  cvvenable = false;

        if (itemBinding1.cvvTextView.getText().toString().trim().length() >= 3) {
            cvvenable = true;
        } else {
            cvvenable = false;
        }

        if ( cvvenable == true) {
            activity.setPayEnable(true);
        } else {
            activity.setPayEnable(false);
        }
    }

}
