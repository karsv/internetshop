<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="items" scope="request" type="java.util.List<mate.academy.internetshop.model.Item>"/>
<jsp:useBean id="order_id" scope="request" type="java.lang.Long"/>
<jsp:useBean id="amount" scope="request" type="java.math.BigDecimal"/>
<html>
<head>
    <title>Order</title>
</head>
<br>
<table border="1">
    <tr>
        <td>Order ID: ${order_id}</td>
    </tr>
    <c:forEach var="item" items="${items}">
        <tr>
            <td>
                <c:out value="${item.itemId}"/>
            </td>
            <td>
                <c:out value="${item.name}"/>
            </td>
            <td>
                <c:out value="${item.price}"/>
            </td>
        </tr>
    </c:forEach>
    <tr>
        <td>Amount: ${amount}</td>
    </tr>
</table>
<br>
<a href="${pageContext.request.contextPath}/index">Back to main</a>
</br>
</body>
</html>
