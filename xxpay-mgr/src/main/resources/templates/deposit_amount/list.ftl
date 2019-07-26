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

    <blockquote class="layui-elem-quote">
        <div class="layui-form">
            <div class="layui-form-item" style="margin:0;">
                <div class="layui-input-inline">
                    <input type="text" class="layui-input" id="dateRange" placeholder="计划解冻日期范围">
                    <input type="hidden" id="dateFrom" name="dateFrom">
                    <input type="hidden" id="dateTo" name="dateTo">
                </div>
                <div class="layui-input-inline">
                    <select name="status" id="state" lay-search="">
                        <option value="-99">所有状态</option>
                        <option value="0">未解冻</option>
                        <option value="1">已解冻</option>
                    </select>
                </div>
                <div class="layui-input-inline">
                    <input type="text" name="mchId" id="mchId" autocomplete="off" class="layui-input" placeholder="商户号">
                </div>
                <button id="search" lay-filter="search" class="layui-btn" lay-submit><i class="fa fa-search" aria-hidden="true"></i> 查询</button>
            </div>
        </div>
    </blockquote>

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

<script type="text/html" id="toolbarTop">
    <div class="layui-btn-container">
        <a class="layui-btn layui-btn-xs" lay-event="unlock">解冻</a>
    </div>
</script>
<script type="text/javascript" src="../plugins/layui-v2.4.5/layui.js"></script>
<script>
    layui.config({
        base: '/js-v2.x/',
        version: new Date().getTime()
    }).use(['depositAmount', 'laydate'], function () {
        var instance = layui.depositAmount();
        instance.initTable();
        var $ = layui.$;
        var laydate = layui.laydate;
//日期范围
        laydate.render({
            elem: '#dateRange'
            , range: true
            , done: function (value, date, endDate) {
                if (!value) {
                    $("#dateFrom").val(null);
                    $("#dateTo").val(null);
                    return;
                }
                var reg = /^(\d{4}-\d{2}-\d{2}) - (\d{4}-\d{2}-\d{2})$/;
                if (!reg.test(value)) {
                    instance.layerTips.alert('日期格式有误！value: ' + value);
                    return;
                }
                var dateFrom = value.replace(reg, "$1");
                var dateTo = value.replace(reg, "$2");
                $("#dateFrom").val(dateFrom);
                $("#dateTo").val(dateTo);
            }
        });
        $('#search').on('click', function () {
            instance.tableReload({
                mchId: $('#mchId').val()
                ,state: $('#state').val()
                , dateFrom: $('#dateFrom').val()
                , dateTo: $('#dateTo').val()
            });
        });
    });
</script>
</body>

</html>