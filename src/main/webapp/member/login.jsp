<%--
  Created by IntelliJ IDEA.
  User: LG
  Date: 25. 10. 28.
  Time: 오후 5:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="/css/signup.css">
</head>
<header>
    <div class="top-logo">
        <a href=""><img src="/img/band-b-logo.png" alt=""></a>
    </div>
</header>
<main>
    <div class="content">
        <form action="/member/signup" method="post">
            <div class="con-title">
                로그인
            </div>
            <div class="login-id">
                <input type="text" placeholder="아이디" name="id" id="id" onkeyup = "updateButtonState()" >
            </div>
            <div class="login-pw">
                <input type="password" placeholder="비밀번호" name="pw" id="pw">
            </div>
            <button class="login-btn">
                <p>로그인하기</p>
            </button>
        </form>
    </div>
</main>
</body>
</html>
