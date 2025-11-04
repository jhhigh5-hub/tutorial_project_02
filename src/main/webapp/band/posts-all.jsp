<%--
  Created by IntelliJ IDEA.
  User: leejihee
  Date: 25. 11. 4.
  Time: 오전 10:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="/css/main-top.css">
</head>
<body>
<%@ include file="/template/main-top.jspf" %>
<main>
    <h1>내 글 전체 모아보기</h1>
    <c:choose>
        <c:when test="${empty myPosts}">
            <p>아직 작성한 게시글이 없어요</p>
        </c:when>
        <c:otherwise>
            <ul class="post-list">
                <c:forEach var="post" items="${myPosts}">
                        <li class="post-list-item">
                            <a href="/band/board?no=${post.bandNo}" class="post-item-link">
                                <span class="band-name">[${post.bandName}]</span>
                                <span class="post-content">${post.content}</span>
                                <small class="post-date">${post.wroteAt}</small>
                            </a>
                        </li>
                </c:forEach>
            </ul>
        </c:otherwise>
    </c:choose>
</main>
</body>
</html>
