<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <meta charset="UTF-8">
    <title> 무봐? 시큐리티 테스트 홈 화면 </title>
</head>
<body>
<h2 style="text-align: center;"> 비밀번호 수정 </h2>
<form action="/mypage/myPassword" method="post" style="text-align: center;">        
    <div class="form-floating mb-3 offset-md-3 col-md-6">
        <input type="text" class="form-control" id="floatingPassword" name="password">
        <label for="floatingPassword">비밀번호를 입력하세요</label>
    </div>
    <button type="submit" class="btn btn-dark">수정</button>
</form>
</body>
</html>