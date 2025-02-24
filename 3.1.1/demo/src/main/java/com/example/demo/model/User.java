package com.example.demo.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long  id;

    @NotBlank(message = "Null!")
    @Column(name = "name")
    private String name;

    @NotBlank(message = "Null!")
    @Column(name = "country")
    private String country;

    @NotBlank(message = "Null!")
    @Column(name = "car")
    private String car;

    public User(String country, String name, long id, String car) {
        this.country = country;
        this.name = name;
        this.id = id;
        this.car = car;
    }

    public User() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", Car='" + car + '\'' +
                '}';
    }
}

