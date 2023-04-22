<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title> 무봐? </title>
</head>
<body>
    <div style="position: fixed; width: 100%; height: 56px; top: 0; z-index: 10;">
        <tiles:insertAttribute name="header" />
    </div>
    <div style="margin-top: 56px;">
        <tiles:insertAttribute name="sidebar"/>
        <div style="text-align: center; margin-right: 15px; width: calc( 100% - 200px ); display: inline-block; float: right;">
            <tiles:insertAttribute name="content" />
        </div>
    </div>
</body>
</html>