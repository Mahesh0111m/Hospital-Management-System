package com.mahesh.HMS.model;

import com.mahesh.HMS.cryptography.StringCryptoConverter;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Convert(converter = StringCryptoConverter.class)
    private String name;
    @Convert(converter = StringCryptoConverter.class)
    private String gender;

    private int age;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }


    @OneToMany(mappedBy = "patient" , cascade = CascadeType.ALL, orphanRemoval = true , fetch = FetchType.LAZY)
    private List<Appointment> appointments;


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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


}
