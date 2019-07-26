<div style="margin: 15px;">
    <form class="layui-form">
        <#if item.id?exists>
            <input type="text" name="id" hidden="hidden" value="${item.id?if_exists }">
        </#if>
        <div class="layui-form-item">
            <label class="layui-form-label">资金变动类型</label>
            <div class="layui-input-block">
                <select name="flowType" id="flowType" lay-search="">
                    <#if item.flowType?exists && item.flowType==1>
                        <option value="0">转入</option>
                        <option value="1" selected="selected">转出</option>
                    <#else >
                        <option value="0">转入</option>
                        <option value="1">转出</option>
                    </#if>
                </select>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">变动金额</label>
            <div class="layui-input-block">
                <input type="number" name="amount" lay-verify="required" placeholder="变动金额(单位元)"
                       autocomplete="off" class="layui-input" value="${item.amount?if_exists }">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">备注</label>
            <div class="layui-input-block">
                <textarea name="remark" lay-verify="required" placeholder="备注" autocomplete="off"
                          class="layui-textarea">${item.remark?if_exists }</textarea>
            </div>
        </div>
        <button lay-filter="edit" lay-submit style="display: none;"></button>
    </form>
</div>