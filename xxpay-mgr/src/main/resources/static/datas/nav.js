var navs = [{
    "title": "用户管理",
    "icon": "&#xe613;",
    "spread": false,
    "children": [{
        //     "title": "角色管理",
        //     "icon": "&#xe641;",
        //     "href": "/role/list.html"
        // }, {
        "title": "用户管理",
        "icon": "&#xe613;",
        "href": "/user/list.html",
        "id":"user"
        // }, {
        //     "title": "部门管理",
        //     "icon": "&#xe63c;",
        //     "href": "/department/list.html"
    }]
}, {
    "title": "商户管理",
    "icon": "&#xe66f;",
    "spread": false,
    "children": [{
        "title": "商户列表",
        "icon": "&#xe66f;",
        "href": "/mch_info/list.html"
    }, {
        "title": "商户通道列表",
        "icon": "&#xe628;",
        "href": "/mch_pay_channel/list.html?mchType=1"
    }, {
        "title": "商户账户信息",
        "icon": "&#xe65e;",
        "href": "/mch_account/list.html"
    }, {
        "title": "商户提现审核",
        "icon": "&#xe642;",
        "href": "/mch_withdraw_audit/list.html?mchType=1"
    }, {
        "title": "商户提现记录",
        "icon": "&#xe60e;",
        "href": "/mch_withdraw_history/list.html?mchType=1"

        // }, {
        //     "title": "商户待审核进件",
        //     "icon": "&#xe63c;",
        //     "href": "/mch_notify1/list.html"
    }]
}, {
    "title": "代理管理",
    "icon": "&#xe770;",
    "spread": false,
    "children": [{
        "title": "代理列表",
        "icon": "&#xe770;",
        "href": "/agent/list.html"
    }, {
        "title": "代理通道列表",
        "icon": "&#xe628;",
        "href": "/agent_mch_pay_channel/list.html"
    }, {
        "title": "代理账户信息",
        "icon": "&#xe65e;",
        "href": "/agent_account/list.html"
    }, {
        "title": "代理提现审核",
        "icon": "&#xe642;",
        "href": "/mch_withdraw_audit/list.html?mchType=0"
    }, {
        "title": "代理提现记录",
        "icon": "&#xe60e;",
        "href": "/mch_withdraw_history/list.html?mchType=0"
    }, {
        "title": "代理银行卡列表",
        "icon": "&#xe63c;",
        "href": "/mch_bank_card/list.html"
    }]
}, {
    "title": "支付交易记录",
    "icon": "&#xe62c;",
    "spread": false,
    "children": [{
        "title": "交易订单管理",
        "icon": "&#xe62c;",
        "href": "/pay_order/list.html"
    }, {
        "title": "平台账单",
        "icon": "&#xe629;",
        "href": "/bill/list.html"
    }]
}, {
    "title": "财务管理",
    "icon": "&#xe60a;",
    "spread": false,
    "children": [{
        "title": "保证金明细",
        "icon": "&#xe60a;",
        "href": "/deposit_amount/list.html"
    },{
        "title": "商户对账单",
        "icon": "&#xe637;",
        "href": "/statement/list.html"
    },{
        "title": "分润统计",
        "icon": "&#xe637;",
        "href": "/statistics_bill/list.html"
    }]
}, {
    "title": "平台通道",
    "icon": "&#xe609;",
    "spread": false,
    "children": [{
        "title": "支付渠道",
        "icon": "&#xe609;",
        "href": "/pay_channel/list.html"
    }, {
        "title": "代付渠道",
        "icon": "&#xe659;",
        "href": "/cash_channel/list.html"
    }, {
        "title": "支付方式",
        "icon": "&#xe649;",
        "href": "/pay_type/list.html"
    }, {
        "title": "渠道名称",
        "icon": "&#xe621;",
        "href": "/channel_name/list.html"
    }]
}, {
    "title": "平台管理",
    "icon": "&#xe620;",
    "spread": false,
    "children": [{
        "title": "平台提现记录",
        "icon": "&#xe65e;",
        "href": "/platform_withdraw/list.html"
    },{
        "title": "平台备付金",
        "icon": "&#xe735;",
        "href": "/reserve_amount/list.html"
    },{
        //     "title": "平台账户",
        //     "icon": "&#xe641;",
        //     "href": "/mch_account1/list.html"
        // }, {
        //     "title": "平台账单",
        //     "icon": "&#xe63c;",
        //     "href": "/bill/list.html"
        // }, {
        "title": "系统参数",
        "icon": "&#xe620;",
        "href": "/system_setting/list.html"
    // }, {
    //     "title": "商户通知",
    //     "icon": "&#xe645;",
    //     "href": "/mch_notify/list.html"
    }, {
        "title": "银行列表",
        "icon": "&#xe60a;",
        "href": "/bank/list.html"
    }]
// },{
// 	"title": "基本信息",
// 	"icon": "fa-cubes",
// 	"spread": false,
// 	"children": [{
// 		"title": "商户信息",
// 		"icon": "&#xe641;",
// 		"href": "/mch_info/list.html"
// 	}, {
// 		"title": "支付渠道",
// 		"icon": "&#xe63c;",
// 		"href": "/pay_channel/list.html"
// 	}, {
// 		"title": "商户通知",
// 		"icon": "&#xe63c;",
// 		"href": "/mch_notify/list.html"
// 	}]
// },{
// 	"title": "订单管理",
// 	"icon": "&#x1002;",
// 	"spread": false,
// 	"children": [{
// 		"title": "支付订单",
// 		"icon": "fa-check-square-o",
// 		"href": "/pay_order/list.html"
// 	}, {
// 		"title": "转账订单",
// 		"icon": "fa-check-square-o",
// 		"href": "/trans_order/list.html"
// 	}, {
// 		"title": "退款订单",
// 		"icon": "fa-check-square-o",
// 		"href": "/refund_order/list.html"
// 	}]
}];