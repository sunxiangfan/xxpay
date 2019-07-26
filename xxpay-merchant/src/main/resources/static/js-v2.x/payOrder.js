layui.define(['form', 'table', 'myList', 'layer'], function (exports) {
    "use strict";

    var $ = layui.$;

    var mod_name = 'payOrder',
        Model = function () {
            var config = {
                title: '订单'
                , listUrl: '/pay_order/list'
                , viewUrl: '/pay_order/view.html'
                , keyName: 'id'
                , toolbar: true
                , cols: [[
                    {type: 'checkbox'}
                    , {fixed: 'right', title: '操作', toolbar: '#barCtrl', width: 140}
                    , {field: 'id', title: '平台单号'}
                    , {field: 'mchOrderNo', title: '商户单号'}
                    , {field: 'mchId', title: '商户号', width: 100}
                    , {field: 'amount', title: '金额', width: 110}
                    , {field: 'subMchActualAmount', title: '到账金额', width: 110}
                    , {field: 'statusLabel', title: '状态', toolbar: '#toolStatusTpl', width: 120}
                    , {field: 'payChannel.payTypeLabel', title: '支付类型', toolbar: '#barPayType', width: 120}
                    , {field: 'createTime', title: '创建时间'}
                ]]

            }
            this.set(config);
        };

    Model.prototype = layui.myList();

    Model.prototype.renotify = function (data) {
        var p = {
            id: data.id
        };
        var scope = this;
        $.get('/pay_order/renotify', p, function (res) {
            if (res.state === 0) {
                scope.layerTips.msg('已提交。请稍候刷新列表查看');
            } else {
                scope.layerTips.msg(res.msg);
            }
        }, 'JSON');
    }
    exports(mod_name, function (options) {
        var model = new Model();
        return model.set(options);
    });

});