<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>商户对账单</title>
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
                    <input type="text" class="layui-input" id="dateRange" placeholder="日期范围">
                    <input type="hidden" id="dateFrom" name="dateFrom">
                    <input type="hidden" id="dateTo" name="dateTo">
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
        <legend>商户对账单</legend>
        <div class="layui-field-box layui-form">
            <table class="layui-hide" id="table" lay-filter="table"></table>
        </div>
    </fieldset>

</div>

<script type="text/javascript" src="../plugins/layui-v2.4.5/layui.js"></script>
<script>
    layui.config({
        base: '/js-v2.x/',
        version: new Date().getTime()
    }).use(['statement', 'laydate'], function () {
        var $ = layui.$;
        var laydate = layui.laydate;
        var instance = layui.statement();

        instance.initTable();
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
                dateFrom: $('#dateFrom').val()
                , dateTo: $('#dateTo').val()
            });
        });
    });
</script>
</body>

</html>