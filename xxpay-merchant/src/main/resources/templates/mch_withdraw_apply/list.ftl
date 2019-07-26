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
    <blockquote class="layui-elem-quote">
        今日成功金额：<span class="layui-badge layui-bg-green">${todayCash.successAmount!0 } 元</span>
        今日待结算：<span class="layui-badge layui-bg-orange">${todayCash.todoAmount!0} 元</span>
        今日成功笔数：<span class="layui-badge layui-bg-green">${todayCash.successCount!0} 笔</span>
        今日失败笔数：<span class="layui-badge ">${todayCash.failCount!0} 笔</span>
    </blockquote>
    <blockquote class="layui-elem-quote">
        总成功金额：<span class="layui-badge layui-bg-green">${totalCash.successAmount!0} 元</span>
        总待结算：<span class="layui-badge layui-bg-orange">${totalCash.todoAmount!0} 元</span>
        总成功笔数：<span class="layui-badge layui-bg-green">${totalCash.successCount!0} 笔</span>
        总失败笔数：<span class="layui-badge ">${totalCash.failCount!0} 笔</span>
    </blockquote>
    <blockquote class="layui-elem-quote">
        <div class="layui-form" >
            <div class="layui-form-item" style="margin:0;">
                <div class="layui-input-inline">
                    <input type="text" class="layui-input" id="dateRange" placeholder="订单创建日期范围">
                    <input type="hidden" id="dateFrom" name="dateFrom">
                    <input type="hidden" id="dateTo" name="dateTo">
                </div>
                <div class="layui-input-inline">
                    <input type="text" name="mchOrderNo" id="mchOrderNo" autocomplete="off" class="layui-input" placeholder="商户单号">
                </div>
                <div class="layui-input-inline">
                    <input type="text" name="id" id="id" autocomplete="off" class="layui-input" placeholder="平台单号">
                </div>
                <div class="layui-input-inline">
                    <input type="text" name="accountName" id="accountName" autocomplete="off" class="layui-input" placeholder="开户名">
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
<script type="text/html" id="toolCashStateTpl">
    {{# if(d.cashState === 0){ }} <span style="color: #1E9FFF">未处理</span> {{# } }}
    {{# if(d.cashState === 1){ }} <span style="color: green">处理中</span> {{# } }}
    {{# if(d.cashState === 2){ }} <span style="color: green">已下发</span> {{# } }}
    {{# if(d.cashState === 3){ }} <span style="color: red">审核未通过</span> {{# } }}
</script>
<script type="text/javascript" src="../plugins/layui-v2.4.5/layui.js"></script>
<script>
    layui.config({
        base: '/js-v2.x/',
        version: new Date().getTime()
    }).use(['agentMchCashApply', 'laydate'], function () {
        var laydate = layui.laydate;
        var $ = layui.$;
        var instance = layui.agentMchCashApply();

        instance.initTable();
        var $ = layui.$;
        //日期范围
        laydate.render({
            elem: '#dateRange'
            , range: true
            , done: function (value, date, endDate) {
                if(!value){
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
                , id:$('#id').val()
                , mchOrderNo: $('#mchOrderNo').val()
                , dateFrom: $('#dateFrom').val()
                , dateTo: $('#dateTo').val()
                , accountName: $('#accountName').val()
            });
        });
    });
</script>
</body>

</html>