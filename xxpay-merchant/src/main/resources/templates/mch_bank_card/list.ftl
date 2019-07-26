<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>商户银行卡列表</title>
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
        保证金金额：<span class="layui-badge layui-bg-orange" >${depositAmount!0 } 元</span>
        <i class="layui-icon layui-icon-help" style="font-size: 15px;color: #393d49;" title="保证金是T1待结算部分。可至保证金明细中查看详情。"></i>
        可提现金额：<span class="layui-badge layui-bg-red">${effectiveBalance!0 } 元</span>
        <i class="layui-icon layui-icon-help" style="font-size: 15px;color: #393d49;" title="可提现金额是指可直接代付，提现的部分"></i>
        冻结金额：<span class="layui-badge layui-bg-cyan">${lockAmount!0 } 元</span>
        <i class="layui-icon layui-icon-help" style="font-size: 15px;color: #393d49;" title="冻结金额是暂不可用部分，三方冻结资金。可在资金变动明细中查看"></i>
        <br>
        提现手续费单笔：<span class="layui-badge layui-bg-green">${cashDeduction !5} 元</span>
        <div class="layui-form" style="margin-top: 15px;">
            <div class="layui-form-item" style="margin:0;">
                <div class="layui-input-inline">
                    <input type="number" name="applyAmount" id="applyAmount" autocomplete="off" class="layui-input"
                           min="100" max="50000" step="1" placeholder="输入提现金额">
                </div>
                <div class="layui-input-inline">
                    <button id="apply" lay-filter="search" class="layui-btn" lay-submit><i class="fa fa-search"
                                                                                           aria-hidden="true"></i> 申请提现
                    </button>
                </div>
            </div>
        </div>
    </blockquote>

    <fieldset class="layui-elem-field">

        <legend>银行卡列表</legend>
        <div class="layui-field-box layui-form">
            <table class="layui-hide" id="table" lay-filter="table"></table>
        </div>
    </fieldset>
</div>

<script type="text/html" id="barTypeTpl">
    {{# if(d.type === 1){ }} <span style="color: green">对公</span> {{# } }}
    {{# if(d.type === 0){ }} <span style="color: red">对私</span> {{# } }}
</script>

<script type="text/html" id="toolbarTop">
    <div class="layui-btn-container">
        <a class="layui-btn layui-btn-xs" lay-event="add">添加银行卡</a>
    </div>
    <div class="layui-form">
        <div class="layui-form-item">
            <div class="layui-input-inline">
                <input type="text" name="number" id="number" autocomplete="off" class="layui-input"
                       placeholder="搜索银行卡号">
            </div>
            <div class="layui-input-inline">
                <input type="text" name="accountName" id="accountName" autocomplete="off" class="layui-input"
                       placeholder="搜索账户名">
            </div>
            <div class="layui-input-inline">
                <button class="layui-btn" lay-event="search"><i class="fa fa-search" aria-hidden="true"></i> 查询
                </button>
            </div>
</script>

<script type="text/html" id="barCtrl">
    <div class="layui-btn-container">
        <a class="layui-btn layui-btn-xs" lay-event="view">详情</a>
        <a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
        <a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="del">删除</a>
    </div>
</script>
<script type="text/javascript" src="../plugins/layui-v2.4.5/layui.js"></script>
<script>
    layui.config({
        base: '/js-v2.x/',
        version: new Date().getTime()
    }).use(['myList', 'mchBankCard'], function () {
        var $ = layui.$;
        var instance = layui.mchBankCard({balance: '${balance}'});

        instance.initTable();
        $('#add').on('click', function () {
            instance.add();
        });
        $('#apply').on('click', function () {
            instance.cashApply();
        });
        var $ = layui.$;

    });
</script>
</body>

</html>