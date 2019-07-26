<div style="margin: 15px;">
    <form class="layui-form">
        <div class="layui-form-item">
            <label class="layui-form-label">原始密码</label>
            <div class="layui-input-block">
                <input type="password" name="oldPassword" lay-verify="required" placeholder="原始密码" autocomplete="off"
                       class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">新密码</label>
            <div class="layui-input-block">
                <input type="password" name="newPassword" lay-verify="required" placeholder="新密码" autocomplete="off"
                       class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">重复新密码</label>
            <div class="layui-input-block">
                <input type="password" name="repeatPassword" lay-verify="required" placeholder="重复新密码"
                       autocomplete="off" class="layui-input">
            </div>
        </div>
        <button lay-filter="edit" lay-submit style="display: none;"></button>
    </form>
</div>