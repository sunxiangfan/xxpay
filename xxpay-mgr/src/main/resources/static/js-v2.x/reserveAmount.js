layui.define(['form', 'table', 'myUpload', 'myList', 'laytpl', 'agentMchPayChannel', 'layer', 'mchInfo'], function (exports) {
    "use strict";

    var $ = layui.$;


    var mod_name = 'reserveAmount',
        Model = function () {
            var config = {
                title: '备付金'
                , listUrl: '/reserve_amount/list'
                , editUrl: '/reserve_amount/edit.html'
                , saveUrl: '/reserve_amount/save'
                , keyName: 'id'
                , toolbar: true
                , cols: [[
                    {type: 'radio'}
                    , {title: '操作', toolbar: '#barCtrl', width: 125}
                    , {field: 'flowType', title: '资金变动', width: 100}
                    , {field: 'amount', title: '金额', width: 100}
                    , {field: 'createTime', title: '创建时间', width: 200}
                    , {field: 'remark', title: '备注'}
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