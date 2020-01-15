<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Registration</title>
</head>
<body>
    <form action="${pageContext.request.contextPath}/registration" method="post">
        <label for="login">Login</label>
        <input type="text" placeholder="login" id="login" name="login">

        <label for="password">Password</label>
        <input type="password" placeholder="password" id="password" name="password">

        <label for="passwordRepeat">Repeat Password</label>
        <input type="password" placeholder="Repeat password" id="passwordRepeat" name="passwordRepeat">

        <button type="submit"> Register </button>
    </form>
</body>
</html>
