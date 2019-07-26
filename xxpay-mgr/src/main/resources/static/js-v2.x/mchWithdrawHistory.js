layui.define(['form', 'table','laytpl'], function (exports) {
    "use strict";

    var $ = layui.$;
    var laytpl=layui.laytpl;

    var mod_name = 'mchWithdrawHistory',
        Model = function () {
            var config = {
                title: '商户提现记录'
                , listUrl: '/mch_withdraw_history/list'
                , viewUrl: '/mch_withdraw_history/view.html'
                , keyName: 'id'
                // , toolbar: '#toolbarTop'
                , cols: [[
                    {type: 'radio'}
                    , {title: '操作', toolbar: '#barCtrl', width: 155}
                    , {field: 'mchId', title: '商户号', width: 85}
                    , {field: 'applyAmount', title: '申请金额', width: 100}
                    , {field: 'platformDeduction', title: '手续费', width: 75}
                    // , {field: 'thirdDeduction', title: '三方手续费', width: 75}
                    , {field: 'state', title: '审核状态', templet: '#toolStateTpl', width: 88}
                    , {field: 'cashState', title: '代付状态', templet: '#toolCashStateTpl', width: 85}
                    , {field: 'number', title: '银行卡号', width: 95}
                    , {field: 'accountName', title: '开户名', width: 80}
                    , {field: 'bankName', title: '银行名称', width: 95}
                    , {field: 'registeredBankName', title: '开户行名称', width: 95}
                    , {field: 'mobile', title: '绑卡手机号', width: 95}
                    , {field: 'createTime', title: '申请时间', width: 138}
                    , {field: 'auditTime', title: '审核时间', width: 110}
                    , {field: 'mchOrderNo', title: '商户单号',width:100}
                    , {field: 'cashChannel.label', title: '代付通道',width:80}
                ]]
            };
            this.set(config);
        };
    Model.prototype = layui.myList();


    Model.prototype.applyCash=function(data){
        var scope = this;
        var p = {};
        p.id = data.id;
        $.get("/mch_withdraw_history/apply_cash.html", p, function (form) {
            var addBoxIndex = layer.open({
                type: 1,
                title: '代付申请',
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
                        var loadIndex = scope.layerTips.load();
                        $.ajax({
                            type: "POST",
                            url: '/mch_withdraw_history/apply_cash',
                            data: "params=" + JSON.stringify(data.field),
                            success: function (res) {
                                if (res.state === 0) {
                                    scope.layerTips.msg('保存成功');
                                    layer.close(index);
                                    // location.reload(); //刷新
                                    // table.reload('table');
                                    scope.tableReload();
                                } else {
                                    scope.layerTips.msg(res.msg);
                                    layer.close(index);
                                    // location.reload(); //刷新
                                    // table.reload('table');
                                }
                                scope.layerTips.close(loadIndex);
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

    Model.prototype.markCashed=function(data){
        var scope = this;
        var p = {};
        p.id = data.id;
        $.get("/mch_withdraw_history/mark_cashed.html", p, function (form) {
            var addBoxIndex = layer.open({
                type: 1,
                title: '已线下打款',
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
                        var loadIndex = scope.layerTips.load();
                        $.ajax({
                            type: "POST",
                            url: '/mch_withdraw_history/mark_cashed',
                            data: "params=" + JSON.stringify(data.field),
                            success: function (res) {
                                if (res.state === 0) {
                                    scope.layerTips.msg('保存成功');
                                    layer.close(index);
                                    // location.reload(); //刷新
                                    // table.reload('table');
                                    scope.tableReload();
                                } else {
                                    scope.layerTips.msg(res.msg);
                                    layer.close(index);
                                    // location.reload(); //刷新
                                    // table.reload('table');
                                }
                                scope.layerTips.close(loadIndex);
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