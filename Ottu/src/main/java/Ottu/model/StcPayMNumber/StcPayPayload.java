package Ottu.model.StcPayMNumber;

public class StcPayPayload {
    public String pg_code;
    public String session_id;
    public String customer_phone;
    public boolean save_card;

    public StcPayPayload(String pg_code, String session_id, String customer_phone, boolean save_card) {
        this.pg_code = pg_code;
        this.session_id = session_id;
        this.customer_phone = customer_phone;
        this.save_card = save_card;
    }

    public void setPg_code(String pg_code) {
        this.pg_code = pg_code;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public void setCustomer_phone(String customer_phone) {
        this.customer_phone = customer_phone;
    }

    public void setSave_card(boolean save_card) {
        this.save_card = save_card;
    }
}
