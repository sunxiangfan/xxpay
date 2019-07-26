<div style="margin: 15px;">
	<form class="layui-form">
		<#if item.id?exists>
			<input type="text" name="id" hidden="hidden" value="${item.id?if_exists }">
		</#if>
		<div class="layui-form-item">
			<label class="layui-form-label">是否启用</label>
			<div class="layui-input-block">
				<input type="checkbox" name="state" lay-skin="switch" <#if (item.state!1) == 1>checked="checked"</#if> >
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
				<input type="password" name="password" lay-verify="required" placeholder="请输入密码" autocomplete="off" class="layui-input">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">手机号</label>
			<div class="layui-input-block">
				<input type="text" name="mobile" lay-verify="required" placeholder="请输入登录名" autocomplete="off" class="layui-input" value="${item.mobile?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">公司名称</label>
			<div class="layui-input-block">
				<input type="text" name="companyName"  placeholder="请输入公司名称" autocomplete="off" class="layui-input" value="${item.companyName?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">客服电话</label>
			<div class="layui-input-block">
				<input type="text" name="companyTel" lay-verify="required" placeholder="请输入客服电话" autocomplete="off" class="layui-input" value="${item.companyTel?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">企业类型</label>
			<div class="layui-input-block">
				<input type="text" name="corpType" placeholder="请输入企业类型" autocomplete="off" class="layui-input" value="${item.corpType?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">公司简称</label>
			<div class="layui-input-block">
				<input type="text" name="shortName"  placeholder="请输入公司简称" autocomplete="off" class="layui-input" value="${item.shortName?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">地址</label>
			<div class="layui-input-block">
				<input type="text" name="address" placeholder="请输入地址" autocomplete="off" class="layui-input" value="${item.address?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">公司类型</label>
			<div class="layui-input-block">
				<input type="text" name="companyType" placeholder="请输入公司类型" autocomplete="off" class="layui-input" value="${item.companyType?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">联系人姓名</label>
			<div class="layui-input-block">
				<input type="text" name="contactName"  placeholder="请输入联系人姓名" autocomplete="off" class="layui-input" value="${item.contactName?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">联系人号码</label>
			<div class="layui-input-block">
				<input type="text" name="contactMobile" placeholder="请输入联系人号码" autocomplete="off" class="layui-input" value="${item.contactMobile?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">营业执照号</label>
			<div class="layui-input-block">
				<input type="text" name="businessNo" placeholder="请输入营业执照号" autocomplete="off" class="layui-input" value="${item.businessNo?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">有效期</label>
			<div class="layui-input-block">
				<input type="text" name="businessNoExpiryDate" placeholder="请输入有效期" autocomplete="off" class="layui-input" value="${item.businessNoExpiryDate?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">法人姓名</label>
			<div class="layui-input-block">
				<input type="text" name="corporationName" placeholder="请输入法人姓名" autocomplete="off" class="layui-input" value="${item.corporationName?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">法人身份证</label>
			<div class="layui-input-block">
				<input type="text" name="corporationId" placeholder="请输入法人身份证" autocomplete="off" class="layui-input" value="${item.corporationId?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">法人手机</label>
			<div class="layui-input-block">
				<input type="text" name="corporationMobile" placeholder="请输入法人手机" autocomplete="off" class="layui-input" value="${item.corporationMobile?if_exists }">
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
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">身份证正面照地址</label>
			<div class="layui-input-block">
				<input type="text" name="idCardFrontUrl"  autocomplete="off" class="layui-input" value="${item.idCardFrontUrl?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">身份证反面照地址</label>
			<div class="layui-input-block">
				<input type="text" name="idCardBackUrl"  autocomplete="off" class="layui-input" value="${item.idCardBackUrl?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">营业执照地址</label>
			<div class="layui-input-block">
				<input type="text" name="businessNoUrl"  autocomplete="off" class="layui-input" value="${item.businessNoUrl?if_exists }">
			</div>
		</div>
		<button lay-filter="edit" lay-submit style="display: none;"></button>
	</form>
</div>