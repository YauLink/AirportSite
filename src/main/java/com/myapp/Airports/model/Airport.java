package com.myapp.Airports.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;


import java.io.Serializable;

@Entity(name = "Airport")
@Table(name = "airports", schema = "bookings")
public class Airport implements Serializable {

    @Id
    @Column(name = "airport_code")
    private String code; //Код аэропорта


    @Column(name = "airport_name")
    private String name; //Название аэропорта


    @Column(name = "city")
    private String city;        // Город


    @Column(name = "timezone")
    private String timezone;    //Часовой пояс аэропорта

    public Airport() {
    }

    public Airport(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    @Override
    public String toString() {
        return "Airport{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", timezone='" + timezone + '\'' +
//                ", coordinates=" + coordinates +
                '}';
    }

}
