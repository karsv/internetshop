<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Bucket</title>
</head>
<body>
<br>
    <a href="items">Go back to items</a>
</br>
<table border="5">
    <tr>
        <td>Id</td>
        <td>Name</td>
        <td>Price</td>
    </tr>
    <jsp:useBean id="bucketItems" scope="request" type="java.util.List<mate.academy.internetshop.model.Item>"/>
    <c:forEach var="item" items="${bucketItems}">
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
                <a href="deleteItemFromBucket?item_id=${item.itemId}">Delete</a>
            </td>
        </tr>
    </c:forEach>
</table>
<br>
<a href="order">Place Order</a>
</br>
<br>
<a href="index">Back to main</a>
</br>
</body>
</html>
