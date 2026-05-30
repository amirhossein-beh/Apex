package com.pedasco.apex.api;


import com.pedasco.apex.domain.entity.Version;
import com.pedasco.apex.service.VersionService;
import org.springframework.core.io.Resource;
import lombok.RequiredArgsConstructor;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/versions")
@RequiredArgsConstructor

public class VersionController {

    private final VersionService versionService;


    @PostMapping("/upload")
    public ResponseEntity<Version> upload(
            @RequestParam String versionNumber,
            @RequestParam boolean mandatory,
            @RequestParam(required = false) String minVersion,
            @RequestParam(required = false) String releaseNotes,
            @RequestParam MultipartFile file,
            Authentication authentication) throws Exception {

        // نام یوزر لاگین شده رو از authentication میگیره
        String createdBy = authentication.getName();

        Version version = versionService.uploadVersion(
                versionNumber, mandatory, minVersion, releaseNotes, file, createdBy);

        return ResponseEntity.ok(version);
    }



    @GetMapping
    public ResponseEntity <List<Version>> getAll(){
        return ResponseEntity.ok(versionService.getAllVersions());
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<String> activate(@PathVariable Long id) {
        versionService.activateVersion(id);
        return ResponseEntity.ok("");
    }

    // نسخه فعال - برای کلاینت C# (بدون JWT)
    @GetMapping("/current")
    public ResponseEntity<?> getCurrent() {
        return versionService.getActiveVersion()
                .map(version -> {
                    // فقط اطلاعات لازم رو برگردون
                    Map<String, Object> response = new HashMap<>();
                    response.put("versionNumber", version.getVersionNumber());
                    response.put("checksum", version.getChecksum());
                    response.put("mandatory", version.isMandatory());
                    response.put("minVersion", version.getMinVersion());
                    response.put("downloadUrl", "/api/versions/download/" + version.getId());
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // دانلود فایل - برای کلاینت C# (بدون JWT)
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable Long id) throws IOException {
        Version version = versionService.getAllVersions().stream()
                .filter(v -> v.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Version not found"));

        Path filePath = Paths.get(version.getFilePath());
        Resource resource = new FileSystemResource(filePath);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + filePath.getFileName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
