package Ottu.model.fetchTxnDetail;

public class PaymentMethod{
    public String code;
    public String name;
    public String pg;
    public String type;
    public String amount;
    public String currency_code;
    public String fee;
    public String icon;
    public String flow;
    public String submit_url ="";
    public String cancel_url;
    public boolean can_save_card = false;
    public String public_key_url;
    public String redirect_url;
}