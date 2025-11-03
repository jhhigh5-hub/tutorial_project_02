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
            <div class="band-action">
                <c:choose>
                    <c:when test="${userBandStatus == 'MASTER'}">
                        <a href="${pageContext.request.contextPath}/band/join-request-manage?no=${band.no}" class="btn btn-warning">회원 관리</a>
                    </c:when>
                    <c:when test="${userBandStatus == 'JOINED'}">
                        <button class="create-post-btn" id="create-post-btn" onclick="writeHandle();">글쓰기</button>
                    </c:when>
                    <c:when test="${userBandStatus == 'PENDING'}">
                        <button class="btn btn-secondary" disabled>가입 신청 대기중</button>
                    </c:when>
                    <c:when test="${userBandStatus == 'NONE_JOINED' || userBandStatus == 'REJECTED'}">
                        <form action="/band/join-request" method="post" onsubmit="joinHandle(event);">
                            <input type="hidden" value="${band.no}" name="bandNo"/>
                            <button type="submit" class="btn btn-success" id="joinBandBtn" >
                                밴드 가입 신청
                            </button>
                        </form>

                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/member/login" class="btn btn-info">로그인하고 가입 신청하기</a>
                    </c:otherwise>
                </c:choose>
            </div>

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
                                <!-- 게시글 내용 -->
                                <div class="post-header">${post.writerId}</div>
                                <div class="post-content">${post.content}</div>

                                <!-- 댓글 리스트 -->
                                <c:if test="${not empty post.comments}">
                                    <div class="comment-list">
                                        <c:forEach var="comment" items="${post.comments}">
                                            <div class="comment-item">
                                                    ${comment.writerId} : ${comment.content}
                                                <span class="comment-date">${comment.commentedAt}</span>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </c:if>

                                <!-- 댓글 작성 폼 -->
                                <c:if test="${auth}">
                                    <form action="/band/comment" method="post" class="comment-form">
                                        <input type="hidden" name="bandNo" value="${band.no}">
                                        <input type="hidden" name="postNo" value="${post.no}">
                                        <textarea name="content" placeholder="댓글을 입력하세요"></textarea>
                                        <button type="submit">등록</button>
                                    </form>
                                </c:if>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <p class="no-posts">아직 게시글이 없습니다. 첫 글을 작성해보세요!</p>
                    </c:otherwise>
                </c:choose>



                <!-- 페이지네이션 -->
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



    function writeHandle() {
        const contentTextarea = document.querySelector('textarea[name="content"]');
        contentTextarea.focus();
    }

    function joinHandle(event) {
        if(!window.confirm("가입신청하시겠습니까?"))  {
            event.preventDefault();
        }
    }

</script>

</body>
</html>
