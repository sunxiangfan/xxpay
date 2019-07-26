layui.define(['form', 'table', 'common', 'colsconfig', 'myList', 'mchPayChannel'], function (exports) {
    "use strict";
    var $ = layui.$;

    var cols = [[
        {type: 'checkbox'}
        , {title: '操作', toolbar: '#barCtrl'}
        , {field: 'agentId', title: '父级商户号'}
        , {field: 'id', title: '商户号'}
        , {field: 'name', title: '商户名称'}
        // , {field: 'mobile', title: '手机号'}
        , {field: 'state', title: '状态', templet: '#stateTpl'}
        , {field: 'createTime', title: '创建时间'}
    ]];
    var mod_name = 'subMchInfo',
        Model = function () {
            var config = {
                title: '子商户'
                , listUrl: '/sub_mch_info/list'
                , viewUrl: '/sub_mch_info/view.html'
                , editUrl: '/sub_mch_info/edit.html'
                , saveUrl: '/sub_mch_info/save'
                , keyName: 'id'
                , elem: '#table'
                , toolbar: null
                , cols: cols
                , where: {
                    v: new Date().getTime()
                }
            }
            this.set(config);
        };

    var myList = layui.myList();
    Model.prototype = myList;
    Model.prototype.managePayChannel = function (data) {
        var scope = this;
        var id = data[scope.config.keyName];
        var addBoxIndex = layer.open({
            type: 2,
            title: '子商户通道管理',
            content: '/mch_pay_channel/list.html?mchId=' + id,
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