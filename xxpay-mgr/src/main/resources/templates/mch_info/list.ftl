<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>子商户列表</title>
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
                    <input type="text" name="id" id="id" autocomplete="off" class="layui-input" placeholder="子商户号">
                </div>
                <div class="layui-input-inline">
                    <#if agentId?exists>
                        <input type="text" name="agentId" id="agentId" autocomplete="off" class="layui-input"
                        value="${agentId}" disabled="disabled" placeholder="代理">
                    <#else >
                        <input type="text" name="agentId" id="agentId" autocomplete="off" class="layui-input" placeholder="代理">
                    </#if>
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
        <legend>${agentId? if_exists} 子商户列表</legend>
        <div class="layui-field-box layui-form">
            <table class="layui-hide" id="table" lay-filter="table"></table>
        </div>
    </fieldset>
</div>
<script type="text/html" id="stateTpl">
    {{# if(d.state === 1){ }} <span style="color: green">启用</span> {{# } }}
    {{# if(d.state === 0){ }} <span style="color: red">停止</span> {{# } }}
</script>

<script type="text/html" id="auditStateTpl">
    {{# if(d.auditState === 2){ }} <span style="color: red">未通过审核</span> {{# } }}
    {{# if(d.auditState === 1){ }} <span style="color: green">已审核</span> {{# } }}
    {{# if(d.auditState === 0){ }} <span style="color: red">未审核</span> {{# } }}
</script>

<script type="text/html" id="barCtrl">
    <div class="layui-btn-container">
        <a class="layui-btn layui-btn-xs" lay-event="view">详情</a>
        <a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
        <a class="layui-btn layui-btn-xs" lay-event="addPayChannel">添加通道</a>
        <a class="layui-btn layui-btn-xs" lay-event="managePayChannel">管理通道</a>
    </div>
</script>


<script type="text/html" id="uploadTpl">
    <div class="layui-upload">
        <button type="button" class="layui-btn test1" id="test1{{d.name}}">上传图片</button>
        <div class="layui-upload-list">
            <img class="layui-upload-img" id="demo1{{d.name}}" width="{{d.width}}" height="{{d.height}}">
            <p id="demoText{{d.name}}"></p>

            <input type="hidden" name="{{d.name}}" autocomplete="off" class="layui-input"
                   id="hidden{{d.name}}">
        </div>
    </div>
</script>

<script type="text/javascript" src="../plugins/layui-v2.4.5/layui.js"></script>
<script>
    layui.config({
        base: '/js-v2.x/',
        version: new Date().getTime()
    }).use(['mchInfo'], function () {
        var $ = layui.$;
        var instance = layui.mchInfo({
            where:{
                agentId:'${agentId? if_exists}'
            }
        });

        instance.initTable();

        $('#add').on('click', function () {
            instance.add();
        });

        $('#search').on('click', function () {
            instance.tableReload({
                id: $('#id').val()
                , agentId: $('#agentId').val()
            });
        });

    });
</script>
</body>

</html>