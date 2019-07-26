layui.define(['form', 'table', 'myList', 'layer'], function (exports) {
    "use strict";

    var $ = layui.$;

    var mod_name = 'amountFlow',
        Model = function () {
            var config = {
                title: '订单'
                , listUrl: '/amount_flow/list'
                , keyName: 'id'
                , toolbar:true
                , cols: [[
                    {type: 'checkbox'}
                    , {field: 'mchId', title: '商户号',width:100}
                    , {field: 'flowType', title: '变动方向'}
                    , {field: 'amountType', title: '变动类型'}
                    , {field: 'amount', title: '变动金额'}
                    // , {field: 'preBalance', title: '变动前余额'}
                    , {field: 'sourceId', title: '关联单号'}
                    , {field: 'remark', title: '备注'}
                    , {field: 'createTime', title: '记录时间'}
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