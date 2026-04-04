package org.goros.habit_tracker.service;


import org.goros.habit_tracker.model.entity.AppUser;

public interface RedisUserCacheService {
    void saveUser(String key, AppUser user, long minutes);
    AppUser getUser(String key);
    void deleteUser(String key);
}
