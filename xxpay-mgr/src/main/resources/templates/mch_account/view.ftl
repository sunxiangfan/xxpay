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
			<label class="layui-form-label">登录名</label>
			<div class="layui-input-block">
				<input type="text" name="loginAccount" disabled="disabled"  autocomplete="off" class="layui-input" value="${item.loginAccount?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">手机号</label>
			<div class="layui-input-block">
				<input type="text" name="mobile" disabled="disabled"  autocomplete="off" class="layui-input" value="${item.mobile?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">公司名称</label>
			<div class="layui-input-block">
				<input type="text" name="companyName"  disabled="disabled"  autocomplete="off" class="layui-input" value="${item.companyName?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">客服电话</label>
			<div class="layui-input-block">
				<input type="text" name="companyTel" disabled="disabled"  autocomplete="off" class="layui-input" value="${item.companyTel?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">企业类型</label>
			<div class="layui-input-block">
				<input type="text" name="corpType" disabled="disabled" autocomplete="off" class="layui-input" value="${item.corpType?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">公司简称</label>
			<div class="layui-input-block">
				<input type="text" name="shortName" disabled="disabled"  autocomplete="off" class="layui-input" value="${item.shortName?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">地址</label>
			<div class="layui-input-block">
				<input type="text" name="address" disabled="disabled"  autocomplete="off" class="layui-input" value="${item.address?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">公司类型</label>
			<div class="layui-input-block">
				<input type="text" name="companyType" disabled="disabled" autocomplete="off" class="layui-input" value="${item.companyType?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">联系人姓名</label>
			<div class="layui-input-block">
				<input type="text" name="contactName"  disabled="disabled" autocomplete="off" class="layui-input" value="${item.contactName?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">联系人号码</label>
			<div class="layui-input-block">
				<input type="text" name="contactMobile" disabled="disabled"  autocomplete="off" class="layui-input" value="${item.contactMobile?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">营业执照号</label>
			<div class="layui-input-block">
				<input type="text" name="businessNo" disabled="disabled"  autocomplete="off" class="layui-input" value="${item.businessNo?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">有效期</label>
			<div class="layui-input-block">
				<input type="text" name="businessNoExpiryDate" disabled="disabled"  autocomplete="off" class="layui-input" value="${item.businessNoExpiryDate?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">法人姓名</label>
			<div class="layui-input-block">
				<input type="text" name="corporationName" disabled="disabled"  autocomplete="off" class="layui-input" value="${item.corporationName?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">法人身份证</label>
			<div class="layui-input-block">
				<input type="text" name="corporationId" disabled="disabled"  autocomplete="off" class="layui-input" value="${item.corporationId?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">法人手机</label>
			<div class="layui-input-block">
				<input type="text" name="corporationMobile" disabled="disabled"  autocomplete="off" class="layui-input" value="${item.corporationMobile?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">审核状态</label>
			<div class="layui-input-block">
				<select name="throughAuditing" id="type" lay-search="">
					<option value="0"  <#if (item.throughAuditing!1) == 0>selected="selected"</#if> >未审核</option>
					<option value="1"  <#if (item.throughAuditing!1) == 1>selected="selected"</#if> >已审核</option>
					<option value="2"  <#if (item.throughAuditing!1) == 2>selected="selected"</#if> >未通过审核</option>
				</select>

			</div>

			<div class="layui-input-block">
				<#if item.throughAuditing = 0>
				<input type="text" style="color: green" disabled="disabled" class="layui-input" value="未审核" }">
				<#elseif item.throughAuditing = 1>
					<input type="text" style="color: green" disabled="disabled" class="layui-input" value="已审核" }">
				<#else >
					<input type="text" style="color: red" disabled="disabled" class="layui-input" value="未通过审核" }">
				</#if>
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">身份证正面照地址</label>
			<div class="layui-input-block">
				<input type="text" name="idCardFrontUrl" disabled="disabled" autocomplete="off" class="layui-input" value="${item.idCardFrontUrl?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">身份证反面照地址</label>
			<div class="layui-input-block">
				<input type="text" name="idCardBackUrl" disabled="disabled" autocomplete="off" class="layui-input" value="${item.idCardBackUrl?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">营业执照地址</label>
			<div class="layui-input-block">
				<input type="text" name="businessNoUrl" disabled="disabled" autocomplete="off" class="layui-input" value="${item.businessNoUrl?if_exists }">
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