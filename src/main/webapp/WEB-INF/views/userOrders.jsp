<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>User Orders</title>
</head>
<body>
<table border="3">
    <tr>
        Items:
    </tr>
    <tr>
        <td>ID</td>
        <td>Amount</td>
    </tr>
    <jsp:useBean id="orders" scope="request" type="java.util.List<mate.academy.internetshop.model.Order>"/>
    <c:forEach var="order" items="${orders}">
        <tr>
            <td>
                <c:out value="${order.orderId}"/>
            </td>
            <td>
                <c:out value="${order.amount}"/>
            </td>
            <td>
                <a href="deleteOrder?order_id=${order.orderId}">Delete</a>
            </td>
        </tr>
    </c:forEach>
</table>
<br>
<a href="${pageContext.request.contextPath}/index">Back to main</a>
</br>
</body>
</html>
