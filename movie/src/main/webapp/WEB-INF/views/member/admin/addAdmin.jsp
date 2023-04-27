<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <meta charset="UTF-8">
    <title>무봐?</title>
</head>
<script>
  $(document).ready(function(){
    $("#admin").addClass("now-click");
  });
  $(document).ready(function(){
      $("#h-admin").addClass("now-click");
  });
</script>
<body>
<div style="margin: 50px;">
  <form action="/admin/administrator/add" method="post">
    <h2 style="text-align: center;"> 관리자 추가 </h2>
    <div class="form-floating mb-3" style="margin-top: 50px;" >
      <input type="text" class="form-control" id="floatingInput" name="email">
      <label for="floatingInput">이메일을 입력하세요</label>
    </div>
    <div class="form-floating mb-3">
      <input type="text" class="form-control" id="floatingPassword" name="password">
      <label for="floatingPassword">비밀번호를 입력하세요</label>
    </div>
    <button type="submit" class="btn btn-dark">등록 완료</button>
  </form>
</div>
</body>
</html>