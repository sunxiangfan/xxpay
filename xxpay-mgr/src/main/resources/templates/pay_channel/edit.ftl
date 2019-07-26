<div style="margin: 15px;">
    <form class="layui-form">
        <#if item.id?exists>
            <input type="text" name="id" hidden="hidden" value="${item.id?if_exists }">
        </#if>

        <div class="layui-form-item">
            <label class="layui-form-label">渠道名称</label>
            <div class="layui-input-block">
                <select name="name" id="name" lay-search="">
                    <#list channelNames as channelName>
                        <#if  item.name?exists&& item.name == channelName.id>
                            <option value="${channelName.id}"
                        selected="selected">${channelName.name} ( ${channelName.id} )</option>
                        <#else>
                            <option value="${channelName.id}"  >${channelName.name} ( ${channelName.id} )</option>
                        </#if>
                    </#list>
                </select>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">支付方式</label>
            <div class="layui-input-block">
                <select name="payType" id="payType" lay-search="">
                    <#list payTypes as payType>
                        <#if  item.payType?exists&& item.payType == payType.id>
                            <option value="${payType.id}"
                        selected="selected">${payType.name} ( ${payType.id} )</option>
                        <#else>
                            <option value="${payType.id}"  >${payType.name} ( ${payType.id} )</option>
                        </#if>
                    </#list>
                </select>
            </div>
        </div>

        <#if item.code?exists >
            <div class="layui-form-item">
            <label class="layui-form-label">通道标识码</label>
            <div class="layui-input-block">
            <input type="text" name="code" lay-verify="required" readonly="readonly" placeholder="请输入通道标识码" autocomplete="off"
            class="layui-input" value="${item.code?if_exists }">
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
            <label class="layui-form-label">到账时间</label>
            <div class="layui-input-block">
                <input type="text" name="accountingCycle" lay-verify="required" placeholder="请输入到账时间" autocomplete="off"
                       class="layui-input" value="${item.accountingCycle?if_exists }">
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
            <label class="layui-form-label">最小交易金额(单位元)</label>
            <div class="layui-input-block">
                <input type="number" name="minTransactionAmount" lay-verify="required" placeholder="请输入最小交易金额(单位元)"
                       autocomplete="off" class="layui-input" value="${item.minTransactionAmount?if_exists }">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">开始时间</label>
            <div class="layui-input-block">
                <input type="time" name="startTime" lay-verify="required" placeholder="请输入开始时间" autocomplete="off"
                       class="layui-input" value="${item.startTime?if_exists }">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">结束时间</label>
            <div class="layui-input-block">
                <input type="time" name="endTime" lay-verify="required" placeholder="请输入结束时间" autocomplete="off"
                       class="layui-input" value="${item.endTime?if_exists }">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">三方手续费率(单位%)</label>
            <div class="layui-input-block">
                <input type="number" name="thirdDeductionRate" lay-verify="required" placeholder="请输入三方手续费率(单位%)"
                       autocomplete="off" class="layui-input" value="${item.thirdDeductionRate?if_exists }">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">支付通道名称</label>
            <div class="layui-input-block">
                <input type="text" name="label" placeholder="支付通道名称" autocomplete="off" class="layui-input"
                       value="${item.label?if_exists }">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">T1比例(单位%)</label>
            <div class="layui-input-block">
                <input type="number" name="t1Rate" lay-verify="required" placeholder="T1比例(单位%)，默认20"
                       autocomplete="off" class="layui-input" value="${item.t1Rate ! 20 }">
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
            <label class="layui-form-label">是否需要进件</label>
            <div class="layui-input-block">
                <input type="checkbox" name="material" lay-skin="switch"
                       <#if (item.material!1) == 1>checked="checked"</#if> >
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