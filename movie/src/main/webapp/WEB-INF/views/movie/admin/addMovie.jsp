<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <meta charset="UTF-8">
    <title> 무봐? 시큐리티 테스트 홈 화면 </title>
</head>
<script>
    $(document).ready(function(){
      $("#movie").addClass("now-click");
    });
    $(document).ready(function(){
      $("#h-admin").addClass("now-click");
    });
</script>
<body>
<div style="margin: 50px;">
<form action="/admin/movie" method="post" enctype="multipart/form-data">
    <h2 style="text-align: center;"> 영화 등록 화면 </h2>
    <br>
    <div class="row">
        <div class="col-md-6">
            <div class="card" style="width: 100%;">
                <img src="" class="card-img-top" style="width: 100%;">
            <div class="card-header">
                <div class="mb-3">
                        <input class="form-control" type="file" id="formFileMultiple" name="moviePoster" accept=".jpg,.png,.jpeg">
                </div>
            </div>
            </div>
        </div>
        <div class="col-md-6">
            <ul class="list-group list-group-flush">
                <li class="list-group-item"><input type="text" name="movieTitle" placeholder="제목을 입력하세요."></li>
                <li class="list-group-item"><input type="text" name="movieRunningTime" placeholder="상영시간을 입력하세요."></li>
                <li class="list-group-item"><input type="date" name="movieOpenDate"></li>
                <li class="list-group-item">       
                    <select name="movieGenre" >
                        <option value="none">=== 선택 ===</option>
                        <option value="코미디">코미디</option>
                        <option value="액션">액션</option>
                        <option value="범죄">범죄</option>
                        <option value="드라마">드라마</option>
                        <option value="SF">SF</option>
                        <option value="공포">공포</option>
                        <option value="로맨스">로맨스</option>
                    </select>
                </li>
            </ul>
        </div>
    </div>
    <br>
    <div class="row">
        <textarea class="form-control" name="movieDescription" placeholder="상세 설명을 입력하세요."></textarea>
    </div>
    <br>
    <button type="submit" class="btn btn-dark">영화 등록</button>
</form>
</div>
</body>
</html>