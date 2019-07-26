<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" >
</head>
<body >
<form name="dinpayForm" method="post" action="https://pay.minfupay.cn/gateway?input_charset=<%=input_charset%>" >
    <input type="hidden" name="sign" value="<%=sign%>" />
    <input type="hidden" name="merchant_code" value="<%=merchant_code%>" />
    <input type="hidden" name="service_type" value="<%=service_type%>" />
    <input type="hidden" name="pay_type" value="<%=pay_type%>" />
    <input type="hidden" name="interface_version" value="<%=interface_version%>" />
    <input type="hidden" name="input_charset" value="<%=input_charset%>" />
    <input type="hidden" name="notify_url" value="<%=notify_url%>"/>
    <input type="hidden" name="sign_type" value="<%=sign_type%>" />
    <input type="hidden" name="order_no" value="<%=order_no%>"/>
    <input type="hidden" name="order_time" value="<%=order_time%>" />
    <input type="hidden" name="order_amount" value="<%=order_amount%>"/>
    <input type="hidden" name="product_name" value="<%=product_name%>" />
    <input type="hidden" name="return_url" value="<%=return_url%>"/>
    <input type="hidden" name="bank_code" value="<%=bank_code%>" />
    <input type="hidden" name="redo_flag" value="<%=redo_flag%>"/>
</form>
</body>
</html>