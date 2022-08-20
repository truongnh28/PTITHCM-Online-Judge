package com.onlinejudge.springthymeleaf.services;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    String storeFile(MultipartFile file);
}
