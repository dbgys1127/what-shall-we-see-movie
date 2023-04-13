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
<h2>문의</h2>
<button type="button" onclick="location.href='/inquiry/add-inquiry-form';">문의 추가</button>
<table style="border: 1px solid black;" >
    <c:forEach var="inquiry" items="${pageData.data}">    
        <tr><td><a href="/inquiry/detail?inquiryId=${inquiry.inquiryId}">${inquiry.inquiryTitle}</a></td></tr>
            <tr>
                <td>
                    <fmt:parseDate value="${inquiry.createdAt}" var="createdAt" pattern="yyyyMMdd"/>                       
                    <fmt:formatDate value="${createdAt}" pattern="yyyy-MM-dd"/>
                </td>
                <td>${inquiry.inquiryStatus}</td>
                <td>
                    <form action="/inquiry/patch?inquiryId=${inquiry.inquiryId}" method="post">
                        <button type="submit">수정</button>
                    </form>
                </td>
                <td>
                    <form action="/inquiry/delete?inquiryId=${inquiry.inquiryId}" method="post">
                        <button type="submit">삭제</button>
                    </form>
                </td>
            </tr>
    </c:forEach>   
</table>
    <!-- 페이징 단추 -->
    <div class="pull-right">
        <ul class="pagination">
                <c:if test="${pageData.prev}">
                    <li class="paginate_button previous"><a href="/inquiry?page=${pageData.startPage-1}">Prev</a></li>
                </c:if>
                <c:forEach var="num" begin="${pageData.startPage}" end="${pageData.endPage}">
                    <li class = "${pageData.nowPage eq num ? 'active':''}">
                        <a href="/inquiry?page=${num}">${num}</a>
                    </li>
                </c:forEach>
                <c:if test="${pageData.next}">
                    <li class="paginate_button next"><a href="/inquiry?page=${pageData.endPage+1}">Next</a></li>
                </c:if>
        </ul>
    </div>
</body>
</html>