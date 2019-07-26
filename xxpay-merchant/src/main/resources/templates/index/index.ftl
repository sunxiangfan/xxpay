<!DOCTYPE html>

<html>

<head>
    <meta charset="utf-8">
    <title>子商户后台</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="format-detection" content="telephone=no">
    <link rel="icon" type="image/x-icon" href="images/favicon.ico">
    <link rel="stylesheet" href="plugins/layui-v2.4.5/css/layui.css" media="all"/>
    <link rel="stylesheet" href="css/global.css" media="all">
    <link rel="stylesheet" href="plugins/font-awesome/css/font-awesome.min.css">

    <meta http-equiv="pragma" content="no-cache"/><!-- 禁止浏览器从本地机的缓存中调阅页面内容 -->
    <meta http-equiv="Cache-Control" content="no-cache, must-revalidate"/>
    <meta http-equiv="expires" content="0"/> <!-- 定义网页 缓存中的过期时间-->
</head>

<body>
<div class="layui-layout layui-layout-admin" style="border-bottom: solid 5px #1aa094;">
    <div class="layui-header header header-demo">
        <div class="layui-main">
            <div class="admin-login-box">
                <a class="logo" style="left: 0;" href="">
                    <span style="font-size: 22px;">商户后台</span>
                </a>
                <div class="admin-side-toggle">
                    <i class="fa fa-bars" aria-hidden="true"></i>
                </div>
                <div class="admin-side-full">
                    <i class="fa fa-life-bouy" aria-hidden="true"></i>
                </div>
            </div>
            <ul class="layui-nav admin-header-item">
                <li class="layui-nav-item">
                    <a href="javascript:;" class="admin-header-user">
                        <img src="images/0.jpg"/>
                        <span>${loginInfo.name}</span>
                    </a>
                    <dl class="layui-nav-child">
                        <dd id="changePassword">
                            <a href="javascript:;"><i class="fa fa-user-circle" aria-hidden="true"></i> 修改登录密码</a>
                        </dd>
                        <#--<dd>-->
                        <#--<a href="javascript:;"><i class="fa fa-gear" aria-hidden="true"></i> 设置</a>-->
                        <#--</dd>-->
                        <#--<dd id="lock">-->
                        <#--<a href="javascript:;">-->
                        <#--<i class="fa fa-lock" aria-hidden="true" style="padding-right: 3px;padding-left: 1px;"></i> 锁屏 (Alt+L)-->
                        <#--</a>-->
                        <#--</dd>-->
                        <dd>
                            <a href="login.html"><i class="fa fa-sign-out" aria-hidden="true"></i> 注销</a>
                        </dd>
                    </dl>
                </li>
            </ul>
            <ul class="layui-nav admin-header-item-mobile">
                <li class="layui-nav-item">
                    <a href="login.html"><i class="fa fa-sign-out" aria-hidden="true"></i> 注销</a>
                </li>
            </ul>
        </div>
    </div>
    <div class="layui-side layui-bg-black" id="admin-side">
        <div class="layui-side-scroll" id="admin-navbar-side" lay-filter="side"></div>
    </div>
    <div class="layui-body" style="bottom: 0;border-left: solid 2px #f2f2f2;" id="admin-body">
        <div class="layui-tab admin-nav-card layui-tab-brief" lay-filter="admin-tab">
            <ul class="layui-tab-title">
                <li class="layui-this">
                    <i class="fa fa-dashboard" aria-hidden="true"></i>
                    <cite>主页</cite>
                </li>
            </ul>
            <div class="layui-tab-content" style="min-height: 150px; padding: 5px 0 0 0;">
                <div class="layui-tab-item layui-show">
                    <iframe src="main.html"></iframe>
                </div>
            </div>
        </div>
    </div>
    <div class="layui-footer footer footer-demo" id="admin-footer">
        <div class="layui-main">
            <p>2019 &copy;
                <a href="">鑫享付商户后台</a>
            </p>
        </div>
    </div>
    <div class="site-tree-mobile layui-hide">
        <i class="layui-icon">&#xe602;</i>
    </div>
    <div class="site-mobile-shade"></div>

    <!--锁屏模板 start-->
    <script type="text/template" id="lock-temp">
        <div class="admin-header-lock" id="lock-box">
            <div class="admin-header-lock-img">
                <img src="images/0.jpg"/>
            </div>
            <div class="admin-header-lock-name" id="lockUserName">beginner</div>
            <input type="text" class="admin-header-lock-input" value="输入密码解锁.." name="lockPwd" id="lockPwd"/>
            <button class="layui-btn layui-btn-small" id="unlock">解锁</button>
        </div>
    </script>
    <!--锁屏模板 end -->


    <script type="text/javascript" src="plugins/layui-v2.4.5/layui.js"></script>
    <script type="text/javascript" src="datas/nav.js"></script>
    <script src="js-v2.x/index.js"></script>

</div>
</body>

</html>