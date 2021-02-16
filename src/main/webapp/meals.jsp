<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <title>Meals</title>
    <style type="text/css">
        td.green {
            color: lightgreen;
        }

        td.red {
            color: red;
        }
        tr.red{ color: red;}
        tr.green{ color: lightgreen;}
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h3><a href="users">Users</a></h3>
<hr>
<h2>Meals</h2>
<table border="1" cellspacing="1" cellpadding="5">
    <thead>
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th>Upd</th>
        <th>Del</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="mealTo" items="${testMealToList}">
        <c:if test="${mealTo.excess == true}">
            <tr class="red">
                <td>${mealTo.date} ${mealTo.time}</td>
                <td>${mealTo.description}</td>
                <td>${mealTo.calories}</td>
                <td><a href="meals?action=edit&mealId=<c:out value="${mealTo.id}"/>">Update</a></td>
                <td><a href="meals?action=delete&mealId=<c:out value="${mealTo.id}"/>">Delete</a></td>
            </tr>
        </c:if>
        <c:if test="${mealTo.excess == false}">
            <tr class="green">
                <td>${mealTo.date} ${mealTo.time}</td>
                <td>${mealTo.description}</td>
                <td>${mealTo.calories}</td>
                <td><a href="meals?action=edit&mealId=<c:out value="${mealTo.id}"/>">Update</a></td>
                <td><a href="meals?action=delete&mealId=<c:out value="${mealTo.id}"/>">Delete</a></td>
            </tr>
        </c:if>
    </c:forEach>
    </tbody>
</table>
<p><a href="meals?action=insert">Add User</a></p>
</body>
</html>