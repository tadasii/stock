<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>股票查询</title>
	<%@ include file="/WEB-INF/views/include/head.jsp"%>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		/**
		 * 签收任务
		 */
		function claim(taskId) {
			$.get('${ctx}/act/task/claim' ,{taskId: taskId}, function(data) {
				if (data == 'true'){
		        	top.$.jBox.tip('签收完成');
		            location = '${ctx}/act/task/todo/';
				}else{
		        	top.$.jBox.tip('签收失败');
				}
		    });
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="">所有股票查询</a></li>
	</ul>
	<form id="searchForm" modelAttribute="timeInVo" action="/stock/consume/stockSearch" method="post" class="breadcrumb form-search">
		<div>

			<label>查询时间：</label>
			<input id="beginDate"  name="beginDate"  type="text"  maxlength="20" class="input-medium Wdate" style="width:163px;"
				value="${timeInVo.beginDate}"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
				　--　
			<input id="endDate" name="endDate" type="text" maxlength="20" class="input-medium Wdate" style="width:163px;"
				value="${timeInVo.endDate}"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
			&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
		</div>
	</form>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>股票编号</th>
				<th>股票当前价格</th>
				<th>股票之前价格</th>
				<th>股票除权价格</th>
				<th>涨幅</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${list}" var="item">
				<tr>
					<td>
						${item.code}
					</td>
					<td>
							${item.nowPrice}
					</td>
					<td>
							${item.beforePrice}
					</td>
					<td>
							${item.realBPrice}
					</td>
					<td>
							${item.percent}
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>
