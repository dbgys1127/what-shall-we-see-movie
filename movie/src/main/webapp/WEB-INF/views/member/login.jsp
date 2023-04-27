<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <meta charset="UTF-8">
    <title>무봐?</title>
</head>
<script>
    $(document).ready(function(){
      $("#h-login").addClass("now-click");
    });
</script>
<body>
<div style="margin: 50px;">
<h2 style="text-align: center;"> 로그인 </h2>
<div style="margin-top: 50px;">
<form action="/process_login" method="post" style="text-align: center;">
    <c:if test="${param.error != null}">
        <div class="row alert alert-danger center offset-md-3 col-md-6" role="alert" >
            로그인 인증에 실패했습니다.
        </div>
    </c:if>
    
    <div class="form-floating mb-3 offset-md-3 col-md-6">
        <input type="text" class="form-control" id="floatingInput" name="username">
        <label for="floatingInput">이메일을 입력하세요</label>
    </div>
    <div class="form-floating mb-3 offset-md-3 col-md-6">
        <input type="password" class="form-control" id="floatingPassword" name="password">
        <label for="floatingPassword">비밀번호를 입력하세요</label>
    </div>
    <button type="submit" class="btn btn-dark">로그인</button>
</form>
</div>
</div>
</body>
</html>