<div style="margin: 15px;">
    <form class="layui-form">
        <#if item.id?exists>
            <input type="text" name="id" hidden="hidden" value="${item.id?if_exists }">
        </#if>
        <div class="layui-form-item">
            <label class="layui-form-label">商户</label>
            <div class="layui-input-block">
                <#if mchInfo?exists>
                    <input type="hidden" name="mchId" lay-verify="required" placeholder="请输入商户" autocomplete="off"
                           class="layui-input" value="${mchInfo.id }">
                    <input type="text" name="mchName" disabled="disabled" lay-verify="required" placeholder="请输入商户" autocomplete="off"
                           class="layui-input" value="${mchInfo.name }">

                </#if>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">支付通道</label>
            <div class="layui-input-block">
                <select name="payChannelId" id="payChannelId" lay-search="">
                    <#list payChannels as payChannel>
                        <#if  item.payChannelId?exists&& item.payChannelId == payChannel.id>
                            <option value="${payChannel.id}"
                        selected="selected">${payChannel.label} ( ${payChannel.code} )</option>
                        <#else>
                            <option value="${payChannel.id}"  >${payChannel.label} ( ${payChannel.code} )</option>
                        </#if>
                    </#list>
                </select>
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
            <label class="layui-form-label">平台手续费率(单位%)</label>
            <div class="layui-input-block">
                <input type="number" name="platformDeductionRate" lay-verify="required" placeholder="请输入平台手续费率(单位%)"
                       autocomplete="off" class="layui-input" value="${item.platformDeductionRate?if_exists }">
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
            <label class="layui-form-label">备注</label>
            <div class="layui-input-block">
                <input type="text" name="remark" placeholder="请输入备注" autocomplete="off" class="layui-input"
                       value="${item.remark?if_exists }">
            </div>
        </div>
        <button lay-filter="edit" lay-submit style="display: none;"></button>
    </form>
</div>