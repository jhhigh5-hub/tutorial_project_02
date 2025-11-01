<%--
  Created by IntelliJ IDEA.
  User: LG
  Date: 25. 10. 30.
  Time: 오전 11:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="/css/board.css">

</head>
<body>
<%@ include file="/template/main-top.jspf" %>
<main>
<div class="header-line"></div>
    <div class="container">
        <div class="band-left">
            <div class="band-images">

            </div>
            <div class="band-name">${band.bandName}</div>
            <div class="band-inform">
                <i class="fa-solid fa-crown"></i> 방장: ${band.createMaster}&ensp;
                <i class="fa-solid fa-user-group"></i> 멤버수: ${band.memberCnt}
            </div>
            <button class="create-post-btn" id="create-post-btn" onclick="this">글쓰기</button>
        </div>
        <div class="band-main">
            <div class="create-post-box">
                <form action="/band/board" method="post">
                    <!-- 현재 밴드 번호를 숨겨서 보냄 -->
                    <input type="hidden" name="bandNo" value="${band.no}">
                    <!-- 게시글 제목 입력 필드 추가 -->
                    <input type="text" name="title" placeholder="#태그 검색" required value="">
                    <textarea name="content" placeholder="글 내용을 입력하세요." rows="20"></textarea>
                    <div class="hashtag-group">
                        <input class="hashtag" type="text" name="tag-input" placeholder="" value="${band.category}">
                        <div class="post-actions">
                            <button type="submit" class="btn">게시</button>
                        </div>
                    </div>
                </form>
            </div>
            <div class="bord">
                <c:choose>
                    <c:when test="${not empty postsList}">
                        <c:forEach var="post" items="${postsList}">
                            <div class="post-item">
                                <div class="post-header">
                                    <i class="fa-solid fa-circle-user profile"></i>
                                    <span class="post-writer">${post.writerId}</span> &nbsp;
                                    <span class="post-date">${fn:replace(post.wroteAt, 'T', ' ')}</span>
                                </div>
                                <div class="post-content">
                                        ${post.content}
                                </div>
                                <c:if test="${not empty post.hashtag}">
                                    <div class="post-hashtag">#${post.hashtag}</div>
                                </c:if>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <p class="no-posts">아직 게시글이 없습니다. 첫 글을 작성해보세요!</p>
                    </c:otherwise>
                </c:choose>
                <c:if test="${lastPage > 1}">
                    <div class="pagination">
                        <c:forEach var="i" begin="1" end="${lastPage}">
                            <c:choose>
                                <c:when test="${i == currentPage}">
                                    <span class="current-page">${i}</span>
                                </c:when>
                                <c:otherwise>
                                    <a href="/band/board?no=${band.no}&page=${i}" class="page-link">${i}</a>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
</main>
<script>
    document.addEventListener('DOMContentLoaded', function() {
        const createPostBtn = document.getElementById('create-post-btn');
        const contentTextarea = document.querySelector('textarea[name="content"]'); // content라는 name을 가진 textarea

        if (createPostBtn && contentTextarea) {
            createPostBtn.addEventListener('click', function() {
                contentTextarea.focus(); 
            });
        }
    });
</script>

</body>
</html>
