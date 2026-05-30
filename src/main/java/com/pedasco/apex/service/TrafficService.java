package com.pedasco.apex.service;


import com.pedasco.apex.domain.entity.Client;
import com.pedasco.apex.domain.entity.TrafficLog;
import com.pedasco.apex.dto.response.TrafficLogResponse;
import com.pedasco.apex.repository.ClientRepository;
import com.pedasco.apex.repository.TrafficLogRepository;
import com.pedasco.apex.websocket.dto.TrafficLogRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrafficService {

    private final TrafficLogRepository trafficLogRepository;
    private final ClientRepository clientRepository;
    private final FileStorageService fileStorageService;


    @Value("${apex.storage.images-path}")
    private String imagesPath;

    String plateImagePath = null;
    String carImagePath = null;

    public TrafficLog saveTrafficLog(UUID clientId , TrafficLogRequest request) throws IOException {


        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found "));


        if (request.getPlateImageBase64() != null){

            plateImagePath = saveImage(clientId, "plate", request.getPlateImageBase64());
        }

        if (request.getCarImageBase64() != null) {
            carImagePath = saveImage(clientId, "car", request.getCarImageBase64());
        }


        TrafficLog trafficLog = new TrafficLog();
        trafficLog.setClient(client);
        trafficLog.setPlateText(request.getPlateText());
        trafficLog.setConfidence(request.getConfidence());
        trafficLog.setCountry(request.getCountry());
        trafficLog.setDirection(request.getDirection().toUpperCase());
        trafficLog.setStreamId(request.getStreamId());
        trafficLog.setPlateImagePath(plateImagePath);
        trafficLog.setCarImagePath(carImagePath);

        if (request.getLogDate() != null && request.getLogTime() != null) {
            trafficLog.setLogDate(request.getLogDate());
            trafficLog.setLogTime(request.getLogTime());
        } else {
            LocalDateTime now = LocalDateTime.now();
            trafficLog.setLogDate(now.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
            trafficLog.setLogTime(now.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        }


        return trafficLogRepository.save(trafficLog);
    }


    private String saveImage(UUID clientId, String type, String base64) throws IOException {
        byte[] imageBytes = Base64.getDecoder().decode(base64);

        // مسیر: images/2025/01/clientId/plate_uuid.jpg
        String datePath = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyy/MM"));
        Path dir = Paths.get(imagesPath, datePath, clientId.toString());
        Files.createDirectories(dir);

        String filename = type + "_" + UUID.randomUUID() + ".jpg";
        Path filePath = dir.resolve(filename);
        Files.write(filePath, imageBytes);

        return filePath.toString();
    }
    public TrafficLogResponse toResponse(TrafficLog log) {
        return new TrafficLogResponse(
                log.getId(),
                log.getClient().getId(),
                log.getPlateText(),
                log.getConfidence(),
                log.getCountry(),
                log.getDirection(),
                log.getStreamId(),
                log.getPlateImagePath(),
                log.getCarImagePath(),
                log.getLogDate(),
                log.getLogTime(),
                log.getReceivedAt(),
                log.getRefId()
        );
    }

    public List<TrafficLogResponse> getClientTrafficLogs(UUID clientId) {
        return trafficLogRepository.findByClientIdOrderByReceivedAtDesc(clientId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<TrafficLog> getUnforwardedLogs() {
        return trafficLogRepository.findByRefIdIsNull();
    }
}
