<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <meta charset="UTF-8">
    <title> 무봐? 시큐리티 테스트 홈 화면 </title>
</head>
<body>
<h2> 회원정보 수정 </h2>
<form action="/my-info" method="patch" enctype="multipart/form-data">
    memberImage: <input type="file" name="memberImage"/>
    password: <input type="text" name="password" />
    <br>
    <button type="submit">회원정보수정</button>
</form>
    <button type="button"><a href="/">메인</a></button>

</body>
</html>