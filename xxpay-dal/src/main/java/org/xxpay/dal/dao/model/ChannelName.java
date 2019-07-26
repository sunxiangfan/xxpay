package org.xxpay.dal.dao.model;

import java.io.Serializable;
import java.util.Date;


public class ChannelName implements Serializable {
	/**
	 * Id
	 */
  private String id;
	/**
	 * 名称
	 */
  private String name;
	/**
	 * 创建人用户ID
	 */
  private String createBy;
	/**
	 * 更新人用户ID
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


	/**
	 *get
	 * @return
	 */
  public String getId() {
    return id;
  }

	/**
	 *set
	 * @return
	 */
  public void setId(String id) {
    this.id = id;
  }


	/**
	 *get
	 * @return
	 */
  public String getName() {
    return name;
  }

	/**
	 *set
	 * @return
	 */
  public void setName(String name) {
    this.name = name;
  }

	/**
	 *get
	 * @return
	 */
  public String getCreateBy() {
    return createBy;
  }

	/**
	 *set
	 * @return
	 */
  public void setCreateBy(String createBy) {
    this.createBy = createBy;
  }


	/**
	 *get
	 * @return
	 */
  public String getUpdateBy() {
    return updateBy;
  }

	/**
	 *set
	 * @return
	 */
  public void setUpdateBy(String updateBy) {
    this.updateBy = updateBy;
  }


	/**
	 *get
	 * @return
	 */
  public Date getCreateTime() {
    return createTime;
  }

	/**
	 *set
	 * @return
	 */
  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }


	/**
	 *get
	 * @return
	 */
  public Date getUpdateTime() {
    return updateTime;
  }

	/**
	 *set
	 * @return
	 */
  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

}
