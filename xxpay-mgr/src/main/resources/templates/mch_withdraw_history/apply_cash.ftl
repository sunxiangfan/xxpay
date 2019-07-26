<div style="margin: 15px;">
    <form class="layui-form">
        <#if item.id?exists>
            <input type="text" name="id" hidden="hidden" value="${item.id?if_exists }">
        </#if>

        <div class="layui-form-item">
            <label class="layui-form-label">渠道名称</label>
            <div class="layui-input-block">
                <select name="channelId" id="channelId" lay-search="">
                    <#list cashChannels as channel>
                        <option value="${channel.id}">${channel.label} </option>
                    </#list>
                </select>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">商户号</label>
            <div class="layui-input-block">
                <input type="text" name="mchId" placeholder="商户号" readonly="readonly" autocomplete="off" class="layui-input"
                       value="${item.mchId?if_exists }">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">申请金额</label>
            <div class="layui-input-block">
                <input type="text" name="applyAmount" placeholder="申请金额" readonly="readonly" autocomplete="off" class="layui-input"
                       value="${item.applyAmount?if_exists }">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">银行名称<span style="color: red">*</span></label>
            <div class="layui-input-block">
                <input type="text" name="bankName" placeholder="银行名称" readonly="readonly" class="layui-input" value="${item.bankName?if_exists }"/>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">开户行名称<span style="color: red">*</span></label>
            <div class="layui-input-block">
                <input type="text" name="registeredBankName" lay-verify="required" readonly="readonly" placeholder="开户行名称"
                       autocomplete="off" class="layui-input" value="${item.registeredBankName?if_exists }">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">省<span style="color: red">*</span></label>
            <div class="layui-input-block">
                <input type="text" name="province" placeholder="省" lay-verify="required" readonly="readonly"  autocomplete="off"
                       class="layui-input" value="${item.province?if_exists }">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">市<span style="color: red">*</span></label>
            <div class="layui-input-block">
                <input type="text" name="city" placeholder="市" lay-verify="required" readonly="readonly"  autocomplete="off"
                       class="layui-input" value="${item.city?if_exists }">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">开户名</label>
            <div class="layui-input-block">
                <input type="text" name="accountName" placeholder="开户名" readonly="readonly" autocomplete="off" class="layui-input"
                       value="${item.accountName?if_exists }">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">卡号</label>
            <div class="layui-input-block">
                <input type="text" name="number" placeholder="卡号" readonly="readonly" autocomplete="off" class="layui-input"
                       value="${item.number?if_exists }">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">预留手机号<span style="color: red">*</span></label>
            <div class="layui-input-block">
                <input type="text" name="mobile" placeholder="预留手机号" lay-verify="required" readonly="readonly" autocomplete="off"
                       class="layui-input" value="${item.mobile?if_exists }">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">开户行联行号</label>
            <div class="layui-input-block">
                <input type="text" name="bankUnionCode" placeholder="开户行联行号，可电话询问开户行"
                       autocomplete="off" class="layui-input" value="${item.bankUnionCode?if_exists }">
                <a href="http://www.lianhanghao.com/" target="_blank" style=" color: red;text-decoration: underline;">点击进入
                    http://www.lianhanghao.com/ 查询开户行联行号</a>或<a href="http://www.kaihuhang.cn/" target="_blank" style=" color: red;text-decoration: underline;">点击进入
                    http://www.kaihuhang.cn/ 查询开户行联行号</a>或
                <span style="color:red;">电话咨询开户行</span>
            </div>
        </div>


        <button lay-filter="edit" lay-submit style="display: none;"></button>
    </form>
</div>