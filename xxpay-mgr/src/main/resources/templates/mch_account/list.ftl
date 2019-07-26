<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>商户账户</title>
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
    <blockquote class="layui-elem-quote">
        账户余额：<span class="layui-badge layui-bg-green">${balance!0 } 元</span>
        保证金金额：<span class="layui-badge layui-bg-orange" title="T1结算部分。可至保证金明细中查看详情。">${depositAmount!0 } 元</span>
        可提现金额：<span class="layui-badge layui-bg-red">${effectiveBalance!0 } 元</span>
        <br><br>
        <div class="layui-form">
            <div class="layui-form-item" style="margin:0;">
                <div class="layui-input-inline">
                    <input type="text" name="mchId" id="mchId" autocomplete="off" class="layui-input" placeholder="商户号">
                </div>
                <div class="layui-input-inline">
                    <button id="search" lay-filter="search" class="layui-btn" lay-submit><i class="fa fa-search"
                                                                                            aria-hidden="true"></i> 查询
                    </button>
                </div>
            </div>
        </div>
    </blockquote>

    <fieldset class="layui-elem-field">
        <legend>商户账户</legend>
        <div class="layui-field-box layui-form">
            <table class="layui-hide" id="table" lay-filter="table"></table>
        </div>
    </fieldset>
</div>

<script type="text/html" id="barCtrl">
    <div class="layui-btn-container">
        <a class="layui-btn layui-btn-xs" lay-event="lockAmount" title="冻结"><i
                    class="layui-icon layui-icon-link"></i></a>
        <a class="layui-btn layui-btn-xs" lay-event="unlockAmount" title="解冻"><i
                    class="layui-icon layui-icon-unlink"></i></a>
        <a class="layui-btn layui-btn-xs" lay-event="adjustIncreaseAmount" title="手工补入账"><i
                    class="layui-icon layui-icon-up"></i></a>
        <a class="layui-btn layui-btn-xs" lay-event="adjustDecreaseAmount" title="手工补出账"><i
                    class="layui-icon layui-icon-down"></i></a>
    </div>
</script>
<script type="text/javascript" src="../plugins/layui-v2.4.5/layui.js"></script>
<script>
    layui.config({
        base: '/js-v2.x/',
        version: new Date().getTime()
    }).use(['mchAccount'], function () {
        var $ = layui.$;
        var instance = layui.mchAccount();

        instance.initTable();

        $('#search').on('click', function () {
            instance.tableReload({
                id: $('#mchId').val()
            });
        });
    });
</script>
</body>

</html>