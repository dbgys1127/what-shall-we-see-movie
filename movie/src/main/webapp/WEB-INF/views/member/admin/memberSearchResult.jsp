<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix = "fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<html>
<head>
    <meta charset="UTF-8">
    <title> 무봐? 시큐리티 테스트 홈 화면 </title>
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
    .sort li{display: inline-block;}
    .active a{color:red;}

</style>
<body>
<div style="margin: 50px;">
<form action = "/admin/member/search" method="get">
    <h2 style="text-align: center;"> 검색한 회원 </h2>
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
                        <a class="page-link" href="/admin/member/search?page=${pageData.startPage-1}&sort=${pageData.sort}&email=${pageData.target}" aria-label="Previous">
                            <span aria-hidden="true">&laquo;</span>
                        </a>
                    </li>
                </c:if>
                <c:forEach var="num" begin="${pageData.startPage}" end="${pageData.endPage}">
                    <li class = "page-item ${pageData.nowPage eq num ? 'active':''}" aria-current="page">
                        <a class="page-link" href="/admin/member/search?page=${num}&sort=${pageData.sort}&email=${pageData.target}">${num}</a>
                    </li>
                </c:forEach>
                <c:if test="${pageData.next}">
                    <li class="page-item">
                        <a class="page-link" href="/admin/member/search?page=${pageData.endPage+1}&sort=${pageData.sort}&email=${pageData.target}" aria-label="Next">
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