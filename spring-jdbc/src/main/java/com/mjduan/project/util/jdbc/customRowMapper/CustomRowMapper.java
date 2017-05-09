package com.mjduan.project.util.jdbc.customRowMapper;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.jdbc.core.RowMapper;

/**
 * 主要用于查询时减少rowMapper的大量代码，这个自定义的rowMapper还是不能减少SQL的量(表字段很多时，SQL语句很长的问题还是不能解决)
 * 在代码里的目的是：将数据库表字段对应到实体类属性上
 * Created by Duan on 2017/1/20.
 */
public class CustomRowMapper<T> implements RowMapper<T> {
    private static final Logger logger = LoggerFactory.getLogger(CustomRowMapper.class);

    private Map<String, String> fields = new HashMap<>();
    private Class<T> clazz;
    private boolean flag;

    public CustomRowMapper(Class<T> clazz) {
        this.clazz = clazz;
    }

    private void doMapper() {
        Field[] declaredFields = this.clazz.getDeclaredFields();
        Stream.of(declaredFields)
                .forEach(field -> {
                    Table annotation = field.getAnnotation(Table.class);
                    String sqlColumn;
                    if (annotation == null) {
                        sqlColumn = field.getName();
                    } else {
                        sqlColumn = annotation.column();
                    }
                    String fieldName = field.getName();
                    fields.put(fieldName, sqlColumn);
                });
    }

    /**
     * 仅仅包含这些字段，要返回的字段较少时用这个。这个效率更高
     *
     * @param columns
     */
    public CustomRowMapper inclusive(String... columns) {
        //clear这个操作应该特别快的，不用异步操作
        //fields.clear();
        flag = true;
        Stream.of(columns)
                .forEach(column -> {
                    String sqlColumn = null;
                    try {
                        Field declaredField = this.clazz.getDeclaredField(column);
                        Table annotation = declaredField.getAnnotation(Table.class);
                        if (annotation == null) {
                            sqlColumn = declaredField.getName();
                        } else {
                            sqlColumn = annotation.column();
                        }
                    } catch (NoSuchFieldException e) {
                        logger.error("出错", e);
                    }
                    fields.put(column, sqlColumn);
                });
        return this;
    }

    public void doMap() {
        doMapper();
    }

    /**
     * 排除这些字段,要返回的字段较多时用这个
     *
     * @param columns
     */
    public void exclusive(String... columns) {
        doMapper();
        Stream.of(columns)
                .forEach(column -> {
                    fields.remove(column);
                });
        flag = true;
    }

    private void doPrint() {
        fields.entrySet().stream()
                .forEach(stringStringEntry -> System.out.println(stringStringEntry.getKey() + "\t" + stringStringEntry.getValue()));
    }

    @Override
    public T mapRow(ResultSet resultSet, int i) throws SQLException {
        if (!flag) {
            doMapper();
        }

        try {
            T instance = this.clazz.newInstance();
            fields.entrySet().stream()
                    .forEach(entry -> {
                        try {
                            Field field = this.clazz.getDeclaredField(entry.getKey());
                            field.setAccessible(true);
                            field.set(instance, resultSet.getObject(entry.getValue()));
                        } catch (NoSuchFieldException e) {
                            e.printStackTrace();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    });
            return instance;
        } catch (InstantiationException e) {
            logger.error("出错", e);
        } catch (IllegalAccessException e) {
            logger.error("出错", e);
        }

        return null;
    }
}
