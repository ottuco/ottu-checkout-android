package ottu.payment.model.redirect;

import java.util.ArrayList;

public class ResponceFetchTxnDetail {
    public String amount;
    public ApplePayConfig apple_pay_config;
    public Object attachment;
    public ArrayList<Card> cards;
    public Object checkout_short_url;
    public String checkout_url;
    public String currency_code;
    public String customer_email;
    public String customer_first_name;
    public String customer_id;
    public String customer_last_name;
    public String customer_phone;
    public ArrayList<Object> email_recipients;
    public Extra extra;
    public Object initiator_id;
    public String language;
    public String mode;
    public Notifications notifications;
    public String operation;
    public Object order_no;
    public ArrayList<PaymentMethod> payment_methods;
    public ArrayList<String> pg_codes;
    public String redirect_url;
    public String session_id;
    public String state;
    public String submit_url;
    public String type;
    public String vendor_name;
    public String webhook_url;
    public boolean apple_pay_available;
}
