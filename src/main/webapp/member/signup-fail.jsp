<%--
  Created by IntelliJ IDEA.
  User: leejihee
  Date: 2025. 10. 28.
  Time: 12:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
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
                회원가입
            </div>
            <div class="signup-id">
                <input type="text" placeholder="아이디" name="id" id="id" onkeyup="updateButtonState()">
                <c:if test="${idERR != null && idERR != ''}">
                    <span class="error">${idERR}</span>
                </c:if>
                <c:if test="${idFormatERR != null && idFormatERR != ''}">
                    <span class="error">${idFormatERR}</span>
                </c:if>
            </div>
            <div class="signup-pw">
                <input type="password" placeholder="비밀번호" name="pw" id="pw">
                <c:choose>
                    <c:when test="${pwFormatERR != null && pwFormatERR != ''}">
                        <p class="error">${pwFormatERR}</p>
                    </c:when>
                    <c:otherwise>
                        <p class="pw-title">6자 이상 대소문자, 숫자를 사용하세요.</p>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="signup-email">
                <input type="text" placeholder="이메일" name="email" id="email">
                <c:if test="${emailFormatERR != null && emailFormatERR != ''}">
                    <span class="error">${emailFormatERR}</span>
                </c:if>
            </div>
            <div class="signup-name">
                <input type="text" placeholder="이름" name="name" id="name">
            </div>
            <div class="signup-nickname">
                <input type="text" placeholder="닉네임" name="nickname" id="nickname">
            </div>
            <c:if test="${nicknameERR != null && nicknameERR != ''}">
                <span class="error">${nicknameERR}</span>
            </c:if>
            <button class="signup-btn">
                <p>회원가입하기</p>
            </button>
        </form>
    </div>
</main>
</body>
</html>
