<%--
  Created by IntelliJ IDEA.
  User: LG
  Date: 25. 10. 29.
  Time: 오후 12:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="/css/main-page.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
</head>
<body>
<header>
    <div class="top-head">
        <div class="top-left-box">
            <div class="top-logo">
                <a href=""><img src="/img/band-b-logo.png" alt=""></a>
            </div>
            <div class="search-box">
                <input type="text" placeholder="밴드, 페이지, 게시글 검색">
                <i class="fa-solid fa-magnifying-glass"></i>
            </div>
        </div>
        <div class="top-right-box">
            <a href="/member/logout"><button class="top-logout">로그아웃</button></a>
            <div class="topbtn">
                <div class="top-new-post">
                    <a href="">
                        <i class="fa-solid fa-bell"></i>
                    </a>
                </div>
                <div class="top-profile">
                    <a href="">
                        <i class="fa-solid fa-circle-user"></i>
                    </a>
                </div>
            </div>

        </div>
    </div>
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
                <a href="/band/new">
                <div class="newbox">
                    <div class="plus-circle">
                        <i class="fa-solid fa-plus"></i>
                    </div>
                    <p>만들기</p>
                </div>
                </a>
                <div class="newbox">
                    <div class="plus-circle">
                        <i class="fa-solid fa-plus"></i>
                    </div>
                    <p>만들기</p>
                </div>
                <div class="newbox">
                    <div class="plus-circle">
                        <i class="fa-solid fa-plus"></i>
                    </div>
                    <p>만들기</p>
                </div>
                <div class="newbox">
                    <div class="plus-circle">
                        <i class="fa-solid fa-plus"></i>
                    </div>
                    <p>만들기</p>
                </div>
            </div>
        </div>
    </div>
    </div>
</main>
</body>
</html>
