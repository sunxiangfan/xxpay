package org.xxpay.dal.dao.model;


import java.util.Date;

public class MchPayChannel {

  /**
   * Id
   */
  private String id;
  /**
   * 商户id
   */
  private String mchId;

  /**
   * 商户类型,0-普通商户,1-子商户
   */
  private Byte mchType;

  /**
   * 通道id
   */
  private String payChannelId;

  /**
   * 代理商户分润率
   */
  private Double agentMchCommissionRate;

  /**
   * 平台手续费率
   */
  private Double platformDeductionRate;

  /**
   * 渠道状态,0-停止使用,1-使用中
   */
  private Byte state;

  /**
   * 备注
   */
  private String remark;


  /**
   * D0比例
   */
  private Double d0Rate;

  /**
   * 创建人用户ID
   */
  private String createBy;

  /**
   * 创建人用户ID
   */
  private String updateBy;
  /**
   * 创建时间
   */
  private Date createTime;
  /**
   * 更新时间
   */
  private Date updateTime;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getMchId() {
    return mchId;
  }

  public void setMchId(String mchId) {
    this.mchId = mchId;
  }

  public Byte getMchType() {
    return mchType;
  }

  public void setMchType(Byte mchType) {
    this.mchType = mchType;
  }

  public String getPayChannelId() {
    return payChannelId;
  }

  public void setPayChannelId(String payChannelId) {
    this.payChannelId = payChannelId;
  }

  public Double getAgentMchCommissionRate() {
    return agentMchCommissionRate;
  }

  public void setAgentMchCommissionRate(Double agentMchCommissionRate) {
    this.agentMchCommissionRate = agentMchCommissionRate;
  }

  public Double getPlatformDeductionRate() {
    return platformDeductionRate;
  }

  public void setPlatformDeductionRate(Double platformDeductionRate) {
    this.platformDeductionRate = platformDeductionRate;
  }

  public Byte getState() {
    return state;
  }

  public void setState(Byte state) {
    this.state = state;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public Double getD0Rate() {
    return d0Rate;
  }

  public void setD0Rate(Double d0Rate) {
    this.d0Rate = d0Rate;
  }

  public String getCreateBy() {
    return createBy;
  }

  public void setCreateBy(String createBy) {
    this.createBy = createBy;
  }

  public String getUpdateBy() {
    return updateBy;
  }

  public void setUpdateBy(String updateBy) {
    this.updateBy = updateBy;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }
}
