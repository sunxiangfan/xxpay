<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>商户通道列表</title>
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
                <label class="layui-form-label">商户号</label>
                <div class="layui-input-inline">
                    <#if mchId?exists>
                        <input type="text" name="mchId" id="mchId" autocomplete="off" class="layui-input"
                        value="${mchId}" disabled="disabled">
                    <#else >
                        <input type="text" name="mchId" id="mchId" autocomplete="off" class="layui-input">
                    </#if>
                </div>
                <label class="layui-form-label">通道id</label>
                <div class="layui-input-inline">
                    <input type="text" name="payChannelId" id="payChannelId" autocomplete="off" class="layui-input">
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
        <legend>${mchId?if_exists} 商户通道列表</legend>
        <div class="layui-field-box layui-form">
            <table class="layui-hide" id="table" lay-filter="table"></table>
        </div>
    </fieldset>
</div>
<script type="text/html" id="barCtrl">
    <div class="layui-btn-container">
        <a class="layui-btn layui-btn-xs" lay-event="view">详情</a>
        <a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
    </div>
</script>

<script type="text/html" id="stateTpl">
    {{# if(d.state === 1){ }} <span style="color: green">启用</span> {{# } }}
    {{# if(d.state === 0){ }} <span style="color: red">停止</span> {{# } }}
</script>
<script type="text/html" id="barPayType">
    {{# if(d.payChannel.payType === 'FAST_PAY'){ }} <span style="color: green"><i class="layui-icon layui-icon-cellphone" ></i>  快捷</span> {{#  } }}
    {{# if(d.payChannel.payType === 'GATEWAY'){ }} <span style="color: blue"><i class="layui-icon layui-icon-auz" ></i>网关</span> {{#  } }}
    {{# if(d.payChannel.payType === 'ALIPAY_WAP'){ }} <span style="color: blue"><i class="layui-icon layui-icon-auz" ></i>支付宝H5</span> {{#  } }}
</script>
<script type="text/javascript" src="../plugins/layui-v2.4.5/layui.js"></script>
<script>
    layui.config({
        base: '/js-v2.x/',
        version: new Date().getTime()
    }).use(['myList', 'agentMchPayChannel'], function () {
        var $ = layui.$;
        var instance = layui.agentMchPayChannel({
            where:{
                mchId:'${mchId?if_exists}'
            }
        });

        instance.initTable();
        $('#add').on('click', function () {
            instance.add();
        });

        $('#search').on('click', function () {
            instance.tableReload({
                mchId: $('#mchId').val()
                , payChannelId: $('#payChannelId').val()
            });
        });
    });
</script>
</body>

</html>