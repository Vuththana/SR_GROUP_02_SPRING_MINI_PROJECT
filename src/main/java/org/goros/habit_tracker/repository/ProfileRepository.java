package org.goros.habit_tracker.repository;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.goros.habit_tracker.model.request.ProfileUpdateRequest;
import org.goros.habit_tracker.model.response.ProfileResponse;

import java.util.UUID;

@Mapper
public interface ProfileRepository {

    @Select("""
            SELECT app_user_id, username, email, level, xp, profile_image, is_verified, created_at
            FROM app_users
            WHERE app_user_id = #{appUserId}
            """)
    @Results(id = "profileResponseMap", value = {
            @Result(property = "appUserId", column = "app_user_id"),
            @Result(property = "profileImageUrl", column = "profile_image"),
            @Result(property = "isVerified", column = "is_verified"),
            @Result(property = "createdAt", column = "created_at")
    })
    ProfileResponse findById(@Param("appUserId") UUID appUserId);

    @Update("""
            UPDATE app_users
            SET username = #{request.username},
                profile_image = #{request.profileImageUrl}
            WHERE app_user_id = #{appUserId}
            """)
    int updateById(@Param("appUserId") UUID appUserId, @Param("request") ProfileUpdateRequest request);

    @Delete("""
            DELETE FROM app_users
            WHERE app_user_id = #{appUserId}
            """)
    int deleteById(@Param("appUserId") UUID appUserId);
}