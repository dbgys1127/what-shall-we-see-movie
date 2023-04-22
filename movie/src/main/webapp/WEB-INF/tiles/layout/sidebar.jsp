<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title> 무봐? </title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-KK94CHFLLe+nY2dmCWGMq91rCGa5gtU4mk92HdvYe+M/SXH301p5ILy+dN9+nJOZ" crossorigin="anonymous">
    <style>
        nav.navbar a, .navbar-brand:hover{color: white;}
        .nav-item a:hover{
            color: white;
            background-color: #EB4F5A;
            border-radius: 15px;
        }
        .nav-link{
         transition: none;   
        }
    </style>
</head>
<body>
    <div class="d-flex flex-column flex-shrink-0 p-3 text-bg-dark" style="width: 160px; height: 100%; display: inline-block !important; margin-right: 0px; position: fixed;">
        <hr>
        <h4>회원관리</h4>
        <ul class="nav nav-pills flex-column mb-auto">
          <li class="nav-item">
            <a href="/admin/member?page=1" class="nav-link text-white" aria-current="page">
                사용자
            </a>
          </li>
          <li class="nav-item">
            <a href="/admin/administrator?page=1" class="nav-link text-white">
                관리자
            </a>
          </li>
          <hr>
          <h4>영화관리</h4>
          <li class="nav-item">
            <a href="/admin/movie?page=1" class="nav-link text-white">
                영화
            </a>
          </li>
          <hr>
          <h4>댓글 관리</h4>
          <li class="nav-item">
            <a href="/admin/comment" class="nav-link text-white">
                신고 댓글 조회
            </a>
          </li>
          <hr>
          <h4>문의 관리</h4>
          <li class="nav-item">
            <a href="/admin/inquiry" class="nav-link text-white">
                문의 내역 조회
            </a>
          </li>
        </ul>
      </div>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.7/dist/umd/popper.min.js" integrity="sha384-zYPOMqeu1DAVkHiLqWBUTcbYfZ8osu1Nd6Z89ify25QV9guujx43ITvfi12/QExE" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.min.js" integrity="sha384-Y4oOpwW3duJdCWv5ly8SCFYWqFDsfob/3GkgExXKV4idmbt98QcxXYs9UoXAB7BZ" crossorigin="anonymous"></script>
</body>
</html>