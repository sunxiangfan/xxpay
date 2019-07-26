package org.xxpay.dal.dao.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AgentMchPayChannelExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<AgentMchPayChannelExample.Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    private Integer limit;

    private Integer offset;

    public AgentMchPayChannelExample() {
        oredCriteria = new ArrayList<AgentMchPayChannelExample.Criteria>();
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

    public List<AgentMchPayChannelExample.Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(AgentMchPayChannelExample.Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public AgentMchPayChannelExample.Criteria or() {
        AgentMchPayChannelExample.Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public AgentMchPayChannelExample.Criteria createCriteria() {
        AgentMchPayChannelExample.Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected AgentMchPayChannelExample.Criteria createCriteriaInternal() {
        AgentMchPayChannelExample.Criteria criteria = new AgentMchPayChannelExample.Criteria();
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

        public AgentMchPayChannelExample.Criteria andIdEqualTo(String value) {
            addCriterion("Id =", value, "id");
            return (AgentMchPayChannelExample.Criteria) this;
        }

        public AgentMchPayChannelExample.Criteria andIdIn(List<String> values) {
            addCriterion("Id in", values, "id");
            return (AgentMchPayChannelExample.Criteria) this;
        }

        public AgentMchPayChannelExample.Criteria andPayChannelIdEqualTo(String value) {
            addCriterion("PayChannelId =", value, "payChannelId");
            return (AgentMchPayChannelExample.Criteria) this;
        }

        public AgentMchPayChannelExample.Criteria andMchIdEqualTo(String value) {
            addCriterion("MchId =", value, "mchId");
            return (AgentMchPayChannelExample.Criteria) this;
        }

        public AgentMchPayChannelExample.Criteria andMchTypeEqualTo(Byte value) {
            addCriterion("MchType =", value, "mchType");
            return (AgentMchPayChannelExample.Criteria) this;
        }

        public AgentMchPayChannelExample.Criteria andStateEqualTo(Byte value) {
            addCriterion("State =", value, "state");
            return (AgentMchPayChannelExample.Criteria) this;
        }

        public AgentMchPayChannelExample.Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("CreateTime >", value, "createTime");
            return (AgentMchPayChannelExample.Criteria) this;
        }

        public AgentMchPayChannelExample.Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("CreateTime >=", value, "createTime");
            return (AgentMchPayChannelExample.Criteria) this;
        }

        public AgentMchPayChannelExample.Criteria andCreateTimeLessThan(Date value) {
            addCriterion("CreateTime <", value, "createTime");
            return (AgentMchPayChannelExample.Criteria) this;
        }

        public AgentMchPayChannelExample.Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("CreateTime <=", value, "createTime");
            return (AgentMchPayChannelExample.Criteria) this;
        }

        public AgentMchPayChannelExample.Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("CreateTime in", values, "createTime");
            return (AgentMchPayChannelExample.Criteria) this;
        }

        public AgentMchPayChannelExample.Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("CreateTime not in", values, "createTime");
            return (AgentMchPayChannelExample.Criteria) this;
        }

        public AgentMchPayChannelExample.Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("CreateTime between", value1, value2, "createTime");
            return (AgentMchPayChannelExample.Criteria) this;
        }

        public AgentMchPayChannelExample.Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("CreateTime not between", value1, value2, "createTime");
            return (AgentMchPayChannelExample.Criteria) this;
        }

        public AgentMchPayChannelExample.Criteria andUpdateTimeIsNull() {
            addCriterion("UpdateTime is null");
            return (AgentMchPayChannelExample.Criteria) this;
        }

        public AgentMchPayChannelExample.Criteria andUpdateTimeIsNotNull() {
            addCriterion("UpdateTime is not null");
            return (AgentMchPayChannelExample.Criteria) this;
        }

        public AgentMchPayChannelExample.Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("UpdateTime =", value, "updateTime");
            return (AgentMchPayChannelExample.Criteria) this;
        }

        public AgentMchPayChannelExample.Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("UpdateTime <>", value, "updateTime");
            return (AgentMchPayChannelExample.Criteria) this;
        }

        public AgentMchPayChannelExample.Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("UpdateTime >", value, "updateTime");
            return (AgentMchPayChannelExample.Criteria) this;
        }

        public AgentMchPayChannelExample.Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("UpdateTime >=", value, "updateTime");
            return (AgentMchPayChannelExample.Criteria) this;
        }

        public AgentMchPayChannelExample.Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("UpdateTime <", value, "updateTime");
            return (AgentMchPayChannelExample.Criteria) this;
        }

        public AgentMchPayChannelExample.Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("UpdateTime <=", value, "updateTime");
            return (AgentMchPayChannelExample.Criteria) this;
        }

        public AgentMchPayChannelExample.Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("UpdateTime in", values, "updateTime");
            return (AgentMchPayChannelExample.Criteria) this;
        }

        public AgentMchPayChannelExample.Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("UpdateTime not in", values, "updateTime");
            return (AgentMchPayChannelExample.Criteria) this;
        }

        public AgentMchPayChannelExample.Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("UpdateTime between", value1, value2, "updateTime");
            return (AgentMchPayChannelExample.Criteria) this;
        }

        public AgentMchPayChannelExample.Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("UpdateTime not between", value1, value2, "updateTime");
            return (AgentMchPayChannelExample.Criteria) this;
        }
    }

    public static class Criteria extends AgentMchPayChannelExample.GeneratedCriteria implements Serializable {

        protected Criteria() {
            super();
        }
    }

}
