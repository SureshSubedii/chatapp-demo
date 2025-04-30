package com.example.chatapp.firebase;

import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(@RequestBody SendNoticeDto sendNoticeDto) {
        try {
            String response = notificationService.sendMessageToToken(sendNoticeDto.token, sendNoticeDto.title, sendNoticeDto.body);
            return ResponseEntity.ok("Sent message ID: " + response);
        } catch (FirebaseMessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send: " + e.getMessage());
        }
    }
}


