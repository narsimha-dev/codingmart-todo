package com.user.role.exception;

import lombok.Data;

@Data
public class FileStorageException extends RuntimeException {
        public FileStorageException(String message) {
            super(message);
        }
        public FileStorageException(String message, Throwable cause) {
            super(message, cause);
        }
    }
