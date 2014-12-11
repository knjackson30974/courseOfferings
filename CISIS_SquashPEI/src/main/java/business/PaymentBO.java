package business;

import beans.Payment;
import database.PaymentDAO;
import java.util.ArrayList;

/**
 * This class will handle the processing associated with a business object.
 *
 * @author BJ
 * @since 20140611
 */
public class PaymentBO {

    public static ArrayList<Payment> getPayments(int memberId) {
        return PaymentDAO.getPayments(memberId);
        
    }

    public static void insertPayment(Payment payment) throws Exception {
        PaymentDAO.insertPayment(payment);
    }

    public static void deletePayment(int paymentId) throws Exception {
        PaymentDAO.deletePayment(paymentId);
    }

}
