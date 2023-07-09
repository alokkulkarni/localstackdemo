package com.alok.localstackdemo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "model")
@Entity
public class Model {

    @Id
    private Integer id;

    @Column(name = "name")
    private String name;

    //ommited getters and setters from here


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
