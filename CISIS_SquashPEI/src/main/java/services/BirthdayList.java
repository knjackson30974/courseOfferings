package services;

import beans.Member;
import database.MemberDAO;
import java.util.ArrayList;
import javax.jws.WebService;
import javax.jws.WebMethod;

/**
 *
 * @author BJ
 */
@WebService(serviceName = "BirthdayList")
public class BirthdayList {

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "getBirthdayMembers")
    public ArrayList<String> getBirthdayMembers() {
        return MemberDAO.getActiveMembersForBirthday();
    }
}
