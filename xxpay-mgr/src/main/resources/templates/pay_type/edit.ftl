<div style="margin: 15px;">
	<form class="layui-form">
		<#if item.id?exists>
			<input type="text" name="id" hidden="hidden" value="${item.id?if_exists }">
		</#if>
		<div class="layui-form-item">
			<label class="layui-form-label">名称</label>
			<div class="layui-input-block">
				<input type="text" name="name" lay-verify="required" placeholder="请输入名称" autocomplete="off" class="layui-input" value="${item.name?if_exists }">
			</div>
		</div>
		<button lay-filter="edit" lay-submit style="display: none;"></button>
	</form>
</div>