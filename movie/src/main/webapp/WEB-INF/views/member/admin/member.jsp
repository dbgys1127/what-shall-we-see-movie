<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix = "fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<html>
<head>
    <meta charset="UTF-8">
    <title>무봐?</title>
</head>
<script>
    $(document).ready(function(){
      $("#user").addClass("now-click");
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
<h2 style="text-align: center;"> 전체 회원 </h2>
<form action = "/admin/member/search" method="get">
    <!-- 정렬 기준 -->
    <div class="d-flex justify-content-end">
        <div class="btn-group btn-group-sm btn-group-right">
            <li><a href="/admin/member?page=1&sort=memberId" class="btn btn-sm btn-outline-dark ${pageData.sort eq 'memberId' ? 'active':''}" aria-current="page">가입일</a></li>
            <li><a href="/admin/member?page=1&sort=warningCard" class="btn btn-sm btn-outline-dark ${pageData.sort eq 'warningCard' ? 'active':''}" aria-current="page">경고순</a></li>
            <li><a href="/admin/member?page=1&sort=memberStatus" class="btn btn-sm btn-outline-dark ${pageData.sort eq 'memberStatus' ? 'active':''}" aria-current="page">차단회원</a></li>
        </div>
    </div>
    <br>
    <table class="table" >
    <!-- 표 헤더 -->
    <thead class="table-dark">
        <th>이메일</th>
        <th>생성일</th>
        <th>경고수</th>
        <th>회원상태</th>
    </thead>
        <c:forEach var="member" items="${pageData.data}">
            <tr>
            <!-- 표안에 보여질 관리자 정보 -->
                <td>
                    <a href="/admin/member/warning-page?email=${member.email}" style="color: black;">${member.email}</a>
                </td>
                <td>
                    <fmt:parseDate value="${member.createdAt}" var="createdAt" pattern="yyyyMMdd"/>                       
                    <fmt:formatDate value="${createdAt}" pattern="yyyy-MM-dd"/>
                </td>
                <td>${member.warningCard}</td>
                <td>${member.memberStatus}</td>
            </tr>
        </c:forEach>
    </table>
    <input class="form-control me-2" type="text" name="email" placeholder="검색할 회원을 입력하세요." aria-label="Search" style="width:50%; height: 50%; display: inline-block;">
    <button type="submit" class="btn btn-dark" style="margin-left: auto; display: inline-block;">검색</button>
    <br><br>
    <!-- 페이징 단추 -->
    <div style="display: block;">
        <nav aria-label="Page navigation example">
            <ul class="pagination justify-content-center">
                <c:if test="${pageData.prev}">
                    <li class="page-item">
                        <a class="page-link" href="/admin/member?page=${pageData.startPage-1}&sort=${pageData.sort}" aria-label="Previous">
                            <span aria-hidden="true">&laquo;</span>
                        </a>
                    </li>
                </c:if>
                <c:forEach var="num" begin="${pageData.startPage}" end="${pageData.endPage}">
                    <li class = "page-item ${pageData.nowPage eq num ? 'active':''}" aria-current="page">
                        <a class="page-link" href="/admin/member?page=${num}&sort=${pageData.sort}">${num}</a>
                    </li>
                </c:forEach>
                <c:if test="${pageData.next}">
                    <li class="page-item">
                        <a class="page-link" href="/admin/member?page=${pageData.endPage+1}&sort=${pageData.sort}" aria-label="Next">
                            <span aria-hidden="true">&raquo;</span>
                        </a>
                    </li>
                </c:if>
            </ul>
        </nav>
    </div>
</form>
</div>
</body>
</html>