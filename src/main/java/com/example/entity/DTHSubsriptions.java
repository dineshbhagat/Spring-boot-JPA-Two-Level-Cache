package com.example.entity;

import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name = "dth_subs")
@Data
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DTHSubsriptions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "this is incremental id of table", nullable = false, unique = true)
    private int id;
    @ManyToOne
    private Customer customer;
    private String plan;

    @Column(name = "is_active")
    private boolean isActive;
}
