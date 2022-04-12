
package ottu.payment.model.fetchTxnDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class RespoFetchTxnDetail {

    private String amount;
    private ApplePayConfig applePayConfig;
    private Object attachment;
    private List<Card> cards = null;
    private Object checkout_short_url;
    private String checkoutUrl;
    private String currencyCode;
    private String customerEmail;
    private String customerFirstName;
    private String customerId;
    private String customerLastName;
    private String customerPhone;
    private List<Object> emailRecipients = null;
    private Extra extra;
    private Object initiatorId;
    private String language;
    private String mode;
    private Notifications notifications;
    private String operation;
    private Object orderNo;
    private ArrayList<PaymentMethod> payment_methods = new ArrayList<>();
    private List<String> pgCodes = null;
    private String redirectUrl;
    private String sessionId;
    private String state;
    private String submitUrl;
    private String type;
    private String vendorName;
    private String webhookUrl;
    private Boolean applePayAvailable;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public ApplePayConfig getApplePayConfig() {
        return applePayConfig;
    }

    public void setApplePayConfig(ApplePayConfig applePayConfig) {
        this.applePayConfig = applePayConfig;
    }

    public Object getAttachment() {
        return attachment;
    }

    public void setAttachment(Object attachment) {
        this.attachment = attachment;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public Object getCheckoutShortUrl() {
        return checkout_short_url;
    }

    public void setCheckoutShortUrl(Object checkout_short_url) {
        this.checkout_short_url = checkout_short_url;
    }

    public String getCheckoutUrl() {
        return checkoutUrl;
    }

    public void setCheckoutUrl(String checkoutUrl) {
        this.checkoutUrl = checkoutUrl;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerFirstName() {
        return customerFirstName;
    }

    public void setCustomerFirstName(String customerFirstName) {
        this.customerFirstName = customerFirstName;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerLastName() {
        return customerLastName;
    }

    public void setCustomerLastName(String customerLastName) {
        this.customerLastName = customerLastName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public List<Object> getEmailRecipients() {
        return emailRecipients;
    }

    public void setEmailRecipients(List<Object> emailRecipients) {
        this.emailRecipients = emailRecipients;
    }

    public Extra getExtra() {
        return extra;
    }

    public void setExtra(Extra extra) {
        this.extra = extra;
    }

    public Object getInitiatorId() {
        return initiatorId;
    }

    public void setInitiatorId(Object initiatorId) {
        this.initiatorId = initiatorId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Notifications getNotifications() {
        return notifications;
    }

    public void setNotifications(Notifications notifications) {
        this.notifications = notifications;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Object getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Object orderNo) {
        this.orderNo = orderNo;
    }

    public ArrayList<PaymentMethod> getPaymentMethods() {
        return payment_methods;
    }

    public void setPaymentMethods(ArrayList<PaymentMethod> payment_methods) {
        this.payment_methods = payment_methods;
    }

    public List<String> getPgCodes() {
        return pgCodes;
    }

    public void setPgCodes(List<String> pgCodes) {
        this.pgCodes = pgCodes;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSubmitUrl() {
        return submitUrl;
    }

    public void setSubmitUrl(String submitUrl) {
        this.submitUrl = submitUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getWebhookUrl() {
        return webhookUrl;
    }

    public void setWebhookUrl(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    public Boolean getApplePayAvailable() {
        return applePayAvailable;
    }

    public void setApplePayAvailable(Boolean applePayAvailable) {
        this.applePayAvailable = applePayAvailable;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
