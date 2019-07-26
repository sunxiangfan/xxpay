
<!DOCTYPE html>
<html lang=zh>
<head>
    <meta charset="UTF-8">
    <title>聚合收银台</title>
    <style>
        *{ font-family:Arial, Helvetica, sans-serif;
            font-size:12px}
        .STYLE1 {font-size: 12px}
    </style>
</head>
<body>
<div style="text-align:center">
    <h2>请选择网上银行</h2>
</div>
<form name="form1" action="pay.jsp" method="post">
    <table width="445" border="0" align="center" cellpadding="0" cellspacing="0" style="border:#99CC00 solid 2px">
        <tr>
            <td  colspan="2" align="center" bordercolor="#00CCFF">
                <table width="68%" border="0" cellpadding="0" cellspacing="1" bgcolor="#CCCCCC">
                    <tr>
                        <td width="32%" height="25" align="left" bgcolor="#FFFFFF"><input type="radio" name="pay_bankcode" value="wx"  />微信支付</td>
                        <td width="32%" align="left" bgcolor="#FFFFFF"><input type="radio" name="pay_bankcode" value="zfb" />支付宝支付</td>
                    </tr>
            </td>
        </tr>
        <tr>
            <td height="36" colspan="2" align="center"><input type="submit" name="submit2" value="确认付款" onclick="return checkMoney()" /></td>
        </tr>
    </table>
</form>
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
