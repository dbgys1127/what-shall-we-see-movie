<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
    <meta charset="UTF-8">
    <title> 무봐? 시큐리티 테스트 홈 화면 </title>
</head>
<body>
<h2> 전체회원 </h2>
<form action = "/admin/member/search" method="get">
    <table style="border: 1px solid black;" >
        <th>이메일</th>
        <th>생성일</th>
        <th>경고수</th>
        <th>회원상태</th>
        <c:forEach var="member" items="${members}">
            <tr>
                <td> <a href="/admin/member/warning?email=${member.email}">${member.email}</a></td>
                <td>${member.createdAt}</td>
                <td>${member.warningCard}</td>
                <td>${member.memberStatus}</td>
            </tr>
        </c:forEach>
    </table>
    <input type = "text" name="email" placeholder = "검색할 회원을 입력하세요."/>
    <button type="submit">검색</button>
</form>
</body>
</html>