package org.goros.habit_tracker.repository;

import org.apache.ibatis.annotations.*;
import org.goros.habit_tracker.model.entity.AppUser;
import org.goros.habit_tracker.model.request.AppUserRequest;
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

    // This is for testing (will be removed soon)
    @Select("""
                SELECT * FROM app_users
                WHERE app_user_id = #{appUserId}
            """)
    @ResultMap("appUserMapper")
    AppUser getUserById(UUID appUserId);

    @Select("""
    INSERT INTO app_users(username, email, password, profile_image, created_at) VALUES (#{req.username}, #{req.email}, #{req.password}, #{req.profileImageUrl}, NOW()) RETURNING *
    """)
    @ResultMap("appUserMapper")
    AppUser register(@Param("req")AppUserRequest request);


    @Update("""
    UPDATE app_users SET is_verified = true WHERE email = #{email}
    """)
    void updateUserVerification(String email);

    @Select("""
    SELECT EXISTS (SELECT 1 FROM app_users WHERE app_user_id = #{appUserId} AND is_verified = false)
    """)
    boolean isUserVerified(UUID appUserId);
}
