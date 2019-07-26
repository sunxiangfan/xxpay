<div style="margin: 15px;">
	<form class="layui-form">

		<div class="layui-form-item">
			<label class="layui-form-label">商户号</label>
			<div class="layui-input-block">
				<input type="text" name="id" disabled="disabled"  autocomplete="off" class="layui-input" value="${item.id?if_exists }">
			</div>
		</div>
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
			<label class="layui-form-label">代理商户号</label>
			<div class="layui-input-block">
				<input type="text" name="agentId" lay-verify="required" disabled="disabled" autocomplete="off"
					   class="layui-input" value="${item.agentId?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">公司名称</label>
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
				<input type="text" name="email" lay-verify="required"  disabled="disabled" autocomplete="off"
					   class="layui-input" value="${item.email?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">联系人姓名</label>
			<div class="layui-input-block">
				<input type="text" name="contactName"  disabled="disabled" autocomplete="off" class="layui-input" value="${item.contactName?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">审核状态</label>
			<div class="layui-input-block">
				<#if item.auditState = 0>
					<input type="text" style="color: green" disabled="disabled" class="layui-input" value="未审核" }">
				<#elseif item.auditState = 1>
					<input type="text" style="color: green" disabled="disabled" class="layui-input" value="已审核" }">
				<#else >
					<input type="text" style="color: red" disabled="disabled" class="layui-input" value="未通过审核" }">
				</#if>
			</div>
		</div>

		<div class="layui-form-item">
			<label class="layui-form-label">请求私钥</label>
			<div class="layui-input-block">
				<textarea name="reqKey"  disabled="disabled" placeholder="请输入请求私钥" autocomplete="off" class="layui-textarea">${item.reqKey?if_exists }</textarea>
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">响应私钥</label>
			<div class="layui-input-block">
				<textarea name="resKey"  disabled="disabled" placeholder="请输入响应私钥" autocomplete="off" class="layui-textarea" >${item.resKey?if_exists }</textarea>
			</div>
		</div>

		<div class="layui-form-item">
			<label class="layui-form-label">客服电话</label>
			<div class="layui-input-block">
				<input type="text" name="companyTel" disabled="disabled"  autocomplete="off" class="layui-input" value="${company.companyTel?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">企业类型</label>
			<div class="layui-input-block">
				<input type="text" name="corpType" disabled="disabled" autocomplete="off" class="layui-input" value="${company.corpType?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">公司简称</label>
			<div class="layui-input-block">
				<input type="text" name="shortName" disabled="disabled"  autocomplete="off" class="layui-input" value="${company.shortName?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">地址</label>
			<div class="layui-input-block">
				<input type="text" name="address" disabled="disabled"  autocomplete="off" class="layui-input" value="${company.address?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">公司类型</label>
			<div class="layui-input-block">
				<input type="text" name="companyType" disabled="disabled" autocomplete="off" class="layui-input" value="${company.companyType?if_exists }">
			</div>
		</div>

		<div class="layui-form-item">
			<label class="layui-form-label">营业执照号</label>
			<div class="layui-input-block">
				<input type="text" name="businessNo" disabled="disabled"  autocomplete="off" class="layui-input" value="${company.businessNo?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">有效期</label>
			<div class="layui-input-block">
				<input type="text" name="businessNoExpiryDate" disabled="disabled"  autocomplete="off" class="layui-input" value="${company.businessNoExpiryDate?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">法人姓名</label>
			<div class="layui-input-block">
				<input type="text" name="corporationName" disabled="disabled"  autocomplete="off" class="layui-input" value="${company.corporationName?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">法人身份证</label>
			<div class="layui-input-block">
				<input type="text" name="corporationId" disabled="disabled"  autocomplete="off" class="layui-input" value="${company.corporationId?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">法人手机</label>
			<div class="layui-input-block">
				<input type="text" name="corporationMobile" disabled="disabled"  autocomplete="off" class="layui-input" value="${company.corporationMobile?if_exists }">
			</div>
		</div>

		<div class="layui-form-item">
			<label class="layui-form-label">身份证正面照地址</label>
			<div class="layui-input-block">
				<img class="layui-upload-img"  width="120px" height="150px" src="${company.idCardFrontUrl?if_exists}">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">身份证反面照地址</label>
			<div class="layui-input-block">
				<img class="layui-upload-img"  width="120px" height="150px" src="${company.idCardBackUrl?if_exists}">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">营业执照地址</label>
			<div class="layui-input-block">
				<img class="layui-upload-img"  width="120px" height="150px" src="${company.businessNoUrl?if_exists}">
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