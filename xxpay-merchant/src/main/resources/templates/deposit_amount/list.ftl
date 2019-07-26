<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>保证金明细</title>
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
        <legend>保证金明细</legend>
        <div class="layui-field-box layui-form">
            <table class="layui-hide" id="table" lay-filter="table"></table>
        </div>
    </fieldset>
</div>

<script type="text/html" id="toolStateTpl">
    {{# if(d.state === 0){ }} <span style="color: red">未解冻</span> {{#  } }}
    {{# if(d.state === 1){ }} <span style="color: green">已解冻</span> {{#  } }}
</script>

<script type="text/javascript" src="../plugins/layui-v2.4.5/layui.js"></script>
<script>
    layui.config({
        base: '/js-v2.x/',
        version: new Date().getTime()
    }).use(['depositAmount'], function () {
        var instance = layui.depositAmount();
        instance.initTable();
        var $ = layui.$;

    });
</script>
</body>

</html>