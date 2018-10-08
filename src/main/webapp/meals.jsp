<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="f" uri="http://mycompany.com/functions" %>
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
                <td>${f:formatLocalDateTime(meal.dateTime, "yyyy-MM-dd HH:mm")}</td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
            </tr>
        </c:forEach>
    </table>
    <h2>Add new meal</h2>
    <form method="post" action="meals">
        <p><label>Введите дату и время: <input type="datetime-local" name="datetime" required/></label></p>
        <p><label>Введите описание: <input type="text" name="description" required/></label></p>
        <p><label>Введите кол-во калорий: <input type="number" min="0" max="10000" name="calories" required/></label></p>
        <p><input type="submit" value="Добавить"></p>
    </form>
</body>
</html>