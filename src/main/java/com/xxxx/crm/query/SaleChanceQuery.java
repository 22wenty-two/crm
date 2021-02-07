package com.xxxx.crm.query;

import com.xxxx.base.BaseQuery;

/**
 * 营销模块查询模板
 * @author arthur
 * @date 2021/1/25 17:59
 */
public class SaleChanceQuery extends BaseQuery {
    /**
     * customerName：客户名
     * createMan：创建人
     * state：分配状态
     * assignMan: 分配人
     * devResult: 开发状态
     */
    private String customerName;
    private String createMan;
    private String state;
    private Integer assignMan;
    private Integer devResult;

    public Integer getAssignMan() {
        return assignMan;
    }

    public void setAssignMan(Integer assignMan) {
        this.assignMan = assignMan;
    }

    public Integer getDevResult() {
        return devResult;
    }

    public void setDevResult(Integer devResult) {
        this.devResult = devResult;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCreateMan() {
        return createMan;
    }

    public void setCreateMan(String createMan) {
        this.createMan = createMan;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
