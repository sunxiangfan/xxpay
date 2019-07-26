layui.define(['form', 'table','laytpl','myList'], function (exports) {
    "use strict";

    var $ = layui.$;
    var laytpl=layui.laytpl;

    var mod_name = 'platformWithdraw',
        Model = function () {
            var config = {
                title: '平台提现记录'
                , listUrl: '/platform_withdraw/list'
                , keyName: 'id'
                // , toolbar: '#toolbarTop'
                , cols: [[
                    {type: 'radio'}
                    , {field: 'applyAmount', title: '申请金额', width: 200}
                    , {field: 'thirdDeduction', title: '三方手续费', width: 175}
                    // , {field: 'number', title: '银行卡号', width: 95}
                    // , {field: 'accountName', title: '开户名', width: 80}
                    // , {field: 'bankName', title: '银行名称', width: 95}
                    // , {field: 'registeredBankName', title: '开户行名称', width: 95}
                    // , {field: 'mobile', title: '绑卡手机号', width: 95}
                    , {field: 'createTime', title: '提现时间', width: 238}
                    , {field: 'cashChannel.label', title: '代付通道',width:180}
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