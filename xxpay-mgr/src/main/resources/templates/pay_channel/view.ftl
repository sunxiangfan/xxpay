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
            <label class="layui-form-label">通道名称</label>
            <div class="layui-input-block">
                <input type="text" name="name" disabled="disabled" autocomplete="off" class="layui-input"
                       value="${item.name?if_exists }">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">通道标识码</label>
            <div class="layui-input-block">
                <input type="text" name="code" disabled="disabled" autocomplete="off" class="layui-input"
                       value="${item.code?if_exists }">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">到账时间</label>
            <div class="layui-input-block">
                <input type="text" name="accountingCycle" disabled="disabled" autocomplete="off" class="layui-input"
                       value="${item.accountingCycle?if_exists }">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">最大交易金额(单位元)</label>
            <div class="layui-input-block">
                <input type="number" name="maxTransactionAmount" disabled="disabled" autocomplete="off"
                       class="layui-input" value="${item.maxTransactionAmount?if_exists }">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">最小交易金额(单位元)</label>
            <div class="layui-input-block">
                <input type="number" name="minTransactionAmount" disabled="disabled" autocomplete="off"
                       class="layui-input" value="${item.minTransactionAmount?if_exists }">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">开始时间</label>
            <div class="layui-input-block">
                <input type="time" name="startTime" disabled="disabled" autocomplete="off" class="layui-input"
                       value="${item.startTime?if_exists }">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">结束时间</label>
            <div class="layui-input-block">
                <input type="time" name="endTime" disabled="disabled" autocomplete="off" class="layui-input"
                       value="${item.endTime?if_exists }">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">三方手续费率(单位%)</label>
            <div class="layui-input-block">
                <input type="number" name="thirdDeductionRate" disabled="disabled" autocomplete="off"
                       class="layui-input" value="${item.thirdDeductionRate?if_exists }">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">配置参数</label>
            <div class="layui-input-block">
                <textarea name="param" disabled="disabled" autocomplete="off"
                          class="layui-textarea">${item.param?if_exists }</textarea>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">是否需要进件</label>
            <div class="layui-input-block">
                <#if item.material = 1>
                    <input type="text" style="color: green" disabled="disabled" class="layui-input" value="需要" }">
                <#else>
                    <input type="text" style="color: red" disabled="disabled" class="layui-input" value="不需要" }">
                </#if>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">备注</label>
            <div class="layui-input-block">
                <input type="text" name="remark" disabled="disabled" autocomplete="off" class="layui-input"
                       value="${item.remark?if_exists }">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">创建时间</label>
            <div class="layui-input-block">
                <input type="text" disabled="disabled" class="layui-input"
                       value="${(item.createTime?string("yyyy-MM-dd HH:mm:ss"))!''} ">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">更新时间</label>
            <div class="layui-input-block">
                <input type="text" disabled="disabled" class="layui-input"
                       value="${(item.updateTime?string("yyyy-MM-dd HH:mm:ss"))!''} ">
            </div>
        </div>
        <button lay-filter="edit" lay-submit style="display: none;"></button>
    </form>
</div>