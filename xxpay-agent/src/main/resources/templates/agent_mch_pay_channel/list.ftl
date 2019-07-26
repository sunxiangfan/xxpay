<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>商户通道列表</title>
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
        <legend>${mchId?if_exists} 商户通道列表</legend>
        <div class="layui-field-box layui-form">
            <table class="layui-hide" id="table" lay-filter="table"></table>
        </div>
    </fieldset>
</div>

<script type="text/html" id="barCtrl">
    <div class="layui-btn-container">
        <a class="layui-btn layui-btn-xs" lay-event="view">详情</a>
    </div>
</script>

<script type="text/html" id="toolPayTypeNameTpl">
    {{# if(d.payChannel.payType === 'FAST_PAY'){ }} <span style="color: red">快捷</span> {{# } }}
    {{# if(d.payChannel.payType === 'GATEWAY'){ }} <span style="color: red">网关</span> {{# } }}
    {{# if(d.payChannel.payType === 'ALIPAY_WAP'){ }} <span style="color: red">支付宝H5</span> {{# } }}
</script>
<script type="text/javascript" src="../plugins/layui-v2.4.5/layui.js"></script>
<script>
    layui.config({
        base: '/js-v2.x/'
    }).use(['myList', 'agentMchPayChannel'], function () {
        var $ = layui.$;
        var instance = layui.agentMchPayChannel({
            where: {
                mchId: '${mchId?if_exists}'
            }
        });

        instance.initTable();
        $('#add').on('click', function () {
            instance.add();
        });

        $('#search').on('click', function () {
            instance.tableReload({
                mchId: $('#mchId').val()
                , payChannelId: $('#payChannelId').val()
            });
        });
    });

</script>
</body>

</html>