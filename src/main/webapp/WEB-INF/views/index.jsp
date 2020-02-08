<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <title>
        Index
    </title>
</head>
<body>
<br>
<a href="${pageContext.request.contextPath}/servlet/addItem">Add Item</a>
</br>
<br>
<a href="${pageContext.request.contextPath}/registration">Register User</a>
</br>
<br>
<a href="${pageContext.request.contextPath}/login">Login</a>
</br>
<br>
<a href="${pageContext.request.contextPath}/logout">Logout</a>
</br>
<br>
<a href="${pageContext.request.contextPath}/servlet/allUsers">All users</a>
</br>
<br>
<a href="${pageContext.request.contextPath}/servlet/items">Show items</a>
</br>
<br>
<a href="${pageContext.request.contextPath}/servlet/userOrders">Show Orders</a>
</br>
</body>
</html>