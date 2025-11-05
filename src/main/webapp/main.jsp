<%--
  Created by IntelliJ IDEA.
  User: LG
  Date: 25. 10. 29.
  Time: 오후 12:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="/css/main-page.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">

</head>
<body>
<header>
    <div class="top-head">
        <div class="top-left-box">
            <div class="top-logo">
                <a href=""><img src="/img/band-b-logo.png" alt=""></a>
            </div>
            <div class="search-box">
                <form action="/search" method="get">
                    <input type="text" placeholder="밴드, 페이지, 게시글 검색" name="keyword">
                    <i class="fa-solid fa-magnifying-glass"></i>
                </form>
            </div>
        </div>
        <div class="top-right-box">
            <a href="/member/logout">
                <button class="top-logout">로그아웃</button>
            </a>
            <div class="topbtn">
                <div class="top-new-post">
                    <a href="/band/posts-all" class="notify-link tooltip-trigger" data-tooltip="내 글 전체보기">
                        <i class="fa-solid fa-bell"></i>
                    </a>
                </div>
                <div class="top-profile">
                    <a href="" class="tooltip-trigger" data-tooltip="내 정보">
                        <i class="fa-solid fa-circle-user"></i>
                    </a>
                </div>
            </div>
        </div>
    </div>
    <div class="top-head-border"></div>
</header>
<main>
    <div class="quick-link">
        <div class="quick-link-box">
            <div class="quick-link-textbox">
                <a href="https://docs.band.us/d/promotion/krbandguide#section_function">
                    <img src="img/b_cdhUd018svc1mx6nqputv6h3_s6p3uv.png" alt="">
                    <p>내가 찾는 기능 여기 다 있네 😍 궁금한 밴드 기능을 검색해보세요!</p>
                </a>
            </div>
            <a href="https://docs.band.us/d/promotion/krbandguide#section_function">
                <button class="quick-link-btn">바로가기</button>
            </a>
        </div>
    </div>
    <div class="section-1">
        <div class="contain">
            <div class="bandtitle">
                내 밴드
            </div>
            <div class="section-2">
                <div class="newbox">
                    <a href="/band/new">
                        <div class="plus-circle">
                            <i class="fa-solid fa-plus"></i>
                        </div>
                        <p>만들기</p>
                    </a>
                </div>
                <c:choose>
                    <c:when test="${not empty myJoinedBands}">
                        <c:forEach var="banditem" items="${myJoinedBands}">
                            <div class="band-card">
                                <a href="/band/board?no=${banditem.no}">
                                    <p class="band-name">${banditem.bandName}</p>
                                    <div class="band-info">
                                        <p>👑 방장: <span>${banditem.createMaster}</span></p>
                                        <p>👥 멤버수: <span>${banditem.memberCnt}</span></p>
                                    </div>
                                </a>

                                <c:if test="${banditem.createMaster eq sessionScope.logonUser.id}">
                                    <form action="/band/delete" method="post" class="delete-form"
                                          onsubmit="return confirm('밴드를 삭제하시겠습니까?');">
                                        <input type="hidden" name="postNo" value="${banditem.no}">
                                        <input type="hidden" name="bandNo" value="${banditem.no}">
                                        <button type="submit" class="delete-btn">
                                            <i class="fa-solid fa-trash-can"></i><!-- 쓰레기통 아이콘 -->
                                        </button>
                                    </form>
                                </c:if>

                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div class="band-item-card">
                            <p>아직 가입한 밴드가 없어요.</p>
                            <p>새로운 밴드를 만들거나 찾아보세요!</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
    <div class="section-2">
        <div class="contain">
            <div class="bandtitle">전체 밴드</div>
            <div class="section-2">
                <c:choose>
                    <c:when test="${not empty allBand}">
                        <c:forEach var="band" items="${allBand}">
                            <c:set var="joined" value="false"/>
                            <c:forEach var="jb" items="${myJoinedBands}">
                                <c:if test="${jb.no == band.no}">
                                    <c:set var="joined" value="true"/>
                                </c:if>
                            </c:forEach>

                            <c:if test="${not joined}">
                            <!-- 아직 가입하지 않은 밴드만 표시 -->
                            <div class="band-card">
                                <a href="/band/board?no=${band.no}">
                                    <p class="band-name">${band.bandName}</p>
                                    <div class="band-info">
                                        <p>👑 방장: ${band.createMaster}</p>
                                        <p>👥 멤버수: ${band.memberCnt}</p>
                                    </div>
                                </a>
                            </div>
                        </c:if>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div class="band-item-card">
                            <p>등록된 밴드가 없습니다.</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</main>
<script>



</script>

</body>
</html>
