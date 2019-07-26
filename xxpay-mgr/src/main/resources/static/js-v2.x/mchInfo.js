layui.define(['form', 'table', 'common', 'colsconfig', 'myList', 'mchPayChannel'], function (exports) {
    "use strict";
    var topWin = (function () {
        var p = window.parent;
        while (p != p.window.parent) {
            p = p.window.parent;
        }
        return p;
    })();
    var $ = layui.$,
        layerTips = topWin.layer === undefined ? layui.layer : topWin.layer, //获取父窗口的layer对象
        layer = layui.layer,
        form = layui.form,
        table = layui.table,
        common = layui.common,
        colsconfig = layui.colsconfg;

    var cols = [[
        {type: 'radio'}
        , {title: '操作', toolbar: '#barCtrl', width: 240}
        , {field: 'agentId', title: '上级商户号'}
        , {field: 'id', title: '商户号'}
        , {field: 'name', title: '商户名称'}
        , {field: 'mobile', title: '手机号'}
        , {field: 'email', title: '邮箱'}
        , {field: 'd0Rate', title: 'D0比例(%)'}
        , {field: 'state', title: '状态', templet: '#stateTpl'}
        , {field: 'auditState', title: '审核状态', templet: '#auditStateTpl'}
        , {field: 'createTime', title: '创建时间'}
    ]];
    var mod_name = 'mchInfo',
        Model = function () {
            var config = {
                title: '子商户'
                , listUrl: '/mch_info/list'
                , viewUrl: '/mch_info/view.html'
                , editUrl: '/mch_info/edit.html'
                , saveUrl: '/mch_info/save'
                , keyName: 'id'
                , elem: '#table'
                , toolbar: null
                , cols: cols
                , where: {
                    v: new Date().getTime()
                }
            }
            this.set(config);
        };

    var myList = layui.myList();
    Model.prototype = myList;
    Model.prototype.add = function (data) {
        var scope = this;
        var p = {};
        if (data) {
            $.extend(true, p, data);
        }
        $.get(scope.config.editUrl, p, function (form) {
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
                        , url: '/file/upload'
                        , elem: '#idCardFrontUrlContent'
                    });
                    idCardFrontUpload.render();

                    //身份证反面
                    var idCardBackUpload = layui.myUpload({
                        name: 'idCardBackUrl'
                        , url: '/file/upload'
                        , elem: '#idCardBackUrlContent'
                    });
                    idCardBackUpload.render();


                    //营业执照
                    var businessNoUrlUpload = layui.myUpload({
                        name: 'businessNoUrl'
                        , url: '/file/upload'
                        , elem: '#businessNoUrlContent'
                    });
                    businessNoUrlUpload.render();

                    $('#btnResetReqKey').on('click', function () {
                        $.get('/common/get_mch_key',null,function (res) {
                            $('#reqKey').val(res.data);
                            scope.layerTips.msg("重置成功！");
                        },'json')
                    });
                    $('#btnResetResKey').on('click', function () {
                        $.get('/common/get_mch_key',null,function (res) {
                            $('#resKey').val(res.data);
                            scope.layerTips.msg("重置成功！");
                        },'json')
                    });

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
                                    scope.tableReload();
                                } else {
                                    scope.layerTips.msg(res.msg);
                                    //layer.close(index);
                                    // location.reload(); //刷新
                                    // table.reload('table');
                                    //scope.tableReload();
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
    Model.prototype.managePayChannel = function (data) {
        var scope = this;
        var id = data[scope.config.keyName];
        var addBoxIndex = layer.open({
            type: 2,
            title: '子商户通道管理',
            content: '/mch_pay_channel/list.html?mchId=' + id,
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
    Model.prototype.addPayChannel = function (data) {
        var scope = this;
        var id = data.id;
        var instance = layui.mchPayChannel();
        instance.add({mchId: id});
    }
    exports(mod_name, function (options) {
        var model = new Model();
        return model.set(options);
    });

});