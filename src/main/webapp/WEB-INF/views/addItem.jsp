<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add Item</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/servlet/addItem" method="post">
    <label for="name">Login</label>
    <input type="text" placeholder="Item's name" id="name" name="name">

    <label for="cost">Cost</label>
    <input type="cost" placeholder="Item's cost" id="cost" name="cost">

    <button type="submit"> Add item </button>
</form>
</body>
</html>
