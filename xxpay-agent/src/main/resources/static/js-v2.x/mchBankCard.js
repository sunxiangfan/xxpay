layui.define(['form', 'table'], function (exports) {
    "use strict";

    var $ = layui.$;

    var mod_name = 'mchBankCard',
        Model = function () {
            var config = {
                title: '银行卡'
                , listUrl: '/mch_bank_card/list'
                , viewUrl: '/mch_bank_card/view.html'
                , editUrl: '/mch_bank_card/edit.html'
                , saveUrl: '/mch_bank_card/save'
                , keyName: 'id'
                , toolbar: '#toolbarTop'
                , cols: [[
                    {type: 'checkbox'}
                    , {title: '操作', toolbar: '#barCtrl'}
                    , {field: 'number', title: '卡号'}
                    , {field: 'accountName', title: '开户名'}
                    , {field: 'bankName', title: '银行名称'}
                    , {field: 'registeredBankName', title: '开户行名称'}
                    , {field: 'mobile', title: '预留手机号'}
                    , {field: 'province', title: '省'}
                    , {field: 'city', title: '市'}
                    , {field: 'idCard', title: '身份证号'}
                    , {field: 'type', title: '类型', toolbar: '#barTypeTpl'}
                    , {field: 'remark', title: '备注'}
                    , {field: 'createTime', title: '创建时间'}
                ]]
            };
            this.set(config);
        };
    Model.prototype = layui.myList();
    Model.prototype.cashApply = function () {
        var scope = this;
        var applyAmount = $("#applyAmount").val();
        if (!applyAmount) {
            scope.layerTips.alert('请输入提现金额！');
            return;
        }
        applyAmount = parseFloat(applyAmount);
        if (isNaN(applyAmount)) {
            scope.layerTips.alert('请输入有效的提现金额！');
            return;
        }
        if (applyAmount < 100) {
            scope.layerTips.alert('单笔提现金额不能少于100元！');
            return;
        }
        var balance=parseFloat(scope.config.balance||'0.00');
        if(applyAmount> balance){
            scope.layerTips.alert('余额不足！');
            return;
        }
        var selectedRows = scope.getCheckedData();
        if (!selectedRows || selectedRows.length <= 0) {
            scope.layerTips.alert('请选择提现银行卡！');
            return;
        }
        if (selectedRows.length > 1) {
            scope.layerTips.alert('只能选择一条提现银行卡数据！');
            return;
        }
        var mchBankCardId = selectedRows[0].id;
        layer.confirm('您确定要申请提现【' + applyAmount + '元】吗？', {
            btn: ['继续', '取消']
        }, function (index, layero) {
            scope.doCashApply(applyAmount, mchBankCardId);

        });
    }
    Model.prototype.doCashApply = function (applyAmount, mchBankCardId) {
        var scope=this;
        //这里可以写ajax方法提交表单
        $.ajax({
            type: "POST",
            url: "/mch_withdraw_apply/save",
            data: "params=" + JSON.stringify({
                applyAmount: applyAmount,
                mchBankCardId: mchBankCardId
            }),
            success: function (res) {
                if (res.code  === 0) {
                    scope.layerTips.msg('提现成功。请等待下发审核！');
                    // layer.alert('提现成功。请等待下发审核！',function (index) {
                    //     location.reload(); //刷新
                    // });
                    // layerTips.close(index);
                    location.reload(); //刷新
                } else {
                    scope.layerTips.msg(res.msg);
                    // layerTips.close(index);
                    location.reload(); //刷新
                }
            }
        });
    }
    exports(mod_name, function (options) {
        var model = new Model();
        return model.set(options);
    });

});