
package ottu.payment.model.fetchTxnDetail;

import java.util.HashMap;
import java.util.Map;



public class Card {

    private String customerId;
    private String brand;
    private String nameOnCard;
    private String number;
    private int expiry_month;
    private int expiry_year;
    private String token;
    private Boolean preferred;
    private Boolean is_expired;
    private String pg_code;
    private String deleteUrl;
    private String submitUrl;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getNameOnCard() {
        return nameOnCard;
    }

    public void setNameOnCard(String nameOnCard) {
        this.nameOnCard = nameOnCard;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getExpiryMonth() {
        return expiry_month;
    }

    public void setExpiryMonth(int expiry_month) {
        this.expiry_month = expiry_month;
    }

    public int getExpiryYear() {
        return expiry_year;
    }

    public void setExpiryYear(int expiry_year) {
        this.expiry_year = expiry_year;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getPreferred() {
        return preferred;
    }

    public void setPreferred(Boolean preferred) {
        this.preferred = preferred;
    }

    public Boolean getIsExpired() {
        return is_expired;
    }

    public void setIsExpired(Boolean is_expired) {
        this.is_expired = is_expired;
    }

    public String getPgCode() {
        return pg_code;
    }

    public void setPgCode(String pg_code) {
        this.pg_code = pg_code;
    }

    public String getDeleteUrl() {
        return deleteUrl;
    }

    public void setDeleteUrl(String deleteUrl) {
        this.deleteUrl = deleteUrl;
    }

    public String getSubmitUrl() {
        return submitUrl;
    }

    public void setSubmitUrl(String submitUrl) {
        this.submitUrl = submitUrl;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
