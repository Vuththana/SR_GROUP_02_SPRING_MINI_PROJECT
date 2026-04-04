package org.goros.habit_tracker.service.impl;

import org.goros.habit_tracker.model.entity.AppUser;
import org.goros.habit_tracker.service.RedisUserCacheService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisUserCacheServiceImpl implements RedisUserCacheService {
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisUserCacheServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    @Override
    public void saveUser(String key, AppUser user, long minutes) {
        redisTemplate.opsForValue().set(key, user, Duration.ofMinutes(minutes));
    }

    @Override
    public AppUser getUser(String key) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value instanceof AppUser) {
            return (AppUser) value;
        }
        return null;
    }
    @Override
    public void deleteUser(String key) {
        redisTemplate.delete(key);
    }
}
