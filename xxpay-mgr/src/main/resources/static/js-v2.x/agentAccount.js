layui.define(['form', 'table', 'myUpload', 'myList', 'laytpl', 'layer'], function (exports) {
    "use strict";

    var $ = layui.$,
        table = layui.table,
        form = layui.form;


    var mod_name = 'agentAccount',
        Model = function () {
            var config = {
                title: '代理账户'
                , listUrl: '/agent_account/list'
                , viewUrl: '/agent_account/view.html'
                , editUrl: '/agent_account/edit.html'
                , saveUrl: '/agent_account/save'
                , lockAmountUrl: '/agent_account/lock_amount.html'
                , unlockAmountUrl: '/agent_account/unlock_amount.html'
                , saveLockAmountUrl: '/agent_account/lock_amount'
                , saveUnlockAmountUrl: '/agent_account/unlock_amount'
                , adjustIncreaseAmountUrl: '/agent_account/adjust_increase_amount.html'
                , saveAdjustIncreaseAmountUrl: '/agent_account/adjust_increase_amount'
                , adjustDecreaseAmountUrl: '/agent_account/adjust_decrease_amount.html'
                , saveAdjustDecreaseAmountUrl: '/agent_account/adjust_decrease_amount'
                , keyName: 'id'
                , cols: [[
                    {type: 'radio'}
                    , {title: '操作', toolbar: '#barCtrl'}
                    , {field: 'id', title: '商户号'}
                    , {field: 'name', title: '商户名称'}
                    , {field: 'mobile', title: '手机号'}
                    , {field: 'balance', title: '余额'}
                    , {field: 'enableCashAmount', title: '可提现余额'}
                    , {field: 'depositAmount', title: '保证金'}
                    , {field: 'cashingAmount', title: '提现中金额'}
                    , {field: 'lockAmount', title: '冻结金额'}
                    , {field: 'createTime', title: '创建时间'}
                ]]
            };
            this.set(config);
        };
    Model.prototype = layui.myList();
    Model.prototype.lockAmount = function (data) {
        var scope = this;
        var p = {
            mchId: data.id
        };
        $.get(scope.config.lockAmountUrl, p, function (form) {
            var addBoxIndex = layer.open({
                type: 1,
                title: '冻结金额',
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
                    form.on('submit(edit)', function (data) {
                        //这里可以写ajax方法提交表单
                        $.ajax({
                            type: "POST",
                            url: scope.config.saveLockAmountUrl,
                            data: "params=" + JSON.stringify(data.field),
                            success: function (res) {
                                if (res.state === 0) {
                                    scope.layerTips.msg('保存成功');
                                    scope.layer.close(index);
                                    // location.reload(); //刷新
                                    table.reload('table');
                                } else {
                                    scope.layerTips.msg(res.msg);
                                    // layer.close(index);
                                    // table.reload('table');
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
    Model.prototype.unlockAmount = function (data) {
        var scope = this;
        var p = {
            mchId: data.id
        };
        $.get(scope.config.unlockAmountUrl, p, function (form) {
            var addBoxIndex = layer.open({
                type: 1,
                title: '冻结金额',
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
                    form.on('submit(edit)', function (data) {
                        //这里可以写ajax方法提交表单
                        $.ajax({
                            type: "POST",
                            url: scope.config.saveUnlockAmountUrl,
                            data: "params=" + JSON.stringify(data.field),
                            success: function (res) {
                                if (res.state === 0) {
                                    scope.layerTips.msg('保存成功');
                                    scope.layer.close(index);
                                    // location.reload(); //刷新
                                    table.reload('table');
                                } else {
                                    scope.layerTips.msg(res.msg);
                                    // layer.close(index);
                                    // table.reload('table');
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
    Model.prototype.adjustIncreaseAmount = function (data) {
        var scope = this;
        var p = {
            mchId: data.id
        };
        $.get(scope.config.adjustIncreaseAmountUrl, p, function (form) {
            var addBoxIndex = layer.open({
                type: 1,
                title: '补入账金额',
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
                    form.on('submit(edit)', function (data) {
                        //这里可以写ajax方法提交表单
                        $.ajax({
                            type: "POST",
                            url: scope.config.saveAdjustIncreaseAmountUrl,
                            data: "params=" + JSON.stringify(data.field),
                            success: function (res) {
                                if (res.state === 0) {
                                    scope.layerTips.msg('保存成功');
                                    scope.layer.close(index);
                                    // location.reload(); //刷新
                                    table.reload('table');
                                } else {
                                    scope.layerTips.msg(res.msg);
                                    // layer.close(index);
                                    // table.reload('table');
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
    Model.prototype.adjustDecreaseAmount = function (data) {
        var scope = this;
        var p = {
            mchId: data.id
        };
        $.get(scope.config.adjustDecreaseAmountUrl, p, function (form) {
            var addBoxIndex = layer.open({
                type: 1,
                title: '补入账金额',
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
                    form.on('submit(edit)', function (data) {
                        //这里可以写ajax方法提交表单
                        $.ajax({
                            type: "POST",
                            url: scope.config.saveAdjustDecreaseAmountUrl,
                            data: "params=" + JSON.stringify(data.field),
                            success: function (res) {
                                if (res.state === 0) {
                                    scope.layerTips.msg('保存成功');
                                    scope.layer.close(index);
                                    // location.reload(); //刷新
                                    table.reload('table');
                                } else {
                                    scope.layerTips.msg(res.msg);
                                    // layer.close(index);
                                    // table.reload('table');
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
    exports(mod_name, function (options) {
        var model = new Model();
        return model.set(options);
    });

});