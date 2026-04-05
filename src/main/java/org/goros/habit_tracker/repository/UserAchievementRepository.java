package org.goros.habit_tracker.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.UUID;

@Mapper
public interface UserAchievementRepository {

    @Select("""
        SELECT achievement_id
        FROM app_user_achievements
        WHERE app_user_id = #{userId}
    """)
    List<UUID> findAchievementIdsByUserId(UUID userId);


    @Insert("""
        INSERT INTO app_user_achievements (achievement_id, app_user_id)
        VALUES (#{achievementId}, #{userId})
        ON CONFLICT (achievement_id, app_user_id) DO NOTHING
    """)
        void insert(@Param("userId") UUID userId, @Param("achievementId") UUID achievementId);
}
