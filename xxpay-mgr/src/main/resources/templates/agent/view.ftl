<div style="margin: 15px;">
	<form class="layui-form">

		<div class="layui-form-item">
			<label class="layui-form-label">是否启用</label>
			<div class="layui-input-block">
				<#if item.state = 1>
					<input type="text" style="color: green" disabled="disabled" class="layui-input" value="启用" }">
				<#else>
					<input type="text" style="color: red" disabled="disabled" class="layui-input" value="停止" }">
				</#if>
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">姓名</label>
			<div class="layui-input-block">
				<input type="text" name="name"  disabled="disabled"  autocomplete="off" class="layui-input" value="${item.name?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">手机号</label>
			<div class="layui-input-block">
				<input type="text" name="mobile" disabled="disabled"  autocomplete="off" class="layui-input" value="${item.mobile?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">邮箱地址</label>
			<div class="layui-input-block">
				<input type="text" name="email" lay-verify="required" disabled="disabled" autocomplete="off"
					   class="layui-input" value="${item.email?if_exists }">
			</div>
		</div>
		<!--
		<div class="layui-form-item">
			<label class="layui-form-label">请求私钥</label>
			<div class="layui-input-block">
                <textarea name="reqKey" lay-verify="required" disabled="disabled" autocomplete="off"
						  class="layui-textarea" >${item.reqKey?if_exists }</textarea>
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">响应私钥</label>
			<div class="layui-input-block">
				<textarea name="resKey" lay-verify="required" disabled="disabled" autocomplete="off"
						  class="layui-textarea" >
										${item.resKey?if_exists }
				</textarea>
			</div>
		</div>-->
		<div class="layui-form-item">
			<label class="layui-form-label">创建时间</label>
			<div class="layui-input-block">
				<input type="text" disabled="disabled" class="layui-input" value="${(item.createTime?string("yyyy-MM-dd HH:mm:ss"))!''} ">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">更新时间</label>
			<div class="layui-input-block">
				<input type="text" disabled="disabled" class="layui-input" value="${(item.updateTime?string("yyyy-MM-dd HH:mm:ss"))!''} ">
			</div>
		</div>
		<button lay-filter="edit" lay-submit style="display: none;"></button>
	</form>
</div>