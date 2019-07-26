layui.define(['form', 'table', 'myList', 'layer'], function (exports) {
    "use strict";

    var $ = layui.$;

    var mod_name = 'depositAmount',
        Model = function () {
            var config = {
                title: '订单'
                , listUrl: '/deposit_amount/list'
                , viewUrl: '/deposit_amount/view.html'
                , keyName: 'id'
                , toolbar: '#toolbarTop'
                , cols: [[
                    {type: 'checkbox'}
                    , {field: 'payOrderId', title: '平台单号'}
                    , {field: 'mchId', title: '商户号', width: 100}
                    , {field: 'amount', title: '金额'}
                    , {title: '状态', toolbar: '#toolStateTpl'}
                    , {field: 'planUnlockTime', title: '预计解冻时间'}
                    , {field: 'unlockTime', title: '实际解冻时间'}
                    , {field: 'createTime', title: '冻结时间'}
                    , {field: 'remark', title: '备注'}
                ]]

            }
            this.set(config);
        };

    Model.prototype = layui.myList();

    Model.prototype.search = function () {
        var scope = this;
        var mchId = $('#mchId').val();
        var state = $('#state').val();
        scope.tableReload({
            mchId: mchId,
            state: state
        });
    }

    Model.prototype.unlock = function (datas) {
        var scope=this;
        if(!datas || datas.length<=0){
            scope.layerTips.alert('请选择数据。');
            return;
        }
        layer.confirm('您确定要解冻【' + datas.length + '】条数据吗？', {
            btn: ['继续', '取消']
        }, function (index, layero) {
            scope.doUnlock(datas);
        });

    }
    Model.prototype.doUnlock=function(datas){
        var ids=[];
        for(var i=0;i<datas.length;i++){
            ids.push(datas[i]["id"]);
        }
        var scope = this;
        //这里可以写ajax方法提交表单
        $.ajax({
            type: "POST",
            url: "/deposit_amount/unlock",
            data: "params=" + JSON.stringify({
                ids: ids
            }),
            success: function (res) {
                if (res.code === 0) {
                    scope.layerTips.msg('操作成功。');
                    location.reload(); //刷新
                } else {
                    scope.layerTips.msg(res.msg);
                }
            }
        });
    }
    exports(mod_name, function (options) {
        var model = new Model();
        return model.set(options);
    });

});