package beans;

/**
 * This action form will contain information about a notification in the system.
 * 
 * @author bjmaclean
 */
public class Payment {
    private int memberId;
    private String userId;
    private int paymentId;
    private String paymentAmount;
    private String paymentDetail;
    private String paymentSeasonCode;
    private String paymentSeasonCodeDescription;

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentDetail() {
        return paymentDetail;
    }

    public void setPaymentDetail(String paymentDetail) {
        this.paymentDetail = paymentDetail;
    }

    public String getPaymentSeasonCode() {
        return paymentSeasonCode;
    }

    public void setPaymentSeasonCode(String paymentSeasonCode) {
        this.paymentSeasonCode = paymentSeasonCode;
    }

    public String getPaymentSeasonCodeDescription() {
        return paymentSeasonCodeDescription;
    }

    public void setPaymentSeasonCodeDescription(String paymentSeasonCodeDescription) {
        this.paymentSeasonCodeDescription = paymentSeasonCodeDescription;
    }

    @Override
    public String toString() {
        return "Payment{" + "memberId=" + memberId + ", userId=" + userId + ", paymentAmount=" + paymentAmount + ", paymentDetail=" + paymentDetail + ", paymentSeasonCode=" + paymentSeasonCode + ", paymentSeasonCodeDescription=" + paymentSeasonCodeDescription + '}';
    }

    
    
}
