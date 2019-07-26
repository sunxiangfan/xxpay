<div style="margin: 15px;">
    <form class="layui-form">
        <#if item.id?exists>
            <input type="text" name="id" hidden="hidden" value="${item.id?if_exists }">
        </#if>

        <div class="layui-form-item">
            <label class="layui-form-label">渠道名称</label>
            <div class="layui-input-block">
                <input type="text" name="payType" lay-verify="required" placeholder="请输入通道标识码" autocomplete="off"
                       class="layui-input" value="${item.payType?if_exists }">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">渠道名称</label>
            <div class="layui-input-block">
                <input type="text" name="name" lay-verify="required" placeholder="请输入渠道名称" autocomplete="off"
                       class="layui-input" value="${item.name?if_exists }">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">显示名称</label>
            <div class="layui-input-block">
                <input type="text" name="label" lay-verify="required" placeholder="请输入渠道显示名称" autocomplete="off"
                       class="layui-input" value="${item.label?if_exists }">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">是否启用</label>
            <div class="layui-input-block">
                <input type="checkbox" name="state" lay-skin="switch"
                       <#if (item.state!1) == 1>checked="checked"</#if> >
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">最小交易金额(单位元)</label>
            <div class="layui-input-block">
                <input type="number" name="minTransactionAmount" lay-verify="required" placeholder="请输入最小交易金额(单位元)"
                       autocomplete="off" class="layui-input" value="${item.minTransactionAmount?if_exists }">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">最大交易金额(单位元)</label>
            <div class="layui-input-block">
                <input type="number" name="maxTransactionAmount" lay-verify="required" placeholder="请输入最大交易金额(单位元)"
                       autocomplete="off" class="layui-input" value="${item.maxTransactionAmount?if_exists }">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">三方手续费(单位元)</label>
            <div class="layui-input-block">
                <input type="number" name="thirdDeduction" lay-verify="required" placeholder="请输入三方手续费(单位元)"
                       autocomplete="off" class="layui-input" value="${item.thirdDeduction?if_exists }">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">配置参数</label>
            <div class="layui-input-block">
                <textarea name="param" placeholder="请输入配置参数" autocomplete="off"
                          class="layui-textarea">${item.param?if_exists }</textarea>
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