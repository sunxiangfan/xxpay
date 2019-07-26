<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>系统参数列表</title>
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
        <legend>系统配置列表</legend>
        <div class="layui-field-box layui-form">
            <table class="layui-hide" id="table" lay-filter="table"></table>
        </div>
    </fieldset>
</div>
<!--模板-->
<script type="text/html" id="barCtrl">
    <a class="layui-btn layui-btn-xs" lay-event="view">详情</a>
    {{# if(d.flagEditable === 1){ }} <a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a> {{#  } }}
</script>
<script type="text/html" id="barFlagEditable">
    {{# if(d.flagEditable === 1){ }} <span style="color: green">是</span> {{#  } }}
    {{# if(d.flagEditable === 0){ }} <span style="color: red">否</span> {{#  } }}
</script>
<script type="text/html" id="barFlagNullable">
    {{# if(d.flagNullable === 1){ }} <span style="color: green">是</span> {{#  } }}
    {{# if(d.flagNullable === 0){ }} <span style="color: red">否</span> {{#  } }}
</script>
<script type="text/html" id="barFlagMoney">
    {{# if(d.flagMoney === 1){ }} <span style="color: green">是</span> {{#  } }}
    {{# if(d.flagMoney === 0){ }} <span style="color: red">否</span> {{#  } }}
</script>
<script type="text/html" id="barFlagNumber">
    {{# if(d.flagNumber === 1){ }} <span style="color: green">是</span> {{#  } }}
    {{# if(d.flagNumber === 0){ }} <span style="color: red">否</span> {{#  } }}
</script>
<script type="text/javascript" src="../plugins/layui-v2.4.5/layui.js"></script>
<script>
    layui.config({
        base: '/js-v2.x/',
        version: new Date().getTime()
    }).use(['systemSetting'], function () {
        var $ = layui.$;
        var constance = layui.systemSetting();
        constance.initTable();
    });
</script>
</body>

</html>