<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="items" scope="request" type="java.util.List<mate.academy.internetshop.model.Item>"/>
<html>
<head>
    <title>Items</title>
</head>
<body>
<table border="3">
    <tr>
        Items:
    </tr>
    <tr>
        <td>ID</td>
        <td>Name</td>
        <td>Price</td>
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
            <td>
                <a href="${pageContext.request.contextPath}/addItemToBucket?item_id=${item.itemId}">Add</a>
            </td>
            <td>
                <a href="${pageContext.request.contextPath}/deleteItem?item_id=${item.itemId}">Delete</a>
            </td>
        </tr>
    </c:forEach>
</table>
<br>
<a href="${pageContext.request.contextPath}/bucket">Show bucket</a>
</br>
<br>
<a href="${pageContext.request.contextPath}/index">Back to main</a>
</br>
</body>
</html>
