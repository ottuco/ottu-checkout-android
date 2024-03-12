package Ottu.model.fetchTxnDetail;

public class PaymentService extends PaymentMethod{
    // ApplePay & GooglePay
    public String merchant_id;
    public String country_code;

    // ApplePay only
    public String domain;
    public String shop_name;
    public String validation_url;
    public String session_id;

    // GooglePay only
    public String environment;
    public String gateway;
    public String gateway_merchant_id;
    public String merchant_name;
    public String total_price;
}
