layui.define(['form', 'table', 'myList',  'layer'], function (exports) {
    "use strict";

    var $ = layui.$;

    var mod_name = 'bill',
        Model = function () {
            var config = {
                title: '平台账单'
                , listUrl: '/bill/list'
                , viewUrl: '/bill/view.html'
                , keyName: 'id'
                , cols: [[
                    {type: 'radio'}
                    , {field: 'agentMchId', title: '代理商户号'}
                    , {field: 'mchId', title: '子商户号'}
                    , {field: 'payOrderId', title: '支付订单号'}
                    , {field: 'amount', title: '金额'}
                    , {field: 'mchActualAmount', title: '到账金额'}
                    , {field: 'thirdDeduction', title: '三方手续费'}
                    , {field: 'agentMchCommission', title: '代理商户分润'}
                    , {field: 'platformDeductionRate', title: '平台手续费率'}
                    , {field: 'platformDeduction', title: '平台手续费'}
                    , {field: 'platformCommission', title: '平台分润'}
                    , {field: 'state', title: '结算状态',toolbar: '#barState'}
                    , {field: 'payOrderTime', title: '交易时间'}
                ]]

            }
            this.set(config);
        };

    Model.prototype = layui.myList();

    exports(mod_name, function (options) {
        var model = new Model();
        return model.set(options);
    });

});