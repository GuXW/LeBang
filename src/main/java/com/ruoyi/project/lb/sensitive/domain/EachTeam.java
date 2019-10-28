package com.ruoyi.project.lb.sensitive.domain;

/**
 * 每层团队信息
 */
public class EachTeam {

    private Integer level;//层级

    private Integer number=0;//人数

    private Integer receivedOrderNum=0;//收到订单数

    private Integer leakOrderNum=0;//漏单数

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getReceivedOrderNum() {
        return receivedOrderNum;
    }

    public void setReceivedOrderNum(Integer receivedOrderNum) {
        this.receivedOrderNum = receivedOrderNum;
    }

    public Integer getLeakOrderNum() {
        return leakOrderNum;
    }

    public void setLeakOrderNum(Integer leakOrderNum) {
        this.leakOrderNum = leakOrderNum;
    }
}
