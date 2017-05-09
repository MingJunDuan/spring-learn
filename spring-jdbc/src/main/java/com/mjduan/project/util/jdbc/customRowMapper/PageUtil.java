package com.mjduan.project.util.jdbc.customRowMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

/**
 * 这个设计的并不通用
 * <p>
 * Created by Duan on 2017/2/8.
 */
@Component("PageUtil")
public class PageUtil {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    /**
     * @param tableName  查询哪张表
     * @param whereSql   where查询语句约束
     * @param params     where查询语句中的参数
     * @param rowMapper
     * @param targetPage 查询第几页的数据
     * @param <T>
     * @return
     */
    public <T> Page<T> getPageObject(String tableName, String whereSql, Object[] params, CustomRowMapper<T> rowMapper, int targetPage) {
        Page<T> page = new Page<T>();
        page.setCurrentPage(targetPage);

        String sqlGetTotalRecords = new StringBuilder("SELECT COUNT(*) as totalRecords FROM ")
                .append(tableName).append(" " + whereSql).toString();
        //异步执行
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            Integer totalRecords = jdbcTemplate.query(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement(sqlGetTotalRecords);
                for (int i = 0; i < params.length; i++) {
                    preparedStatement.setObject(i + 1, params[i]);
                }
                return preparedStatement;
            }, resultSet -> {
                //一定要调用resultSet.next()，不然会报错
                return resultSet.next() ? resultSet.getInt("totalRecords") : 0;
            });
            page.setRecordTotal(totalRecords);
        });

        String sqlGetRecords = new StringBuilder("SELECT * FROM " + tableName)
                .append(" " + whereSql + " ")
                .append(" LIMIT " + ((page.getCurrentPage() - 1) * page.getPageSize()) + "," + page.getPageSize()).toString();
        List<T> result = jdbcTemplate.query(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlGetRecords);
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            return preparedStatement;
        }, rowMapper);

        page.setContent(result);
        future.join();
        return page;
    }

    /**
     * @param tableName
     * @param rowMapper
     * @param targetPage
     * @param <T>
     * @return
     */
    public <T> Page<T> getPageObject(String tableName, CustomRowMapper<T> rowMapper, int targetPage) {
        Page<T> page = new Page<T>();
        page.setCurrentPage(targetPage);

        String sqlGetTotalRecords = "SELECT COUNT(*) as totalRecords FROM " + tableName;

        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            Integer totalRecords = jdbcTemplate.query(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement preparedStatement = connection.prepareStatement(sqlGetTotalRecords);
                    return preparedStatement;
                }
            }, new ResultSetExtractor<Integer>() {
                @Override
                public Integer extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                    //一定要调用resultSet.next()，不然会报错
                    return resultSet.next() ? resultSet.getInt("totalRecords") : 0;
                }
            });
            page.setRecordTotal(totalRecords);
        });

        String sqlGetRecords = "SELECT * FROM " + tableName + " LIMIT " + ((page.getCurrentPage() - 1) * page.getPageSize()) + "," + page.getPageSize();
        List<T> result = jdbcTemplate.query(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement preparedStatement = connection.prepareStatement(sqlGetRecords);
                return preparedStatement;
            }
        }, rowMapper);

        page.setContent(result);
        future.join();
        return page;
    }

}
