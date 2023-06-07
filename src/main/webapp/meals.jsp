<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<br>
<table border="1px" cellspacing="0" cellpadding="3">
    <tr>
        <th>Date & Time</th>
        <th>Description</th>
        <th>Calories</th>
        <th></th>
        <th></th>
    </tr>
    <c:forEach var="meal" items="${mealTos}">
        <tr style="color: ${meal.excess ? 'crimson' : 'green'}">
            <td align="center" width="150px">
                <fmt:parseDate value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm"
                               var="parsedDateTime"
                               type="both"/>
                <fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${parsedDateTime}"/></td>
            <td width="250px" align="center">${meal.description}</td>
            <td width="100px" align="center">${meal.calories}</td>
            <td width="80px" align="center">
                <c:url var="updateButton" value="meals">
                    <c:param name="action" value="update"/>
                    <c:param name="id" value="${meal.id}"/>
                </c:url>
                <input type="button" value="Update"
                       onclick="window.location.href = '${updateButton}'"/>
            </td>

            <td width="80px" align="center">
                <c:url var="deleteButton" value="meals">
                    <c:param name="action" value="delete"/>
                    <c:param name="id" value="${meal.id}"/>
                </c:url>
                <input type="button" value="Delete"
                       onclick="window.location.href = '${deleteButton}'"/>
            </td>
        </tr>
    </c:forEach>
</table>
<br>
<c:url var="addButton" value="meals">
    <c:param name="action" value="create"/>
</c:url>
<input type="button" value="ADD MEAL" onclick="window.location.href = '${addButton}'"/>
</body>
</html>