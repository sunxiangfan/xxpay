layui.define(['form', 'table', 'myList', 'agentMchPayChannel', 'layer'], function (exports) {
    "use strict";

    var $ = layui.$;

    var mod_name = 'cashChannel',
        Model = function () {
            var config = {
                title: '通道'
                , listUrl: '/cash_channel/list'
                , viewUrl: '/cash_channel/view.html'
                , editUrl: '/cash_channel/edit.html'
                , saveUrl: '/cash_channel/save'
                , keyName: 'id'
                , cols: [[
                    {type: 'radio'}
                    , {fixed: 'right', title: '操作', toolbar: '#barCtrl', width: 120}
                    , {field: 'label', title: '渠道别名'}
                    , {field: 'name', title: '渠道名称'}
                    , {field: 'code', title: '标识码'}
                    , {field: 'minTransactionAmount', title: '最小交易金额'}
                    , {field: 'maxTransactionAmount', title: '最大交易金额'}
                    , {field: 'thirdDeduction', title: '三方手续费'}
                    , {field: 'state', title: '状态', templet: '#stateTpl'}
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