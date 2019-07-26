<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>支付订单</title>
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
                    <input type="text" class="layui-input" id="dateRange" placeholder="订单创建日期范围">
                    <input type="hidden" id="dateFrom" name="dateFrom">
                    <input type="hidden" id="dateTo" name="dateTo">
                </div>
                <div class="layui-input-inline">
                    <input type="text" name="id" id="id" autocomplete="off" class="layui-input" placeholder="支付单号">
                </div>
                <div class="layui-input-inline">
                    <input type="text" name="mchOrderNo" id="mchOrderNo" autocomplete="off" class="layui-input" placeholder="商户单号">
                </div>
                <div class="layui-input-inline">
                    <input type="text" name="mchId" id="mchId" autocomplete="off" class="layui-input" placeholder="商户号">
                </div>
                <div class="layui-input-inline">
                    <select name="status" id="status" lay-search="">
                        <option value="-99">所有订单状态</option>
                        <option value="0">订单生成</option>
                        <option value="1">支付中</option>
                        <option value="2">支付成功</option>
                        <option value="3">处理完成</option>
                    </select>
                </div>
                <div class="layui-input-inline">
                    <select name="payChannelId" id="payChannelId" lay-search="">
                        <option value="">所有支付通道</option>
                        <#list payChannels as payChannel>
                            <option value="${payChannel.id}">${payChannel.label} ( ${payChannel.code} )</option>
                        </#list>
                    </select>
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
        <legend>订单列表</legend>
        <div class="layui-field-box layui-form">
            <table class="layui-hide" id="table" lay-filter="table"></table>
        </div>
    </fieldset>

</div>

<script type="text/html" id="barStatus">
    {{# if(d.status === 0){ }} <span style="color: black">订单生成</span> {{#  } }}
    {{# if(d.status === 1){ }} <span style="color: red">支付中</span> {{#  } }}
    {{# if(d.status === 2){ }} <span style="color: blue">支付成功</span> {{#  } }}
    {{# if(d.status === 3){ }} <span style="color: green">处理完成</span> {{#  } }}
</script>

<script type="text/html" id="barPayType">
    {{# if(d.payChannel.payType === 'FAST_PAY'){ }} <span style="color: green"><i
                class="layui-icon layui-icon-cellphone"></i>  快捷</span> {{#  } }}
    {{# if(d.payChannel.payType === 'GATEWAY'){ }} <span style="color: blue"><i class="layui-icon layui-icon-auz"></i>网关</span> {{#  } }}
    {{# if(d.payChannel.payType === 'ALIPAY_WAP'){ }} <span style="color: blue"><i class="layui-icon layui-icon-auz"></i>支付宝H5</span> {{#  } }}
</script>

<script type="text/html" id="barCtrl">
    {{# if( true ){ }} <a class="layui-btn layui-btn-xs" lay-event="view">详情</a> {{#  } }}
    {{# if(d.status === 2 || d.status === 3  ){ }} <a class="layui-btn layui-btn-xs"
                                                      lay-event="renotify">补发通知</a> {{#  } }}
</script>

<script type="text/javascript" src="../plugins/layui-v2.4.5/layui.js"></script>
<script>
    layui.config({
        base: '/js-v2.x/',
        version: new Date().getTime()
    }).use(['payOrder', 'laydate'], function () {
        var $ = layui.$;
        var laydate = layui.laydate;
        var instance = layui.payOrder();

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
                mchId: $('#mchId').val()
                , id: $('#id').val()
                , mchOrderNo: $('#mchOrderNo').val()
                , payChannelId: $('#payChannelId').val()
                , status: $('#status').val()
                , dateFrom: $('#dateFrom').val()
                , dateTo: $('#dateTo').val()
            });
        });
    });
</script>
</body>

</html>