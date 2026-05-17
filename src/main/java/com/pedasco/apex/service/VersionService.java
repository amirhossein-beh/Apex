package com.pedasco.apex.service;

import com.pedasco.apex.domain.entity.Version;
import com.pedasco.apex.repository.VersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VersionService {

    private final VersionRepository versionRepository;
    private final FileStorageService fileStorageService;

    public Version uploadVersion(String versionNumber,
                                 boolean mandatory,
                                 String minVersion,
                                 String releaseNotes,
                                 MultipartFile file,
                                 String createdBy) throws Exception {

        // ۱. فایل رو روی دیسک ذخیره میکنه
        String filePath = fileStorageService.saveVersionFile(versionNumber, file);

        // ۲. checksum فایل رو حساب میکنه
        String checksum = fileStorageService.calculateChecksum(file);

        // ۳. entity میسازه و ذخیره میکنه
        Version version = new Version();
        version.setVersionNumber(versionNumber);
        version.setFilePath(filePath);
        version.setChecksum(checksum);
        version.setMandatory(mandatory);
        version.setMinVersion(minVersion);
        version.setReleaseNotes(releaseNotes);
        version.setCreatedBy(createdBy);
        version.setActive(false); // پیش‌فرض غیرفعاله، باید دستی activate بشه

        return versionRepository.save(version);
    }

    public void activateVersion(Long versionId) {

        // نسخه‌ای که میخوایم فعال کنیم پیدا کن
        Version version = versionRepository.findById(versionId)
                .orElseThrow(() -> new RuntimeException("Version not found"));

        // همه نسخه‌های فعال رو غیرفعال کن
        versionRepository.findByActiveTrue().ifPresent(active -> {
            active.setActive(false);
            versionRepository.save(active);
        });

        // این نسخه رو فعال کن
        version.setActive(true);
        versionRepository.save(version);
    }

    public Optional<Version> getActiveVersion() {
        return versionRepository.findByActiveTrue();
    }

    public List<Version> getAllVersions() {
        return versionRepository.findAll();
    }
}