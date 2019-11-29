<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>hello 股票</title>
    <%@ include file="/WEB-INF/views/include/head.jsp"%>
</head>
        <body>
            <div><a href="/stock/consume/stockRank?type=1">最近一个月的股票跌幅排名</a></div>
            <div><a href="/stock/consume/stockRank?type=3">最近三个月的股票跌幅排名</a></div>
            <div><a href="/stock/consume/stockRank?type=6">最近半年的股票跌幅排名</a></div>
            <div><a href="/stock/consume/stockRank?type=12">最近一年的股票跌幅排名</a></div>
        </body>
</html>
