<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
    <meta charset="UTF-8">
    <title> 무봐? 시큐리티 테스트 홈 화면 </title>
</head>
<body>
    <form action = "/admin/member/warning?email=${email}" method="post">
        <h2 style="text-align: center;"> 회원 경고 </h2>
    <!-- 정렬 기준 -->
    <br>
        <table class="table" >
        <!-- 표 헤더 -->
        <thead class="table-dark">
            <th>이메일</th>
            <th>경고수</th>
            <th>경고추가</th>
            <th>회원차단</th>
        </thead>
            <tr>
            <!-- 표안에 보여질 관리자 정보 -->
                <td>${email}</td>
                <td>${warningCard}</td>                
                <td><input type="checkbox" name="warning"></td>
                <td>
                    <input type="checkbox" name="block" <c:if test="${memberStatus eq '차단'}">checked</c:if> />
                </td>
            </tr>
        </table>
              <button type="submit" class="btn btn-dark" style="margin-left: auto; display: inline-block;">상태 변경</button>
              <br><br>
    </form>
</body>
</html>