package com.pedasco.apex.api;


import com.pedasco.apex.domain.enums.CommandAction;
import com.pedasco.apex.dto.request.CommandRequest;
import com.pedasco.apex.service.CommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/commands")
@RequiredArgsConstructor
public class CommandController {

    private final CommandService commandService;

    @PostMapping("/{clientId}")
    public ResponseEntity<String> sendCommand(@PathVariable UUID clientId , @RequestBody CommandRequest request){

        try{
            CommandAction action = CommandAction.valueOf(request.getAction());
            commandService.sendCommand(clientId, action, request.getPayload());
            return ResponseEntity.ok("Command sent successfully");
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body("Invalid action: "+ request.getAction());
        }catch (RuntimeException e) {
            // اگه کلاینت آفلاین بود یا پیدا نشد
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to send command");
        }
    }
}
