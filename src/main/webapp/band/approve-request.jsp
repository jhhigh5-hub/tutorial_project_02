<%--
  Created by IntelliJ IDEA.
  User: leejihee
  Date: 25. 11. 3.
  Time: 오전 11:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%@ include file="/template/main-top.jspf" %>
<div class="header-line"></div>
<main>
    <h2>${band.bandName} 가입 요청 관리</h2>
    <c:choose>
        <c:when test="${not empty pendingRequests}">
            <table border="1">
                <thead>
                <tr>
                    <th>신청 회원 ID</th>
                    <th>신청 일시</th>
                    <th>상태</th>
                    <th>처리</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="request" items="${pendingRequests}">
                    <tr>
                        <td>${request.memberId}</td>
                        <td>${request.joinAt}</td>
                        <td>${request.joinStatus}</td>
                        <td>
                            <form action="/band/join-request-manage" method="post" style="display:inline;">
                                <input type="hidden" name="idx" value="${request.idx}">
                                <input type="hidden" name="bandNo" value="${band.no}">
                                <input type="hidden" name="action" value="approve">
                                <button type="submit" class="btn btn-success">승인</button>
                            </form>
                            <form action="/band/join-request-manage" method="post" style="display:inline;">
                                <input type="hidden" name="idx" value="${request.idx}">
                                <input type="hidden" name="bandNo" value="${band.no}">
                                <input type="hidden" name="action" value="reject">
                                <button type="submit" class="btn btn-danger">거절</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:when>
        <c:otherwise>
            <p>현재 대기 중인 가입 요청이 없습니다.</p>
        </c:otherwise>
    </c:choose>
    <a href="/band/board?no=${band.no}">밴드 페이지로 돌아가기</a>
</main>
</body>
</html>
