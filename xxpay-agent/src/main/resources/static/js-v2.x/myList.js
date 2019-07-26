layui.define(['form', 'table', 'common'], function (exports) {
    "use strict";
    var topWin = (function () {
        var p = window.parent;
        while (p != p.window.parent) {
            p = p.window.parent;
        }
        return p;
    })();
    var $ = layui.$,
        layerTips = topWin.layer === undefined ? layui.layer : topWin.layer, //获取父窗口的layer对象
        layer = layui.layer,
        form = layui.form,
        table = layui.table
        , common = layui.common;

    var mod_name = 'myList',
        MyList = function () {
            this.config = {
                title: ''
                , listUrl: ''
                , viewUrl: ''
                , editUrl: ''
                , saveUrl: ''
                , keyName: 'id'
                , elem: '#table'
                , toolbar: null
                , cols: []
                , where: {
                    v: new Date().getTime()
                }
                , toolbarFn: null
                , toolFn: null
                , parseData: common.parseData
            }

        };
    MyList.prototype.layer = layer;
    MyList.prototype.layerTips = layerTips;
    /**
     * 参数设置
     * @param {Object} options
     */
    MyList.prototype.set = function (options) {
        var scope = this;
        scope.init();
        $.extend(true, scope.config, options);
        return scope;
    };
    MyList.prototype.init = function () {
        var scope = this;
        var defaultToolFn = function (event, data) {
            if ($.isFunction(scope[event])) {
                scope[event](data);
            }
        }

        var defaultToolbarFn = function (event, datas) {
            // if (datas.length <= 0) {
            //     layerTips.alert('未选中任何行！');
            //     return;
            // }
            // if (datas.length > 1) {
            //     layerTips.alert('一次只能操作一条数据！');
            //     return;
            // }
            // var data = datas[0];
            if ($.isFunction(scope[event])) {
                scope[event](datas);
            }
        }
        scope.config.toolFn = defaultToolFn;
        scope.config.toolbarFn = defaultToolbarFn;
    }
    MyList.prototype.initTable = function () {
        var scope = this;
        scope.tableInstance = table.render({
            elem: scope.config.elem
            , url: scope.config.listUrl
            , cols: scope.config.cols
            , id: 'table'
            , page: true
            , where: scope.config.where
            , toolbar: scope.config.toolbar
            , parseData: scope.config.parseData
        });


        //监听行工具事件
        table.on('tool(table)', function (obj) {
            var event = obj.event;
            var data = obj.data;
            scope.config.toolFn(event, data);
        });

        //工具栏事件
        table.on('toolbar(table)', function (obj) {
            var checkStatus = table.checkStatus(obj.config.id);
            var data = checkStatus.data;
            var event = obj.event;
            scope.config.toolbarFn(event, data);
        });
        //监听状态操作
        form.on('checkbox(state)', function (obj) {
            var state = obj.elem.checked === true ? 1 : 0;
            $.ajax({
                type: "POST",
                url: "/user/change_state",
                data: "params=" + JSON.stringify({
                    userId: obj.elem.dataset.id,
                    state: state
                }),
                success: function (res) {
                    if (res.state === 0) {
                        layerTips.msg('保存成功');
                        table.reload('table');
                    } else {
                        layerTips.msg(res.msg);
                    }
                }
            });
        });

    }

    MyList.prototype.tableReload = function (options) {
        var scope = this;
        var where = {v: new Date().getTime()};
        $.extend(true, where, scope.config.where);
        $.extend(true, where, options);
        //执行重载
        table.reload('table', {
            page: {
                curr: 1 //重新从第 1 页开始
            }
            , where: where
        });
    }
    MyList.prototype.on = function (eventName, fn) {
        var scope = this;
        if (eventName == "toolbar") {
            scope.config.toolbarFn = fn;
        } else if (eventName == "tool") {
            scope.config.toolFn = fn;
        }
    }

    MyList.prototype.getCheckedData=function(){
        var scope=this;
        var checkStatus = table.checkStatus(scope.tableInstance.config.id);
        var data = checkStatus.data;
        return data;
    }
    MyList.prototype.edit = function (data) {
        var scope = this;
        var p = {};
        p[scope.config.keyName] = data[scope.config.keyName];
        $.get(scope.config.editUrl, p, function (form) {
            var addBoxIndex = layer.open({
                type: 1,
                title: '编辑' + scope.config.title,
                content: form,
                btn: ['保存', '取消'],
                shade: false,
                offset: ['100px', '30%'],
                area: ['600px', '450px'],
                zIndex: 19950924,
                maxmin: false,
                yes: function (index) {
                    //触发表单的提交事件
                    $('form.layui-form').find('button[lay-filter=edit]').click();
                },
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
                },
                success: function (layero, index) {
                    //弹出窗口成功后渲染表单
                    var form = layui.form;
                    form.render();
                    form.on('submit(edit)', function (data) {
                        //这里可以写ajax方法提交表单
                        $.ajax({
                            type: "POST",
                            url: scope.config.saveUrl,
                            data: "params=" + JSON.stringify(data.field),
                            success: function (res) {
                                if (res.state === 0) {
                                    layerTips.msg('保存成功');
                                    layer.close(index);
                                    // location.reload(); //刷新
                                    table.reload('table');
                                } else {
                                    layerTips.msg(res.msg);
                                    layer.close(index);
                                    // location.reload(); //刷新
                                    table.reload('table');
                                }
                            }
                        });
                        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
                    });
                },
                end: function () {
                    addBoxIndex = -1;
                }
            });
            layer.full(addBoxIndex);
        });
    }
    MyList.prototype.add = function (data) {
        var scope = this;
        var p = {};
        if (data) {
            $.extend(true, p, data);
        }
        $.get(scope.config.editUrl, p, function (form) {
            var addBoxIndex = layer.open({
                type: 1,
                title: '添加' + scope.config.title,
                content: form,
                btn: ['保存', '取消'],
                shade: false,
                offset: ['100px', '30%'],
                area: ['600px', '450px'],
                zIndex: 19950924,
                maxmin: false,
                yes: function (index) {
                    //触发表单的提交事件
                    $('form.layui-form').find('button[lay-filter=edit]').click();
                },
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
                },
                success: function (layero, index) {
                    //弹出窗口成功后渲染表单
                    var form = layui.form;
                    form.render();
                    form.on('submit(edit)', function (data) {
                        //这里可以写ajax方法提交表单
                        $.ajax({
                            type: "POST",
                            url: scope.config.saveUrl,
                            data: "params=" + JSON.stringify(data.field),
                            success: function (res) {
                                if (res.state === 0) {
                                    layerTips.msg('保存成功');
                                    layer.close(index);
                                    // location.reload(); //刷新
                                    table.reload('table');
                                } else {
                                    layerTips.msg(res.msg);
                                    layer.close(index);
                                    // location.reload(); //刷新
                                    // table.reload('table');
                                    scope.tableReload();
                                }
                            }
                        });
                        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
                    });
                },
                end: function () {
                    addBoxIndex = -1;
                }
            });
            layer.full(addBoxIndex);
        });
    }
    MyList.prototype.view = function (data) {
        var scope = this;
        var p = {};
        p[scope.config.keyName] = data[scope.config.keyName];
        $.get(scope.config.viewUrl, p, function (form) {
            var addBoxIndex = layer.open({
                type: 1,
                title: '查看' + scope.config.title,
                content: form,
                shade: false,
                offset: ['100px', '30%'],
                area: ['600px', '550px'],
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
                },
                end: function () {
                    addBoxIndex = -1;
                }
            });
            layer.full(addBoxIndex);
        });
    }
    exports(mod_name, function (options) {
        var myList = new MyList();
        return myList.set(options);
    });

});