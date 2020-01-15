<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
</head>
<body>
<div>${errorMsg}</div>
<form action="${pageContext.request.contextPath}/login" method="post">
    <label for="login">Login</label>
    <input type="text" placeholder="login" id="login" name="login">

    <label for="password">Password</label>
    <input type="password" placeholder="password" id="password" name="password">

    <button type="submit"> Log In </button>
</form>
<br>
<a href="${pageContext.request.contextPath}/registration">Register User</a>
</br>
</body>
</html>
