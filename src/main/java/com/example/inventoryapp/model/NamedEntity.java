package com.example.inventoryapp.model;

import java.io.Serializable;
import java.util.Objects;

public abstract class NamedEntity implements Serializable {

    private String name;
    private long id;


    public NamedEntity(String name, long id) {
        this.name = name;
        this.id = id;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NamedEntity that = (NamedEntity) o;
        return Objects.equals(name, that.name);
    }
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public NamedEntity(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "NamedEntity{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
