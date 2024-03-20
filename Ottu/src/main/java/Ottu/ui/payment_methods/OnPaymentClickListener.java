package Ottu.ui.payment_methods;

import Ottu.model.fetchTxnDetail.PaymentMethod;

public interface OnPaymentClickListener {

    void onPaymentClicked(PaymentMethod method);

}
