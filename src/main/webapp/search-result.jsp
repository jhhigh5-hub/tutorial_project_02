<%--
  Created by IntelliJ IDEA.
  User: leejihee
  Date: 25. 11. 3.
  Time: 오전 10:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="/css/main-page.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
</head>
<body>
<%@ include file="/template/main-top.jspf" %>
<h3>밴드 검색 결과</h3>
<c:choose>
    <c:when test="${not empty bands}">
        <ul>
            <c:forEach var="band" items="${bands}">
                <li><a href="/band/board?no=${band.no}">${band.bandName}</a>
                    (멤버${band.memberCnt}명)</li>
            </c:forEach>
        </ul>
    </c:when>
    <c:otherwise>
        <p>검색된 밴드가 없습니다.</p>
    </c:otherwise>
</c:choose>

<h3>게시글 검색 결과</h3>
<c:choose>
    <c:when test="${not empty posts}">
        <ul>
            <c:forEach var="post" items="${posts}">
                <li><a href="posts/detail?no=${post.no}">${post.hashtag != null ? '#' + post.hashtag : '게시글'}: ${post.content}</a><br>
                    작성자: ${post.writerId}, 작성일: ${post.wroteAt}
                </li>
            </c:forEach>
        </ul>
    </c:when>
    <c:otherwise>
        <p>검색된 게시글이 없습니다.</p>
    </c:otherwise>
</c:choose>
</body>
</html>
