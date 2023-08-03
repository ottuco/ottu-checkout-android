package Ottu.model.StcPayOtp;

public class StcOtpPayload {
    String session_id;
    String otp;

    public StcOtpPayload(String session_id, String otp) {
        this.session_id = session_id;
        this.otp = otp;
    }
}
