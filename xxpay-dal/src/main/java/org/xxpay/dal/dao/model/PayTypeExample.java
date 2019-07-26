package org.xxpay.dal.dao.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PayTypeExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<PayTypeExample.Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    private Integer limit;

    private Integer offset;

    public PayTypeExample() {
        oredCriteria = new ArrayList<PayTypeExample.Criteria>();
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

    public List<PayTypeExample.Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(PayTypeExample.Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public PayTypeExample.Criteria or() {
        PayTypeExample.Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public PayTypeExample.Criteria createCriteria() {
        PayTypeExample.Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected PayTypeExample.Criteria createCriteriaInternal() {
        PayTypeExample.Criteria criteria = new PayTypeExample.Criteria();
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

        public PayTypeExample.Criteria andIdEqualTo(String value) {
            addCriterion("Id =", value, "id");
            return (PayTypeExample.Criteria) this;
        }

        public PayTypeExample.Criteria andIdIn(List<String> values) {
            addCriterion("Id in", values, "id");
            return (PayTypeExample.Criteria) this;
        }

        public PayTypeExample.Criteria andNameEqualTo(String value) {
            addCriterion("Name =", value, "name");
            return (PayTypeExample.Criteria) this;
        }

        public PayTypeExample.Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("CreateTime >", value, "createTime");
            return (PayTypeExample.Criteria) this;
        }

        public PayTypeExample.Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("CreateTime >=", value, "createTime");
            return (PayTypeExample.Criteria) this;
        }

        public PayTypeExample.Criteria andCreateTimeLessThan(Date value) {
            addCriterion("CreateTime <", value, "createTime");
            return (PayTypeExample.Criteria) this;
        }

        public PayTypeExample.Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("CreateTime <=", value, "createTime");
            return (PayTypeExample.Criteria) this;
        }

        public PayTypeExample.Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("CreateTime in", values, "createTime");
            return (PayTypeExample.Criteria) this;
        }

        public PayTypeExample.Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("CreateTime not in", values, "createTime");
            return (PayTypeExample.Criteria) this;
        }

        public PayTypeExample.Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("CreateTime between", value1, value2, "createTime");
            return (PayTypeExample.Criteria) this;
        }

        public PayTypeExample.Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("CreateTime not between", value1, value2, "createTime");
            return (PayTypeExample.Criteria) this;
        }

        public PayTypeExample.Criteria andUpdateTimeIsNull() {
            addCriterion("UpdateTime is null");
            return (PayTypeExample.Criteria) this;
        }

        public PayTypeExample.Criteria andUpdateTimeIsNotNull() {
            addCriterion("UpdateTime is not null");
            return (PayTypeExample.Criteria) this;
        }

        public PayTypeExample.Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("UpdateTime =", value, "updateTime");
            return (PayTypeExample.Criteria) this;
        }

        public PayTypeExample.Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("UpdateTime <>", value, "updateTime");
            return (PayTypeExample.Criteria) this;
        }

        public PayTypeExample.Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("UpdateTime >", value, "updateTime");
            return (PayTypeExample.Criteria) this;
        }

        public PayTypeExample.Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("UpdateTime >=", value, "updateTime");
            return (PayTypeExample.Criteria) this;
        }

        public PayTypeExample.Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("UpdateTime <", value, "updateTime");
            return (PayTypeExample.Criteria) this;
        }

        public PayTypeExample.Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("UpdateTime <=", value, "updateTime");
            return (PayTypeExample.Criteria) this;
        }

        public PayTypeExample.Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("UpdateTime in", values, "updateTime");
            return (PayTypeExample.Criteria) this;
        }

        public PayTypeExample.Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("UpdateTime not in", values, "updateTime");
            return (PayTypeExample.Criteria) this;
        }

        public PayTypeExample.Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("UpdateTime between", value1, value2, "updateTime");
            return (PayTypeExample.Criteria) this;
        }

        public PayTypeExample.Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("UpdateTime not between", value1, value2, "updateTime");
            return (PayTypeExample.Criteria) this;
        }
    }

    public static class Criteria extends PayTypeExample.GeneratedCriteria implements Serializable {

        protected Criteria() {
            super();
        }
    }

}
