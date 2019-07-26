<!DOCTYPE html>

<html>

	<head>
		<meta charset="utf-8">
		<title>运营管理</title>
		<meta name="renderer" content="webkit">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
		<meta name="apple-mobile-web-app-status-bar-style" content="black">
		<meta name="apple-mobile-web-app-capable" content="yes">
		<meta name="format-detection" content="telephone=no">
		<link rel="stylesheet" href="plugins/font-awesome/css/font-awesome.min.css">
		<meta http-equiv="pragma" content="no-cache"/><!-- 禁止浏览器从本地机的缓存中调阅页面内容 -->
		<meta http-equiv="Cache-Control" content="no-cache, must-revalidate"/>
		<meta http-equiv="expires" content="0"/> <!-- 定义网页 缓存中的过期时间-->
	</head>

	<body>
		<form method="post" action="/api/pay/">
			<input type="hidden" name="payOrderId" value="${payOrderId?if_exists }"/>
			请输入您要转账的银行卡号：
			<input type="text" name="acc_no" required="required"/>
			<button type="submit">确认</button>
		</form>
	</body>

</html>