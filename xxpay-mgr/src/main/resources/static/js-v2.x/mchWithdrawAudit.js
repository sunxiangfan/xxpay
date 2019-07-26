layui.define(['form', 'table'], function (exports) {
    "use strict";

    var $ = layui.$;

    var mod_name = 'mchWithdrawAudit',
        Model = function () {
            var config = {
                title: '商户提现审核'
                , listUrl: '/mch_withdraw_audit/list'
                , viewUrl: '/mch_withdraw_audit/view.html'
                , passUrl: '/mch_withdraw_audit/pass'
                , refuseUrl: '/mch_withdraw_audit/refuse'
                , keyName: 'id'
                , cols: [[
                    {type: 'radio'}
                    , {title: '操作', toolbar: '#barCtrl', width: 120}
                    , {field: 'mchId', title: '商户号'}
                    , {field: 'applyAmount', title: '申请金额'}
                    , {field: 'actualAmount', title: '实际到账金额'}
                    , {field: 'platformDeduction', title: '手续费'}
                    , {field: 'state', title: '审核状态', templet: '#toolStateTpl'}
                    , {field: 'number', title: '银行卡号'}
                    , {field: 'accountName', title: '开户名'}
                    , {field: 'bankName', title: '银行名称'}
                    , {field: 'registeredBankName', title: '开户行名称'}
                    , {field: 'mobile', title: '绑卡手机号'}
                    , {field: 'createTime', title: '申请时间'}
                ]]
            };
            this.set(config);
        };
    Model.prototype = layui.myList();
    Model.prototype.pass = function (data) {
        var scope = this;
        var p = {
            id: data.id
        }
        scope.layerTips.confirm('确定要通过该申请吗？', {
            btn: ['继续', '取消']
        }, function () {
            $.ajax({
                type: "POST",
                url: scope.config.passUrl,
                data: "params=" + JSON.stringify(p),
                success: function (res) {
                    if (res.code === 0) {
                        scope.layerTips.msg('操作成功！');
                        // location.reload(); //刷新
                        scope.tableReload();
                    } else {
                        scope.layerTips.msg(res.msg);
                        // location.reload(); //刷新
                        scope.tableReload();
                    }
                }
            });
        })
    }
    Model.prototype.refuse = function (data) {
        var scope = this;
        var p = {
            id: data.id
        };
        scope.layerTips.confirm('确定要拒绝该申请吗？', {
            btn: ['继续', '取消']
        }, function () {
            var loadIndex = scope.layerTips.load();
            $.ajax({
                type: "POST",
                url: scope.config.refuseUrl,
                data: "params=" + JSON.stringify(p),
                success: function (res) {
                    if (res.code === 0) {
                        scope.layerTips.msg('操作成功！');
                        // location.reload(); //刷新
                        scope.tableReload();
                    } else {
                        scope.layerTips.msg('操作失败');
                        // location.reload(); //刷新
                        scope.tableReload();
                    }
                    scope.layerTips.close(loadIndex);
                }
            });
        })
    }
    exports(mod_name, function (options) {
        var model = new Model();
        return model.set(options);
    });

});