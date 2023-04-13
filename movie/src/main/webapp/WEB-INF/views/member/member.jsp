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
<h2> 전체회원 </h2>

<form action = "/admin/member/search" method="get">
    <!-- 정렬 기준 -->
    <ul class="sort">
        <li class="${pageData.sort eq 'memberId' ? 'active':''}"><a href="/admin/member?page=1&sort=memberId">가입일</a></li>
        <li class="${pageData.sort eq 'warningCard' ? 'active':''}" ><a href="/admin/member?page=1&sort=warningCard">경고수</a></li>
        <li class="${pageData.sort eq 'memberStatus' ? 'active':''}"><a href="/admin/member?page=1&sort=memberStatus">차단멤버</a></li>
    </ul>
    <!-- 표 헤드값 -->
    <table style="border: 1px solid black;" >
        <th>이메일</th>
        <th>생성일</th>
        <th>경고수</th>
        <th>회원상태</th>
        <c:forEach var="member" items="${pageData.data}">
        <!-- 표 안에 보여질 멤버 내용 -->
            <tr>
                <td> <a href="/admin/member/warning-page?email=${member.email}">${member.email}</a></td>
                <td>
                    <fmt:parseDate value="${member.createdAt}" var="createdAt" pattern="yyyyMMdd"/>                       
                    <fmt:formatDate value="${createdAt}" pattern="yyyy-MM-dd"/>
                </td>
                <td>${member.warningCard}</td>
                <td>${member.memberStatus}</td>
            </tr>
        </c:forEach>
    </table>
    <!-- 페이징 단추 -->
    <div class="pull-right">
        <ul class="pagination">
                <c:if test="${pageData.prev}">
                    <li class="paginate_button previous"><a href="/admin/member?page=${pageData.startPage-1}&sort=${pageData.sort}">Prev</a></li>
                </c:if>

                <c:forEach var="num" begin="${pageData.startPage}" end="${pageData.endPage}">
                    <li class = "${pageData.nowPage eq num ? 'active':''}">
                        <a href="/admin/member?page=${num}&sort=${pageData.sort}">${num}</a>
                    </li>
                </c:forEach>

                <c:if test="${pageData.next}">
                    <li class="paginate_button next"><a href="/admin/member?page=${pageData.endPage+1}&sort=${pageData.sort}">Next</a></li>
                </c:if>
        </ul>
    </div>

    <input type = "text" name="email" placeholder = "검색할 회원을 입력하세요."/>
    <button type="submit">검색</button>
</form>
</body>
</html>