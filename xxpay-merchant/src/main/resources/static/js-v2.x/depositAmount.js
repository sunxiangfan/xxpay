layui.define(['form', 'table', 'myList', 'layer'], function (exports) {
    "use strict";

    var $ = layui.$;

    var mod_name = 'depositAmount',
        Model = function () {
            var config = {
                title: '订单'
                , listUrl: '/deposit_amount/list'
                , viewUrl: '/deposit_amount/view.html'
                , keyName: 'id'
                , toolbar:true
                , cols: [[
                    {type: 'checkbox'}
                    , {field: 'payOrderId', title: '平台单号'}
                    , {field: 'mchId', title: '商户号',width:100}
                    , {field: 'amount', title: '金额'}
                    , { title: '状态', toolbar: '#toolStateTpl'}
                    , {field: 'planUnlockTime', title: '预计解冻时间'}
                    , {field: 'unlockTime', title: '实际解冻时间'}
                    , {field: 'createTime', title: '冻结时间'}
                    , {field: 'remark', title: '备注'}
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