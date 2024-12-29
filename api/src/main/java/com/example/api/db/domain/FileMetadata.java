package com.example.api.db.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * @author gunha
 * @version 1.0
 * @since 2024. 12. 29.
 */
@Entity
@Getter
@Setter
@Table(name = "file_metadata")
public class FileMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "metadata_id")
    private Long metadataId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id")
    private FileUpload fileUpload;

    @Column(name = "file_format", length = 50)
    private String fileFormat;

    @Column(name = "schema_info", columnDefinition = "TEXT")
    private String schemaInfo;

    @Column(name = "file_date")
    private LocalDate fileDate;

    @Column(name = "processed", nullable = false)
    private Boolean processed = false;
}
