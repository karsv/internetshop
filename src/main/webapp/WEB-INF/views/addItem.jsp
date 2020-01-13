<%--
  Created by IntelliJ IDEA.
  User: afterlie
  Date: 1/13/20
  Time: 11:26 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add Item</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/addItem" method="post">
    <label for="name">Login</label>
    <input type="text" placeholder="Item's name" id="name" name="name">

    <label for="cost">Cost</label>
    <input type="cost" placeholder="Item's cost" id="cost" name="cost">

    <button type="submit"> Add item </button>
</form>
</body>
</html>
