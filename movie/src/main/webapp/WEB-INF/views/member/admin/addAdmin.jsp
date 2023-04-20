<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <meta charset="UTF-8">
    <title> 무봐? 시큐리티 테스트 홈 화면 </title>
</head>
<body>
  <form action="/admin/administrator/add" method="post" style="text-align: center; margin-left: auto; margin-right: auto;">
    <h2 style="text-align: center;"> 관리자 추가 </h2>
    <div class="form-floating mb-3" >
      <input type="text" class="form-control" id="floatingInput" name="email">
      <label for="floatingInput">이메일을 입력하세요</label>
    </div>
    <div class="form-floating mb-3">
      <input type="text" class="form-control" id="floatingPassword" name="password">
      <label for="floatingPassword">비밀번호를 입력하세요</label>
    </div>
    <button type="submit" class="btn btn-dark">등록 완료</button>
  </form>
</body>
</html>