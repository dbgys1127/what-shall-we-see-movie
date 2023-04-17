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
<!-- 정렬 기준 -->
<br>
<div class="d-flex justify-content-end">
    <div class="btn-group btn-group-sm btn-group-right">
        <li><a href="/movie?page=1&sort=movieOpenDate" class="btn btn-sm btn-outline-dark ${pageData.sort eq 'movieOpenDate' ? 'active':''}" aria-current="page">상영일자순</a></li>
        <li><a href="/movie?page=1&sort=avgSawCount" class="btn btn-sm btn-outline-dark ${pageData.sort eq 'avgSawCount' ? 'active':''}" aria-current="page">평균 시청순</a></li>
        <li><a class="btn btn-sm btn-outline-dark" aria-current="page">장르별</a></li>
        <li>
         <form action = "/movie/search/genre" method="get">
            <li>        
                <select class="form-select form-select-sm" aria-label=".form-select-sm example" name="movieGenre">
                    <option value="">=== 선택 ===</option>
                    <option value="코미디">코미디</option>
                    <option value="액션">액션</option>
                    <option value="범죄">범죄</option>
                    <option value="드라마">드라마</option>
                    <option value="SF">SF</option>
                    <option value="공포">공포</option>
                    <option value="로맨스">로맨스</option>
                </select>
            </li>
            <button type="submit" class="btn btn-sm btn-dark">적용</button>
         </form>
        </li>
    </div>
</div>
<br><br>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.7/dist/umd/popper.min.js" integrity="sha384-zYPOMqeu1DAVkHiLqWBUTcbYfZ8osu1Nd6Z89ify25QV9guujx43ITvfi12/QExE" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.min.js" integrity="sha384-Y4oOpwW3duJdCWv5ly8SCFYWqFDsfob/3GkgExXKV4idmbt98QcxXYs9UoXAB7BZ" crossorigin="anonymous"></script>
</body>
</html>