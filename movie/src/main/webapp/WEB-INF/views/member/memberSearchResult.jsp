<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

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
<h2> 검색된 회원 </h2>

<form action = "/admin/member/search" method="get">
    <table style="border: 1px solid black;" >
        <th>이메일</th>
        <th>생성일</th>
        <th>경고수</th>
        <th>회원상태</th>
        <c:forEach var="member" items="${pageData.data}">
            <tr>
                <td> <a href="/admin/member/warning?email=${member.email}">${member.email}</a></td>
                <td>${member.createdAt}</td>
                <td>${member.warningCard}</td>
                <td>${member.memberStatus}</td>
            </tr>
        </c:forEach>
    </table>
    <div class="pull-right">
        <ul class="pagination">
                <c:if test="${pageData.prev}">
                    <li class="paginate_button previous"><a href="/admin/member/search?page=${pageData.startPage-1}&email=${pageData.target}">Prev</a></li>
                </c:if>

                <c:forEach var="num" begin="${pageData.startPage}" end="${pageData.endPage}">
                    <li class = "${pageData.nowPage eq num ? 'active':''}">
                        <a href="/admin/member/search?page=${num}&email=${pageData.target}">${num}</a>
                    </li>
                </c:forEach>

                <c:if test="${pageData.next}">
                    <li class="paginate_button next"><a href="/admin/member/search?page=${pageData.endPage+1}&email=${pageData.target}">Next</a></li>
                </c:if>
        </ul>
    </div>

    <input type = "text" name="email" placeholder = "검색할 회원을 입력하세요."/>
    <button type="submit">검색</button>
</form>
</body>
</html>