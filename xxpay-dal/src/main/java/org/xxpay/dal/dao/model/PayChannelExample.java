package org.xxpay.dal.dao.model;

import org.apache.ibatis.type.JdbcType;
import org.mybatis.generator.internal.types.Jdbc4Types;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PayChannelExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<PayChannelExample.Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    private Integer limit;

    private Integer offset;

    public PayChannelExample() {
        oredCriteria = new ArrayList<PayChannelExample.Criteria>();
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

    public List<PayChannelExample.Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(PayChannelExample.Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public PayChannelExample.Criteria or() {
        PayChannelExample.Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public PayChannelExample.Criteria createCriteria() {
        PayChannelExample.Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected PayChannelExample.Criteria createCriteriaInternal() {
        PayChannelExample.Criteria criteria = new PayChannelExample.Criteria();
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

        public PayChannelExample.Criteria andIdEqualTo(String value) {
            addCriterion("Id =", value, "id");
            return (PayChannelExample.Criteria) this;
        }

        public PayChannelExample.Criteria andIdIn(List<String> values) {
            addCriterion("Id in", values, "id");
            return (PayChannelExample.Criteria) this;
        }

        public PayChannelExample.Criteria andNameEqualTo(String value) {
            addCriterion("Name =", value, "name");
            return (PayChannelExample.Criteria) this;
        }

        public PayChannelExample.Criteria andCodeEqualTo(String value) {
            addCriterion("Code =", value, "code");
            return (PayChannelExample.Criteria) this;
        }

        public PayChannelExample.Criteria andTypeIdEqualTo(String value) {
            addCriterion("Type =", value, "typeId");
            return (PayChannelExample.Criteria) this;
        }

        public PayChannelExample.Criteria andStateEqualTo(Byte value) {
            addCriterion("State =", value, "state");
            return (PayChannelExample.Criteria) this;
        }

        public PayChannelExample.Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("CreateTime >", value, "createTime");
            return (PayChannelExample.Criteria) this;
        }

        public PayChannelExample.Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("CreateTime >=", value, "createTime");
            return (PayChannelExample.Criteria) this;
        }

        public PayChannelExample.Criteria andCreateTimeLessThan(Date value) {
            addCriterion("CreateTime <", value, "createTime");
            return (PayChannelExample.Criteria) this;
        }

        public PayChannelExample.Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("CreateTime <=", value, "createTime");
            return (PayChannelExample.Criteria) this;
        }

        public PayChannelExample.Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("CreateTime in", values, "createTime");
            return (PayChannelExample.Criteria) this;
        }

        public PayChannelExample.Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("CreateTime not in", values, "createTime");
            return (PayChannelExample.Criteria) this;
        }

        public PayChannelExample.Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("CreateTime between", value1, value2, "createTime");
            return (PayChannelExample.Criteria) this;
        }

        public PayChannelExample.Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("CreateTime not between", value1, value2, "createTime");
            return (PayChannelExample.Criteria) this;
        }

        public PayChannelExample.Criteria andUpdateTimeIsNull() {
            addCriterion("UpdateTime is null");
            return (PayChannelExample.Criteria) this;
        }

        public PayChannelExample.Criteria andUpdateTimeIsNotNull() {
            addCriterion("UpdateTime is not null");
            return (PayChannelExample.Criteria) this;
        }

        public PayChannelExample.Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("UpdateTime =", value, "updateTime");
            return (PayChannelExample.Criteria) this;
        }

        public PayChannelExample.Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("UpdateTime <>", value, "updateTime");
            return (PayChannelExample.Criteria) this;
        }

        public PayChannelExample.Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("UpdateTime >", value, "updateTime");
            return (PayChannelExample.Criteria) this;
        }

        public PayChannelExample.Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("UpdateTime >=", value, "updateTime");
            return (PayChannelExample.Criteria) this;
        }

        public PayChannelExample.Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("UpdateTime <", value, "updateTime");
            return (PayChannelExample.Criteria) this;
        }

        public PayChannelExample.Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("UpdateTime <=", value, "updateTime");
            return (PayChannelExample.Criteria) this;
        }

        public PayChannelExample.Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("UpdateTime in", values, "updateTime");
            return (PayChannelExample.Criteria) this;
        }

        public PayChannelExample.Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("UpdateTime not in", values, "updateTime");
            return (PayChannelExample.Criteria) this;
        }

        public PayChannelExample.Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("UpdateTime between", value1, value2, "updateTime");
            return (PayChannelExample.Criteria) this;
        }

        public PayChannelExample.Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("UpdateTime not between", value1, value2, "updateTime");
            return (PayChannelExample.Criteria) this;
        }
    }

    public static class Criteria extends PayChannelExample.GeneratedCriteria implements Serializable {

        protected Criteria() {
            super();
        }
    }

}
