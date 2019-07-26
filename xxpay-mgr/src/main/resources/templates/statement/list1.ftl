<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>结算明细列表</title>
    <link rel="stylesheet" href="../plugins/layui-v2.4.5/css/layui.css" media="all"/>
    <link rel="stylesheet" href="../css/global.css" media="all">
    <link rel="stylesheet" href="../plugins/font-awesome/css/font-awesome.min.css">
    <link rel="stylesheet" href="../css/table.css"/>
    <meta http-equiv="pragma" content="no-cache"/><!-- 禁止浏览器从本地机的缓存中调阅页面内容 -->
    <meta http-equiv="Cache-Control" content="no-cache, must-revalidate"/>
    <meta http-equiv="expires" content="0"/> <!-- 定义网页 缓存中的过期时间-->
</head>

<body>
<div class="admin-main">
    <fieldset class="layui-elem-field">
        <legend>对账单</legend>

        <table class="layui-table" lay-filter="parse-table-demo">
            <colgroup>
                <col>
                <col>
                <col>
                <col>
                <col>
                <col>
                <col>
                <col>
                <col>
            </colgroup>
            <thead>
            <tr>
                <th lay-data="{field:'mchId', width:140}">商户号</th>
                <th lay-data="{field:'mchInfo.name', width:140}">商户</th>
                <th lay-data="{field:'createDate', width:140}">日期</th>
                <th lay-data="{field:'totalCount', width:140}">总订单数</th>
                <th lay-data="{field:'successCount', width:140}">成功订单数</th>
                <th lay-data="{field:'failCount', width:140}">未支付订单数</th>
                <th lay-data="{field:'applyAmount', width:140}">订单总额</th>
                <th lay-data="{field:'successAmount', width:140}">订单实付总额</th>
                <th lay-data="{field:'successActualAmount', width:140}">商户到账总额</th>
            </tr>
            </thead>
            <tbody>
            <#list list as row>
                <tr>
                    <td>${row.mchId}</td>
                    <td>${row.mchInfo.name}</td>
                    <td>${row.createDate}</td>
                    <td>${row.totalCount}</td>
                    <td>${row.successCount}</td>
                    <td>${row.failCount}</td>
                    <td>${row.applyAmount}</td>
                    <td>${row.successAmount}</td>
                    <td>${row.successActualAmount}</td>
                </tr>
            </#list>
            </tbody>
        </table>
    </fieldset>

</div>

<script type="text/javascript" src="../plugins/layui-v2.4.5/layui.js"></script>
<script>
    layui.use('table', function () {
        var table = layui.table;

        var $ = layui.$, active = {
            parseTable: function () {
                table.init('parse-table-demo', { //转化静态表格
                    height: 'full'
                    // height: 315 //设置高度
                     ,limit: ${list?size} //注意：请务必确保 limit 参数（默认：10）是与你服务端限定的数据条数一致
                    , toolbar: true
                });
            }
        };

        setTimeout(function () {
            active.parseTable();
        }, 10)

    });
</script>
</body>

</html>