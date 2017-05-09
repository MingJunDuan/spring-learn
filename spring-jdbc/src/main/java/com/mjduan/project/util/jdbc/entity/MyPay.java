package com.mjduan.project.util.jdbc.entity;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * 支出实体
 *
 * Created by Duan on 2017/2/7.
 */
@Getter
@Setter
public class MyPay {
    private long id;
    private BigDecimal amountOfMoney;
    private String	remark;
    private String	time;
    private long user;

    public MyPay() {
    }

    public MyPay(BigDecimal amountOfMoney, String remark, long user) {
        this.amountOfMoney = amountOfMoney;
        this.remark = remark;
        this.user = user;
    }

    @Override
    public String toString() {
        return "MyPay{" +
                "id=" + id +
                ", amountOfMoney=" + amountOfMoney +
                ", remark='" + remark + '\'' +
                ", time='" + time + '\'' +
                ", user=" + user +
                '}';
    }
}
