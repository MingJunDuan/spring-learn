package com.mjduan.project.util.jdbc.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 访问记录实体
 *
 * Created by Duan on 2017/2/7.
 */
@Getter
@Setter
public class Record {
    private long id;
    private long user;
    private String	time;
    private String	ipv4;
    private String	ipv6;

    @Override
    public String toString() {
        return "Record{" +
                "id=" + id +
                ", user=" + user +
                ", time='" + time + '\'' +
                ", ipv4='" + ipv4 + '\'' +
                ", ipv6='" + ipv6 + '\'' +
                '}';
    }

}
