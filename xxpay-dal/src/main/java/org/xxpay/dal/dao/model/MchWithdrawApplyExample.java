package org.xxpay.dal.dao.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MchWithdrawApplyExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<MchWithdrawApplyExample.Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    private Integer limit;

    private Integer offset;

    public MchWithdrawApplyExample() {
        oredCriteria = new ArrayList<MchWithdrawApplyExample.Criteria>();
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

    public List<MchWithdrawApplyExample.Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(MchWithdrawApplyExample.Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public MchWithdrawApplyExample.Criteria or() {
        MchWithdrawApplyExample.Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public MchWithdrawApplyExample.Criteria createCriteria() {
        MchWithdrawApplyExample.Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected MchWithdrawApplyExample.Criteria createCriteriaInternal() {
        MchWithdrawApplyExample.Criteria criteria = new MchWithdrawApplyExample.Criteria();
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

        public MchWithdrawApplyExample.Criteria andIdEqualTo(String value) {
            addCriterion("Id =", value, "id");
            return (MchWithdrawApplyExample.Criteria) this;
        }

        public MchWithdrawApplyExample.Criteria andIdIn(List<String> values) {
            addCriterion("Id in", values, "id");
            return (MchWithdrawApplyExample.Criteria) this;
        }


        public MchWithdrawApplyExample.Criteria andMchIdEqualTo(String value) {
            addCriterion("MchId =", value, "mchId");
            return (MchWithdrawApplyExample.Criteria) this;
        }

        public MchWithdrawApplyExample.Criteria andCashChannelIdEqualTo(String value) {
            addCriterion("CashChannelId =", value, "cashChannelId");
            return (MchWithdrawApplyExample.Criteria) this;
        }

        public MchWithdrawApplyExample.Criteria andMchOrderNoEqualTo(String value) {
            addCriterion("MchOrderNo =", value, "mchOrderNo");
            return (MchWithdrawApplyExample.Criteria) this;
        }


        public MchWithdrawApplyExample.Criteria andStateEqualTo(Byte value) {
            addCriterion("State =", value, "state");
            return (MchWithdrawApplyExample.Criteria) this;
        }


        public MchWithdrawApplyExample.Criteria andCashStateEqualTo(Byte value) {
            addCriterion("CashState =", value, "cashState");
            return (MchWithdrawApplyExample.Criteria) this;
        }


        public MchWithdrawApplyExample.Criteria andStateIn(List<Byte> values) {
            addCriterion("State in", values, "state");
            return (MchWithdrawApplyExample.Criteria) this;
        }

        public MchWithdrawApplyExample.Criteria andAccountNameEqualTo(String value) {
            addCriterion("AccountName =", value, "accountName");
            return (MchWithdrawApplyExample.Criteria) this;
        }

        public MchWithdrawApplyExample.Criteria andMchTypeEqualTo(Byte value) {
            addCriterion("MchType =", value, "mchType");
            return (MchWithdrawApplyExample.Criteria) this;
        }

        public MchWithdrawApplyExample.Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("CreateTime >", value, "createTime");
            return (MchWithdrawApplyExample.Criteria) this;
        }

        public MchWithdrawApplyExample.Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("CreateTime >=", value, "createTime");
            return (MchWithdrawApplyExample.Criteria) this;
        }

        public MchWithdrawApplyExample.Criteria andCreateTimeLessThan(Date value) {
            addCriterion("CreateTime <", value, "createTime");
            return (MchWithdrawApplyExample.Criteria) this;
        }

        public MchWithdrawApplyExample.Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("CreateTime <=", value, "createTime");
            return (MchWithdrawApplyExample.Criteria) this;
        }

        public MchWithdrawApplyExample.Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("CreateTime in", values, "createTime");
            return (MchWithdrawApplyExample.Criteria) this;
        }

        public MchWithdrawApplyExample.Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("CreateTime not in", values, "createTime");
            return (MchWithdrawApplyExample.Criteria) this;
        }

        public MchWithdrawApplyExample.Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("CreateTime between", value1, value2, "createTime");
            return (MchWithdrawApplyExample.Criteria) this;
        }

        public MchWithdrawApplyExample.Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("CreateTime not between", value1, value2, "createTime");
            return (MchWithdrawApplyExample.Criteria) this;
        }

        public MchWithdrawApplyExample.Criteria andUpdateTimeIsNull() {
            addCriterion("UpdateTime is null");
            return (MchWithdrawApplyExample.Criteria) this;
        }

        public MchWithdrawApplyExample.Criteria andUpdateTimeIsNotNull() {
            addCriterion("UpdateTime is not null");
            return (MchWithdrawApplyExample.Criteria) this;
        }

        public MchWithdrawApplyExample.Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("UpdateTime =", value, "updateTime");
            return (MchWithdrawApplyExample.Criteria) this;
        }

        public MchWithdrawApplyExample.Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("UpdateTime <>", value, "updateTime");
            return (MchWithdrawApplyExample.Criteria) this;
        }

        public MchWithdrawApplyExample.Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("UpdateTime >", value, "updateTime");
            return (MchWithdrawApplyExample.Criteria) this;
        }

        public MchWithdrawApplyExample.Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("UpdateTime >=", value, "updateTime");
            return (MchWithdrawApplyExample.Criteria) this;
        }

        public MchWithdrawApplyExample.Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("UpdateTime <", value, "updateTime");
            return (MchWithdrawApplyExample.Criteria) this;
        }

        public MchWithdrawApplyExample.Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("UpdateTime <=", value, "updateTime");
            return (MchWithdrawApplyExample.Criteria) this;
        }

        public MchWithdrawApplyExample.Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("UpdateTime in", values, "updateTime");
            return (MchWithdrawApplyExample.Criteria) this;
        }

        public MchWithdrawApplyExample.Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("UpdateTime not in", values, "updateTime");
            return (MchWithdrawApplyExample.Criteria) this;
        }

        public MchWithdrawApplyExample.Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("UpdateTime between", value1, value2, "updateTime");
            return (MchWithdrawApplyExample.Criteria) this;
        }

        public MchWithdrawApplyExample.Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("UpdateTime not between", value1, value2, "updateTime");
            return (MchWithdrawApplyExample.Criteria) this;
        }
    }

    public static class Criteria extends MchWithdrawApplyExample.GeneratedCriteria implements Serializable {

        protected Criteria() {
            super();
        }
    }

}
