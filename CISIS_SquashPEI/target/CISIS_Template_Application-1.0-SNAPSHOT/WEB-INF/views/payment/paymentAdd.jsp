<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<form action="payment" method="post">
    <spring:nestedPath path="payment">
        <h2><fmt:message key="label.payment"/></h2>
        <table>
            <tr>
                <td>
                    <label class="alignCenter"  for="paymentSeasonCode">
                        <strong><fmt:message key="label.season" /></strong></label>
                        <form:select path="paymentSeasonCode">
                            <form:options items="${sessionScope.seasons}" itemLabel="description" itemValue="codeValueSequence"/>
                        </form:select>
                </td>
            </tr>

            <tr>
                <td><label class="alignCenter"  for="paymentAmount">
                        <fmt:message key="label.payment.amount" /></label><br />
                        <form:input  path="paymentAmount"/>
                </td>
            </tr>

            <tr>
                <td><label class="alignCenter"  for="paymentDetail">
                        <fmt:message key="label.payment.detail" /></label><br />
                        <form:input  path="paymentDetail"/>
                </td>
            </tr>
            
            <tr/>
            <tr align="center">
                <td colspan="2">
                    <input type="submit" value=<fmt:message key="label.submit"/>
                </td>
            </tr>
        </table>

    </div>
</spring:nestedPath>
</html:form>
