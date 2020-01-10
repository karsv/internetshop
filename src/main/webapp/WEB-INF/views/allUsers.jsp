<%--
  Created by IntelliJ IDEA.
  User: afterlie
  Date: 1/10/20
  Time: 1:11 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Users</title>
</head>
<body>
<table border="3">
    <tr>
        Users:
    </tr>
    <tr>
        <td>ID</td>
        <td>login</td>
    </tr>

    <jsp:useBean id="users" scope="request" type="java.util.List<mate.academy.internetshop.model.User>"/>
    <c:forEach var="user" items="${users}">
        <tr>
            <td>
                <c:out value="${user.userId}"/>
            </td>
            <td>
                <c:out value="${user.name}"/>
            </td>
            <td>
                <a href="${pageContext.request.contextPath}/deleteUser?user_id=${user.userId}">Delete</a>
            </td>
        </tr>
    </c:forEach>
</table>
<br>
<a href="${pageContext.request.contextPath}/index"/> Back to main </a>
</br>
</body>
</html>
