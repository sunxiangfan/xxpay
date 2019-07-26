<!DOCTYPE html>

<html>

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <title>商户后台登录</title>
    <link rel="icon" type="image/x-icon" href="images/favicon.ico">
    <link rel="stylesheet" href="plugins/layui/css/layui.css" media="all"/>
    <link rel="stylesheet" href="css/login.css"/>
    <meta http-equiv="pragma" content="no-cache"/><!-- 禁止浏览器从本地机的缓存中调阅页面内容 -->
    <meta http-equiv="Cache-Control" content="no-cache, must-revalidate"/>
    <meta http-equiv="expires" content="0"/> <!-- 定义网页 缓存中的过期时间-->
</head>

<body class="beg-login-bg">
<div class="beg-login-box">
    <header>
        <h1>商户后台</h1>
    </header>
    <div class="beg-login-main">
        <form action="/login" class="layui-form" method="post"><input name="__RequestVerificationToken" type="hidden"
                                                                      value="fkfh8D89BFqTdrE2iiSdG_L781RSRtdWOH411poVUWhxzA5MzI8es07g6KPYQh9Log-xf84pIR2RIAEkOokZL3Ee3UKmX0Jc8bW8jOdhqo81"/>
            <div class="layui-form-item">
                <label class="beg-login-icon">
                    <i class="layui-icon">&#xe612;</i>
                </label>
                <input type="text" name="userName" lay-verify="userName" autocomplete="off" placeholder="这里输入登录名"
                       class="layui-input" value="">
            </div>
            <div class="layui-form-item">
                <label class="beg-login-icon">
                    <i class="layui-icon">&#xe642;</i>
                </label>
                <input type="password" name="password" lay-verify="password" autocomplete="off" placeholder="这里输入密码"
                       class="layui-input" value="">
            </div>
            <div class="layui-form-item">
                <label class="beg-login-icon">
                    <i class="layui-icon">&#xe642;</i>
                </label>
                <input type="text" name="verifyCode" lay-verify="verifyCode" autocomplete="off"
                       placeholder="这里输入验证码"
                       class="layui-input">
                <img src="/verify.jpg" style="float: right; margin-top: -38px;height: 38px;"
                     id="verifyImg">
            </div>
            <div class="layui-form-item">
                <#--<div class="beg-pull-left beg-login-remember">-->
                <#--<label>记住帐号？</label>-->
                <#--<input type="checkbox" name="rememberMe" value="true" lay-skin="switch" checked title="记住帐号">-->
                <#--</div>-->
                <div class="beg-pull-right">
                    <button class="layui-btn layui-btn-primary" lay-submit lay-filter="login">
                        <i class="layui-icon">&#xe650;</i> 登录
                    </button>
                </div>
                <div class="beg-clear"></div>
            </div>
        </form>
    </div>
    <footer>
        <p>商户后台</p>
    </footer>
</div>
<script type="text/javascript" src="plugins/layui/layui.js"></script>
<script>
    layui.use(['layer', 'form'], function () {
        var layer = layui.layer,
            $ = layui.jquery,
            form = layui.form();

        form.on('submit(login)', function (data) {
            //这里可以写ajax方法提交表单
            $.ajax({
                type: "POST",
                url: "/login",
                data: "params=" + JSON.stringify(data.field),
                dataType: "json",
                success: function (res) {
                    if (res.state === 0) {
                        layer.msg('登录成功');
                        location.href = 'index.html';
                    } else if (res.msg) {
                        layer.msg(res.msg);
                        flushVerifyImg();
                    } else {
                        layer.msg("登录失败！");
                        flushVerifyImg();
                    }
                }
            });
            return false;
        });
        $('#verifyImg').on('click', function () {
            flushVerifyImg();
        })

        function flushVerifyImg() {
            $('#verifyImg')[0].src = '/verify.jpg?tt=' + new Date().getTime();
        }
    });


</script>
</body>

</html>