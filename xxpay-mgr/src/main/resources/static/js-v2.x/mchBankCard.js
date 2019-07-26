layui.define(['form', 'table'], function (exports) {
    "use strict";

    var $ = layui.$;

    var mod_name = 'mchBankCard',
        Model = function () {
            var config = {
                title: '银行卡'
                , listUrl: '/mch_bank_card/list'
                , viewUrl: '/mch_bank_card/view.html'
                , editUrl: '/mch_bank_card/edit.html'
                , saveUrl: '/mch_bank_card/save'
                , keyName: 'id'
                , cols: [[
                    {type: 'radio'}
                    , {title: '操作', toolbar: '#barCtrl'}
                    , {field: 'mchId', title: '商户号'}
                    , {field: 'number', title: '卡号'}
                    , {field: 'registeredBankName', title: '开户行名称'}
                    , {field: 'bankName', title: '银行名称'}
                    , {field: 'type', title: '类型', toolbar: '#barTypeTpl'}
                    , {field: 'mobile', title: '绑卡手机号'}
                    , {field: 'remark', title: '备注'}
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