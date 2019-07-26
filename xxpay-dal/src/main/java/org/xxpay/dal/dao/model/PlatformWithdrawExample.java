package org.xxpay.dal.dao.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PlatformWithdrawExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<PlatformWithdrawExample.Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    private Integer limit;

    private Integer offset;

    public PlatformWithdrawExample() {
        oredCriteria = new ArrayList<PlatformWithdrawExample.Criteria>();
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

    public List<PlatformWithdrawExample.Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(PlatformWithdrawExample.Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public PlatformWithdrawExample.Criteria or() {
        PlatformWithdrawExample.Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public PlatformWithdrawExample.Criteria createCriteria() {
        PlatformWithdrawExample.Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected PlatformWithdrawExample.Criteria createCriteriaInternal() {
        PlatformWithdrawExample.Criteria criteria = new PlatformWithdrawExample.Criteria();
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

        public PlatformWithdrawExample.Criteria andIdEqualTo(String value) {
            addCriterion("Id =", value, "id");
            return (PlatformWithdrawExample.Criteria) this;
        }

        public PlatformWithdrawExample.Criteria andIdIn(List<String> values) {
            addCriterion("Id in", values, "id");
            return (PlatformWithdrawExample.Criteria) this;
        }

        public PlatformWithdrawExample.Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("CreateTime >", value, "createTime");
            return (PlatformWithdrawExample.Criteria) this;
        }

        public PlatformWithdrawExample.Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("CreateTime >=", value, "createTime");
            return (PlatformWithdrawExample.Criteria) this;
        }

        public PlatformWithdrawExample.Criteria andCreateTimeLessThan(Date value) {
            addCriterion("CreateTime <", value, "createTime");
            return (PlatformWithdrawExample.Criteria) this;
        }

        public PlatformWithdrawExample.Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("CreateTime <=", value, "createTime");
            return (PlatformWithdrawExample.Criteria) this;
        }

        public PlatformWithdrawExample.Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("CreateTime in", values, "createTime");
            return (PlatformWithdrawExample.Criteria) this;
        }

        public PlatformWithdrawExample.Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("CreateTime not in", values, "createTime");
            return (PlatformWithdrawExample.Criteria) this;
        }

        public PlatformWithdrawExample.Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("CreateTime between", value1, value2, "createTime");
            return (PlatformWithdrawExample.Criteria) this;
        }

        public PlatformWithdrawExample.Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("CreateTime not between", value1, value2, "createTime");
            return (PlatformWithdrawExample.Criteria) this;
        }

        public PlatformWithdrawExample.Criteria andUpdateTimeIsNull() {
            addCriterion("UpdateTime is null");
            return (PlatformWithdrawExample.Criteria) this;
        }

        public PlatformWithdrawExample.Criteria andUpdateTimeIsNotNull() {
            addCriterion("UpdateTime is not null");
            return (PlatformWithdrawExample.Criteria) this;
        }

        public PlatformWithdrawExample.Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("UpdateTime =", value, "updateTime");
            return (PlatformWithdrawExample.Criteria) this;
        }

        public PlatformWithdrawExample.Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("UpdateTime <>", value, "updateTime");
            return (PlatformWithdrawExample.Criteria) this;
        }

        public PlatformWithdrawExample.Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("UpdateTime >", value, "updateTime");
            return (PlatformWithdrawExample.Criteria) this;
        }

        public PlatformWithdrawExample.Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("UpdateTime >=", value, "updateTime");
            return (PlatformWithdrawExample.Criteria) this;
        }

        public PlatformWithdrawExample.Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("UpdateTime <", value, "updateTime");
            return (PlatformWithdrawExample.Criteria) this;
        }

        public PlatformWithdrawExample.Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("UpdateTime <=", value, "updateTime");
            return (PlatformWithdrawExample.Criteria) this;
        }

        public PlatformWithdrawExample.Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("UpdateTime in", values, "updateTime");
            return (PlatformWithdrawExample.Criteria) this;
        }

        public PlatformWithdrawExample.Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("UpdateTime not in", values, "updateTime");
            return (PlatformWithdrawExample.Criteria) this;
        }

        public PlatformWithdrawExample.Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("UpdateTime between", value1, value2, "updateTime");
            return (PlatformWithdrawExample.Criteria) this;
        }

        public PlatformWithdrawExample.Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("UpdateTime not between", value1, value2, "updateTime");
            return (PlatformWithdrawExample.Criteria) this;
        }
    }

    public static class Criteria extends PlatformWithdrawExample.GeneratedCriteria implements Serializable {

        protected Criteria() {
            super();
        }
    }

}
