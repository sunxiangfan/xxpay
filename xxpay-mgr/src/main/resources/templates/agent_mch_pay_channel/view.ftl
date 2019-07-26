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
			<label class="layui-form-label">商户</label>
			<div class="layui-input-block">
				<#if agent?exists>
					<input type="hidden" name="mchId" lay-verify="required" placeholder="请输入商户" autocomplete="off"
						   class="layui-input" value="${agent.id }">
					<input type="text" name="mchName" disabled="disabled" lay-verify="required" placeholder="请输入商户" autocomplete="off"
						   class="layui-input" value="${agent.name }">

				</#if>
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">支付通道</label>
			<div class="layui-input-block">
				<#if  payChannel?exists>
					<input type="text" name="passagewayName" disabled="disabled" lay-verify="required"  autocomplete="off"
						   class="layui-input" value="${payChannel.label} ( ${payChannel.code} )">
				</#if>
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">商户分润率(单位%)</label>
			<div class="layui-input-block">
				<input type="number" name="agentMchCommissionRate"  disabled="disabled" lay-verify="required"
					   autocomplete="off" class="layui-input" value="${item.agentMchCommissionRate?if_exists }">
			</div>
		</div>

		<div class="layui-form-item">
			<label class="layui-form-label">通道标识码</label>
			<div class="layui-input-block">
				<input type="text" name="code" disabled="disabled" autocomplete="off" class="layui-input" value="${payChannel.code?if_exists }">
			</div>
		</div>

		<div class="layui-form-item">
			<label class="layui-form-label">到账时间</label>
			<div class="layui-input-block">
				<input type="text" name="accountingCycle" disabled="disabled"  autocomplete="off" class="layui-input" value="${payChannel.accountingCycle?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">最大交易金额(单位元)</label>
			<div class="layui-input-block">
				<input type="number" name="maxTransactionAmount" disabled="disabled"  autocomplete="off" class="layui-input" value="${payChannel.maxTransactionAmount?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">最小交易金额(单位元)</label>
			<div class="layui-input-block">
				<input type="number" name="minTransactionAmount" disabled="disabled"  autocomplete="off" class="layui-input" value="${payChannel.minTransactionAmount?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">开始时间</label>
			<div class="layui-input-block">
				<input type="time" name="startTime" disabled="disabled"  autocomplete="off" class="layui-input" value="${payChannel.startTime?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">结束时间</label>
			<div class="layui-input-block">
				<input type="time" name="endTime" disabled="disabled"  autocomplete="off" class="layui-input" value="${payChannel.endTime?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">备注</label>
			<div class="layui-input-block">
				<input type="text" name="remark"  disabled="disabled" autocomplete="off" class="layui-input" value="${item.remark?if_exists }">
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