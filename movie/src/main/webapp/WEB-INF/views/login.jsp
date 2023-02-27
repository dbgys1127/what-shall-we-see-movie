<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <meta charset="UTF-8">
    <title> 무봐? 시큐리티 테스트 홈 화면 </title>
</head>
<body>
<h2> 로그인 화면 </h2>
<form action="/process_login" method="post">

    <div class="row alert alert-danger center" role="alert" >
        <c:if test="${param.error != null}">로그인 인증에 실패했습니다.</c:if>
    </div>
    email: <input type="text" name="username" />
    password: <input type="text" name="password" />
    <button type="submit">로그인</button>
</form>
</body>
</html>