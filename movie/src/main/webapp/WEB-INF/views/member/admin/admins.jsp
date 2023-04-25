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
      $("#admin").addClass("now-click");
    });
    $(document).ready(function(){
      $("#h-admin").addClass("now-click");
    });
    $(document).on("click", ".delete-admin", function(){
        var emailVal = $(this).siblings("#email").val();
        var params = {
            email : emailVal
        }; 
        $.ajax({
            type : "POST",            
            url : "/admin/administrator/delete",   
            data : params,           
            success : function(res){ 
                location.reload(true);
            }
        });
    });
</script>
<style>
    .pagination li{display:inline-block;}
    .sort li{display: inline-block;}
    .active a{color:red;}

</style>
<body>
<div style="margin: 50px;">
    <h2 style="text-align: center;"> 전체 관리자 </h2>
<form action = "/admin/administrator/search" method="get">
<!-- 정렬 기준 -->
    <table class="table" style="margin-top: 50px;" >
    <!-- 표 헤더 -->
    <thead class="table-dark">
        <th>이메일</th>
        <th>생성일</th>
        <th>관리자 삭제</th>
    </thead>
    <c:forEach var="admin" items="${pageData.data}">
        <tr>
        <!-- 표안에 보여질 관리자 정보 -->
            <td> ${admin.email}</td>
            <td>
                <fmt:parseDate value="${admin.createdAt}" var="createdAt" pattern="yyyyMMdd"/>                       
                <fmt:formatDate value="${createdAt}" pattern="yyyy-MM-dd"/>
            </td>
            <td>
                <div style="margin: 5px 5px 5px; margin-right: 0px; display:inline-block;">
                    <input type="hidden" id="email" value="${admin.email}"/>
                    <button type="button" class="btn btn-dark delete-admin">삭제</button>
                </div>                
            </td>
        </tr>
    </c:forEach>
    </table>
    <input class="form-control me-2" type="text" name="email" placeholder="검색할 회원을 입력하세요." aria-label="Search" style="width:50%; height: 50%; display: inline-block;">
    <button type="submit" class="btn btn-dark" style="margin-left: auto; display: inline-block;">검색</button>
    <button type="button" onclick="location.href='/admin/administrator/add-admin-form';" class="btn btn-dark" style="display: inline-block;">관리자추가</button>
    <br><br>
    <!-- 페이징 단추 -->
    <div style="display: block;">
        <nav aria-label="Page navigation example">
            <ul class="pagination justify-content-center">
                <c:if test="${pageData.prev}">
                    <li class="page-item">
                        <a class="page-link" href="/admin/administrator?page=${pageData.startPage-1}&sort=${pageData.sort}" aria-label="Previous">
                            <span aria-hidden="true">&laquo;</span>
                        </a>
                    </li>
                </c:if>
                <c:forEach var="num" begin="${pageData.startPage}" end="${pageData.endPage}">
                    <li class = "page-item ${pageData.nowPage eq num ? 'active':''}" aria-current="page">
                        <a class="page-link" href="/admin/administrator?page=${num}&sort=${pageData.sort}">${num}</a>
                    </li>
                </c:forEach>
                <c:if test="${pageData.next}">
                    <li class="page-item">
                        <a class="page-link" href="/admin/administrator?page=${pageData.endPage+1}&sort=${pageData.sort}" aria-label="Next">
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