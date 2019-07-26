layui.define(['form', 'table', 'myUpload', 'myList', 'laytpl', 'agentMchPayChannel', 'layer', 'mchInfo'], function (exports) {
    "use strict";

    var $ = layui.$;


    var mod_name = 'agent',
        Model = function () {
            var config = {
                title: '代理'
                , listUrl: '/agent/list'
                , viewUrl: '/agent/view.html'
                , editUrl: '/agent/edit.html'
                , saveUrl: '/agent/save'
                , keyName: 'id'
                , toolbar: true
                , cols: [[
                    {type: 'radio'}
                    , {title: '操作', toolbar: '#barCtrl', width: 400}
                    , {field: 'id', title: '商户号'}
                    , {field: 'name', title: '姓名'}
                    , {field: 'mobile', title: '手机号'}
                    , {field: 'email', title: '邮箱'}
                    , {field: 'state', title: '状态', templet: '#stateTpl'}
                    , {field: 'createTime', title: '创建时间'}
                ]]
            };
            this.set(config);
        };
    Model.prototype = layui.myList();

    Model.prototype.addPayChannel = function (data) {
        var scope = this;
        var agentMchPayChannel = layui.agentMchPayChannel();
        agentMchPayChannel.add({mchId: data[scope.config.keyName]});
    }
    Model.prototype.managePayChannel = function (data) {
        var scope = this;
        var id = data[scope.config.keyName];
        var addBoxIndex = layer.open({
            type: 2,
            title: '商户通道管理',
            content: '/agent_mch_pay_channel/list.html?mchId=' + id,
            shade: false,
            offset: ['100px', '30%'],
            area: ['600px', '450px'],
            zIndex: 19950924,
            maxmin: false,
            full: function (elem) {
                var win = window.top === window.self ? window : parent.window;
                $(win).on('resize', function () {
                    var $this = $(this);
                    elem.width($this.width()).height($this.height()).css({
                        top: 0,
                        left: 0
                    });
                    elem.children('div.layui-layer-content').height($this.height() - 95);
                });
            }
        });
        layer.full(addBoxIndex);
    }
    Model.prototype.addSubMch = function (data) {
        var scope = this;
        var instance = layui.mchInfo();
        instance.add({agentId: data[scope.config.keyName]});
    }

    Model.prototype.manageSubMch = function (data) {
        var scope = this;
        var id = data[scope.config.keyName];
        var addBoxIndex = layer.open({
            type: 2,
            title: '子商户管理',
            content: '/mch_info/list.html?agentId=' + id,
            shade: false,
            offset: ['100px', '30%'],
            area: ['600px', '450px'],
            zIndex: 19950924,
            maxmin: false,
            full: function (elem) {
                var win = window.top === window.self ? window : parent.window;
                $(win).on('resize', function () {
                    var $this = $(this);
                    elem.width($this.width()).height($this.height()).css({
                        top: 0,
                        left: 0
                    });
                    elem.children('div.layui-layer-content').height($this.height() - 95);
                });
            }
        });
        layer.full(addBoxIndex);
    }
    exports(mod_name, function (options) {
        var model = new Model();
        return model.set(options);
    });

});