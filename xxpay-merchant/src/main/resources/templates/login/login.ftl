<!DOCTYPE html>

<html>

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <title>商户后台登录</title>
    <link rel="icon" type="image/x-icon" href="images/favicon.ico">
    <link href="css/login.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="plugins/layui-v2.4.5/css/layui.css" media="all"/>
    <meta http-equiv="pragma" content="no-cache"/><!-- 禁止浏览器从本地机的缓存中调阅页面内容 -->
    <meta http-equiv="Cache-Control" content="no-cache, must-revalidate"/>
    <meta http-equiv="expires" content="0"/> <!-- 定义网页 缓存中的过期时间-->
</head>

<body class="beg-login-bg">
<div class="layui-container">
    <div class="layui-row">
        <div style="height: 120px; width: 100%;"></div>
        <div class="layui-col-md5 " style="text-align: center">
            <img src="images/login-img.png">
        </div>
        <div class="layui-col-md5 layui-col-md-offset2">
            <div class="login">
                <div class="login_logo"><img src="images/login_logo.png"></div> 
                <div class="login_name">
                    <p>商户后台登录</p>
                </div>
                <form action="/login" class="layui-form" method="post">
                    <input name="userName" type="text" placeholder="用户名">
                    <input name="password" type="password" id="password" placeholder="密码">
                    <input type="text" name="verifyCode" lay-verify="verifyCode" autocomplete="off"
                           placeholder="这里输入验证码"
                           class="layui-input"
                           style="width:190px;">
                    <img src="/verify.jpg" style="float: right; height: 50px;margin-top:-65px;"
                         id="verifyImg">
                    <input value="登录" style="width:100%;" type="submit" lay-submit lay-filter="login">

                </form>
            </div>
        </div>
    </div>
</div>
<div class="copyright">2019@ 鑫享付商户平台</div>
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