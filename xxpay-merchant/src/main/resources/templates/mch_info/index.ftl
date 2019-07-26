<!DOCTYPE html>
<html>

	<head>
		<meta charset="UTF-8">
		<title>商户列表</title>
		<link rel="stylesheet" href="../plugins/layui-v2.4.5/css/layui.css" media="all" />
		<link rel="stylesheet" href="../css/global.css" media="all">
		<link rel="stylesheet" href="../plugins/font-awesome/css/font-awesome.min.css">
		<link rel="stylesheet" href="../css/table.css" />
		<meta http-equiv="pragma" content="no-cache"/><!-- 禁止浏览器从本地机的缓存中调阅页面内容 -->
		<meta http-equiv="Cache-Control" content="no-cache, must-revalidate"/>
		<meta http-equiv="expires" content="0"/> <!-- 定义网页 缓存中的过期时间-->
	</head>
	<body>
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
				<label class="layui-form-label">商户号</label>
				<div class="layui-input-block">
					<input type="text" name="id" disabled="disabled"  autocomplete="off" class="layui-input" value="${item.id?if_exists }">
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
					<input type="text" name="name"  disabled="disabled"  autocomplete="off" class="layui-input" value="${item.name?if_exists }">
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">联系人姓名</label>
				<div class="layui-input-block">
					<input type="text" name="contactName"  disabled="disabled" autocomplete="off" class="layui-input" value="${item.contactName?if_exists }">
				</div>
			</div>
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
						  class="layui-textarea" >${item.resKey?if_exists }
				</textarea>
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">D0比例</label>
				<div class="layui-input-block">
					<input type="text" name="d0Rate"  disabled="disabled" autocomplete="off" class="layui-input" value="${item.d0Rate?if_exists }">
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
	</body>

</html>