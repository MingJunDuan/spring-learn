package com.mjduan.project.util.jdbc.customRowMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by Duan on 2017/2/7.
 */
@Component("JdbcUtil")
public class JdbcUtil {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
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
    public <T> Optional<T> queryEntity(String sql, CustomRowMapper<T> rowMapper, Object[] params) {
        T t = null;
        try {
            t = jdbcTemplate.queryForObject(sql, rowMapper, params);
        } catch (EmptyResultDataAccessException e) {
            //查询结果为空
        } catch (IncorrectResultSizeDataAccessException e) {
            //查询结果为多个
        }
        return Optional.ofNullable(t);
    }

    /**
     * 这个慎用
     *
     * @param sql
     * @param params
     * @return
     */
    public Object queryObject(String sql, Object[] params) {
        Object result = jdbcTemplate.query(sql, preparedStatement -> {
            IntStream.rangeClosed(1, params.length).forEach(i -> {
                try {
                    preparedStatement.setObject(i, params[i - 1]);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }, resultSet -> {
            return resultSet.next() ? resultSet.getObject(1) : null;
        });
        return result;
    }

    /**
     * 对应更新操作，由于没有rowMapper限制，所以不存在"只能操作一张表的问题"
     *
     * @param sql
     * @param params
     * @return
     */
    public int update(String sql, Object[] params) {
        return jdbcTemplate.update(sql, params);
    }

    /**
     * 往数据库表中新增记录，返回的是该记录对应的主键
     *
     * @param sql
     * @param params
     * @return
     */
    public int addObject(String sql, Object[] params) {
        try (PreparedStatement preparedStatement = jdbcTemplate.getDataSource().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            IntStream.rangeClosed(1, params.length).forEach(i -> {
                try {
                    preparedStatement.setObject(i, params[i - 1]);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            generatedKeys.next();
            return generatedKeys.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("出错，sql=" + sql + ".\tparams=" + Arrays.asList(params).toString() + "\t.", e);
        }

        return -1;
    }

    /**
     * 批量更新操作
     *
     * @param sql
     * @param params
     */
    public void updateBatch(String sql, List<Object[]> params) {
        jdbcTemplate.batchUpdate(sql, params);
    }

}
