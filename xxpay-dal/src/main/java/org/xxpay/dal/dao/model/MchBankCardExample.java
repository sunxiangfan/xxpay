package org.xxpay.dal.dao.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MchBankCardExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<MchBankCardExample.Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    private Integer limit;

    private Integer offset;

    public MchBankCardExample() {
        oredCriteria = new ArrayList<MchBankCardExample.Criteria>();
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

    public List<MchBankCardExample.Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(MchBankCardExample.Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public MchBankCardExample.Criteria or() {
        MchBankCardExample.Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public MchBankCardExample.Criteria createCriteria() {
        MchBankCardExample.Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected MchBankCardExample.Criteria createCriteriaInternal() {
        MchBankCardExample.Criteria criteria = new MchBankCardExample.Criteria();
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

        public MchBankCardExample.Criteria andIdEqualTo(String value) {
            addCriterion("Id =", value, "id");
            return (MchBankCardExample.Criteria) this;
        }


        public MchBankCardExample.Criteria andIdIn(List<String> values) {
            addCriterion("Id in", values, "id");
            return (MchBankCardExample.Criteria) this;
        }

        public MchBankCardExample.Criteria andMchTypeEqualTo(Byte value) {
            addCriterion("MchType =", value, "mchType");
            return (MchBankCardExample.Criteria) this;
        }

        public MchBankCardExample.Criteria andMchIdEqualTo(String value) {
            addCriterion("MchId =", value, "mchId");
            return (MchBankCardExample.Criteria) this;
        }


        public MchBankCardExample.Criteria andNumberEqualTo(String value) {
            addCriterion("Number =", value, "number");
            return (MchBankCardExample.Criteria) this;
        }

        public MchBankCardExample.Criteria andNumberLikeTo(String value) {
            addCriterion("Number like", value, "number");
            return (MchBankCardExample.Criteria) this;
        }

        public MchBankCardExample.Criteria andAccountNameLikeTo(String value) {
            addCriterion("AccountName like", value, "accountName");
            return (MchBankCardExample.Criteria) this;
        }

        public MchBankCardExample.Criteria andTypeEqualTo(Byte value) {
            addCriterion("Type =", value, "type");
            return (MchBankCardExample.Criteria) this;
        }

        public MchBankCardExample.Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("CreateTime >", value, "createTime");
            return (MchBankCardExample.Criteria) this;
        }

        public MchBankCardExample.Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("CreateTime >=", value, "createTime");
            return (MchBankCardExample.Criteria) this;
        }

        public MchBankCardExample.Criteria andCreateTimeLessThan(Date value) {
            addCriterion("CreateTime <", value, "createTime");
            return (MchBankCardExample.Criteria) this;
        }

        public MchBankCardExample.Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("CreateTime <=", value, "createTime");
            return (MchBankCardExample.Criteria) this;
        }

        public MchBankCardExample.Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("CreateTime in", values, "createTime");
            return (MchBankCardExample.Criteria) this;
        }

        public MchBankCardExample.Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("CreateTime not in", values, "createTime");
            return (MchBankCardExample.Criteria) this;
        }

        public MchBankCardExample.Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("CreateTime between", value1, value2, "createTime");
            return (MchBankCardExample.Criteria) this;
        }

        public MchBankCardExample.Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("CreateTime not between", value1, value2, "createTime");
            return (MchBankCardExample.Criteria) this;
        }

        public MchBankCardExample.Criteria andUpdateTimeIsNull() {
            addCriterion("UpdateTime is null");
            return (MchBankCardExample.Criteria) this;
        }

        public MchBankCardExample.Criteria andUpdateTimeIsNotNull() {
            addCriterion("UpdateTime is not null");
            return (MchBankCardExample.Criteria) this;
        }

        public MchBankCardExample.Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("UpdateTime =", value, "updateTime");
            return (MchBankCardExample.Criteria) this;
        }

        public MchBankCardExample.Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("UpdateTime <>", value, "updateTime");
            return (MchBankCardExample.Criteria) this;
        }

        public MchBankCardExample.Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("UpdateTime >", value, "updateTime");
            return (MchBankCardExample.Criteria) this;
        }

        public MchBankCardExample.Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("UpdateTime >=", value, "updateTime");
            return (MchBankCardExample.Criteria) this;
        }

        public MchBankCardExample.Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("UpdateTime <", value, "updateTime");
            return (MchBankCardExample.Criteria) this;
        }

        public MchBankCardExample.Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("UpdateTime <=", value, "updateTime");
            return (MchBankCardExample.Criteria) this;
        }

        public MchBankCardExample.Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("UpdateTime in", values, "updateTime");
            return (MchBankCardExample.Criteria) this;
        }

        public MchBankCardExample.Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("UpdateTime not in", values, "updateTime");
            return (MchBankCardExample.Criteria) this;
        }

        public MchBankCardExample.Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("UpdateTime between", value1, value2, "updateTime");
            return (MchBankCardExample.Criteria) this;
        }

        public MchBankCardExample.Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("UpdateTime not between", value1, value2, "updateTime");
            return (MchBankCardExample.Criteria) this;
        }
    }

    public static class Criteria extends MchBankCardExample.GeneratedCriteria implements Serializable {

        protected Criteria() {
            super();
        }
    }

}
