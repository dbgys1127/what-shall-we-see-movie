<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<html>
<head>
    <meta charset="UTF-8">
    <title> 무봐? 시큐리티 테스트 홈 화면 </title>
</head>
<body>
<h2> 전체회원 </h2>
<form action = "/admin/search/member" method="get">
    <input type = "text" name="email" placeholder = "검색할 회원을 입력하세요."/>
    <button type="submit">검색</button>
</form>
</body>
</html>