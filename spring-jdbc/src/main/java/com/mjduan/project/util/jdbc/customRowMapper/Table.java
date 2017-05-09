package com.mjduan.project.util.jdbc.customRowMapper;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在实体类属性上进行注解
 * <p>
 * Created by Duan on 2017/1/20.
 */
@Documented
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
    //对应的列名
    String column() default "";
}
