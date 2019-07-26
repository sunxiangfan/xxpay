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
        <div class="layui-btn layui-btn-small" id=""></div>
        <div class="layui-form" style="float:right;">
            <div class="layui-form-item" style="margin:0;">
                <label class="layui-form-label">支付单号</label>
                <div class="layui-input-inline">
                    <input type="text" name="id" id="id" autocomplete="off" class="layui-input">
                </div>
                <label class="layui-form-label">商户ID</label>
                <div class="layui-input-inline">
                    <input type="text" name="mchId" id="mchId" autocomplete="off" class="layui-input">
                </div>
                <label class="layui-form-label">订单状态</label>
                <div class="layui-input-inline">
                    <select name="status" id="status" lay-search="">
                        <option value="-99">所有状态</option>
                        <option value="0">订单生成</option>
                        <option value="1">支付中</option>
                        <option value="2">支付成功</option>
                        <option value="3">处理完成</option>
                    </select>
                </div>
                <div class="layui-form-mid layui-word-aux" style="padding:0;">
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

<script type="text/html" id="toolStatusTpl">
    {{# if(d.status === 0){ }} <span style="color: black">订单生成</span> {{#  } }}
    {{# if(d.status === 1){ }} <span style="color: blue">支付中</span> {{#  } }}
    {{# if(d.status === 2){ }} <span style="color: green">支付成功</span> {{#  } }}
    {{# if(d.status === 3){ }} <span style="color: orange">处理完成</span> {{#  } }}
</script>


<script type="text/html" id="barCtrl">
    {{# if( true ){ }} <a class="layui-btn layui-btn-xs" lay-event="view">详情</a> {{#  } }}
</script>
<script type="text/javascript" src="../plugins/layui-v2.4.5/layui.js"></script>
<script>
    layui.config({
        base: '/js-v2.x/',
        version: new Date().getTime()
    }).use(['payOrder'], function () {
        var instance = layui.payOrder();
        instance.initTable();
        var $ = layui.$;

        $('#search').on('click', function () {
            instance.tableReload({
                mchId: $('#mchId').val()
                , id:$('#id').val()
                , status: $('#status').val()
            });
        });
    });
</script>
</body>

</html>