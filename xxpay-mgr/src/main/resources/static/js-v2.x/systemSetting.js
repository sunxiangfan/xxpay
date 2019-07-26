layui.define(['form', 'table', 'myList'], function (exports) {
    "use strict";
    var $ = layui.$;

    var mod_name = 'systemSetting',
        Model = function () {
            var config = {
                title: '系统配置'
                , listUrl: '/system_setting/list'
                , viewUrl: '/system_setting/view.html'
                , editUrl: '/system_setting/edit.html'
                , saveUrl: '/system_setting/save'
                , keyName: 'id'
                , cols: [[
                    {type: 'radio'}
                    , {fixed: 'right', title: '操作', toolbar: '#barCtrl'}
                    , {field: 'paramName', title: '参数名称'}
                    , {field: 'paramValue', title: '参数值'}
                    , {field: 'paramDesc', title: '参数描述'}
                    , {field: 'paramOrder', title: '排序'}
                    , {field: 'flagEditable', title: '是否可编辑', templet: '#barFlagEditable'}
                    , {field: 'flagNullable', title: '是否可空', templet: '#barFlagNullable'}
                    , {field: 'flagMoney', title: '是否为货币', templet: '#barFlagMoney'}
                    , {field: 'flagNumber', title: '是否为数字', templet: '#barFlagNumber'}
                    , {field: 'updateTime', title: '修改时间'}
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