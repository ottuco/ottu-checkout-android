package Ottu.model.StcPayOtp;

public class PaymentGatewayInfo {
    private String pg_code;

    private String pg_name;

    private Pg_response pg_response;

    public void setPg_code(String pg_code){
        this.pg_code = pg_code;
    }
    public String getPg_code(){
        return this.pg_code;
    }
    public void setPg_name(String pg_name){
        this.pg_name = pg_name;
    }
    public String getPg_name(){
        return this.pg_name;
    }
    public void setPg_response(Pg_response pg_response){
        this.pg_response = pg_response;
    }
    public Pg_response getPg_response(){
        return this.pg_response;
    }
}