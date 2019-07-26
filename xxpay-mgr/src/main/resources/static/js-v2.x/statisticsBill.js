layui.define(['form', 'table', 'myList',  'layer'], function (exports) {
    "use strict";

    var $ = layui.$;

    var mod_name = 'statisticsBill',
        Model = function () {
            var config = {
                title: '分润统计'
                , listUrl: '/statistics_bill/list'
                , keyName: 'id'
                , toolbar: true
                , cols: [[
                    {type: 'radio'}
                    , {field: 'createDate', title: '日期', width: 125}
                    , {field: 'amount', title: '成交金额', width: 100}
                    , {field: 'thirdDeduction', title: '三方手续费', width: 110}
                    , {field: 'platformCommission', title: '平台分润', width: 110}
                    , {field: 'agentMchCommission', title: '代理商户分润', width: 140}
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