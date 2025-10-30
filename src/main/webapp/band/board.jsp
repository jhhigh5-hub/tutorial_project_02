<%--
  Created by IntelliJ IDEA.
  User: LG
  Date: 25. 10. 30.
  Time: 오전 11:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="/css/board.css">

</head>
<body>
<%@ include file="/template/main-top.jspf" %>
<div class="header-line"></div>
<main>
    <div class="container">
        <div class="band-left">
            <div class="band-images"></div>
            <div class="band-name"></div>
            <div class="band-cnt"></div>
            <button class="create-post-btn">글쓰기</button>
        </div>
        <div class="band-main">
            <div class="create-post-box">
                <form action="/band/board" method="post">
                    <!-- 현재 밴드 번호를 숨겨서 보냄 -->
                    <input type="hidden" name="bandNo" value="">
                    <!-- 게시글 제목 입력 필드 추가 -->
                    <input type="text" name="title" placeholder="#태그 검색" required>
                    <textarea name="content" placeholder="글 내용을 입력하세요." rows="50"></textarea>
                    <div class="post-actions">
                        <!-- '등록하기' div를 submit 버튼으로 변경! -->
                        <button type="submit" class="btn">게시</button>
                    </div>
                </form>
            </div>
            <div class="bord"></div>
        </div>
    </div>
</main>


</body>
</html>
