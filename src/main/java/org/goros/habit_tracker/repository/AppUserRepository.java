package org.goros.habit_tracker.repository;

import org.apache.ibatis.annotations.*;
import org.goros.habit_tracker.model.entity.AppUser;
import org.goros.habit_tracker.model.request.AppUserRequest;
import org.goros.habit_tracker.model.response.AppUserResponse;
import org.goros.habit_tracker.repository.typehandler.UUIDTypeHandler;

import java.util.UUID;

@Mapper
public interface AppUserRepository {

    @Results(id = "appUserMapper", value = {
            @Result(property = "appUserId", column = "app_user_id", typeHandler = UUIDTypeHandler.class),
            @Result(property = "profileImageUrl", column = "profile_image"),
            @Result(property = "isVerified", column = "is_verified"),
            @Result(property = "createdAt", column = "created_at")
    })
    @Select("""
    SELECT * FROM app_users WHERE LOWER(username) = LOWER(#{identifier}) OR LOWER(email) = LOWER(#{identifier})
    """)
    AppUser getUserByEmailOrUsername(String identifier);

    @Select("""
    SELECT 
        app_user_id AS appUserId,
        username,
        email,
        level,
        xp,
        profile_image AS profileImageUrl,
        is_verified AS isVerified,
        created_at AS createdAt
    FROM app_users
    WHERE app_user_id = #{appUserId}
    """)
    AppUserResponse getUserById(UUID appUserId);

    @Select("""
    INSERT INTO app_users(app_user_id, username, email, password, profile_image, created_at, is_verified) VALUES (#{appUserId}, #{req.username}, #{req.email}, #{req.password}, #{req.profileImageUrl}, NOW(), true) RETURNING *
    """)
    @ResultMap("appUserMapper")
    AppUser registerWithUuid(@Param("appUserId") UUID appUserId, @Param("req")AppUserRequest request);

    @Select("""
    SELECT EXISTS (SELECT 1 FROM app_users WHERE app_user_id = #{appUserId} AND is_verified = false)
    """)
    boolean isUserVerified(UUID appUserId);

    @Update("""
    UPDATE app_users SET xp = #{xp}, level = #{level} WHERE app_user_id = #{appUserId}
    """)
    void updateXpAndLevel(UUID appUserId, int xp, int level);
}
