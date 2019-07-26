package org.xxpay.dal.dao.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CashChannelExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<CashChannelExample.Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    private Integer limit;

    private Integer offset;

    public CashChannelExample() {
        oredCriteria = new ArrayList<CashChannelExample.Criteria>();
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

    public List<CashChannelExample.Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(CashChannelExample.Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public CashChannelExample.Criteria or() {
        CashChannelExample.Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public CashChannelExample.Criteria createCriteria() {
        CashChannelExample.Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected CashChannelExample.Criteria createCriteriaInternal() {
        CashChannelExample.Criteria criteria = new CashChannelExample.Criteria();
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

        public CashChannelExample.Criteria andIdEqualTo(String value) {
            addCriterion("Id =", value, "id");
            return (CashChannelExample.Criteria) this;
        }

        public CashChannelExample.Criteria andIdIn(List<String> values) {
            addCriterion("Id in", values, "id");
            return (CashChannelExample.Criteria) this;
        }

        public CashChannelExample.Criteria andNameEqualTo(String value) {
            addCriterion("Name =", value, "name");
            return (CashChannelExample.Criteria) this;
        }

        public CashChannelExample.Criteria andCodeEqualTo(String value) {
            addCriterion("Code =", value, "code");
            return (CashChannelExample.Criteria) this;
        }

        public CashChannelExample.Criteria andTypeIdEqualTo(String value) {
            addCriterion("Type =", value, "typeId");
            return (CashChannelExample.Criteria) this;
        }

        public CashChannelExample.Criteria andStateEqualTo(Byte value) {
            addCriterion("State =", value, "state");
            return (CashChannelExample.Criteria) this;
        }

        public CashChannelExample.Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("CreateTime >", value, "createTime");
            return (CashChannelExample.Criteria) this;
        }

        public CashChannelExample.Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("CreateTime >=", value, "createTime");
            return (CashChannelExample.Criteria) this;
        }

        public CashChannelExample.Criteria andCreateTimeLessThan(Date value) {
            addCriterion("CreateTime <", value, "createTime");
            return (CashChannelExample.Criteria) this;
        }

        public CashChannelExample.Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("CreateTime <=", value, "createTime");
            return (CashChannelExample.Criteria) this;
        }

        public CashChannelExample.Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("CreateTime in", values, "createTime");
            return (CashChannelExample.Criteria) this;
        }

        public CashChannelExample.Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("CreateTime not in", values, "createTime");
            return (CashChannelExample.Criteria) this;
        }

        public CashChannelExample.Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("CreateTime between", value1, value2, "createTime");
            return (CashChannelExample.Criteria) this;
        }

        public CashChannelExample.Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("CreateTime not between", value1, value2, "createTime");
            return (CashChannelExample.Criteria) this;
        }

        public CashChannelExample.Criteria andUpdateTimeIsNull() {
            addCriterion("UpdateTime is null");
            return (CashChannelExample.Criteria) this;
        }

        public CashChannelExample.Criteria andUpdateTimeIsNotNull() {
            addCriterion("UpdateTime is not null");
            return (CashChannelExample.Criteria) this;
        }

        public CashChannelExample.Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("UpdateTime =", value, "updateTime");
            return (CashChannelExample.Criteria) this;
        }

        public CashChannelExample.Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("UpdateTime <>", value, "updateTime");
            return (CashChannelExample.Criteria) this;
        }

        public CashChannelExample.Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("UpdateTime >", value, "updateTime");
            return (CashChannelExample.Criteria) this;
        }

        public CashChannelExample.Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("UpdateTime >=", value, "updateTime");
            return (CashChannelExample.Criteria) this;
        }

        public CashChannelExample.Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("UpdateTime <", value, "updateTime");
            return (CashChannelExample.Criteria) this;
        }

        public CashChannelExample.Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("UpdateTime <=", value, "updateTime");
            return (CashChannelExample.Criteria) this;
        }

        public CashChannelExample.Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("UpdateTime in", values, "updateTime");
            return (CashChannelExample.Criteria) this;
        }

        public CashChannelExample.Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("UpdateTime not in", values, "updateTime");
            return (CashChannelExample.Criteria) this;
        }

        public CashChannelExample.Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("UpdateTime between", value1, value2, "updateTime");
            return (CashChannelExample.Criteria) this;
        }

        public CashChannelExample.Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("UpdateTime not between", value1, value2, "updateTime");
            return (CashChannelExample.Criteria) this;
        }
    }

    public static class Criteria extends CashChannelExample.GeneratedCriteria implements Serializable {

        protected Criteria() {
            super();
        }
    }

}
