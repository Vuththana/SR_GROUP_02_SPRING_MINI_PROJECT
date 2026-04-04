package org.goros.habit_tracker.repository;

import org.apache.ibatis.annotations.*;
import org.goros.habit_tracker.model.entity.Habit;
import org.goros.habit_tracker.model.request.HabitRequest;
import org.goros.habit_tracker.repository.typehandler.UUIDTypeHandler;

import java.util.List;
import java.util.UUID;

@Mapper
public interface HabitRepository {
    @Results(id = "habitMapper" , value = {
            @Result(property = "habitId" , column = "habit_id" , typeHandler = UUIDTypeHandler.class),
            @Result(property = "isActive" , column = "is_active"),
            @Result(property = "appUserResponse" , column = "app_user_id" , one=@One(select = "org.goros.habit_tracker.repository.AppUserRepository.getUserById")),
    })
    @Select("""
        SELECT * FROM habits WHERE app_user_id = #{currentAppUserId} LIMIT #{size} OFFSET #{offset} 
    """)
    List<Habit> getAllHabits(int offset, Integer size, UUID currentAppUserId);

    @ResultMap("habitMapper")
    @Select("""
    SELECT * FROM habits WHERE habit_id = #{habitId} AND app_user_id = #{currentAppUserId}
    """)
    Habit getHabitById(UUID habitId , UUID currentAppUserId);

    @Select("""
    INSERT INTO habits(title, description, frequency, app_user_id, is_active) VALUES (#{req.title}, #{req.description}, #{req.frequency}, #{req.appUserId},true) RETURNING *
    """)
    @ResultMap("habitMapper")
    Habit saveHabit(@Param("req")HabitRequest request);

    @Delete("""
    DELETE FROM habits WHERE habit_id = #{habitId} AND app_user_id = #{currentAppUserId}
    """)
    int deleteHabitById(UUID habitId , UUID currentAppUserId);

    @Select("""
    UPDATE habits 
    SET title = #{req.title}, 
        description = #{req.description}, 
        frequency = #{req.frequency}
    WHERE habit_id = #{id} AND app_user_id = #{currentAppUserId}
    RETURNING *
""")
    @ResultMap("habitMapper")
    Habit updateHabitById(@Param("id") UUID habitId, @Param("req") HabitRequest habitRequest , UUID currentAppUserId);
}
