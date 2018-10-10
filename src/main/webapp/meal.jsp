<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="f" uri="http://mycompany.com/functions" %>
<html>
<head>
    <title>Еда</title>
</head>
<body>
<h2>Изменить еду</h2>
<%--id: ${meal.id}<br>
Дата и время: ${f:formatLocalDateTime(meal.dateTime, "yyyy-MM-dd HH:mm")}<br>
Описание: ${meal.description}<br>
Калории: ${meal.calories}<br>--%>
<form method="post" action="${pageContext.servletContext.contextPath}/meals?action=put">
    <input name="id" value="${meal.id}" hidden/>
    <p><label>Введите дату и время: <input type="datetime-local" name="datetime" value="${meal.dateTime}" required/></label></p>
    <p><label>Введите описание: <input type="text" name="description" value="${meal.description}" required/></label></p>
    <p><label>Введите кол-во калорий: <input type="number" min="0" max="10000" name="calories" value="${meal.calories}" required/></label></p>
    <p><input type="submit" value="Изменить"></p>
</form>
</body>
</html>
