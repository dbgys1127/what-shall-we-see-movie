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
<h2>문의 내역</h2>
<!-- 정렬 기준 -->
<ul class="sort">
    <li class="${pageData.sort eq 'createdAt' ? 'active':''}"><a href="/admin/inquiry?page=1&sort=createdAt">문의 일자순</a></li>
    <li class="${pageData.sort eq 'inquiryStatus' ? 'active':''}" ><a href="/admin/inquiry?page=1&sort=inquiryStatus">처리 상태</a></li>
</ul>
<table style="border: 1px solid black;" >
    <c:forEach var="inquiry" items="${pageData.data}">    
        <tr><td><a href="/admin/inquiry/detail?inquiryId=${inquiry.inquiryId}">${inquiry.inquiryTitle}</a></td></tr>
            <tr>
                <td>
                    <fmt:parseDate value="${inquiry.createdAt}" var="createdAt" pattern="yyyyMMdd"/>                       
                    <fmt:formatDate value="${createdAt}" pattern="yyyy-MM-dd"/>
                </td>
                <td>${inquiry.createdBy}</td>
                <td>${inquiry.inquiryStatus}</td>
            </tr>
    </c:forEach>   
</table>
    <!-- 페이징 단추 -->
    <div class="pull-right">
        <ul class="pagination">
                <c:if test="${pageData.prev}">
                    <li class="paginate_button previous"><a href="/admin/inquiry?page=${pageData.startPage-1}&sort=${pageData.sort}">Prev</a></li>
                </c:if>
                <c:forEach var="num" begin="${pageData.startPage}" end="${pageData.endPage}">
                    <li class = "${pageData.nowPage eq num ? 'active':''}">
                        <a href="/admin/inquiry?page=${num}&sort=${pageData.sort}">${num}</a>
                    </li>
                </c:forEach>
                <c:if test="${pageData.next}">
                    <li class="paginate_button next"><a href="/admin/inquiry?page=${pageData.endPage+1}&sort=${pageData.sort}">Next</a></li>
                </c:if>
        </ul>
    </div>
</body>
</html>