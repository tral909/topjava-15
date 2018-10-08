<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="f" uri="http://mycompany.com/functions" %>
<html>
<head>
    <title>meal</title>
</head>
<body>
<h2>Meal</h2>
id: ${meal.id}<br>
date and time: ${f:formatLocalDateTime(meal.dateTime, "yyyy-MM-dd HH:mm")}<br>
description: ${meal.description}<br>
calories: ${meal.calories}<br>
<a href="${pageContext.servletContext.contextPath}/meals">back</a>
</body>
</html>
