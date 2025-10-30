<%--
  Created by IntelliJ IDEA.
  User: leejihee
  Date: 2025. 10. 28.
  Time: 12:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="/css/signup.css">
</head>
<body>
<header>
    <div class="top-logo">
        <a href=""><img src="/img/band-b-logo.png" alt=""></a>
    </div>
</header>
<main>
    <div class="content">
        <form action="/member/login" method="post">
            <div class="con-title">로그인</div>
            <!-- 아이디 입력 -->
            <div class="signup-id">
                <input type="text" placeholder="아이디" name="id" id="id">
            </div>
            <!-- 비밀번호 입력 -->
            <div class="signup-pw">
                <input type="password" placeholder="비밀번호" name="pw" id="pw" onkeyup="updateButtonState()">
            </div>
            <!-- 로그인 상태 유지 -->
            <div class="keepLogin">
                <div class="keepLogin-p">
                    <label for="keepLogin">로그인 상태 유지</label>
                </div>
                <div class="checkbox-login">
                    <input type="checkbox" name="keepLogin" id="keepLogin">
                </div>
            </div>
            <!-- 로그인 버튼 -->
            <button type="submit" class="signup-btn" id="loginBt" disabled><p>로그인</p></button>
        </form>
        <!-- 회원가입 안내 -->
        <p class="text-center">
            아직 회원이 아니신가요? <a href="/member/signup" style="color: #222; font-weight: 600">&nbsp;&nbsp; 회원가입</a>
        </p>
    </div>
</main>
<script>
    function keepLoginConfirm(){
        if(document.getElementById("keepLogin").checked){
            document.getElementById("keepLogin").checked = false;
            }
        }

    // 로그인 버튼 활성화/비활성화
    function updateButtonState(){
        const idValue = document.getElementById("id").value;
        const passValue = document.getElementById("pw").value;
        document.getElementById("loginBt").disabled = !(idValue && passValue);
    }


</script>
</body>
</html>
