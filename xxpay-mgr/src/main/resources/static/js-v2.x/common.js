/** common.js By Beginner Emain:zheng_jinfan@126.com HomePage:http://www.zhengjinfan.cn */
layui.define(['layer'], function(exports) {
	"use strict";

	var $ = layui.jquery,
		layer = layui.layer;

	var common = {
		/**
		 * 抛出一个异常错误信息
		 * @param {String} msg
		 */
		throwError: function(msg) {
			throw new Error(msg);
			return;
		},
		/**
		 * 弹出一个错误提示
		 * @param {String} msg
		 */
		msgError: function(msg) {
			layer.msg(msg, {
				icon: 5
			});
			return;
		},
		/**
		 * 转换list返回数据格式
		 * @param res
		 * @returns {*}
		 */
		parseData: function (res) {
			if(res.code !==0 || !res.data){
				return res;
			}
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
			}
			return res;
		}
	};

	exports('common', common);
});