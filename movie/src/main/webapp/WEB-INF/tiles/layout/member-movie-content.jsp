<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title> 무봐?</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-KK94CHFLLe+nY2dmCWGMq91rCGa5gtU4mk92HdvYe+M/SXH301p5ILy+dN9+nJOZ" crossorigin="anonymous">
</head>

<style>
    .pagination li{display:inline-block;}
    .btn-group li{
        display: inline-block;
        text-align: right;
    }
    .active a{color:red;}
    .active div{color: red;}
</style>
<body>
<c:forEach var="movie" items="${pageData.data}" varStatus="loop">
    <c:if test="${loop.index % 5 == 0}">
        <div class="row">
    </c:if>
        <div class="col-sm">
        <div class="card" style="width: 100%;">
            <a href="/movie/detail?movieTitle=${movie.movieTitle}"><img src="${movie.moviePoster}" class="card-img-top" style="width: 100%;"></a>
        <div class="card-header">
            <a href="/movie/detail?movieTitle=${movie.movieTitle}">${movie.movieTitle}</a>
        </div>
        <ul class="list-group list-group-flush">
            <li class="list-group-item">개봉일 : ${movie.movieOpenDate}</li>
            <li class="list-group-item">장르 : ${movie.movieGenre}</li>
            <li class="list-group-item">평균 시청횟수 :${movie.avgSawCount}</li>
        </ul>
        </div>
        </div>
    <c:if test="${(loop.index + 1) % 5 == 0 || loop.last}">
        </div>
    </c:if>

    <br>
    
</c:forEach>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.7/dist/umd/popper.min.js" integrity="sha384-zYPOMqeu1DAVkHiLqWBUTcbYfZ8osu1Nd6Z89ify25QV9guujx43ITvfi12/QExE" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.min.js" integrity="sha384-Y4oOpwW3duJdCWv5ly8SCFYWqFDsfob/3GkgExXKV4idmbt98QcxXYs9UoXAB7BZ" crossorigin="anonymous"></script>
</body>
</html>