package org.xxpay.dal.dao.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SystemSettingExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<SystemSettingExample.Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    private Integer limit;

    private Integer offset;

    public SystemSettingExample() {
        oredCriteria = new ArrayList<SystemSettingExample.Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<SystemSettingExample.Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(SystemSettingExample.Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public SystemSettingExample.Criteria or() {
        SystemSettingExample.Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public SystemSettingExample.Criteria createCriteria() {
        SystemSettingExample.Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected SystemSettingExample.Criteria createCriteriaInternal() {
        SystemSettingExample.Criteria criteria = new SystemSettingExample.Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getOffset() {
        return offset;
    }

    protected abstract static class GeneratedCriteria implements Serializable {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public SystemSettingExample.Criteria andIdEqualTo(String value) {
            addCriterion("Id =", value, "id");
            return (SystemSettingExample.Criteria) this;
        }

        public SystemSettingExample.Criteria andIdIn(List<String> values) {
            addCriterion("Id in", values, "id");
            return (SystemSettingExample.Criteria) this;
        }

        public SystemSettingExample.Criteria andParamNameEqualTo(String value) {
            addCriterion("ParamName =", value, "paramName");
            return (SystemSettingExample.Criteria) this;
        }

        public SystemSettingExample.Criteria andVersionEqualTo(int value) {
            addCriterion("Version =", value, "version");
            return (SystemSettingExample.Criteria) this;
        }

        public SystemSettingExample.Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("CreateTime >", value, "createTime");
            return (SystemSettingExample.Criteria) this;
        }

        public SystemSettingExample.Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("CreateTime >=", value, "createTime");
            return (SystemSettingExample.Criteria) this;
        }

        public SystemSettingExample.Criteria andCreateTimeLessThan(Date value) {
            addCriterion("CreateTime <", value, "createTime");
            return (SystemSettingExample.Criteria) this;
        }

        public SystemSettingExample.Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("CreateTime <=", value, "createTime");
            return (SystemSettingExample.Criteria) this;
        }

        public SystemSettingExample.Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("CreateTime in", values, "createTime");
            return (SystemSettingExample.Criteria) this;
        }

        public SystemSettingExample.Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("CreateTime not in", values, "createTime");
            return (SystemSettingExample.Criteria) this;
        }

        public SystemSettingExample.Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("CreateTime between", value1, value2, "createTime");
            return (SystemSettingExample.Criteria) this;
        }

        public SystemSettingExample.Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("CreateTime not between", value1, value2, "createTime");
            return (SystemSettingExample.Criteria) this;
        }

        public SystemSettingExample.Criteria andUpdateTimeIsNull() {
            addCriterion("UpdateTime is null");
            return (SystemSettingExample.Criteria) this;
        }

        public SystemSettingExample.Criteria andUpdateTimeIsNotNull() {
            addCriterion("UpdateTime is not null");
            return (SystemSettingExample.Criteria) this;
        }

        public SystemSettingExample.Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("UpdateTime =", value, "updateTime");
            return (SystemSettingExample.Criteria) this;
        }

        public SystemSettingExample.Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("UpdateTime <>", value, "updateTime");
            return (SystemSettingExample.Criteria) this;
        }

        public SystemSettingExample.Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("UpdateTime >", value, "updateTime");
            return (SystemSettingExample.Criteria) this;
        }

        public SystemSettingExample.Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("UpdateTime >=", value, "updateTime");
            return (SystemSettingExample.Criteria) this;
        }

        public SystemSettingExample.Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("UpdateTime <", value, "updateTime");
            return (SystemSettingExample.Criteria) this;
        }

        public SystemSettingExample.Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("UpdateTime <=", value, "updateTime");
            return (SystemSettingExample.Criteria) this;
        }

        public SystemSettingExample.Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("UpdateTime in", values, "updateTime");
            return (SystemSettingExample.Criteria) this;
        }

        public SystemSettingExample.Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("UpdateTime not in", values, "updateTime");
            return (SystemSettingExample.Criteria) this;
        }

        public SystemSettingExample.Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("UpdateTime between", value1, value2, "updateTime");
            return (SystemSettingExample.Criteria) this;
        }

        public SystemSettingExample.Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("UpdateTime not between", value1, value2, "updateTime");
            return (SystemSettingExample.Criteria) this;
        }
    }

    public static class Criteria extends SystemSettingExample.GeneratedCriteria implements Serializable {

        protected Criteria() {
            super();
        }
    }

}
