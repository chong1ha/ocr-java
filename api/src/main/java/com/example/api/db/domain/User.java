package com.example.api.db.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * @author gunha
 * @version 1.0
 * @since 2024. 12. 29.
 */
@Entity
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username", nullable = false, unique = true, length = 255)
    private String username;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Embedded
    private CreatePeriod createPeriod;

    /**
     * 생성 시,
     */
    @PrePersist
    public void onCreate() {

        if (this.createPeriod == null) {
            this.createPeriod = new CreatePeriod();
        }
        this.createPeriod.onCreate();
    }

    /**
     * 생성 시,
     */
    @PreUpdate
    public void onUpdate() {

        if (this.createPeriod == null) {
            this.createPeriod = new CreatePeriod();
        }
        this.createPeriod.onUpdate();
    }
}
