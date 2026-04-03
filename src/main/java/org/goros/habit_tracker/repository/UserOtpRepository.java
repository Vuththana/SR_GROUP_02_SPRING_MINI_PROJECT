package org.goros.habit_tracker.repository;

import org.apache.ibatis.annotations.*;
import org.goros.habit_tracker.model.entity.UserOtp;
import org.goros.habit_tracker.mybatis.UUIDTypeHandler;

import java.util.UUID;

@Mapper
public interface UserOtpRepository {
    @Results(id = "userOtpMapper", value = {
            @Result(property = "otpId", column = "otp_id"),
            @Result(property = "appUserId", column = "app_user_id", typeHandler = UUIDTypeHandler.class),
            @Result(property = "otpCode", column = "otp_code"),
            @Result(property = "expiresAt", column = "expires_at")
    })
    @Select("""
    SELECT uo.* FROM user_otps uo JOIN app_users au ON au.email = #{email} AND uo.otp_code = #{otpCode} AND uo.used = false AND uo.expires_at > now()
    """)
    UserOtp validateOtp(String email, String otpCode);

    @Insert("""
    INSERT INTO user_otps (app_user_id, otp_code, expires_at) VALUES (#{appUserId}, #{otpCode}, #{expiresAt})
    """)
    void saveOtp(UserOtp otp);

    @Update("""
    UPDATE user_otps SET used = true WHERE otp_id = #{otpId}
    """)
    void markOtpUsed(Integer otpId);
}
