package Ottu.model.StcPayOtp;

public class StcOtpResponce {
    public String form_of_payment;
    public String status;
    public String message;
    public String session_id;
    public String order_no;
    public String reference_number;
    public String redirect_url;
    public PaymentGatewayInfo payment_gateway_info;
    public String detail;


    public void setForm_of_payment(String form_of_payment){
        this.form_of_payment = form_of_payment;
    }
    public String getForm_of_payment(){
        return this.form_of_payment;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public String getStatus(){
        return this.status;
    }
    public void setMessage(String message){
        this.message = message;
    }
    public String getMessage(){
        return this.message;
    }
    public void setSession_id(String session_id){
        this.session_id = session_id;
    }
    public String getSession_id(){
        return this.session_id;
    }
    public void setOrder_no(String order_no){
        this.order_no = order_no;
    }
    public String getOrder_no(){
        return this.order_no;
    }
    public void setReference_number(String reference_number){
        this.reference_number = reference_number;
    }
    public String getReference_number(){
        return this.reference_number;
    }
    public void setRedirect_url(String redirect_url){
        this.redirect_url = redirect_url;
    }
    public String getRedirect_url(){
        return this.redirect_url;
    }
    public void setPayment_gateway_info(PaymentGatewayInfo payment_gateway_info){
        this.payment_gateway_info = payment_gateway_info;
    }
    public PaymentGatewayInfo getPayment_gateway_info(){
        return this.payment_gateway_info;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
