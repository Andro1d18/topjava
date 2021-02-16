<%--
  Created by IntelliJ IDEA.
  User: andro1d
  Date: 14.02.2021
  Time: 13:38
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Edit meal</title>
</head>
<body>
<form method="POST" action='meals' name="frmAddMeal">
    Meal ID : <input type="text" readonly="readonly" name="mealid"
                     value="<c:out value="${meal.id}" />"/> <br/>
    Descriptin : <input type="text" name="description"
                        value="<c:out value="${meal.description}" />"/> <br/>
    Calories : <input type="text" name="calories"
                      value="<c:out value="${meal.calories}" />"/> <br/>
    Date : <input type="text" name="date"
                  value="${meal.dateTime}"/> <br/>
    <input type="submit" value="Submit"/>
</form>

<ul>
    <li><a href="users">Users</a></li>
    <li><a href="meals">Meals</a></li>
</ul>
</body>
</html>
