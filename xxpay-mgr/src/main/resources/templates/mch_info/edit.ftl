<div style="margin: 15px;">
    <form class="layui-form">
        <fieldset class="layui-elem-field">
            <legend>商户信息</legend>
            <#if item.id ? exists>
                <div class="layui-form-item">
                <label class="layui-form-label">商户号</label>
                <div class="layui-input-block">
                <input type="text" name="id" disabled="disabled" autocomplete="off"
                class="layui-input" value="${item.id?if_exists }">
                </div>
                </div>
            </#if>
            <div class="layui-form-item">
                <label class="layui-form-label">是否启用</label>
                <div class="layui-input-block">
                    <input type="checkbox" name="state" lay-skin="switch"
                           <#if (item.state!1) == 1>checked="checked"</#if> >
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
                <label class="layui-form-label">密码</label>
                <div class="layui-input-block">
                    <#if item.password ? exists>
                        <input type="password" name="password" placeholder="请输入密码" autocomplete="off"
                               class="layui-input">
                    <#else >
                        <input type="password" name="password" lay-verify="required" placeholder="请输入密码"
                               autocomplete="off"
                               class="layui-input">
                    </#if>
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">商户名称</label>
                <div class="layui-input-block">
                    <input type="text" name="name" placeholder="请输入商户名称" lay-verify="required" autocomplete="off"
                           class="layui-input"
                           value="${item.name?if_exists }">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">手机号</label>
                <div class="layui-input-block">
                    <input type="text" name="mobile" lay-verify="required" placeholder="请输入手机号" autocomplete="off"
                           class="layui-input" value="${item.mobile?if_exists }">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">邮箱地址</label>
                <div class="layui-input-block">
                    <input type="text" name="email" lay-verify="required" placeholder="请输入邮箱地址" autocomplete="off"
                           class="layui-input" value="${item.email?if_exists }">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">联系人姓名</label>
                <div class="layui-input-block">
                    <input type="text" name="contactName" placeholder="请输入联系人姓名" autocomplete="off" class="layui-input"
                           value="${item.contactName?if_exists }">
                </div>
            </div>

            <div class="layui-form-item">
                <label class="layui-form-label">D0比例(单位%)</label>
                <div class="layui-input-block">
                    <input type="number" name="d0Rate" lay-verify="required" placeholder="请输入平台手续费率(单位%)"
                           autocomplete="off" class="layui-input" value="${item.d0Rate?if_exists }">
                </div>
            </div>

            <div class="layui-form-item">
                <label class="layui-form-label">请求私钥</label>
                <div class="layui-input-block">
                    <textarea name="reqKey" lay-verify="required" id="reqKey" lay-filter="reqKey" readonly="readonly" placeholder="请输入请求私钥" autocomplete="off"
                          class="layui-textarea">${item.reqKey?if_exists }</textarea>
                    <button type="button" class="layui-btn layui-btn-sm" data-src="reqKey" id="btnResetReqKey"><i class="layui-icon layui-icon-refresh" aria-hidden="true"></i>
                        重置
                    </button>
                </div>

            </div>

            <div class="layui-form-item">
                <label class="layui-form-label">响应私钥</label>
                <div class="layui-input-block">
				<textarea name="resKey" lay-verify="required" id="resKey" lay-filter="resKey" readonly="readonly" placeholder="请输入响应私钥" autocomplete="off"
                          class="layui-textarea">${item.resKey?if_exists }</textarea>
                    <button type="button" class="layui-btn layui-btn-sm" data-src="resKey" id="btnResetResKey"><i class="layui-icon layui-icon-refresh" aria-hidden="true"></i>
                        重置
                    </button>
                </div>

            </div>

            <div class="layui-form-item">
                <label class="layui-form-label">审核状态</label>
                <div class="layui-input-block">
                    <select name="auditState" id="type" lay-search="">
                        <option value="0" <#if (item.auditState!1) == 0>selected="selected"</#if> >未审核</option>
                        <option value="1" <#if (item.auditState!1) == 1>selected="selected"</#if> >已审核</option>
                        <option value="2" <#if (item.auditState!1) == 2>selected="selected"</#if> >未通过审核</option>
                    </select>
                </div>
            </div>
        </fieldset>

        <fieldset class="layui-elem-field">
            <legend>进件信息</legend>
            <div class="layui-form-item">
                <label class="layui-form-label">客服电话</label>
                <div class="layui-input-block">
                    <input type="text" name="companyTel" placeholder="请输入客服电话" autocomplete="off"
                           class="layui-input" value="${company.companyTel?if_exists }">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">企业类型</label>
                <div class="layui-input-block">
                    <input type="text" name="corpType" placeholder="请输入企业类型" autocomplete="off" class="layui-input"
                           value="${company.corpType?if_exists }">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">公司简称</label>
                <div class="layui-input-block">
                    <input type="text" name="shortName" placeholder="请输入公司简称" autocomplete="off" class="layui-input"
                           value="${company.shortName?if_exists }">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">地址</label>
                <div class="layui-input-block">
                    <input type="text" name="address" placeholder="请输入地址" autocomplete="off" class="layui-input"
                           value="${company.address?if_exists }">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">公司类型</label>
                <div class="layui-input-block">
                    <input type="text" name="companyType" placeholder="请输入公司类型" autocomplete="off" class="layui-input"
                           value="${company.companyType?if_exists }">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">营业执照号</label>
                <div class="layui-input-block">
                    <input type="text" name="businessNo" placeholder="请输入营业执照号" autocomplete="off" class="layui-input"
                           value="${company.businessNo?if_exists }">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">有效期</label>
                <div class="layui-input-block">
                    <input type="text" name="businessNoExpiryDate" placeholder="请输入有效期" autocomplete="off"
                           class="layui-input" value="${company.businessNoExpiryDate?if_exists }">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">法人姓名</label>
                <div class="layui-input-block">
                    <input type="text" name="corporationName" placeholder="请输入法人姓名" autocomplete="off"
                           class="layui-input"
                           value="${company.corporationName?if_exists }">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">法人身份证</label>
                <div class="layui-input-block">
                    <input type="text" name="corporationId" placeholder="请输入法人身份证" autocomplete="off"
                           class="layui-input"
                           value="${company.corporationId?if_exists }">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">法人手机</label>
                <div class="layui-input-block">
                    <input type="text" name="corporationMobile" placeholder="请输入法人手机" autocomplete="off"
                           class="layui-input"
                           value="${company.corporationMobile?if_exists }">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">身份证正面照地址</label>
                <div id="idCardFrontUrlContent"></div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">身份证反面照地址</label>
                <div class="layui-input-block">
                    <div id="idCardBackUrlContent"></div>
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">营业执照地址</label>
                <div class="layui-input-block">
                    <div id="businessNoUrlContent"></div>
                </div>
            </div>
        </fieldset>
        <button lay-filter="edit" lay-submit style="display: none;"></button>
    </form>
</div>