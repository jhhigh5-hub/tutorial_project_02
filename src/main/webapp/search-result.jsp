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
    <link rel="stylesheet" href="/css/search-result.css">

</head>
<body>
<%@ include file="/template/main-top.jspf" %>
<main>
    <div class="content">
        <!-- 밴드 검색 결과 -->
        <h3 class="con-title">밴드 검색 결과</h3>
        <c:choose>
            <c:when test="${not empty bands}">
                <ul class="search-list">
                    <c:forEach var="band" items="${bands}">
                        <li class="search-item">
                            <a href="/band/board?no=${band.no}" class="search-link">${band.bandName}</a>
                            <span class="search-info">멤버 ${band.memberCnt}명</span>
                        </li>
                    </c:forEach>
                </ul>
            </c:when>
            <c:otherwise>
                <p class="no-result">검색된 밴드가 없습니다.</p>
            </c:otherwise>
        </c:choose>

        <!-- 게시글 검색 결과 -->
        <h3 class="con-title">게시글 검색 결과</h3>
        <c:choose>
            <c:when test="${not empty posts}">
                <ul class="search-list">
                    <c:forEach var="post" items="${posts}">
                        <li class="search-item">
                            <a href="/band/board?no=${post.bandNo}" class="search-link">
                                    ${post.hashtag != null ? ('#' ) : ''}${post.hashtag != null ? post.hashtag : '게시글'}
<%--                            ${post.hashtag != null ? '#'+post.hashtag : '게시글'}: ${post.content}--%>
                            </a>
                            <span class="search-info">작성자: ${post.writerId}</span>
                            <span class="search-info">작성일: ${post.wroteAt}</span>
                        </li>
                    </c:forEach>
                </ul>
            </c:when>
            <c:otherwise>
                <p class="no-result">검색된 게시글이 없습니다.</p>
            </c:otherwise>
        </c:choose>
    </div>
</main>

</body>
</html>
