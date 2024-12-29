# RHEL 8.5
FROM openjdk:17

LABEL version="1.0"
LABEL description="OCR demo"
LABEL last_updated="2024-00-00"

# Non-Root User
RUN useradd -ms /bin/bash appuser

# Application Setting
COPY api-1.0.jar /apps/ocr/api-1.0.jar
COPY lib /apps/ocr/lib

WORKDIR /apps/ocr

# non-root user 'appuser' for running the Application
RUN chown -R appuser:appuser /apps/ocr
USER appuser

CMD ["java", "-jar", "api-1.0.jar"]