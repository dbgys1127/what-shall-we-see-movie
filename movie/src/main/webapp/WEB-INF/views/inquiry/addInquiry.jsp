<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <meta charset="UTF-8">
    <title> 무봐? 시큐리티 테스트 홈 화면 </title>
</head>
<script>
    $(document).ready(function(){
      $("#h-inquiry").addClass("now-click");
    });
</script>
<body>
<h2 style="text-align: center;"> 문의 등록 </h2>
<form action="/inquiry" method="post" style="text-align: center;">        
    <div class="form-floating mb-3 offset-md-3 col-md-6">
        <input type="text" class="form-control" id="floatingInput" name="inquiryTitle">
        <label for="floatingInput">제목을 입력하세요</label>
    </div>
    <div class="form-floating mb-3 offset-md-3 col-md-6">
        <input type="text" class="form-control" id="floatingPassword" name="inquiryDescription">
        <label for="floatingPassword">내용을 입력하세요</label>
    </div>
    <button type="submit" class="btn btn-dark">입력완료</button>
</form>
</body>
</html>