layui.define(['form', 'table', 'myList', 'layer'], function (exports) {
    "use strict";

    var $ = layui.$;

    var mod_name = 'payOrder',
        Model = function () {
            var config = {
                title: '通道'
                , listUrl: '/pay_order/list'
                , viewUrl: '/pay_order/view.html'
                , keyName: 'id'
                , toolbar: true
                , cols: [[
                    {fixed: 'right', title: '操作', toolbar: '#barCtrl', width: 138}
                    , {field: 'id', title: '平台单号', width: 210}
                    , {field: 'mchOrderNo', title: '商户单号', width: 210}
                    , {field: 'mchInfo.name', title: '商户名', width: 120}
                    , {field: 'mchId', title: '商户号', width: 90}
                    , {field: 'agent.name', title: '代理', width: 80}
                    , {field: 'amount', title: '订单金额', width: 110}
                    , {field: 'subMchActualAmount', title: '到账金额', width: 110}
                    , {field: 'payAmount', title: '支付金额', width: 110}
                    , {field: 'status', title: '状态', toolbar: '#barStatus', width: 90}
                    , {field: 'payChannel.payType', title: '支付类型', toolbar: '#barPayType', width: 90}
                    , {field: 'createTime', title: '创建时间', width: 166}
                    , {field: 'payChannel.label', title: '支付渠道', width: 100}
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