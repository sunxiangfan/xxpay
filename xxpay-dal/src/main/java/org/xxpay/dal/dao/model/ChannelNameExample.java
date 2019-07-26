package org.xxpay.dal.dao.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChannelNameExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<ChannelNameExample.Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    private Integer limit;

    private Integer offset;

    public ChannelNameExample() {
        oredCriteria = new ArrayList<ChannelNameExample.Criteria>();
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

    public List<ChannelNameExample.Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(ChannelNameExample.Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public ChannelNameExample.Criteria or() {
        ChannelNameExample.Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public ChannelNameExample.Criteria createCriteria() {
        ChannelNameExample.Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected ChannelNameExample.Criteria createCriteriaInternal() {
        ChannelNameExample.Criteria criteria = new ChannelNameExample.Criteria();
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

        public ChannelNameExample.Criteria andIdEqualTo(String value) {
            addCriterion("Id =", value, "id");
            return (ChannelNameExample.Criteria) this;
        }

        public ChannelNameExample.Criteria andIdIn(List<String> values) {
            addCriterion("Id in", values, "id");
            return (ChannelNameExample.Criteria) this;
        }

        public ChannelNameExample.Criteria andNameEqualTo(String value) {
            addCriterion("Name =", value, "name");
            return (ChannelNameExample.Criteria) this;
        }

        public ChannelNameExample.Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("CreateTime >", value, "createTime");
            return (ChannelNameExample.Criteria) this;
        }

        public ChannelNameExample.Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("CreateTime >=", value, "createTime");
            return (ChannelNameExample.Criteria) this;
        }

        public ChannelNameExample.Criteria andCreateTimeLessThan(Date value) {
            addCriterion("CreateTime <", value, "createTime");
            return (ChannelNameExample.Criteria) this;
        }

        public ChannelNameExample.Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("CreateTime <=", value, "createTime");
            return (ChannelNameExample.Criteria) this;
        }

        public ChannelNameExample.Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("CreateTime in", values, "createTime");
            return (ChannelNameExample.Criteria) this;
        }

        public ChannelNameExample.Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("CreateTime not in", values, "createTime");
            return (ChannelNameExample.Criteria) this;
        }

        public ChannelNameExample.Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("CreateTime between", value1, value2, "createTime");
            return (ChannelNameExample.Criteria) this;
        }

        public ChannelNameExample.Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("CreateTime not between", value1, value2, "createTime");
            return (ChannelNameExample.Criteria) this;
        }

        public ChannelNameExample.Criteria andUpdateTimeIsNull() {
            addCriterion("UpdateTime is null");
            return (ChannelNameExample.Criteria) this;
        }

        public ChannelNameExample.Criteria andUpdateTimeIsNotNull() {
            addCriterion("UpdateTime is not null");
            return (ChannelNameExample.Criteria) this;
        }

        public ChannelNameExample.Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("UpdateTime =", value, "updateTime");
            return (ChannelNameExample.Criteria) this;
        }

        public ChannelNameExample.Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("UpdateTime <>", value, "updateTime");
            return (ChannelNameExample.Criteria) this;
        }

        public ChannelNameExample.Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("UpdateTime >", value, "updateTime");
            return (ChannelNameExample.Criteria) this;
        }

        public ChannelNameExample.Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("UpdateTime >=", value, "updateTime");
            return (ChannelNameExample.Criteria) this;
        }

        public ChannelNameExample.Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("UpdateTime <", value, "updateTime");
            return (ChannelNameExample.Criteria) this;
        }

        public ChannelNameExample.Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("UpdateTime <=", value, "updateTime");
            return (ChannelNameExample.Criteria) this;
        }

        public ChannelNameExample.Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("UpdateTime in", values, "updateTime");
            return (ChannelNameExample.Criteria) this;
        }

        public ChannelNameExample.Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("UpdateTime not in", values, "updateTime");
            return (ChannelNameExample.Criteria) this;
        }

        public ChannelNameExample.Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("UpdateTime between", value1, value2, "updateTime");
            return (ChannelNameExample.Criteria) this;
        }

        public ChannelNameExample.Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("UpdateTime not between", value1, value2, "updateTime");
            return (ChannelNameExample.Criteria) this;
        }
    }

    public static class Criteria extends ChannelNameExample.GeneratedCriteria implements Serializable {

        protected Criteria() {
            super();
        }
    }

}
