<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <meta charset="UTF-8">
    <title> 무봐? 시큐리티 테스트 홈 화면 </title>
</head>
<body>
<h2> 회원가입 화면 </h2>
<form action="/join" method="post">
    email: <input type="text" name="email" />
    password: <input type="text" name="password" />
    <button type="submit">회원가입</button>
</form>
</body>
</html>