<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <meta charset="UTF-8">
    <title> 무봐? 시큐리티 테스트 홈 화면 </title>
</head>
<body>
<h2> 문의 등록 화면 </h2>
<form action="/inquiry" method="post">
    <input type="text" name="inquiryTitle" placeholder="제목을 입력하세요.">
    <br>
    <input type="text" name="inquiryDescription" placeholder="내용을 입력하세요." />
    <br>
    <button type="submit">입력완료</button>
</form>
    <button type="button"><a href="/">입력취소</a></button>

</body>
</html>