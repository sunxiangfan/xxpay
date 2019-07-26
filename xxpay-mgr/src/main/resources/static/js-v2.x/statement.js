layui.define(['form', 'table', 'myList',  'layer'], function (exports) {
    "use strict";

    var $ = layui.$;

    var mod_name = 'statement',
        Model = function () {
            var config = {
                title: '对账单'
                , listUrl: '/statement/list'
                , keyName: 'id'
                , toolbar: true
                , cols: [[
                    {type: 'radio'}
                    , {field: 'createDate', title: '日期', width: 125}
                    , {field: 'mchId', title: '商户号', width: 90}
                    , {field: 'mchInfo.name', title: '商户名', width: 240}
                    , {field: 'totalCount', title: '总单数', width: 100}
                    , {field: 'successCount', title: '成功单数', width: 110}
                    , {field: 'failCount', title: '未支付单数', width: 110}
                    , {field: 'applyAmount', title: '订单总额', width: 140}
                    , {field: 'successAmount',title: '订单实付总额',width:140}
                    , {field: 'successActualAmount', title: '商户到账总额',width:140}
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