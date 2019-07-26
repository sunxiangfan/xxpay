layui.define(['form', 'table', 'myList', 'layer'], function (exports) {
    "use strict";

    var $ = layui.$;

    var mod_name = 'payType',
        Model = function () {
            var config = {
                title: '支付类型'
                , listUrl: '/pay_type/list'
                , viewUrl: '/pay_type/view.html'
                , editUrl: '/pay_type/edit.html'
                , saveUrl: '/pay_type/save'
                , keyName: 'id'
                , cols: [[
                    {type: 'radio'}
                    , {field: 'id', title: 'id'}
                    , {field: 'name', title: '名称'}
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