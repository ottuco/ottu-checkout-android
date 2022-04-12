package ottu.payment.model.DeleteCard;

public class SendDeleteCard {
    String type;
    String customer_id;

    public SendDeleteCard(String type, String customer_id) {
        this.type = type;
        this.customer_id = customer_id;
    }
}
