/** common.js By Beginner Emain:zheng_jinfan@126.com HomePage:http://www.zhengjinfan.cn */
layui.define(['layer', 'upload', 'laytpl'], function (exports) {
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
        layer = layui.layer
        , upload = layui.upload
        , laytpl = layui.laytpl;

    var mod_name = 'myUpload',
        tplId = 'uploadTpl',
        MyUpload = function () {
            this.config = {
                name: ''
                , elem: ''
                , url: ''
                , require: false
                ,width:'120px'
                ,height:'150px'
            }

        };

    /**
     * 参数设置
     * @param {Object} options
     */
    MyUpload.prototype.set = function (options) {
        var scope = this;
        scope.init();
        $.extend(true, scope.config, options);
        return scope;
    };
    MyUpload.prototype.render = function () {
        var scope = this;
        laytpl($('#' + tplId).html()).render({
            name: scope.config.name
            ,require: scope.config.require
            ,width: scope.config.width
            ,height: scope.config.height
        }, function (string) {
            $(scope.config.elem).html(string);
            //普通图片上传
            var uploadInst = upload.render({
                elem: '#test1' + scope.config.name
                , url: scope.config.url
                , before: function (obj) {
                    //预读本地文件示例，不支持ie8
                    obj.preview(function (index, file, result) {
                        $('#demo1' + scope.config.name).attr('src', result); //图片链接（base64）
                    });
                }
                , done: function (res) {
                    //如果上传失败
                    if (res.code > 0) {
                        return layer.msg('上传失败');
                    }
                    //上传成功
                    $('#hidden'+scope.config.name).val(res.fileName);
                    layer.msg('上传成功！');
                }
                , error: function () {
                    //演示失败状态，并实现重传
                    var demoText = $('#demo' + scope.config.name);
                    demoText.html('<span style="color: #FF5722;">上传失败</span> <a class="layui-btn layui-btn-xs demo-reload">重试</a>');
                    demoText.find('.demo-reload').on('click', function () {
                        uploadInst.upload();
                    });
                }
            });
        });
    }
    MyUpload.prototype.init = function () {
    }

    exports(mod_name, function (options) {
        var myUpload = new MyUpload();
        return myUpload.set(options);
    });
});