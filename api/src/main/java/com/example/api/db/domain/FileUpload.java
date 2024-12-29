package com.example.api.db.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author gunha
 * @version 1.0
 * @since 2024. 12. 29.
 */
@Entity
@Getter
@Setter
@Table(name = "file_uploads")
@ToString
public class FileUpload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long fileId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;

    @Column(name = "s3_path", nullable = false, length = 1024)
    private String s3Path;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "file_type", length = 50)
    private String fileType;

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
