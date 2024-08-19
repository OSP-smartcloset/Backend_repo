package com.example.smartcloset.chat.controller;

import com.example.smartcloset.chat.dto.NotificationRequest;
import com.example.smartcloset.chat.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/notification")
    public ResponseEntity pushMessage(@RequestBody NotificationRequest notificationRequest) throws IOException {
        System.out.println(notificationRequest.getTargetToken() + " "
                + notificationRequest.getTitle() + " " + notificationRequest.getBody());

        notificationService.sendMessageTo(
                notificationRequest.getTargetToken(),
                notificationRequest.getTitle(),
                notificationRequest.getBody());
        return ResponseEntity.ok().build();
    }
}