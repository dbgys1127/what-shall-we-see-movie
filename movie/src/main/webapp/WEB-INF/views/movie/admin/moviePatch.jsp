<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <meta charset="UTF-8">
    <title>무봐?</title>
</head>
<script>
    $(document).ready(function(){
      $("#movie").addClass("now-click");
    });
    $(document).ready(function(){
      $("#h-admin").addClass("now-click");
    });
</script>
<style>
    .pagination li{display:inline-block;}
    .btn-group li{
        display: inline-block;
        text-align: right;
    }
    .active a{color:red;}

</style>
<body>
<div style="margin: 50px;">
<form action="/admin/movie/patch?preMovieTitle=${movie.movieTitle}" method="post" enctype="multipart/form-data">
<h2 style="text-align: center;"> 영화수정 화면 </h2>
<br>
<div class="row">
    <div class="col-md-4">
        <div class="card" style="width: 175px; height:250px;">
            <img src="${movie.moviePoster}" class="card-img-top" style="width: 175px; height:250px;">
        </div>
    </div>
    <div class="col-md-6">
            <ul class="list-group list-group-flush">
                <li class="list-group-item"><input class="form-control" type="file" id="formFileMultiple" name="moviePoster" accept=".jpg,.png,.jpeg" placeholder="${movie.moviePoster}" ></li>
                <li class="list-group-item"><input type="text" name="movieTitle" placeholder="제목 : ${movie.movieTitle}"></li>
                <li class="list-group-item"><input type="text" name="movieRunningTime" placeholder="상영시간 : ${movie.movieRunningTime}"></li>
                <li class="list-group-item"><input type="date" name="movieOpenDate" value="${movie.movieOpenDate}"></li>
                <li class="list-group-item">       
                    <select name="movieGenre" >
                        <option value="${movie.movieGenre}">현재 장르 : ${movie.movieGenre}</option>
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
    <textarea class="form-control" name="movieDescription" placeholder="상세 설명 : ${movie.movieDescription}"></textarea>
</div>
<br>
<button type="submit" class="btn btn-dark">영화 수정</button>
</form>
</div>
</body>
</html>