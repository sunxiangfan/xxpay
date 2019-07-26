<!DOCTYPE html>
<html>

	<head>
		<meta charset="UTF-8">
		<title></title>
		<link rel="stylesheet" href="plugins/layui/css/layui.css" media="all" />
		<link rel="stylesheet" href="css/main.css" />
	</head>

	<body>
		<div class="admin-main">
			<blockquote class="layui-elem-quote">
				<p>子商户总数：<span style="color:red;">${mchCount}</span></p>
			</blockquote>
			<blockquote class="layui-elem-quote">
				<p>今日交易额：<span style="color:red;">${todayAmount} 元</span></p>
				<p>总交易金额：<span style="color:red;">${totalAmount} 元</span></p>
			</blockquote>
			<blockquote class="layui-elem-quote">
				<p>今日订单数据：<span style="color:red;">${todayCount}</span></p>
				<p>总订单数据：<span style="color:red;">${totalCount}</span></p>
			</blockquote>
			<blockquote class="layui-elem-quote">
				<p>今日分润：<span style="color:red;">${todayCommission} 元</span></p>
				<p>总分润：<span style="color:red;">${totalCommission} 元</span></p>
			</blockquote>
		</div>
	</body>

</html>