layui.define(['form', 'table'], function (exports) {
    "use strict";

    var $ = layui.$;

    var mod_name = 'agentMchCashApply',
        Model = function () {
            var config = {
                title: '结算明细'
                , listUrl: '/mch_withdraw_apply/list'
                , viewUrl: '/mch_withdraw_apply/view.html'
                , editUrl: '/mch_withdraw_apply/edit.html'
                , saveUrl: '/mch_withdraw_apply/save'
                , keyName: 'id'
                , cols: [[
                    {type: 'checkbox'}
                    , {field: 'applyAmount', title: '申请金额'}
                    , {field: 'actualAmount', title: '到账金额'}
                    , {field: 'state', title: '审核状态', templet: '#stateTpl'}
                    , {field: 'number', title: '银行卡号'}
                    , {field: 'registeredBankName', title: '开户行名称'}
                    , {field: 'bankName', title: '银行名称'}
                    , {field: 'mobile', title: '绑卡手机号'}
                    , {field: 'createTime', title: '申请时间'}
                    , {field: 'auditTime', title: '审核时间'}
                ]]
            };
            this.set(config);
        };
    Model.prototype = layui.myList();
    exports(mod_name, function (options) {
        var model = new Model();
        return model.set(options);
    });

});