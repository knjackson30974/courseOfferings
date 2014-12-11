package controller;

import beans.MemberSquash;
import beans.Payment;
import business.PaymentBO;
import forms.Menu;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for the Welcome
 *
 * @author bjmaclean
 */
@Controller
@RequestMapping("payment")
public class PaymentController {

//    @RequestMapping(method = RequestMethod.POST)
//    public String loadMember(ModelMap model) {
//
//        model.addAttribute("memberBio", new Member());
//        return "welcome";
//    }
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView onSubmit(HttpServletRequest request, @ModelAttribute("payment") Payment payment) {
        //pass validation if they enter "TEST" and "TEST"

        MemberSquash loggedInMember = (MemberSquash) request.getSession().getAttribute("loggedInMember");
        MemberSquash currentMember = (MemberSquash) request.getSession().getAttribute("currentMember");

        String informationMessage = "";
        String errorMessage = "";
        String toDelete = request.getParameter("delete");
        if (!(toDelete == null || toDelete.isEmpty())) {
            try {
                PaymentBO.deletePayment(Integer.parseInt(toDelete));
                informationMessage = "Payment deleted";
            } catch (Exception ex) {
                System.out.println("Error deleting payment");
                errorMessage = "Error deleting payment";

            }
        } else {

            System.out.println("About to add a payment (" + payment.getPaymentDetail() + ")");
            try {
                payment.setUserId(loggedInMember.getMember().getUserId());
                payment.setMemberId(currentMember.getMember().getMemberId());
                PaymentBO.insertPayment(payment);
                informationMessage = "Payment added";
            } catch (Exception ex) {
                errorMessage = "Error adding payment";
                System.out.println("Error inserting payment");
            }
        }

        ModelAndView mv;
        mv = new ModelAndView("payment");
        mv.addObject("payments", PaymentBO.getPayments(loggedInMember.getMember().getMemberId()));
        mv.addObject("errorMessage", errorMessage);
        mv.addObject("informationMessage", informationMessage);
        mv.addObject("menu", new Menu());

        return mv;
    }
}
