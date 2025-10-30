<%--
  Created by IntelliJ IDEA.
  User: leejihee
  Date: 25. 10. 29.
  Time: 오후 8:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="/css/main-top.css">
    <link rel="stylesheet" href="/css/main-page.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
</head>
<body>
<%@ include file="/template/main-top.jspf" %>
<main>
    <div class="content">
        <form action="/band/new" method="post">
            <div class="con-title">
                밴드 생성
            </div>
            <div class="signup-id">
                <input type="text" placeholder="밴드 이름 입력" name="bandName" id="bandName">
            </div>
            <div class="signup-pw">
                <div>
                    <label for="category">어떤 관심사로 시작할까요?</label>
                </div>
                <div>
                    <select id="category" name="category">
                        <option value="hobby">취미, 동호회</option>
                        <option value="family">가족</option>
                        <option value="school-club">학교, 동아리</option>
                        <option value="company-team">회사, 팀</option>
                        <option value="exercise">운동 모임</option>
                        <option value="game">게임</option>
                        <option value="study">스터디</option>
                        <option value="fan-group">팬밴드</option>
                        <option value="etc">기타</option>
                    </select>
                </div>
            </div>
            <button>밴드 만들기</button>
        </form>
    </div>
</main>
</body>
</html>
