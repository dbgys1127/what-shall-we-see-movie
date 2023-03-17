<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
    <meta charset="UTF-8">
    <title> 무봐? 시큐리티 테스트 홈 화면 </title>
</head>
<body>
    <h2> 개인회원 </h2>
    <form action = "/admin/member/warning?email=${email}" method="post">
        <table style="border: 1px solid black;" >
            <th>이메일</th>
            <th>경고수</th>
            <th>경고추가</th>
            <th>회원차단</th>
            <tr>
                <td>${email}</td>
                <td>${warningCard}</td>
                <td><input type="checkbox" name="warning" value="on"></td>
                <td>
                    <c:if test="${memberStatus eq '활성'}">
                    <input type="checkbox" name="block" value="on" />
                    </c:if>
                    <c:if test="${memberStatus eq '차단'}">
                    <input type="checkbox" name="block" value="off" checked />
                    </c:if>
                </td>
            </tr>
    <button type="submit">수정</button>
</form>
</body>
</html>