layui.define(['form', 'table', 'myList', 'agentMchPayChannel', 'layer'], function (exports) {
    "use strict";

    var $ = layui.$;

    var mod_name = 'payChannel',
        Model = function () {
            var config = {
                title: '通道'
                , listUrl: '/pay_channel/list'
                , viewUrl: '/pay_channel/view.html'
                , editUrl: '/pay_channel/edit.html'
                , saveUrl: '/pay_channel/save'
                , keyName: 'id'
                , cols: [[
                    {type: 'radio'}
                    , {fixed: 'right', title: '操作', toolbar: '#barCtrl', width: 120}
                    , {field: 'name', title: '渠道名称'}
                    , {field: 'payType', title: '支付方式'}
                    , {field: 'code', title: '标识码'}
                    , {field: 'accountingCycle', title: '到账日期'}
                    , {field: 'maxTransactionAmount', title: '最大交易金额'}
                    , {field: 'minTransactionAmount', title: '最小交易金额'}
                    , {field: 'startTime', title: '开始时间'}
                    , {field: 'endTime', title: '结束时间'}
                    , {field: 'thirdDeductionRate', title: '三方手续费率'}
                    , {field: 't1Rate', title: 'T1比例%'}
                    , {field: 'state', title: '状态', templet: '#stateTpl'}
                    , {field: 'label', title: '通道名称'}
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