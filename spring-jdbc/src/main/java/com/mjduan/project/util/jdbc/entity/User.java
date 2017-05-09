package com.mjduan.project.util.jdbc.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户实体
 *
 * Created by Duan on 2017/2/7.
 */
@Setter
@Getter
public class User {
    private long id;
    private String	name;
    private String	time;
    private String	email;
    private int status;
    private String	account;//账号
    private String	password;//密码

    public User() {
    }

    public User(String account, String password) {
        this.account = account;
        this.password = password;
    }

    public User(String name, String email, String account, String password) {
        this.name = name;
        this.email = email;
        this.account = account;
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", time='" + time + '\'' +
                ", email='" + email + '\'' +
                ", status=" + status +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
