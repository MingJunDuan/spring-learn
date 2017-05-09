package com.mjduan.project.util;

import java.util.logging.Logger;

import javax.annotation.Resource;

import org.junit.Test;

import com.mjduan.project.LearnMysqlApplicationTests;
import com.mjduan.project.util.jdbc.customRowMapper.JdbcUtil;

/**
 * Created by duan on 2017/5/9.
 */
public class JdbcUtilTest extends LearnMysqlApplicationTests {
    private static final Logger LOG = Logger.getLogger(JdbcUtil.class.getName());
    @Resource(name = "JdbcUtil")
    private JdbcUtil jdbcUtil;

    @Test
    public void test(){

    }

}
