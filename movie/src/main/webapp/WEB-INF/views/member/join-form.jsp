<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <meta charset="UTF-8">
    <title> 무봐? 시큐리티 테스트 홈 화면 </title>
</head>
<script>
    $(document).ready(function(){
      $("#h-join").addClass("now-click");
    });
</script>
<body>
<div style="margin: 50px;">
<h2 style="text-align: center;"> 회원가입 </h2>
<div style="margin-top: 50px;">
<form action="/join" method="post" style="text-align: center;">        
    <div class="form-floating mb-3 offset-md-3 col-md-6">
        <input type="text" class="form-control" id="floatingInput" name="email">
        <label for="floatingInput">이메일을 입력하세요</label>
    </div>
    <div class="form-floating mb-3 offset-md-3 col-md-6">
        <input type="password" class="form-control" id="floatingPassword" name="password">
        <label for="floatingPassword">비밀번호를 입력하세요</label>
    </div>
    <button type="submit" class="btn btn-dark">회원가입</button>
</form>
</div>
</div>
</body>
</html>