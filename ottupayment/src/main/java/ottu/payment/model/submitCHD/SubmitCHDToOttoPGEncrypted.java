package ottu.payment.model.submitCHD;

public class SubmitCHDToOttoPGEncrypted {
    private String merchant_id;

    private String session_id;

    private String payment_method;
//    private String token;
//    private String cvv;

    private String card;
    private String secret_id;


    public SubmitCHDToOttoPGEncrypted(String merchant_id, String session_id, String payment_method, String secretId, String encryptedCard) {
        this.merchant_id = merchant_id;
        this.session_id = session_id;
        this.payment_method = payment_method;
        this.card = encryptedCard;
        this.secret_id = secretId;

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