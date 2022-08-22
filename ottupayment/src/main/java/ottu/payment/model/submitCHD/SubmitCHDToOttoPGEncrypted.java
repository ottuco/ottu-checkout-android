package ottu.payment.model.submitCHD;

public class SubmitCHDToOttoPGEncrypted {
    private String merchant_id;

    private String session_id;

    private String payment_method;
    private String token;
    private String cvv;

    private String card;

    public SubmitCHDToOttoPGEncrypted(String merchant_id, String session_id, String payment_method, String card) {
        this.merchant_id = merchant_id;
        this.session_id = session_id;
        this.payment_method = payment_method;
        this.card = card;
    }

    public SubmitCHDToOttoPGEncrypted(String merchantId, String  session_id , String payment_method, String token, String cvv) {
        this.merchant_id = merchantId;
        this.session_id = session_id;
        this.payment_method = payment_method;
        this.token = token;
        this.cvv = cvv;
    }

    public void setMerchant_id(String merchant_id){
        this.merchant_id = merchant_id;
    }
    public String getMerchant_id(){
        return this.merchant_id;
    }
    public void setSession_id(String session_id){
        this.session_id = session_id;
    }
    public String getSession_id(){
        return this.session_id;
    }
    public void setPayment_method(String payment_method){
        this.payment_method = payment_method;
    }
    public String getPayment_method(){
        return this.payment_method;
    }
    public void setCard(String card){
        this.card = card;
    }
    public String getCard(){
        return this.card;
    }
}