<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<html>
<head>
    <meta charset="UTF-8">
    <title> 무봐? 시큐리티 테스트 홈 화면 </title>
</head>
<body>
<h2> Mypage </h2>
<img src="${memberImage}" />
<br>
${email}
<button type="button"><a href="/my-info/myImage">대표 이미지 수정</a></button>
<button type="button"><a href="/my-info/myPassword">비밀번호 수정</a></button>
</body>
</html>