
-- 사용자 정보
--
CREATE TABLE ocr.users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 파일 업로드 정보
--
CREATE TABLE ocr.file_uploads (
    file_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    s3_path VARCHAR(1024) NOT NULL,
    file_size BIGINT,
    file_type VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES ocr.users(user_id) ON DELETE CASCADE
);

-- 파일 메타데이터 정보
--
CREATE TABLE ocr.file_metadata (
    metadata_id SERIAL PRIMARY KEY,
    file_id INT NOT NULL,
    file_format VARCHAR(50),
    schema_info TEXT,
    file_date DATE,
    processed BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (file_id) REFERENCES ocr.file_uploads(file_id) ON DELETE CASCADE
);
