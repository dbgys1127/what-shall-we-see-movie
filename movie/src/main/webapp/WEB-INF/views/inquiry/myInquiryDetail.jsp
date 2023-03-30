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
    <th>문의 작성일</th>
    <th>처리 상태</th>
    <tr>
        <td>${inquiry.createdAt}</td>
        <td>${inquiry.inquiryStatus}</td>
    </tr>
    <tr><td>문의 제목: ${inquiry.inquiryTitle}</td></tr>
    <tr><td>문의 내용: ${inquiry.inquiryDescription}</td></tr>
    <tr>
        <td><button type="submit"><a href="/inquiry/patch?inquiryId=${inquiry.inquiryId}" >수정하기</a></button></td>
        <td><button type="button"><a href="/">메인</a></button></td>
    </tr>
</table>
</body>
</html>