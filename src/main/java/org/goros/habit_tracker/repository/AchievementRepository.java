package org.goros.habit_tracker.repository;

import org.apache.ibatis.annotations.*;
import org.goros.habit_tracker.model.entity.Achievement;

import java.util.List;
import java.util.UUID;

@Mapper
public interface AchievementRepository {

    @Results(id = "achievementMapper", value = {
            @Result(property = "achievementId", column = "achievement_id", id = true),
            @Result(property = "xpRequired", column = "xp_required")
    })
    @Select("""
        SELECT * FROM achievements OFFSET #{offset} LIMIT #{size};
    """)
    List<Achievement> getAllAchievement(@Param("offset") Long offset,@Param("size") Long size);

    @ResultMap("achievementMapper")
    @Select("""
         SELECT a.* FROM app_user_achievements aa INNER JOIN achievements a on aa.achievement_id = a.achievement_id WHERE aa.app_user_id = #{userId} OFFSET #{offset} LIMIT #{size};
     """)
    List<Achievement> getAllAchievementByAppUserId(@Param("offset") Long offset, @Param("size") Long size, @Param("userId") UUID userId);
}
