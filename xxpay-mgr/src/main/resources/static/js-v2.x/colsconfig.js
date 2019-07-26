/**
 * 配置列表字段
 */
layui.define(['layer'], function (exports) {
    "use strict";

    var $ = layui.jquery,
        layer = layui.layer;

    var colsconfig = {
        /**
         * 子商户
         */
        subMchInfo: [[
            {type: 'checkbox'}
            , {title: '操作', toolbar: '#barCtrl'}
            , {field: 'parentId', title: '父级商户号'}
            , {field: 'id', title: '商户号'}
            , {field: 'name', title: '商户名称'}
            , {field: 'mobile', title: '手机号'}
            , {field: 'state', title: '状态', templet: '#stateTpl'}
            , {field: 'createTime', title: '创建时间'}
        ]],
        /**
         * 转换list返回数据格式
         * @param res
         * @returns {*}
         */
        parseData: function (res) {
            var datas = res.data;
            for (var i = 0; i < datas.length; i++) {
                var item = datas[i];
                for (var popName in item) {
                    var val = item[popName];
                    var isObject = $.isPlainObject(val);
                    if (isObject) {
                        for (var innerPopName in val) {
                            var innerVal = val[innerPopName];
                            item[popName + "." + innerPopName] = innerVal;
                        }
                    }
                }
                console.log(item);
            }
            return res;
        }
    };

    exports('colsconfig', colsconfig);
});