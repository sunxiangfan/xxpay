
<!DOCTYPE html>
<html lang=zh>
<head>
    <meta charset="UTF-8">
    <title>聚合收银台</title>
    <link href="/api/css/tengjing/cashier.css" rel="stylesheet">
    <script src="/api/js/jquery.min.js"></script>
    <style>
		.pay-infor input[type=text]{
			height: 24px;
			line-height: 24px;
			width: 170px;
			padding-left: 10px;
		}
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
                    <form action="/api/pay/tengjing/transfer" method="post" autocomplete="off" onsubmit="return submit1(this);">
                        <input type="hidden" name="payOrderId" value="${payOrderId?if_exists }"/>
                   
                        <div class="two-step">
                            <p><strong>请您及时付款，以便订单尽快处理！</strong>请您在提交订单后<span>24小时</span>内完成支付，否则订单会自动取消。</p>
                            <ul class="pay-infor">
                                <li>订单编号：<span>${payOrderId?if_exists }</span></li>
								
                                <li>银行卡号<span style="color:red;">*</span>：<strong><input type="text" id="acc_no" name="acc_no" value="" required> </strong></li>
                            </ul>                            
                            <div class="btns" style="margin-top:300px;z-index: auto">
                                <button type="submit" class="pcdemo-btn sbpay-btn">立即支付</button>
                            </div>

                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    function submit1(form) {
        var acc_no=$('#acc_no').val();
        if(!acc_no|| acc_no==''){
            alert('请输入银行卡号！');
            $('#acc_no').focus();
            return false;
        }
        else{
            return true;
        }
    }
</script>
</body>
</html>
