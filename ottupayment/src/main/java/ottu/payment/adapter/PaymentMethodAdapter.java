package ottu.payment.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.style.ReplacementSpan;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ottu.payment.R;
import ottu.payment.databinding.ItemPaymentMethodBinding;
import ottu.payment.model.GenerateToken.CreatePaymentTransaction;
import ottu.payment.model.redirect.PaymentMethod;
import ottu.payment.model.redirect.ResponceFetchTxnDetail;
import ottu.payment.model.submitCHD.Card_SubmitCHD;
import ottu.payment.ui.PaymentActivity;
import ottu.payment.util.ImageLoader;

import static ottu.payment.ui.PaymentActivity.Amount;
import static ottu.payment.ui.PaymentActivity.savedCardSelected;
import static ottu.payment.util.Util.listCardPatter;

public class PaymentMethodAdapter extends RecyclerView.Adapter<PaymentMethodAdapter.ViewHolder>{

    ArrayList<ottu.payment.model.redirect.PaymentMethod> listPaymentMethod;
    ResponceFetchTxnDetail transactionDetail;
    private ItemPaymentMethodBinding binding;
    ItemPaymentMethodBinding itemBinding1;
    PaymentActivity context;
    ImageLoader imageLoader ;
    public static int selectedCardPos = -1;
    int lastSeected = -1;
    SparseArray<Pattern> mCCPatterns = listCardPatter();
    final Pattern CODE_PATTERN = Pattern.compile("([0-9]{0,4})|([0-9]{4}-)+|([0-9]{4}-[0-9]{0,4})+");
    String a;
    int keyDel;
    private boolean internalStopFormatFlag;


    public PaymentMethodAdapter(PaymentActivity paymentActivity, ResponceFetchTxnDetail cards) {
        context = paymentActivity;
        listPaymentMethod = cards.payment_methods;
        imageLoader = new ImageLoader(paymentActivity);
        transactionDetail = cards;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemPaymentMethodBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.bindData(listPaymentMethod.get(position),position);

    }

    @Override
    public int getItemCount() {
        return listPaymentMethod.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {

        ItemPaymentMethodBinding itemBinding;
        public ViewHolder(ItemPaymentMethodBinding itemView) {
            super(itemView.getRoot());
            itemBinding = itemView;

        }

        public void bindData(PaymentMethod paymentMethod, int position) {

            if (selectedCardPos == position){
                itemBinding.layoutCardInfo.setBackgroundColor(context.getResources().getColor(R.color.text_gray7));
            }else {
                itemBinding.layoutCardInfo.setBackgroundColor(context.getResources().getColor(R.color.white));
            }
            itemBinding.cardNumber.setText(listPaymentMethod.get(position).name);
            itemBinding.amount.setText(listPaymentMethod.get(position).amount+" "+listPaymentMethod.get(position).currency_code);

//            itemBinding.cardImage.setImageBitmap(getImage(listPaymentMethod.get(position).icon));
//            imageLoader.DisplayImage(listPaymentMethod.get(position).icon, itemBinding.cardImage);
            Glide.with(context).load(listPaymentMethod.get(position).icon).into(itemBinding.cardImage);

            itemBinding.cardNumberTextView.addTextChangedListener(new TextWatcher() {
                boolean considerChange = false;

                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {




                    if (considerChange)
                    {
                        int mDrawableResId = 0;
                        for (int n = 0; n < mCCPatterns.size(); n++) {
                            int key = mCCPatterns.keyAt(n);
                            // get the object by the key.
                            Pattern p = mCCPatterns.get(key);
                            Matcher m = p.matcher(charSequence);
                            if (m.find()) {
                                mDrawableResId = key;
                                break;
                            }
                        }
                        if (mDrawableResId > 0 && mDrawableResId != 0) {
                            binding.cardIndicatorImage.setImageDrawable(context.getResources().getDrawable(mDrawableResId));
                        }
                    }
                    considerChange = !considerChange;
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() > 0 && !CODE_PATTERN.matcher(s).matches()) {
                        String input = s.toString();
                        String numbersOnly = keepNumbersOnly(input);
                        String code = formatNumbersAsCode(numbersOnly);

                        itemBinding.cardNumberTextView.removeTextChangedListener(this);
                        itemBinding.cardNumberTextView.setText(code);
                        // You could also remember the previous position of the cursor
                        itemBinding.cardNumberTextView.setSelection(code.length());
                        itemBinding.cardNumberTextView.addTextChangedListener(this);
                    }
                    if (s.length() > 14){
                        itemBinding1.cardNumberErrorTextView.setText("");
                    }
                }
            });
            itemBinding.datetextView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int start, int removed, int added) {


                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (internalStopFormatFlag) {
                        return;
                    }
                    internalStopFormatFlag = true;
                    formatExpiryDate(editable, 4);
                    internalStopFormatFlag = false;
                    if (editable.length() > 4){
                        itemBinding1.expiredateErrorTextView.setText("");
                    }

                    if (editable.length() > 1){
                    if (Integer.parseInt(String.valueOf(editable.charAt(0))) > 1 || Integer.parseInt(String.valueOf(editable.subSequence(0,2))) > 12){
                        itemBinding1.expiredateErrorTextView.setText("Month is wrong");
                    }else {
                        itemBinding1.expiredateErrorTextView.setText("");
                    }
                    }

                }
            });


            GestureDetector gestureDetector = new GestureDetector(context, new SingleTapConfirm());

            itemBinding.layoutCardInfo.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
//                    switch(event.getAction())
//                    {
//                        case MotionEvent.ACTION_DOWN:
//                            itemBinding.layoutCardInfo.setBackgroundColor(context.getResources().getColor(R.color.text_gray7));
//                            break;
//                        case MotionEvent.ACTION_UP:
//                            itemBinding.layoutCardInfo.setBackgroundColor(context.getResources().getColor(R.color.white));
//                            break;
//                    }
                    if (gestureDetector.onTouchEvent(event)) {
                        if (position == 0) {
                            selectedCardPos = position;
                            if (itemBinding.layoutCardDetail.getVisibility() == View.GONE) {
                                itemBinding1 = null;
                                itemBinding1 = itemBinding;
                                itemBinding.layoutCardDetail.setVisibility(View.VISIBLE);
                                context.setPayEnable(true);
                            } else {
                                selectedCardPos = -1;
                                itemBinding1 = null;
                                itemBinding.layoutCardDetail.setVisibility(View.GONE);
                                context.setPayEnable(false);
                            }
                        }else if (position == 1){
                            if (itemBinding1 != null) {
                                itemBinding1.layoutCardDetail.setVisibility(View.GONE);
                            }
                            itemBinding1 = null;
                            selectedCardPos = position;
                            context.setPayEnable(true);


                        }else if (position == 2){
                            if (itemBinding1 != null) {
                                itemBinding1.layoutCardDetail.setVisibility(View.GONE);
                            }
                            itemBinding1 = null;
                            selectedCardPos = position;
                            context.setPayEnable(true);

//                            CreatePaymentTransaction paymentTransaction = new CreatePaymentTransaction(transactionDetail.type
//                                    ,transactionDetail.pg_codes
//                                    ,Amount
//                                    ,listPaymentMethod.get(position).currency_code
//                                    ,transactionDetail.redirect_url.replace("redirected","disclose_ok")
//                                    ,transactionDetail.redirect_url
//                                    ,transactionDetail.customer_id
//                                    ,"1");
//                            context.createTrx(paymentTransaction,transactionDetail.pg_codes.get(position));
                        }
                        notifyDataSetChanged();
                        savedCardSelected = false;
                        context.notifySavedCardAdapter();
                        // single tap
                        return true;
                    }
                    return true;
                }
            });
        }
    }

    private Bitmap getImage(String icon) {
        Bitmap bitmap = null;
        InputStream input = null;
        try {
            input = new URL(icon).openStream();
            bitmap = BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;

    }

    public Card_SubmitCHD getCardData(){


        DateFormat month = new SimpleDateFormat("MM");
        DateFormat year = new SimpleDateFormat("yy");
        Date time = new Date();
        Card_SubmitCHD submitCHD = null;

        if (itemBinding1 == null){
            return submitCHD;
        }

        if (selectedCardPos == -1){
            return submitCHD;
        }else {

            String name = listPaymentMethod.get(selectedCardPos).name;
            String cardNumber = itemBinding1.cardNumberTextView.getText().toString().trim().replace("-","");
            String date = itemBinding1.datetextView.getText().toString().trim();
//            String[] time = date.split("/");


            String cvv = itemBinding1.cvvTextView.getText().toString().trim();
            boolean saveCard = itemBinding1.saveCard.isChecked();

            if (cardNumber.equals("") || cardNumber.length() < 15 ){
                itemBinding1.cardNumberErrorTextView.setText(context.getText(R.string.card_number_not_correct));
                return submitCHD;
            }
            if (date.equals("") ||date.length() < 4){
                itemBinding1.expiredateErrorTextView.setText(context.getText(R.string.expire_data_notbe_past));
                return submitCHD;
            }
            String expiryMonth = date.substring(0,2);
            String expiryYear = date.substring(2);
            String innput = expiryMonth+"/"+expiryYear;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/yy");
            simpleDateFormat.setLenient(false);
            Calendar cal= Calendar.getInstance();
            cal.set(Calendar.MONTH,Integer.parseInt(expiryMonth));
            cal.set(Calendar.YEAR,Integer.parseInt("20"+expiryYear));



            boolean expired = cal.before(Calendar.getInstance());
            if (expired){
                itemBinding1.expiredateErrorTextView.setText(context.getText(R.string.expire_data_notbe_past));
                return submitCHD;
            }else if (Integer.parseInt(String.valueOf(date.charAt(0))) > 1){
                itemBinding1.expiredateErrorTextView.setText("Month is wrong");
            }else {
                itemBinding1.expiredateErrorTextView.setText("");
            }


            if (cvv.equals("") ||cvv.length() < 3){
                itemBinding1.expiredateErrorTextView.setText("Enter valid cvv");
                return submitCHD;
            }

            submitCHD = new Card_SubmitCHD(name,cardNumber,expiryMonth,expiryYear,Integer.parseInt(cvv),saveCard);
            return submitCHD;
        }
    }


    private String keepNumbersOnly(CharSequence s) {
        return s.toString().replaceAll("[^0-9]", ""); // Should of course be more robust
    }

    private String formatNumbersAsCode(CharSequence s) {
        int groupDigits = 0;
        String tmp = "";
        for (int i = 0; i < s.length(); ++i) {
            tmp += s.charAt(i);
            ++groupDigits;
            if (groupDigits == 4) {
                tmp += "-";
                groupDigits = 0;
            }
        }
        return tmp;
    }

    public static void formatExpiryDate(@NonNull Editable expiryDate, int maxLength) {
        int textLength = expiryDate.length();
        // first remove any previous span
        SlashSpan[] spans = expiryDate.getSpans(0, expiryDate.length(), SlashSpan.class);
        for (int i = 0; i < spans.length; ++i) {
            expiryDate.removeSpan(spans[i]);
        }
        // then truncate to max length
        if (maxLength > 0 && textLength > maxLength - 1) {
            expiryDate.replace(maxLength, textLength, "");
            --textLength;
        }
        // finally add margin spans
        for (int i = 1; i <= ((textLength - 1) / 2); ++i) {
            int end = i * 2 + 1;
            int start = end - 1;
            SlashSpan marginSPan = new SlashSpan();
            expiryDate.setSpan(marginSPan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }
    public static class SlashSpan extends ReplacementSpan {

        public SlashSpan() {}

        @Override
        public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
            float[] widths = new float[end - start];
            float[] slashWidth = new float[1];
            paint.getTextWidths(text, start, end, widths);
            paint.getTextWidths("/", slashWidth);
            int sum = (int) slashWidth[0];
            for (int i = 0; i < widths.length; ++i) {
                sum += widths[i];
            }
            return sum;
        }

        @Override
        public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
            String xtext = "/" + text.subSequence(start, end);
            canvas.drawText(xtext, 0, xtext.length(), x, y, paint);
        }
    }

    private class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            return true;
        }
    }
}

