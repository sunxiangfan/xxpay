layui.define(['form', 'table', 'myList'], function (exports) {
    "use strict";
    var $ = layui.$;

    var mod_name = 'user',
        Model = function () {
            var config = {
                title: '用户'
                , listUrl: '/user/list'
                , viewUrl: '/user/view.html'
                , editUrl: '/user/edit.html'
                , saveUrl: '/user/save'
                , keyName: 'userId'
                , cols: [[
                    {type: 'checkbox'}
                    , {fixed: 'right', title: '操作', toolbar: '#barCtrl'}
                    , {field: 'name', title: '姓名'}
                    , {field: 'loginAccount', title: '登录名'}
                    , {field: 'mobile', title: '手机号'}
                    , {field: 'state', title: '状态', templet: '#stateCheckboxTpl'}
                    , {field: 'createTime', title: '创建时间'}
                ]],
            }
            this.set(config);
        };
    Model.prototype = layui.myList();
    exports(mod_name, function (options) {
        var model = new Model();
        return model.set(options);
    });

});