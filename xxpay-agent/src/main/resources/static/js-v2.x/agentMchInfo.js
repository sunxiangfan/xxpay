layui.define(['form', 'table', 'myUpload', 'myList', 'laytpl', 'agentMchPayChannel', 'layer', 'subMchInfo'], function (exports) {
    "use strict";

    var $ = layui.$;


    var mod_name = 'agentMchInfo',
        Model = function () {
            var config = {
                title: '商户'
                , listUrl: '/agent_mch_info/list'
                , viewUrl: '/agent_mch_info/view.html'
                , editUrl: '/agent_mch_info/edit.html'
                , saveUrl: '/agent_mch_info/save'
                , keyName: 'id'
                , toolbar: '#toolbarTop'
                , cols: [[
                    {type: 'checkbox'}
                    , {title: '操作', toolbar: '#barCtrl'}
                    , {field: 'id', title: '商户号'}
                    , {field: 'name', title: '商户名称'}
                    , {field: 'mobile', title: '手机号'}
                    , {field: 'state', title: '状态', templet: '#stateTpl'}
                    , {field: 'auditState', title: '审核状态', templet: '#auditStateTpl'}
                    , {field: 'createTime', title: '创建时间'}
                ]]
            };
            this.set(config);
        };
    Model.prototype = layui.myList();
    Model.prototype.add = function (data) {
        var scope = this;
        $.get(scope.config.editUrl, null, function (form) {
            var addBoxIndex = layer.open({
                type: 1,
                title: '添加' + scope.config.title,
                content: form,
                btn: ['保存', '取消'],
                shade: false,
                offset: ['100px', '30%'],
                area: ['600px', '450px'],
                zIndex: 19950924,
                maxmin: false,
                yes: function (index) {
                    //触发表单的提交事件
                    $('form.layui-form').find('button[lay-filter=edit]').click();
                },
                full: function (elem) {
                    var win = window.top === window.self ? window : parent.window;
                    $(win).on('resize', function () {
                        var $this = $(this);
                        elem.width($this.width()).height($this.height()).css({
                            top: 0,
                            left: 0
                        });
                        elem.children('div.layui-layer-content').height($this.height() - 95);
                    });
                },
                success: function (layero, index) {
                    //弹出窗口成功后渲染表单
                    var form = layui.form;
                    form.render();
                    //身份证正面
                    var idCardFrontUpload = layui.myUpload({
                        name: 'idCardFrontUrl'
                        ,url:'/file/upload'
                        ,elem:'#idCardFrontUrlContent'
                    });
                    idCardFrontUpload.render();

                    //身份证反面
                    var idCardBackUpload = layui.myUpload({
                        name: 'idCardBackUrl'
                        ,url:'/file/upload'
                        ,elem:'#idCardBackUrlContent'
                    });
                    idCardBackUpload.render();


                    //营业执照
                    var businessNoUrlUpload = layui.myUpload({
                        name: 'businessNoUrl'
                        ,url:'/file/upload'
                        ,elem:'#businessNoUrlContent'
                    });
                    businessNoUrlUpload.render();

                    form.on('submit(edit)', function (data) {
                        //这里可以写ajax方法提交表单
                        $.ajax({
                            type: "POST",
                            url: scope.config.saveUrl,
                            data: "params=" + JSON.stringify(data.field),
                            success: function (res) {
                                if (res.state === 0) {
                                    scope.layerTips.msg('保存成功');
                                    layer.close(index);
                                    // location.reload(); //刷新
                                    table.reload('table');
                                } else {
                                    scope.layerTips.msg(res.msg);
                                    layer.close(index);
                                    // location.reload(); //刷新
                                    // table.reload('table');
                                    scope.tableReload();
                                }
                            }
                        });
                        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
                    });
                },
                end: function () {
                    addBoxIndex = -1;
                }
            });
            layer.full(addBoxIndex);
        });
    }
    Model.prototype.addPayChannel = function (data) {
        var scope = this;
        var agentMchPayChannel = layui.agentMchPayChannel();
        agentMchPayChannel.add({mchId: data[scope.config.keyName]});
    }
    Model.prototype.managePayChannel = function (data) {
        var scope = this;
        var id = data[scope.config.keyName];
        var addBoxIndex = layer.open({
            type: 2,
            title: '商户通道管理',
            content: '/agent_mch_pay_channel/list.html?mchId=' + id,
            shade: false,
            offset: ['100px', '30%'],
            area: ['600px', '450px'],
            zIndex: 19950924,
            maxmin: false,
            full: function (elem) {
                var win = window.top === window.self ? window : parent.window;
                $(win).on('resize', function () {
                    var $this = $(this);
                    elem.width($this.width()).height($this.height()).css({
                        top: 0,
                        left: 0
                    });
                    elem.children('div.layui-layer-content').height($this.height() - 95);
                });
            }
        });
        layer.full(addBoxIndex);
    }
    Model.prototype.addSubMch = function (data) {
        var scope = this;
        var instance = layui.subMchInfo();
        instance.add({parentId: data[scope.config.keyName]});
        return;
    }

    Model.prototype.manageSubMch = function (data) {
        var scope = this;
        var id = data[scope.config.keyName];
        var addBoxIndex = layer.open({
            type: 2,
            title: '子商户管理',
            content: '/sub_mch_info/list.html?parentId=' + id,
            shade: false,
            offset: ['100px', '30%'],
            area: ['600px', '450px'],
            zIndex: 19950924,
            maxmin: false,
            full: function (elem) {
                var win = window.top === window.self ? window : parent.window;
                $(win).on('resize', function () {
                    var $this = $(this);
                    elem.width($this.width()).height($this.height()).css({
                        top: 0,
                        left: 0
                    });
                    elem.children('div.layui-layer-content').height($this.height() - 95);
                });
            }
        });
        layer.full(addBoxIndex);
    }
    exports(mod_name, function (options) {
        var model = new Model();
        return model.set(options);
    });

});