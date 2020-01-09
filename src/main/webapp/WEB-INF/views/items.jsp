<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Items</title>
</head>
<body>
Items:
<table border="3">
    <tr>
        <td>ID</td>
        <td>Name</td>
        <td>Price</td>
    </tr>

    <jsp:useBean id="items" scope="request" type="java.util.List<mate.academy.internetshop.model.Item>"/>
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
                <a href="addItemToBucket?item_id=${item.itemId}">Add</a>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
