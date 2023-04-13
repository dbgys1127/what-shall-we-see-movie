<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix = "fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<html>
<head>
    <meta charset="UTF-8">
    <title> 무봐? 시큐리티 테스트 홈 화면 </title>
</head>

<style>
    .pagination li{display:inline-block;}
    .sort li{display: inline-block;}
    .active a{color:red;}

</style>
<body>
<h2> 전체 관리자 </h2>

<form action = "/admin/administrator/search" method="get">
<!-- 정렬 기준 -->
    <table style="border: 1px solid black;" >
    <!-- 표 헤더 -->
        <th>이메일</th>
        <th>생성일</th>
        <th>관리자 삭제</th>
        <c:forEach var="admin" items="${pageData.data}">
            <tr>
            <!-- 표안에 보여질 관리자 정보 -->
                <td> <a href="">${admin.email}</a></td>
                <td>
                    <fmt:parseDate value="${admin.createdAt}" var="createdAt" pattern="yyyyMMdd"/>                       
                    <fmt:formatDate value="${createdAt}" pattern="yyyy-MM-dd"/>
                </td>
                <td><button type="button" onclick="location.href='/admin/administrator/delete?email=${admin.email}';">삭제</button></td>
            </tr>
        </c:forEach>
    </table>
    <div class="pull-right">
    <!-- 페이징 단추 -->
        <ul class="pagination">
                <c:if test="${pageData.prev}">
                    <li class="paginate_button previous"><a href="/admin/administrator?page=${pageData.startPage-1}&sort=${pageData.sort}">Prev</a></li>
                </c:if>

                <c:forEach var="num" begin="${pageData.startPage}" end="${pageData.endPage}">
                    <li class = "${pageData.nowPage eq num ? 'active':''}">
                        <a href="/admin/administrator?page=${num}&sort=${pageData.sort}">${num}</a>
                    </li>
                </c:forEach>

                <c:if test="${pageData.next}">
                    <li class="paginate_button next"><a href="/admin/administrator?page=${pageData.endPage+1}&sort=${pageData.sort}">Next</a></li>
                </c:if>
        </ul>
    </div>

    <input type = "text" name="email" placeholder = "검색할 회원을 입력하세요."/>
    <button type="submit">검색</button>
</form>
<button type="button" onclick="location.href='/admin/administrator/add-admin-form';">관리자 추가</button>
</body>
</html>