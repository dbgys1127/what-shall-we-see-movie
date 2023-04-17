<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title> 무봐? </title>
</head>
<body>
    <tiles:insertAttribute name="header" />
    <tiles:insertAttribute name="member-movie-sort"/>
    <tiles:insertAttribute name="member-movie-content"/>
    <tiles:insertAttribute name="content" />
</body>
</html>