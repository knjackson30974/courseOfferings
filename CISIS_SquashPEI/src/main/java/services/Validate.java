//package services;
//
//import business.AccessBO;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javax.jws.WebService;
//import javax.jws.WebMethod;
//import javax.jws.WebParam;
//
///**
// * This will be a web service which provides validation.
// *
// * @author BJ
// * @since 20141103
// */
//@WebService(serviceName = "Validate")
//public class Validate {
//
//    /**
//     * This is a sample web service operation
//     */
//    @WebMethod(operationName = "validate")
//    public String validate(@WebParam(name = "username") String username, @WebParam(name = "password") String password) {
//        try {
//            return String.valueOf(AccessBO.validate(username, password));
//        } catch (Exception ex) {
//            System.out.println("Error validating");
//            return "error";
//        }
//    }
//}
