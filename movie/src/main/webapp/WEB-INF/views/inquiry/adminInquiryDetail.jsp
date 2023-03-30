<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <meta charset="UTF-8">
    <title> 무봐? 시큐리티 테스트 홈 화면 </title>
</head>
<body>
<h2> 문의 상세화면 </h2>
<table>
    <th>문의 작성자</th>
    <th>문의 작성일</th>
    <th>처리 상태</th>
    <th>처리 체크</th>
    <tr>
        <td>${inquiry.createdBy}</td>
        <td>${inquiry.createdAt}</td>
        <td>${inquiry.inquiryStatus}</td>
<form action="/inquiry/answer?inquiryId=${inquiry.inquiryId}" method="post"></form>
        <td><input type="checkbox" name="block" <c:if test="${inquiry.inquiryStatus eq '처리'}">checked</c:if> /></td>
    </tr>
</table>
<table>
    <tr><td>문의 제목: ${inquiry.inquiryTitle}</td></tr>
    <tr><td>문의 내용: ${inquiry.inquiryDescription}</td></tr>
    <tr><td><input type="text" name="answer" placeholder="답변을 등록해주세요."/></td></tr>
</table>
    <button type="submit">답변등록</button>
</form>
<button type="button"><a href="/">입력취소</a></button>
</body>
</html>