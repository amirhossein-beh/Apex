package com.pedasco.apex.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class FileStorageService {

    @Value("${apex.storage.versions-path}")
    private String versionsPath;

    public String saveVersionFile(String versionNumber, MultipartFile file) throws IOException {

        // ۱. مسیر فولدر رو میسازه: C:/apex/assets/versions/1.0.0/
        Path versionDir = Paths.get(versionsPath, versionNumber);

        // اگه فولدر نبود میسازه
        Files.createDirectories(versionDir);

        // ۲. مسیر کامل فایل رو میسازه: C:/apex/assets/versions/1.0.0/package.zip
        Path filePath = versionDir.resolve(file.getOriginalFilename());

        // ۳. فایل رو روی دیسک ذخیره میکنه
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // ۴. مسیر رو برمیگردونه
        return filePath.toString();
    }

    public String calculateChecksum(MultipartFile file) throws IOException, NoSuchAlgorithmException {

        // الگوریتم SHA-256 رو آماده میکنه
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        // محتوای فایل رو hash میکنه
        byte[] hashBytes = digest.digest(file.getBytes());

        // byte array رو به hex string تبدیل میکنه
        // مثلاً: a3f5b2...
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }

        return hexString.toString();
    }

}
