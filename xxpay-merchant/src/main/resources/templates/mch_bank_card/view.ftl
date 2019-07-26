<div style="margin: 15px;">
	<form class="layui-form">

		<div class="layui-form-item">
			<label class="layui-form-label">是否对公</label>
			<div class="layui-input-block">
				<#if item.type = 1>
					<input type="text" style="color: green" disabled="disabled" class="layui-input" value="对公" }">
				<#else>
					<input type="text" style="color: red" disabled="disabled" class="layui-input" value="对私" }">
				</#if>
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">银行卡号</label>
			<div class="layui-input-block">
				<input type="text" name="number" lay-verify="required"  disabled="disabled" placeholder="请输入银行卡号" autocomplete="off" class="layui-input" value="${item.number?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">开户行名称</label>
			<div class="layui-input-block">
				<input type="text" name="registeredBankName" lay-verify="required" disabled="disabled"  placeholder="请输入开户行名称" autocomplete="off" class="layui-input" value="${item.registeredBankName?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">银行名称</label>
			<div class="layui-input-block">
				<input type="text" name="bankName" lay-verify="required"  disabled="disabled" placeholder="请输入银行名称" autocomplete="off" class="layui-input" value="${item.bankName?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">绑卡手机号</label>
			<div class="layui-input-block">
				<input type="text" name="mobile"  disabled="disabled" placeholder="请输入绑卡手机号" autocomplete="off" class="layui-input" value="${item.mobile?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">备注</label>
			<div class="layui-input-block">
				<input type="text" name="remark"  disabled="disabled"  placeholder="请输入备注" autocomplete="off" class="layui-input" value="${item.remark?if_exists }">
			</div>
		</div>

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