<div style="margin: 15px;">
	<form class="layui-form">
		<#if item.userId?exists>
			<input type="text" name="userId" hidden="hidden" value="${item.userId?if_exists }">
		</#if>
		<div class="layui-form-item">
			<label class="layui-form-label">用户名称</label>
			<div class="layui-input-block">
				<input type="text" name="name" lay-verify="required" placeholder="请输入用户名称" autocomplete="off" class="layui-input" value="${item.name?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">登录名</label>
			<div class="layui-input-block">
				<input type="text" name="loginAccount" lay-verify="required" placeholder="请输入登录名" autocomplete="off" class="layui-input" value="${item.loginAccount?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">密码</label>
			<div class="layui-input-block">
				<#if item.password ? exists>
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
			<label class="layui-form-label">是否启用</label>
			<div class="layui-input-block">
				<input type="checkbox" name="state" lay-skin="switch" <#if (item.state!1) == 1>checked="checked"</#if> >
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">部门</label>
			<div class="layui-input-block">
				<input type="text" name="department" lay-verify="required" placeholder="请输入登录名" autocomplete="off" class="layui-input" value="${item.department?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">手机号</label>
			<div class="layui-input-block">
				<input type="text" name="mobile" lay-verify="required" placeholder="请输入登录名" autocomplete="off" class="layui-input" value="${item.mobile?if_exists }">
			</div>
		</div>
		<button lay-filter="edit" lay-submit style="display: none;"></button>
	</form>
</div>