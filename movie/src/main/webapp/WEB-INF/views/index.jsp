<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
    <meta charset="UTF-8">
    <title> 무봐? 시큐리티 테스트 홈 화면 </title>
</head>
<body>
<h2> Security Test </h2>
<ul>
    ${nowMember}
    ${sessionId}

    <li><a href="/">홈 화면 </a></li>
    <li><a href="/movie">영화 </a></li>
    <li><a href="/inquiry">문의 </a></li>
    <li><a href="/mypage">마이 페이지 </a></li>
    <li><a href="/admin">관리자 화면 </a></li>
    <li><a href="/login-form"> 로그인 </a></li>
    <li><a href="/join-form"> 회원가입 </a></li>
    <li><a href="/logout-form">로그아웃 </a></li>
</ul>
</body>
</html>