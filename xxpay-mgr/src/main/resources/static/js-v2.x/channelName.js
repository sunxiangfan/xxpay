layui.define(['form', 'table', 'myList', 'layer'], function (exports) {
    "use strict";

    var $ = layui.$;

    var mod_name = 'channelName',
        Model = function () {
            var config = {
                title: '支付类型'
                , listUrl: '/channel_name/list'
                , viewUrl: '/channel_name/view.html'
                , editUrl: '/channel_name/edit.html'
                , saveUrl: '/channel_name/save'
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