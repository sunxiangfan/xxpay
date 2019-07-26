<div style="margin: 15px;">
    <form class="layui-form">
        <#if item.id?exists>
            <input type="text" name="id" hidden="hidden" value="${item.id?if_exists }">
        </#if>

        <#if item.id ? exists>
            <div class="layui-form-item">
            <label class="layui-form-label">商户号</label>
            <div class="layui-input-block">
            <input type="text" name="id" disabled="disabled" autocomplete="off"
            class="layui-input" value="${item.id?if_exists }">
            </div>
            </div>
        </#if>
        <div class="layui-form-item">
            <label class="layui-form-label">是否启用</label>
            <div class="layui-input-block">
                <input type="checkbox" name="state" lay-skin="switch"
                       <#if (item.state!1) == 1>checked="checked"</#if> >
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">密码</label>
            <div class="layui-input-block">
                <#if item.id ? exists>
                    <input type="password" name="password" placeholder="请输入密码" autocomplete="off"
                           class="layui-input">
                <#else >
                    <input type="password" name="password" lay-verify="required" placeholder="请输入密码"
                           autocomplete="off"
                           class="layui-input">
                </#if>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">姓名</label>
            <div class="layui-input-block">
                <input type="text" name="name" placeholder="请输入姓名" lay-verify="required" autocomplete="off"
                       class="layui-input"
                       value="${item.name?if_exists }">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">手机号</label>
            <div class="layui-input-block">
                <input type="text" name="mobile" lay-verify="required" placeholder="请输入手机号" autocomplete="off"
                       class="layui-input" value="${item.mobile?if_exists }">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">邮箱地址</label>
            <div class="layui-input-block">
                <input type="text" name="email" lay-verify="required" placeholder="请输入邮箱地址" autocomplete="off"
                       class="layui-input" value="${item.email?if_exists }">
            </div>
        </div>
        <!--
        <div class="layui-form-item">
            <label class="layui-form-label">请求私钥</label>
            <div class="layui-input-block">
                <textarea name="reqKey" lay-verify="required" placeholder="请输入请求私钥" autocomplete="off"
                          class="layui-textarea">${item.reqKey?if_exists }</textarea>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">响应私钥</label>
            <div class="layui-input-block">
				<textarea name="resKey" lay-verify="required" placeholder="请输入响应私钥" autocomplete="off"
                          class="layui-textarea">${item.resKey?if_exists }</textarea>
            </div>
        </div>-->
        <button lay-filter="edit" lay-submit style="display: none;"></button>
    </form>
</div>