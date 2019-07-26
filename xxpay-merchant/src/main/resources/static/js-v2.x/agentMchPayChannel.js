layui.define(['form', 'table'], function (exports) {
    "use strict";

    var $ = layui.$;

    var mod_name = 'agentMchPayChannel',
        Model = function () {
            var config = {
                title: '商户通道'
                , listUrl: '/agent_mch_pay_channel/list'
                , viewUrl: '/agent_mch_pay_channel/view.html'
                , editUrl: '/agent_mch_pay_channel/edit.html'
                , saveUrl: '/agent_mch_pay_channel/save'
                , keyName: 'id'
                , cols: [[
                    {type: 'checkbox'}
                    , {title: '操作', toolbar: '#barCtrl'}
                    , {field: 'mchInfo.id', title: '商户号'}
                    , {field: 'mchInfo.name', title: '商户名称'}
                    , {field: 'payChannel.payType', title: 'payType'}
                    , {field: 'payChannel.accountingCycle', title: '到账日期'}
                    , {field: 'payChannel.maxTransactionAmount', title: '最大交易金额'}
                    , {field: 'payChannel.minTransactionAmount', title: '最小交易金额'}
                    , {field: 'payChannel.startTime', title: '开启时间'}
                    , {field: 'payChannel.endTime', title: '关闭时间'}
                    , {field: 'agentMchCommissionRate', title: '商户分润费'}
                    , {field: 'createTime', title: '创建时间'}
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