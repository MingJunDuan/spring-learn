package com.mjduan.project.dao.user;

import java.util.Optional;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

import com.mjduan.project.util.jdbc.customRowMapper.CustomRowMapper;
import com.mjduan.project.util.jdbc.customRowMapper.JdbcUtil;
import com.mjduan.project.util.jdbc.customRowMapper.Page;
import com.mjduan.project.util.jdbc.customRowMapper.PageUtil;
import com.mjduan.project.util.jdbc.entity.User;
import com.mjduan.project.util.jdbc.entity.entityEnum.DBTables;
import com.mjduan.project.util.jdbc.entity.entityEnum.UserEnum;

/**
 * Created by Duan on 2017/2/8.
 */
@Component("UserDaoImpl")
public class UserDaoImpl {
    private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    @Resource(name = "JdbcUtil")
    private JdbcUtil jdbcUtil;
    @Resource(name = "PageUtil")
    private PageUtil pageUtil;

    public Optional<User> queryUser(int userId) {
        StringBuilder sql = new StringBuilder("SELECT * FROM " + DBTables.user)
                .append(" WHERE " + UserEnum.id + "=?");
        Object[] params = {userId};
        Optional<User> user = jdbcUtil.queryEntity(sql.toString(), new CustomRowMapper<User>(User.class), params);
        return user;
    }

    public Optional<User> queryUser(String account, String password) {
        StringBuilder sql = new StringBuilder("SELECT * FROM " + DBTables.user)
                .append(" WHERE " + UserEnum.account + "=? AND " + UserEnum.password + "=?");
        Object[] params = {account, password};
        Optional<User> user = jdbcUtil.queryEntity(sql.toString(), new CustomRowMapper<>(User.class), params);
        if (!user.isPresent()) {
            logger.warn("没有查询到相应的用户,查询参数是account=\"" + account + "\"\tpassword=\"" + password + "\"");
        }
        return user;
    }

    public Optional<Page<User>> queryUsers(int targetPage) {
        Page<User> page = pageUtil.getPageObject(DBTables.user, new CustomRowMapper<User>(User.class), targetPage);
        return Optional.ofNullable(page);
    }

    public long queryUserCountByName(String name) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM " + DBTables.user)
                .append(" WHERE " + UserEnum.name + "=?");
        Object[] params = {name};
        Object result = jdbcUtil.queryObject(sql.toString(), params);
        return (Long) result;
    }

    public long queryUserCountByEmail(String email) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM " + DBTables.user)
                .append(" WHERE " + UserEnum.email + "=?");
        Object[] params = {email};
        Object result = jdbcUtil.queryObject(sql.toString(), params);
        return (Long) result;
    }

    public long queryUserCountByAccount(String account) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM " + DBTables.user)
                .append(" WHERE " + UserEnum.account + "=?");
        Object[] params = {account};
        Object result = jdbcUtil.queryObject(sql.toString(), params);
        return (Long) result;
    }

    public int addUser(User user) {
        StringBuilder sql = new StringBuilder("INSERT INTO " + DBTables.user)
                .append("(" + UserEnum.name + "," + UserEnum.email + "," + UserEnum.time + "," + UserEnum.account + "," + UserEnum.password + ")")
                .append(" VALUES(?,?,?,?,?)");
        Object[] params = {user.getName(), user.getEmail(), user.getTime(), user.getAccount(), user.getPassword()};
        int userId = jdbcUtil.addObject(sql.toString(), params);
        return userId;
    }

    public int deleteUserById(int userId) {
        StringBuilder sql = new StringBuilder("DELETE FROM " + DBTables.user)
                .append(" WHERE " + UserEnum.id + "=?");
        Object[] params = {userId};
        return jdbcUtil.update(sql.toString(), params);
    }

}
