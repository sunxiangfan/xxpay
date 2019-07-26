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
        <div class="layui-btn layui-btn-small" id=""></div>
        <div class="layui-form" style="float:right;">
            <div class="layui-form-item" style="margin:0;">
                <label class="layui-form-label">商户号</label>
                <div class="layui-input-inline">
                    <input type="text" name="mchId" id="mchId" autocomplete="off" class="layui-input">
                </div>
                <label class="layui-form-label">银行卡号</label>
                <div class="layui-input-inline">
                    <input type="text" name="number" id="number" autocomplete="off" class="layui-input">
                </div>
                <button id="search" lay-filter="search" class="layui-btn" lay-submit><i class="fa fa-search" aria-hidden="true"></i> 查询</button>
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

<script type="text/html" id="barCtrl">
    <div class="layui-btn-container">
        <a class="layui-btn layui-btn-xs" lay-event="view">详情</a>
    </div>
</script>
<script type="text/javascript" src="../plugins/layui-v2.4.5/layui.js"></script>
<script>
    layui.config({
        base: '/js-v2.x/',
        version: new Date().getTime()
    }).use(['myList', 'mchBankCard'], function () {
        var $ = layui.$;
        var instance = layui.mchBankCard();

        instance.initTable();
        $('#add').on('click', function () {
            instance.add();
        });
        $('#search').on('click', function () {
            instance.tableReload({
                mchId: $('#mchId').val()
                ,number: $('#number').val()
            });
        });
    });
</script>
</body>

</html>