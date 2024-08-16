package com.example.smartcloset.controller;

import com.example.smartcloset.dto.ChatGptRequest;
import com.example.smartcloset.dto.ChatGptResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/bot")
public class ChatGptController {

    @Value("${openai.model}")
    private String model;

    @Value(("${openai.api.url}"))
    private String apiURL;

    @Autowired
    private RestTemplate template;

    @GetMapping("/chat")
    public String chat(@RequestParam("prompt") String prompt) {
        try {
            ChatGptRequest request = new ChatGptRequest(model, prompt);
            ChatGptResponse chatGptResponse = template.postForObject(apiURL, request, ChatGptResponse.class);

            if (chatGptResponse == null || chatGptResponse.getChoices() == null || chatGptResponse.getChoices().isEmpty()) {
                return "에러: API로부터 응답이 없습니다.";
            }

            String gptResult = chatGptResponse.getChoices().get(0).getMessage().getContent();
            return gptResult;
        } catch (Exception e) {
            // 에러 메시지를 로그로 남기고 사용자에게 표시
            return "에러가 발생했습니다. " + e.getMessage();
        }
    }

}
