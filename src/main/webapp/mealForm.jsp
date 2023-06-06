<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html>
<body>
<%--@elvariable id="meal" type="ru.javawebinar.topjava.model.Meal"--%>
<c:set var="operation" value="${empty meal.id || meal.id == 0 ? 'Edit' : 'Create' }"/>
<h2> ${operation} meal</h2>

<form method="POST" action='meals' name="addMeal">
    <input type="text" name="id" hidden value="${meal.id}">
    <table width="400px">
        <tr>
            <td width="180px">Date</td>
            <td>
                <input type="datetime-local" name="dateTime"
                       value="${meal.dateTime}"/>
            </td>
        </tr>
        <tr>
            <td width="180px">Description</td>
            <td>
                <input type="text" name="description"
                       value="${meal.description}"/>
            </td>
        </tr>
        <tr>
            <td width="180px">Calories</td>
            <td>
                <input type="number" name="calories"
                       value="${meal.calories}"/>
            </td>
        </tr>
        <tr>
            <td width="180px"></td>
            <td>
                <c:url var="saveButton" value="meals"/>
                <input type="submit" value="Save"/>
                <c:url var="cancelButton" value="meals"/>
                <input type="button" value="Cancel" onclick="window.location.href =
                        '${cancelButton}'"/>
            </td>
        </tr>
    </table>
</form>
</body>
</html>
