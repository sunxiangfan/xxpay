<div style="margin: 15px;">
	<form class="layui-form">
		<div class="layui-form-item">
			<label class="layui-form-label">商户号</label>
			<div class="layui-input-block">
				<input type="text" name="mchId" disabled="disabled" autocomplete="off" class="layui-input" value="${item.id?if_exists }">
			</div>
		</div>

		<div class="layui-form-item">
			<label class="layui-form-label">商户名称</label>
			<div class="layui-input-block">
				<input type="text" name="name" disabled="disabled" autocomplete="off" class="layui-input" value="${item.name?if_exists }">
			</div>
		</div>

		<div class="layui-form-item">
			<label class="layui-form-label">账户余额</label>
			<div class="layui-input-block">
				<input type="text" name="balance" disabled="disabled" autocomplete="off" class="layui-input" value="${item.balance?if_exists }">
			</div>
		</div>

		<div class="layui-form-item">
			<label class="layui-form-label">本次补入金额</label>
			<div class="layui-input-block">
				<input type="number" name="amount" lay-verify="required" placeholder="请输入本次补入金额" autocomplete="off" class="layui-input" >
			</div>
		</div>

		<div class="layui-form-item">
			<label class="layui-form-label">原因</label>
			<div class="layui-input-block">
				<input type="text" name="remark" lay-verify="required" placeholder="请输入原因" autocomplete="off" class="layui-input" >
			</div>
		</div>
		<button lay-filter="edit" lay-submit style="display: none;"></button>
	</form>
</div>