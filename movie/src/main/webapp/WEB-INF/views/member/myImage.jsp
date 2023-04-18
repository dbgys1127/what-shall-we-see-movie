<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <meta charset="UTF-8">
    <title> 무봐? 시큐리티 테스트 홈 화면 </title>
</head>
<body>
<h2 style="text-align: center;"> 이미지 수정 </h2>
<form action="/mypage/myImage" method="post" style="text-align: center;" enctype="multipart/form-data">        
    <div class="mb-3">
        <input class="form-control" type="file" id="formFileMultiple" name="myImage" accept=".jpg,.png,.jpeg" >
    </div>
    <button type="submit" class="btn btn-dark">수정</button>
</form>
</body>
</html>