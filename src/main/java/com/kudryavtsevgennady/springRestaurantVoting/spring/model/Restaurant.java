package com.kudryavtsevgennady.springRestaurantVoting.spring.model;

import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.List;

@Component
@Table(name = "restaurants")
public class Rsstaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address")
    private String address;

    private List<Dish> dishes;

    public Rsstaurant() {
    }

    public Rsstaurant(int id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
