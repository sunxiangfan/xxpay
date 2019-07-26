<!DOCTYPE HTML>
<html>
<head>
    <meta charset="htf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>支付中心</title>
    <style>
        body {
            font-family: 'Microsoft YaHei';
        }

        #amount, #error {
            height: 80px;
            line-height: 80px;
            text-align: center;
            color: #f00;
            font-size: 30px;
            font-weight: bold;
        }

        #error {
            font-size: 20px;
        }

        #info {
            padding: 0 10px;
            font-size: 12px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
        }

        tr {
            border: 1px solid #ddd;
        }

        td {
            padding: 10px;
        }

        .fr {
            text-align: right;
            font-weight: bold;
        }
    </style>
    <script src="//cdn.jsdelivr.net/jquery/1.12.1/jquery.min.js"></script>
</head>
<body>
<form method="get" action="/pay/do_pay">
    <div id="info" style="font-size: 16px;">
        <table>
            <tr>
                <td>金额(元)</td>
                <td class="fr">
                <input type="number" name="amount" placeholder="充值金额（单位元）" style="width: 100%">
                </td>
            </tr>
            <tr>
                <td>支付方式</td>
                <td class="fr">
                    <select name="payType" style="width: 100%">
                        <option value="FAST_PAY" selected="selected">快捷</option>
                        <option value="GATEWAY">网关</option>
                        <option value="ALIPAY_WAP">支付宝H5</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td class="fr" colspan="2">
                    <button type="submit" style="width: 100px;height: 28px;">继续</button>
                </td>
            </tr>
        </table>
    </div>
</form>
</body>
</html>