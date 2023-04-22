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
    <nav class="navbar navbar-dark bg-dark navbar-expand-lg">
        <div class="container-fluid">
          <a class="navbar-brand" href="/">무봐</a>
          <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
          </button>
          <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
              <li class="nav-item">
                <a class="nav-link" aria-current="page" href="/movie?page=1">영화</a>
              </li>
    <c:choose>
        <c:when test="${empty nowMember}">
                <li class="nav-item">
                    <a class="nav-link" href="/login-form">로그인</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/join-form">회원가입</a>
                </li>
        </c:when>
        <c:otherwise>
                <li class="nav-item">
                    <a class="nav-link" href="/inquiry">문의</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/mypage">마이페이지</a>
                </li>
                <c:if test="${nowMemberAuth eq 'ROLE_ADMIN'}">
                <li class="nav-item">
                    <a class="nav-link" href="/admin">관리자페이지</a>
                </li>
                </c:if>
                <li class="nav-item">
                    <a class="nav-link" href="/logout-form">로그아웃</a>
                </li>
            </ul>
            <form class="d-flex" action="/movie/search/title" method="get" role="search">
              <input class="form-control me-2" type="text" name="movieTitle"  placeholder="검색할 영화를 입력하세요." aria-label="Search">
              <button class="btn btn-outline-success" type="submit">Search</button>
            </form>
        </c:otherwise>
    </c:choose>
          </div>
        </div>
    </nav>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.7/dist/umd/popper.min.js" integrity="sha384-zYPOMqeu1DAVkHiLqWBUTcbYfZ8osu1Nd6Z89ify25QV9guujx43ITvfi12/QExE" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.min.js" integrity="sha384-Y4oOpwW3duJdCWv5ly8SCFYWqFDsfob/3GkgExXKV4idmbt98QcxXYs9UoXAB7BZ" crossorigin="anonymous"></script>
</body>
</html>