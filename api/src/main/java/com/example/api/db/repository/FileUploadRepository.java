package com.example.api.db.repository;

import com.example.api.db.domain.FileUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author gunha
 * @version 1.0
 * @since 2024. 12. 29.
 */
@Repository
public interface FileUploadRepository extends JpaRepository<FileUpload, Long> {
}
