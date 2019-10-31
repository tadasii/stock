<%--
  Created by IntelliJ IDEA.
  User: zhangzheng
  Date: 2019/10/24
  Time: 10:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>hello 股票</title>
    <%@ include file="/WEB-INF/views/include/head.jsp"%>
    <script type="text/javascript">
        function getAllCode(){
            $("#allCode").text("正在初始化股票池。。。");
            $.get("/stock/ini/getAllCode", function(data){
                $("#allCode").text("股票池数据为："+data);
            });
        }
    </script>
</head>
        <body>
            <div id="getAllCode" onclick="getAllCode();"><a href="javascript:void(0);">初始化股票池</a></div>
            <div id="allCode"></div>
            <div><a href="/stock/consume/stockSearch">跳转到股票查询页面</a></div>
        </body>
</html>
