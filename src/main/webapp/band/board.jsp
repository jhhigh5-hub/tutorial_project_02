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
            <div class="create-post-box"></div>
            <div class="bord"></div>
        </div>
    </div>
</main>


</body>
</html>
