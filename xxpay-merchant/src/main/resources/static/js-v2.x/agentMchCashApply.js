layui.define(['form', 'table','myList'], function (exports) {
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
                , toolbar:true
                , cols: [[
                    {type: 'checkbox'}
                    , {field: 'id', title: '平台单号'}
                    , {field: 'mchOrderNo', title: '商户单号'}
                    , {field: 'applyAmount', title: '申请金额'}
                    , {field: 'actualAmount', title: '到账金额'}
                    , {field: 'platformDeduction', title: '手续费'}
                    , {field: 'stateLabel', title: '审核状态', templet: '#toolStateTpl'}
                    , {field: 'accountName', title: '开户名'}
                    , {field: 'number', title: '银行卡号'}
                    , {field: 'bankName', title: '银行名称'}
                    , {field: 'registeredBankName', title: '开户行名称'}
                    , {field: 'createTime', title: '申请时间'}
                    , {field: 'auditTime', title: '审核时间'}
                    , {field: 'cashStateLabel', title: '代付状态', templet: '#toolCashStateTpl'}
                    , {field: 'cashSuccTime', title: '下发时间'}
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