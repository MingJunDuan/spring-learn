package com.mjduan.project.util.jdbc.customRowMapper;

import javax.annotation.Resource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by Duan on 2017/2/7.
 */
@Component("JdbcOnOneEntity")
public class JdbcOnOneEntity {
    @Resource(name = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    /**
     * 只能在一张表上查询，不能多张表联合查询，这是由于rowMapper的限制
     *
     * @param sql
     * @param rowMapper
     * @param params
     * @param <T>
     * @return
     */
    @Deprecated
    public <T> T queryEntity(String sql, CustomRowMapper<T> rowMapper, Object[] params) {
        T t = null;
        try {
            t = jdbcTemplate.queryForObject(sql, rowMapper, params);
        } catch (EmptyResultDataAccessException e) {
            //查询结果为空
        } catch (IncorrectResultSizeDataAccessException e) {
            //查询结果为多个
        }
        return t;
    }


}
