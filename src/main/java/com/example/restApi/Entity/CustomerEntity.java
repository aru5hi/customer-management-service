package com.example.restApi.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.time.Instant;

@Entity
@Table(name="customer-data")
@Getter @Setter
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 150)
    private  String email;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    // Getters and setters
//
//    public Long getId() { return id; }
//    public void setId(Long id) { this.id = id; }
//
//    public String getName() { return name; }
//    public void setName (String name) {this.name = name; }
//
//    public String getEmail() { return email; }
//    public void setEmail(String email) { this.email = email; }
//
//    public Instant getCreatedAt() {return createdAt;}
//    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

}
