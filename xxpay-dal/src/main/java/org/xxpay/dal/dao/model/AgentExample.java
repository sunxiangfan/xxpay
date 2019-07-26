package org.xxpay.dal.dao.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AgentExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    private Integer limit;

    private Integer offset;

    public AgentExample() {
        oredCriteria = new ArrayList<Criteria>();
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

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
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

        public Criteria andIdEqualTo(String value) {
            addCriterion("Id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<String> values) {
            addCriterion("Id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("Name =", value, "name");
            return (Criteria) this;
        }

        public Criteria andVersionEqualTo(Integer value) {
            addCriterion("Version =", value, "version");
            return (Criteria) this;
        }

        public Criteria andBalanceEqualTo(Long value) {
            addCriterion("Balance =", value, "balance");
            return (Criteria) this;
        }
        public Criteria andReqKeyIsNull() {
            addCriterion("ReqKey is null");
            return (Criteria) this;
        }

        public Criteria andReqKeyIsNotNull() {
            addCriterion("ReqKey is not null");
            return (Criteria) this;
        }

        public Criteria andReqKeyEqualTo(String value) {
            addCriterion("ReqKey =", value, "reqKey");
            return (Criteria) this;
        }

        public Criteria andReqKeyNotEqualTo(String value) {
            addCriterion("ReqKey <>", value, "reqKey");
            return (Criteria) this;
        }

        public Criteria andReqKeyGreaterThan(String value) {
            addCriterion("ReqKey >", value, "reqKey");
            return (Criteria) this;
        }

        public Criteria andReqKeyGreaterThanOrEqualTo(String value) {
            addCriterion("ReqKey >=", value, "reqKey");
            return (Criteria) this;
        }

        public Criteria andReqKeyLessThan(String value) {
            addCriterion("ReqKey <", value, "reqKey");
            return (Criteria) this;
        }

        public Criteria andReqKeyLessThanOrEqualTo(String value) {
            addCriterion("ReqKey <=", value, "reqKey");
            return (Criteria) this;
        }

        public Criteria andReqKeyLike(String value) {
            addCriterion("ReqKey like", value, "reqKey");
            return (Criteria) this;
        }

        public Criteria andReqKeyNotLike(String value) {
            addCriterion("ReqKey not like", value, "reqKey");
            return (Criteria) this;
        }

        public Criteria andReqKeyIn(List<String> values) {
            addCriterion("ReqKey in", values, "reqKey");
            return (Criteria) this;
        }

        public Criteria andReqKeyNotIn(List<String> values) {
            addCriterion("ReqKey not in", values, "reqKey");
            return (Criteria) this;
        }

        public Criteria andReqKeyBetween(String value1, String value2) {
            addCriterion("ReqKey between", value1, value2, "reqKey");
            return (Criteria) this;
        }

        public Criteria andReqKeyNotBetween(String value1, String value2) {
            addCriterion("ReqKey not between", value1, value2, "reqKey");
            return (Criteria) this;
        }

        public Criteria andResKeyIsNull() {
            addCriterion("ResKey is null");
            return (Criteria) this;
        }

        public Criteria andResKeyIsNotNull() {
            addCriterion("ResKey is not null");
            return (Criteria) this;
        }

        public Criteria andResKeyEqualTo(String value) {
            addCriterion("ResKey =", value, "resKey");
            return (Criteria) this;
        }

        public Criteria andResKeyNotEqualTo(String value) {
            addCriterion("ResKey <>", value, "resKey");
            return (Criteria) this;
        }

        public Criteria andResKeyGreaterThan(String value) {
            addCriterion("ResKey >", value, "resKey");
            return (Criteria) this;
        }

        public Criteria andResKeyGreaterThanOrEqualTo(String value) {
            addCriterion("ResKey >=", value, "resKey");
            return (Criteria) this;
        }

        public Criteria andResKeyLessThan(String value) {
            addCriterion("ResKey <", value, "resKey");
            return (Criteria) this;
        }

        public Criteria andResKeyLessThanOrEqualTo(String value) {
            addCriterion("ResKey <=", value, "resKey");
            return (Criteria) this;
        }

        public Criteria andResKeyLike(String value) {
            addCriterion("ResKey like", value, "resKey");
            return (Criteria) this;
        }

        public Criteria andResKeyNotLike(String value) {
            addCriterion("ResKey not like", value, "resKey");
            return (Criteria) this;
        }

        public Criteria andResKeyIn(List<String> values) {
            addCriterion("ResKey in", values, "resKey");
            return (Criteria) this;
        }

        public Criteria andResKeyNotIn(List<String> values) {
            addCriterion("ResKey not in", values, "resKey");
            return (Criteria) this;
        }

        public Criteria andResKeyBetween(String value1, String value2) {
            addCriterion("ResKey between", value1, value2, "resKey");
            return (Criteria) this;
        }

        public Criteria andResKeyNotBetween(String value1, String value2) {
            addCriterion("ResKey not between", value1, value2, "resKey");
            return (Criteria) this;
        }

        public Criteria andStateIsNull() {
            addCriterion("State is null");
            return (Criteria) this;
        }

        public Criteria andStateIsNotNull() {
            addCriterion("State is not null");
            return (Criteria) this;
        }

        public Criteria andStateEqualTo(Byte value) {
            addCriterion("State =", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotEqualTo(Byte value) {
            addCriterion("State <>", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateGreaterThan(Byte value) {
            addCriterion("State >", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateGreaterThanOrEqualTo(Byte value) {
            addCriterion("State >=", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateLessThan(Byte value) {
            addCriterion("State <", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateLessThanOrEqualTo(Byte value) {
            addCriterion("State <=", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateIn(List<Byte> values) {
            addCriterion("State in", values, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotIn(List<Byte> values) {
            addCriterion("State not in", values, "state");
            return (Criteria) this;
        }

        public Criteria andStateBetween(Byte value1, Byte value2) {
            addCriterion("State between", value1, value2, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotBetween(Byte value1, Byte value2) {
            addCriterion("State not between", value1, value2, "state");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("CreateTime is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("CreateTime is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("CreateTime =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("CreateTime <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("CreateTime >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("CreateTime >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("CreateTime <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("CreateTime <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("CreateTime in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("CreateTime not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("CreateTime between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("CreateTime not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("UpdateTime is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("UpdateTime is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("UpdateTime =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("UpdateTime <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("UpdateTime >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("UpdateTime >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("UpdateTime <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("UpdateTime <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("UpdateTime in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("UpdateTime not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("UpdateTime between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("UpdateTime not between", value1, value2, "updateTime");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria implements Serializable {

        protected Criteria() {
            super();
        }
    }
}