layui.define(['form', 'table', 'myList', 'agentMchPayChannel', 'layer'], function (exports) {
    "use strict";

    var $ = layui.$;

    var mod_name = 'bank',
        Model = function () {
            var config = {
                title: '银行列表'
                , listUrl: '/bank/list'
                , editUrl: '/bank/edit.html'
                , saveUrl: '/bank/save'
                , keyName: 'id'
                , cols: [[
                    {type: 'radio'}
                    , {fixed: 'right', title: '操作', toolbar: '#barCtrl', width: 120}
                    , {field: 'name', title: '银行名称'}
                    , {field: 'code', title: '银行编码'}
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