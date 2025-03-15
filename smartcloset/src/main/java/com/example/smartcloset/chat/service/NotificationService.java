package com.example.smartcloset.chat.service;

import com.example.smartcloset.chat.dto.NotificationMessage;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.example.smartcloset.global.common.service.RedisService;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationService {

    private final String API_URL = "https://fcm.googleapis.com/v1/projects/smartcloset-81c5d/messages:send";
    private final ObjectMapper objectMapper;

    private final RedisService redisService;

    public void sendMessageTo(String targetToken, String title, String body) throws IOException {
        // 캐시 키: 동일 대상, 제목의 알림이 최근 전송되었는지 체크 (예: 5분 간 중복 전송 방지)
        String cacheKey = "fcm:notification:" + targetToken + ":" + title.hashCode();
        if (redisService.exists(cacheKey)) {
            // 이미 전송된 알림이 있으면 전송하지 않음
            return;
        }

        String message = makeMessage(targetToken, title, body);

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message,
                MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        Response response = client.newCall(request).execute();

        System.out.println(response.body().string());

        // 알림 전송 후 캐시 설정 (예: 5분 동안 동일 알림 재전송 방지)
        redisService.setValue(cacheKey, "sent", 300);
    }

    private String makeMessage(String targetToken, String title, String body) throws JsonParseException, JsonProcessingException {
        NotificationMessage notificationMessage = NotificationMessage.builder()
                .message(NotificationMessage.Message.builder()
                        .token(targetToken)
                        .notification(NotificationMessage.Notification.builder()
                                .title(title)
                                .body(body)
                                .image(null)
                                .build()
                        ).build()).validateOnly(false).build();

        return objectMapper.writeValueAsString(notificationMessage);
    }

    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "firebase_service_key.json";

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }

    @Scheduled(fixedRate = 60000)  // 예: 1분마다 실행
    public void sendPeriodicNotifications() {
        // 주기적으로 알림을 보내는 로직 (필요 시 캐싱 활용 가능)
    }

    public void sendLikeNotification(Long postId) {
        // 좋아요 알림 전송 시에도 캐싱 로직 적용 가능
    }

    public void sendTopPostNotification(Long postId) {
        // 인기 게시물 알림 전송 시에도 캐싱 로직 적용 가능
    }
}
