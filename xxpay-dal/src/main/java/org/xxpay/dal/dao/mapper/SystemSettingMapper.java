package org.xxpay.dal.dao.mapper;

import org.xxpay.dal.dao.model.SystemSetting;
import org.xxpay.dal.dao.model.SystemSettingExample;

/**
 * 系统设置项Mapper
 */
public interface SystemSettingMapper extends BaseMapper<SystemSetting, SystemSettingExample> {
    SystemSetting selectByName(String name);

}
