package database;

import beans.Member;
import exceptions.PasswordException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import util.ConnectionUtils;
import util.DbUtils;

/**
 *
 * @author BJ
 */
public class MemberDAO {

    public static void addMember(Member member) throws Exception {

        System.out.println("inserting a new member");
        PreparedStatement ps = null;
        String sql = null;
        Connection conn = null;
        conn = ConnectionUtils.getConnection();
        try {
            sql = "SELECT max(member_id) FROM member_access";

            ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            int nextMESequence = 1;
            if (rs.next()) {
                nextMESequence = rs.getInt(1) + 1;
            }
            member.setMemberId(nextMESequence);
            sql = "INSERT INTO member "
                    + "  (`member_id`, `user_id`, `organization_type`, `status_type`, "
                    + "   `user_type`, `created_date_time`, `created_user_id`, `updated_date_time`, `updated_user_id`) "
                    + "  VALUES (?, ?, ?, ?, ?,"
                    + "  sysdate(), ?, sysdate(), ?)";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, nextMESequence);
            ps.setString(2, member.getNewUserId());
            ps.setInt(3, 1);
            ps.setInt(4, 1);
            ps.setInt(5, member.getUserType());
            ps.setString(6, member.getUserId());
            ps.setString(7, member.getUserId());
            ps.executeUpdate();

            sql = "INSERT INTO member_access "
                    + "  (member_id, user_id, password, access_string, "
                    + "   created_date_time, created_user_id, updated_date_time, updated_user_id) "
                    + "  VALUES (?, ?, ?, ?,"
                    + "  sysdate(), ?, sysdate(), ?)";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, nextMESequence);
            ps.setString(2, member.getNewUserId());
            ps.setString(3, "rboard");
            ps.setString(4, "11111111");
            ps.setString(5, member.getUserId());
            ps.setString(6, member.getUserId());
            ps.executeUpdate();

//            sql = "INSERT INTO member_employment(member_id, "
//                    + "employment_status_code, currency_code, practice_via_telephone_ind, "
//                    + "practice_via_internet_ind, practice_in_person_ind, practice_jurisdictions) "
//                    + "VALUES (?, null, null, 0, 0, 0, null)";
//
//            ps = conn.prepareStatement(sql);
//            ps.setInt(1, nextMESequence);
//            ps.executeUpdate();
            sql = "INSERT INTO member_bio (member_id, first_name, middle_name, last_name, email_address) "
                    + "VALUES (?,?,?,?,?)";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, nextMESequence);
            ps.setString(2, member.getFirstName());
            ps.setString(3, member.getMiddleName());
            ps.setString(4, member.getLastName());
            ps.setString(5, member.getEmailAddress());
            ps.executeUpdate();

        } catch (Exception e) {
            String errorMessage = e.getMessage();
            e.printStackTrace();
            throw e;
        } finally {
            DbUtils.close(ps, conn);
        }
        return;
    }

    public static void changePassword(int memberId, String password, String userId) throws PasswordException {

        System.out.println("updating member");
        PreparedStatement psMember = null;
        String sql = null;
        Connection conn = null;

        /*
         * Setup the sql to update or insert the row (based on update flag).
         */
        try {
            conn = ConnectionUtils.getConnection();

            sql = "UPDATE member_access "
                    + "SET password=?,"
                    + "    updated_user_id = ?, "
                    + "    updated_date_time = sysdate() "
                    + "WHERE member_id = ?";

            psMember = conn.prepareStatement(sql);
            psMember.setString(1, password);
            psMember.setString(2, userId);
            psMember.setInt(3, memberId);

            psMember.executeUpdate();

        } catch (Exception e) {
            String errorMessage = e.getMessage();
            e.printStackTrace();
            throw new PasswordException("Error");
        } finally {
            DbUtils.close(psMember, conn);
        }

        return;
    }

    public static void deleteMember(int memberId, String updatedUserId) {

        System.out.println("deleting member");
        PreparedStatement psMember = null;
        String sql = null;
        Connection conn = null;

        /*
         * Setup the sql to update or insert the row (based on update flag).
         */
        try {
            conn = ConnectionUtils.getConnection();

            sql = "UPDATE member "
                    + "SET status_type=2,"
                    + "    updated_user_id = ?, "
                    + "    updated_date_time = sysdate() "
                    + "WHERE member_id = ?";

            psMember = conn.prepareStatement(sql);
            psMember.setString(1, updatedUserId);
            psMember.setInt(2, memberId);
            psMember.executeUpdate();

        } catch (Exception e) {
            String errorMessage = e.getMessage();
            e.printStackTrace();
        } finally {
            DbUtils.close(psMember, conn);
        }
    }

    public static String getAllActiveMembersEmails() {
        String emails = "";
        boolean first = true;
        for (Member member : getAllActiveMembers()) {
            if (!first) {
                emails += "; ";
            }
            first = false;
            emails += member.getEmailAddress();
        }
        return emails;
    }

    /**
     * This method will return all members that have dobs which start with the
     * current year and month.
     *
     * @return the list of members with birthdays in the current year/month.
     * @since 201411127
     * @author BJ MacLean
     */
    public static ArrayList<String> getActiveMembersForBirthday() {
        ArrayList<Member> allMembers = getAllActiveMembers();
        ArrayList<String> birthdayMembers = new ArrayList();

        SimpleDateFormat formatter = new SimpleDateFormat("MM");
        String today = formatter.format(new java.util.Date());
        System.out.println("Comparing for month=" + today);

        for (Member nextMember : allMembers) {
            try {
                System.out.println("Birthday start=" + nextMember.getDateOfBirth().substring(5));
                if (nextMember.getDateOfBirth().length() > 6) {
                    if (nextMember.getDateOfBirth().substring(5).startsWith(today)) {
                        birthdayMembers.add(nextMember.getFirstName() + " " + nextMember.getLastName());
                    }
                }
            } catch (Exception e) {
                System.out.println("There was an exception in the dob");
            }
        }
        return birthdayMembers;
    }

    public static ArrayList<Member> getAllActiveMembers() {

        PreparedStatement ps = null;
        String sql = null;
        Connection conn = null;

        ArrayList<Member> members = new ArrayList();
        try {
            conn = ConnectionUtils.getConnection();

            sql = "SELECT * FROM member_bio mb, member m where "
                    + "m.status_type = 1 and m.member_id = mb.member_id";

            ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Member newMember = new Member();
                newMember.setMemberId(rs.getInt("member_id"));
                newMember.setFirstName(rs.getString("first_name"));
                newMember.setMiddleName(rs.getString("middle_name"));
                newMember.setLastName(rs.getString("last_name"));
                newMember.setAddressLine1(rs.getString("address_1"));
                newMember.setAddressLine2(rs.getString("address_2"));
                newMember.setMunicipality(rs.getString("municipality"));
                newMember.setProvinceCode(rs.getInt("province_code"));
                newMember.setPostalCode(rs.getString("postal_code"));
                newMember.setHomePhone(rs.getString("home_phone"));
                newMember.setWorkPhone(rs.getString("work_phone"));
                newMember.setWorkPhoneExtension(rs.getString("work_phone_extension"));
                newMember.setFax(rs.getString("fax_number"));
                newMember.setEmailAddress(rs.getString("email_address"));
                newMember.setDateOfBirth(rs.getString("date_of_birth"));
                newMember.setGenderCode(rs.getInt("gender_code"));
                members.add(newMember);
            }
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            e.printStackTrace();
        } finally {
            DbUtils.close(ps, conn);
        }
        return members;
    }

    public static Member getMemberByUserid(String userId) {
        PreparedStatement ps = null;
        String sql = null;
        Connection conn = null;

        int memberId = 0;
        try {
            conn = ConnectionUtils.getConnection();

            sql = "SELECT member_id FROM member WHERE user_id = ?";

            ps = conn.prepareStatement(sql);
            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                memberId = (rs.getInt("member_id"));
                System.out.println("found member for " + userId + " the member id=" + memberId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error encountered getting member by user id");
        }

        return getMember(String.valueOf(memberId));
    }

    public static boolean checkUserid(String userId) {
        System.out.println("###accessing check ");
        PreparedStatement ps = null;
        String sql = null;
        Connection conn = null;

        boolean exists = false;
        try {
            conn = ConnectionUtils.getConnection();

            sql = "SELECT * FROM member WHERE user_id like ?";

            ps = conn.prepareStatement(sql);
            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()!= false) {
                System.out.println("#### made it to the results exists area");
                exists = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error encountered getting member by user id");
        }

        return exists;
    }

    public static Member getMember(String memberId) {
        PreparedStatement ps = null;
        String sql = null;
        Connection conn = null;
        Member newMember = new Member();
        newMember.setMemberId(Integer.parseInt(memberId));
        try {
            conn = ConnectionUtils.getConnection();

            sql = "SELECT * FROM member WHERE member_id = " + memberId;

            ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                newMember.setUserType(rs.getInt("user_type"));
                newMember.setUserId(rs.getString("user_id"));
            }

            sql = "SELECT * FROM member_bio WHERE member_id = " + memberId;

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                newMember.setMemberId(Integer.parseInt(memberId));
                newMember.setFirstName(rs.getString("first_name"));
                newMember.setMiddleName(rs.getString("middle_name"));
                newMember.setLastName(rs.getString("last_name"));
                // newMember.setSalutationCode(rs.getInt("salutation_code"));
                newMember.setAddressLine1(rs.getString("address_1"));
                newMember.setAddressLine2(rs.getString("address_2"));
                newMember.setMunicipality(rs.getString("municipality"));
                newMember.setProvinceCode(rs.getInt("province_code"));
                newMember.setPostalCode(rs.getString("postal_code"));
                newMember.setHomePhone(rs.getString("home_phone"));
                newMember.setWorkPhone(rs.getString("work_phone"));
                newMember.setWorkPhoneExtension(rs.getString("work_phone_extension"));
                newMember.setFax(rs.getString("fax_number"));
                newMember.setEmailAddress(rs.getString("email_address"));
                newMember.setDateOfBirth(rs.getString("date_of_birth"));
                newMember.setGenderCode(rs.getInt("gender_code"));
            }
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            e.printStackTrace();
        } finally {
            DbUtils.close(ps, conn);
        }
        return newMember;
    }

    /**
     * This method will update.
     *
     * @return
     * @author BJ
     * @since 20131202
     */
    public static void updateMember(Member member) throws Exception {
        //Have to update the member based on member id.

        System.out.println("updating member");
        PreparedStatement psMember = null;
        String sql = null;
        Connection conn = null;

        /*
         * Setup the sql to update or insert the row (based on update flag).
         */
        try {
            conn = ConnectionUtils.getConnection();

            sql = "UPDATE member_bio "
                    + "SET first_name=?,middle_name=?,last_name=?,work_phone=?,work_phone_extension=?,email_address=?"
                    + "WHERE member_id = ?";

            psMember = conn.prepareStatement(sql);
            psMember.setString(1, member.getFirstName());
            psMember.setString(2, member.getMiddleName());
            psMember.setString(3, member.getLastName());
            psMember.setString(4, member.getWorkPhone());
            psMember.setString(5, member.getWorkPhoneExtension());

            psMember.setString(6, member.getEmailAddress());
            psMember.setInt(7, member.getMemberId());

//            } else {
//                //Have to insert the new member.
//
//                sql = "INSERT INTO member(user_type, last_name, first_name, password, email, "
//                        + "phone_cell, phone_home, phone_work,"
//                        + " address, status, membership_type, member_id, membership_date) "
//                        + " values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
//
//                psMember = conn.prepareStatement(sql);
//                psMember.setInt(1, memberForm.getUserType());
//                psMember.setString(2, memberForm.getLastName());
//                psMember.setString(3, memberForm.getFirstName());
//                psMember.setString(4, memberForm.getPassword());
//                psMember.setString(5, memberForm.getEmail());
//                psMember.setString(6, memberForm.getPhoneCell());
//                psMember.setString(7, memberForm.getPhoneHome());
//                psMember.setString(8, memberForm.getPhoneWork());
//                psMember.setString(9, memberForm.getAddress());
//                psMember.setInt(10, memberForm.getStatus());
//                psMember.setInt(11, memberForm.getMemberShipType());
//                psMember.setInt(12, memberForm.getNewMemberId());
//                psMember.setDate(13, null);
//
//            }
            psMember.executeUpdate();

        } catch (Exception e) {
            String errorMessage = e.getMessage();
            e.printStackTrace();
            throw e;
        } finally {
            DbUtils.close(psMember, conn);
        }

    }
}
