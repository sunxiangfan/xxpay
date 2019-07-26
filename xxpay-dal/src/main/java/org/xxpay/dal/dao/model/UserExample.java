package org.xxpay.dal.dao.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<UserExample.Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    private Integer limit;

    private Integer offset;

    public UserExample() {
        oredCriteria = new ArrayList<UserExample.Criteria>();
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

    public List<UserExample.Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(UserExample.Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public UserExample.Criteria or() {
        UserExample.Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public UserExample.Criteria createCriteria() {
        UserExample.Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected UserExample.Criteria createCriteriaInternal() {
        UserExample.Criteria criteria = new UserExample.Criteria();
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
        protected List<UserExample.Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<UserExample.Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<UserExample.Criterion> getAllCriteria() {
            return criteria;
        }

        public List<UserExample.Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new UserExample.Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new UserExample.Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new UserExample.Criterion(condition, value1, value2));
        }

        public UserExample.Criteria andUserIdEqualTo(String value) {
            addCriterion("UserId =", value, "userId");
            return (UserExample.Criteria) this;
        }

        public UserExample.Criteria andUserIdIn(List<String> values) {
            addCriterion("UserId in", values, "userId");
            return (UserExample.Criteria) this;
        }

        public UserExample.Criteria andUserIdNotIn(List<String> values) {
            addCriterion("UserId not in", values, "userId");
            return (UserExample.Criteria) this;
        }

        public UserExample.Criteria andLoginAccountEqualTo(String value) {
            addCriterion("LoginAccount =", value, "loginAccount");
            return (UserExample.Criteria) this;
        }

        public UserExample.Criteria andBizIdEqualTo(String value) {
            addCriterion("BizId =", value, "bizId");
            return (UserExample.Criteria) this;
        }

        public UserExample.Criteria andNameEqualTo(String value) {
            addCriterion("Name =", value, "name");
            return (UserExample.Criteria) this;
        }

        public UserExample.Criteria andNameLike(String value) {
            addCriterion("Name like", value, "name");
            return (UserExample.Criteria) this;
        }

        public UserExample.Criteria andStateEqualTo(Byte value) {
            addCriterion("State =", value, "state");
            return (UserExample.Criteria) this;
        }


        public UserExample.Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("CreateTime >", value, "createTime");
            return (UserExample.Criteria) this;
        }

        public UserExample.Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("CreateTime >=", value, "createTime");
            return (UserExample.Criteria) this;
        }

        public UserExample.Criteria andCreateTimeLessThan(Date value) {
            addCriterion("CreateTime <", value, "createTime");
            return (UserExample.Criteria) this;
        }

        public UserExample.Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("CreateTime <=", value, "createTime");
            return (UserExample.Criteria) this;
        }

        public UserExample.Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("CreateTime in", values, "createTime");
            return (UserExample.Criteria) this;
        }

        public UserExample.Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("CreateTime not in", values, "createTime");
            return (UserExample.Criteria) this;
        }

        public UserExample.Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("CreateTime between", value1, value2, "createTime");
            return (UserExample.Criteria) this;
        }

        public UserExample.Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("CreateTime not between", value1, value2, "createTime");
            return (UserExample.Criteria) this;
        }

        public UserExample.Criteria andUpdateTimeIsNull() {
            addCriterion("UpdateTime is null");
            return (UserExample.Criteria) this;
        }

        public UserExample.Criteria andUpdateTimeIsNotNull() {
            addCriterion("UpdateTime is not null");
            return (UserExample.Criteria) this;
        }

        public UserExample.Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("UpdateTime =", value, "updateTime");
            return (UserExample.Criteria) this;
        }

        public UserExample.Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("UpdateTime <>", value, "updateTime");
            return (UserExample.Criteria) this;
        }

        public UserExample.Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("UpdateTime >", value, "updateTime");
            return (UserExample.Criteria) this;
        }

        public UserExample.Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("UpdateTime >=", value, "updateTime");
            return (UserExample.Criteria) this;
        }

        public UserExample.Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("UpdateTime <", value, "updateTime");
            return (UserExample.Criteria) this;
        }

        public UserExample.Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("UpdateTime <=", value, "updateTime");
            return (UserExample.Criteria) this;
        }

        public UserExample.Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("UpdateTime in", values, "updateTime");
            return (UserExample.Criteria) this;
        }

        public UserExample.Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("UpdateTime not in", values, "updateTime");
            return (UserExample.Criteria) this;
        }

        public UserExample.Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("UpdateTime between", value1, value2, "updateTime");
            return (UserExample.Criteria) this;
        }

        public UserExample.Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("UpdateTime not between", value1, value2, "updateTime");
            return (UserExample.Criteria) this;
        }
    }

    public static class Criteria extends UserExample.GeneratedCriteria implements Serializable {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion implements Serializable {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}
