<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="items" scope="request" type="java.util.List<mate.academy.internetshop.model.Item>"/>
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
                <button type="button" class="ui-button">Add</button>
            </td>
        </tr>
    </c:forEach>
</table>


</body>
</html>
