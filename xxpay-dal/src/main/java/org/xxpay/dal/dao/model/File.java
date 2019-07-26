package org.xxpay.dal.dao.model;

import java.io.Serializable;
import java.util.Date;


public class File implements Serializable {
	/**
	 * Id
	 */
  private String id;
	/**
	 * 文件字节码数组
	 */
  private byte[] bytes;

	/**
	 * 创建时间
	 */
  private Date createTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
