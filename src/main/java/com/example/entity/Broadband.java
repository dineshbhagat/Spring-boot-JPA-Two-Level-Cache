package com.example.entity;

import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name = "broadband")
@Data
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Broadband {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "this is incremental id of table", nullable = false, unique = true)
    private int id;

    private String plan;
    @OneToOne
    private Customer customer;

    @Column(name = "is_active")
    private boolean isActive;
}
