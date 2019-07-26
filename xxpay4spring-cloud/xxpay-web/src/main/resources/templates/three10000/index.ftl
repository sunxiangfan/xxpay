<!DOCTYPE html>
<html lang=zh>
<head>
	<meta charset=UTF-8>
	<title>聚合收银台</title>
	<link href="/api/css/tengjing/cashier.css" rel="stylesheet">
	<script src="/api/js/jquery.min.js"></script>
	<style>
		.banks{display: none}
		.banks li{position:relative;float: left;width:240px;padding-top: 5px}
	</style>
</head>
<body>
<div class="tastesdk-box">
	<div class="header clearfix">
		<div class="title">
			<p class="logo">
				<span>收银台</span>
			</p>
			<div class="right">
				<div class="clearfix">
					<ul class="clearfix">

					</ul>
				</div>
			</div>
		</div>
	</div>
	<div class="main">
		<div class="typedemo">
			<div class="demo-pc">
				<div class="pay-jd">
					<form action="/api/pay/three10000/gateway" method="post" autocomplete="off">
						<input type="hidden" name="payOrderId" value="${payOrderId}">
						<input type="hidden" name="mchOrderNo" value="${mchOrderNo}">

						<div class="two-step">
							<p><strong>请您及时付款，以便订单尽快处理！</strong>请您在提交订单后<span>24小时</span>内完成支付，否则订单会自动取消。</p>
							<ul class="pay-infor">
								<li>支付金额：<strong>${dispayMoney}元</span></strong></li>
							</ul>
							<h5>选择支付方式：</h5>
							<ul class="pay-label">
								<li>
									<input value="907" name="channel" id="bd" type="radio" checked>
									<label id="ylzf" for="bd"><img src="/api/images/yinlian.png" alt="网银支付"><span>网银支付</span></label>
								</li>
							</ul>
							<ul class="banks">
								<li><label for="b1"><input id="b1" type="radio" value="10001" name="pay_bankcode" checked>工商银行</label></li>
								<li><label for="b2"><input id="b2" type="radio" value="10002" name="pay_bankcode">农业银行</label></li>
								<li><label for="b3"><input id="b3" type="radio" value="10003" name="pay_bankcode">中国银行</label></li>
								<li><label for="b4"><input id="b4" type="radio" value="10004" name="pay_bankcode">建设银行</label></li>
								<li><label for="b5"><input id="b5" type="radio" value="10005" name="pay_bankcode">交通银行</label></li>
							</ul>
							<ul class="banks">
								<li><label for="b6"><input id="b6" type="radio" value="10006" name="pay_bankcode">招商银行</label></li>
								<li><label for="b7"><input id="b7" type="radio" value="10007" name="pay_bankcode">广东发展银行</label></li>
								<li><label for="b8"><input id="b8" type="radio" value="10008" name="pay_bankcode">中信银行</label></li>
								<li><label for="b9"><input id="b9" type="radio" value="10009" name="pay_bankcode">民生银行</label></li>
								<li><label for="b10"><input id="b10" type="radio" value="10010" name="pay_bankcode">光大银行</label></li>
							</ul>
							<ul class="banks">
								<li><label for="b11"><input id="b11" type="radio" value="10011" name="pay_bankcode">平安银行</label></li>
								<li><label for="b12"><input id="b12" type="radio" value="10012" name="pay_bankcode">上海浦东发展银行</label></li>
								<li><label for="b13"><input id="b13" type="radio" value="10013" name="pay_bankcode">中国邮政储蓄银行</label></li>
								<li><label for="b14"><input id="b14" type="radio" value="10014" name="pay_bankcode">华夏银行</label></li>
								<li><label for="b15"><input id="b15" type="radio" value="10015" name="pay_bankcode">兴业银行</label></li>
							</ul>
							<ul class="banks">
								<li><label for="b16"><input id="b16" type="radio" value="10016" name="pay_bankcode">北京银行</label></li>
								<li><label for="b17"><input id="b17" type="radio" value="10017" name="pay_bankcode">上海银行</label></li>
							</ul>
							<div class="btns" style="margin-top:900px;z-index: auto">
								<button type="submit" class="pcdemo-btn sbpay-btn">立即支付</button>
							</div>

						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
</div>
</body>
<script>
	$('.banks').show();
</script>
</html>
