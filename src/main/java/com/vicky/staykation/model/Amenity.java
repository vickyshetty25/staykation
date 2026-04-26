package com.vicky.staykation.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "amenities")
@Data
public class Amenity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @Column(nullable = false)
    private String name;
}