package com.acme.biz.api.entity;

/**
 * @author qi.li
 * @email liq@hzgjgc.com
 * @since 2022/10/12
 */
public class User {
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}