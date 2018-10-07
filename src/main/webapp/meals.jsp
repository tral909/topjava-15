<%--
  Created by IntelliJ IDEA.
  User: Roman
  Date: 07.10.2018
  Time: 20:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>meals</title>
</head>
<body>
    <table border="1" cellspacing="0" cellpadding="5">
        <tr>
            <th>datetime</th>
            <th>description</th>
            <th>calories</th>
        </tr>
        <c:forEach items="${meals}" var="meal">
            <tr style="color: ${meal.exceed eq true ? "red" : "green"};">
                <td>${meal.formattedDateTime()}</td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
