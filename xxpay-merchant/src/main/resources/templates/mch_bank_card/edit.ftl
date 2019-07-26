<div style="margin: 15px;">
    <form class="layui-form">
        <#if item.id?exists>
            <input type="text" name="id" hidden="hidden" value="${item.id?if_exists }">
        </#if>
        <div class="layui-form-item">
            <label class="layui-form-label">是否对公<span style="color: red">*</span></label>
            <div class="layui-input-block">
                <input type="checkbox" name="type" lay-skin="switch" <#if (item.type!1) == 1>checked="checked"</#if> >
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">银行卡号<span style="color: red">*</span></label>
            <div class="layui-input-block">
                <input type="text" name="number" lay-verify="required" placeholder="银行卡号" autocomplete="off"
                       class="layui-input" value="${item.number?if_exists }">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">开户名<span style="color: red">*</span></label>
            <div class="layui-input-block">
                <input type="text" name="accountName" lay-verify="required" placeholder="开户名" autocomplete="off"
                       class="layui-input" value="${item.accountName?if_exists }">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">银行名称<span style="color: red">*</span></label>
            <div class="layui-input-block">
                <select name="bankName" id="bankName" lay-search="">
                    <#list banks as bank>
                        <#if  item.bankName?exists&& item.bankName == bank.name>
                            <option value="${bank.name}"
                                    selected="selected">${bank.name}</option>
                        <#else>
                            <option value="${bank.name}">${bank.name}</option>
                        </#if>
                    </#list>
                </select>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">开户行名称<span style="color: red">*</span></label>
            <div class="layui-input-block">
                <input type="text" name="registeredBankName" lay-verify="required" placeholder="开户行名称"
                       autocomplete="off" class="layui-input" value="${item.registeredBankName?if_exists }">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">预留手机号<span style="color: red">*</span></label>
            <div class="layui-input-block">
                <input type="text" name="mobile" placeholder="预留手机号" lay-verify="required" autocomplete="off"
                       class="layui-input" value="${item.mobile?if_exists }">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">省<span style="color: red">*</span></label>
            <div class="layui-input-block">
                <input type="text" name="province" placeholder="省" lay-verify="required" autocomplete="off"
                       class="layui-input" value="${item.province?if_exists }">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">市<span style="color: red">*</span></label>
            <div class="layui-input-block">
                <input type="text" name="city" placeholder="市" lay-verify="required" autocomplete="off"
                       class="layui-input" value="${item.city?if_exists }">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">开户行联行号<span style="color: red">*</span></label>
            <div class="layui-input-block">
                <input type="text" name="bankUnionCode" placeholder="开户行联行号，可电话询问开户行" lay-verify="required"
                       autocomplete="off" class="layui-input" value="${item.bankUnionCode?if_exists }">
                <a href="http://www.lianhanghao.com/" target="_blank" style=" color: red;text-decoration: underline;">点击进入
                    http://www.lianhanghao.com/ 查询开户行联行号</a>或<a href="http://www.kaihuhang.cn/" target="_blank" style=" color: red;text-decoration: underline;">点击进入
                    http://www.kaihuhang.cn/ 查询开户行联行号</a>或
                <span style="color:red;">电话咨询开户行</span>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">身份证号</label>
            <div class="layui-input-block">
                <input type="text" name="idCard" placeholder="身份证号" autocomplete="off" class="layui-input"
                       value="${item.idCard?if_exists }">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">备注</label>
            <div class="layui-input-block">
                <input type="text" name="remark" placeholder="请输入备注" autocomplete="off" class="layui-input"
                       value="${item.remark?if_exists }">
            </div>
        </div>
        <button lay-filter="edit" lay-submit style="display: none;"></button>
    </form>
</div>