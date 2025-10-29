<%--
  Created by IntelliJ IDEA.
  User: leejihee
  Date: 25. 10. 29.
  Time: 오후 1:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="/css/signup.css">
</head>
<body>
<div>
    <h3>로그인 이력</h3>
    <ul>
        <c:forEach items="${list}" var="one">
            <li>${one.memberId} - ${one.loginAt}</li>
        </c:forEach>
    </ul>
</div>
</body>
</html>
