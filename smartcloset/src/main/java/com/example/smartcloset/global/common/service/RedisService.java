package com.example.smartcloset.global.common.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final StringRedisTemplate stringRedisTemplate;

    public void addReportCount(Long commentId) {
        stringRedisTemplate.opsForValue().increment("reportCount:" + commentId);
    }

    public Set<String> getAllKeys() {
        return stringRedisTemplate.keys("reportCount:*");
    }

    public Map<Long, Integer> getReportCount(List<Long> onlyKeys) {
        Map<Long, Integer> commentIdAndReportCount = new HashMap<>();
        for (Long key : onlyKeys) {
            int reportCount = Integer.parseInt(Objects.requireNonNull(
                    stringRedisTemplate.opsForValue().get("reportCount:" + key)));
            commentIdAndReportCount.put(key, reportCount);
        }
        return commentIdAndReportCount;
    }

    // 캐싱에 사용할 메서드들

    public void setValue(String key, String value, long seconds) {
        stringRedisTemplate.opsForValue().set(key, value, Duration.ofSeconds(seconds));
    }

    public String getValue(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public boolean exists(String key) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key));
    }
}
