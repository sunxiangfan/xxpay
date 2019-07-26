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
        <legend>结算明细列表</legend>
        <div class="layui-field-box layui-form">
            <table class="layui-hide" id="table" lay-filter="table"></table>
        </div>
    </fieldset>
</div>

<script type="text/html" id="toolStateTpl">
    {{# if(d.state === 0){ }} <span style="color: #1E9FFF">审核中</span> {{# } }}
    {{# if(d.state === 1){ }} <span style="color: green">审核通过</span> {{# } }}
    {{# if(d.state === 2){ }} <span style="color: red">审核未通过</span> {{# } }}
</script>
<script type="text/javascript" src="../plugins/layui-v2.4.5/layui.js"></script>
<script>
    layui.config({
        base: '/js-v2.x/'
    }).use(['myList', 'agentMchCashApply'], function () {
        var $ = layui.$;
        var instance = layui.agentMchCashApply();

        instance.initTable();

    });

</script>
</body>

</html>