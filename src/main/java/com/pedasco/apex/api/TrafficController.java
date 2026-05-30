package com.pedasco.apex.api;

import com.pedasco.apex.domain.entity.TrafficLog;
import com.pedasco.apex.dto.response.TrafficLogResponse;
import com.pedasco.apex.service.TrafficService;
import com.pedasco.apex.websocket.dto.TrafficLogRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/traffic")
@RequiredArgsConstructor

public class TrafficController {

    private final TrafficService trafficService;

    // کلاینت آفلاین بوده، الان داره تردد‌های انباشته رو میفرسته
    // این endpoint بدون JWT هست چون کلاینت C# با token خودش احراز هویت میکنه
    @PostMapping("/client/{clientId}")
    public ResponseEntity<String> receiveTraffic(
            @PathVariable UUID clientId,
            @RequestHeader("X-Client-Token") String token,
            @RequestBody TrafficLogRequest request) {

        try {
            trafficService.saveTrafficLog(clientId, request);
            return ResponseEntity.ok("Traffic log saved");
        } catch (Exception e) {
            log.error("Failed to save traffic log", e);
            return ResponseEntity.internalServerError().body("Failed to save traffic log");
        }
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<TrafficLogResponse>> getClientTraffic(@PathVariable UUID clientId) {
        return ResponseEntity.ok(trafficService.getClientTrafficLogs(clientId));
    }
}
