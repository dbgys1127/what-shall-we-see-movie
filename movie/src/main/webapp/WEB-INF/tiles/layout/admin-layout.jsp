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
    <div style="display: flex;">
        <tiles:insertAttribute name="sidebar"/>
        <tiles:insertAttribute name="content" />
    </div>
</body>
</html>