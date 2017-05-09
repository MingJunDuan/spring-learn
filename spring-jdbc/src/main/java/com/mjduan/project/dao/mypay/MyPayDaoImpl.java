package com.mjduan.project.dao.mypay;

import java.util.Optional;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

import com.mjduan.project.util.jdbc.customRowMapper.CustomRowMapper;
import com.mjduan.project.util.jdbc.customRowMapper.JdbcUtil;
import com.mjduan.project.util.jdbc.customRowMapper.Page;
import com.mjduan.project.util.jdbc.customRowMapper.PageUtil;
import com.mjduan.project.util.jdbc.entity.MyPay;
import com.mjduan.project.util.jdbc.entity.entityEnum.DBTables;
import com.mjduan.project.util.jdbc.entity.entityEnum.MypayEnum;

/**
 * Created by Duan on 2017/2/8.
 */
@Component("MypayDaoImpl")
public class MyPayDaoImpl {
    private static final Logger logger = LoggerFactory.getLogger(MyPayDaoImpl.class);
    @Resource(name = "JdbcUtil")
    private JdbcUtil jdbcUtil;
    @Resource(name = "PageUtil")
    private PageUtil pageUtil;

    public Optional<MyPay> queryMyPay(int myPayId) {
        StringBuilder sql = new StringBuilder("SELECT * FROM " + DBTables.mypay)
                .append(" WHERE " + MypayEnum.id + "=?");
        Object[] params = {myPayId};
        return jdbcUtil.queryEntity(sql.toString(), new CustomRowMapper<MyPay>(MyPay.class), params);
    }

    public Optional<Page<MyPay>> queryMyPays(int targetPage) {
        Page<MyPay> page = pageUtil.getPageObject(DBTables.mypay, new CustomRowMapper<MyPay>(MyPay.class), targetPage);
        return Optional.ofNullable(page);
    }

    public int addMyPay(MyPay myPay) {
        StringBuilder sql = new StringBuilder("INSERT INTO " + DBTables.mypay)
                .append("(" + MypayEnum.amountOfMoney + "," + MypayEnum.remark + "," + MypayEnum.time + "," + MypayEnum.user + ")")
                .append(" VALUES(?,?,?,?)");
        Object[] params = {myPay.getAmountOfMoney(), myPay.getRemark(), myPay.getTime(), myPay.getUser()};
        return jdbcUtil.addObject(sql.toString(), params);
    }

    public int deleteMyPayById(int myPayId) {
        StringBuilder sql = new StringBuilder("DELETE FROM " + DBTables.mypay)
                .append(" WHERE " + MypayEnum.id + "=?");
        Object[] params = {myPayId};
        return jdbcUtil.update(sql.toString(), params);
    }

}
