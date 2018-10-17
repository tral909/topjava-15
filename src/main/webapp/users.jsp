<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Users</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>Users</h2>
<form action="users" method="get">
    <select name="user_id">
        <option value="1">Tony (id=1)</option>
        <option value="2">Smoke (id=2)</option>
<%--        <c:forEach items="${users}" var="user">
            <option value="${user.id}">${user.name + user.email}</option>
        </c:forEach>--%>
    </select>
    <input type="submit" value="Authorize"/>
</form>
</body>
</html>