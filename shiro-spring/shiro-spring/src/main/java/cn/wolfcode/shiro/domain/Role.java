package cn.wolfcode.shiro.domain;

import lombok.Getter;
import lombok.Setter;


public class Role {
    private Long id;
    private String name;  //角色名称
    private String sn;  //角色表达式： empMgr

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

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }
}
