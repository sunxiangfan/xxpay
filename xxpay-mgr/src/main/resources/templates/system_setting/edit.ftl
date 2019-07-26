<div style="margin: 15px;">
	<form class="layui-form">
		<#if item.id?exists>
			<input type="text" name="id" hidden="hidden" value="${item.id?if_exists }">
		</#if>
		<div class="layui-form-item">
			<label class="layui-form-label">参数名称</label>
			<div class="layui-input-block">
				<input type="text" name="paramName" lay-verify="required" disabled="disabled" autocomplete="off" class="layui-input" value="${item.paramName?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">参数值</label>
			<div class="layui-input-block">
				<input type="text" name="paramValue" autocomplete="off" class="layui-input" value="${item.paramValue?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">描述</label>
			<div class="layui-input-block">
				<input type="text" name="paramDesc" lay-verify="required" disabled="disabled" autocomplete="off" class="layui-input" value="${item.paramDesc?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">参数排序</label>
			<div class="layui-input-block">
				<input type="number" name="paramOrder" lay-verify="required" autocomplete="off" class="layui-input" value="${item.paramOrder?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">是否可编辑</label>
			<div class="layui-input-block">
				<#if item.flagEditable = 1>
					<input type="text" style="color: green" disabled="disabled" class="layui-input" value="是" }">
				<#else>
					<input type="text" style="color: red" disabled="disabled" class="layui-input" value="否" }">
				</#if>
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">是否为空</label>
			<div class="layui-input-block">
				<#if item.flagNullable = 1>
					<input type="text" style="color: green" disabled="disabled" class="layui-input" value="是" }">
				<#else>
					<input type="text" style="color: red" disabled="disabled" class="layui-input" value="否" }">
				</#if>
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">是否为货币</label>
			<div class="layui-input-block">
				<#if item.flagMoney = 1>
					<input type="text" style="color: green" disabled="disabled" class="layui-input" value="是" }">
				<#else>
					<input type="text" style="color: red" disabled="disabled" class="layui-input" value="否" }">
				</#if>
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">是否为数字</label>
			<div class="layui-input-block">
				<#if item.flagNumber = 1>
					<input type="text" style="color: green" disabled="disabled" class="layui-input" value="是" }">
				<#else>
					<input type="text" style="color: red" disabled="disabled" class="layui-input" value="否" }">
				</#if>
			</div>
		</div>
		<button lay-filter="edit" lay-submit style="display: none;"></button>
	</form>
</div>