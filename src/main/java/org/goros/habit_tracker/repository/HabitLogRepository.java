package org.goros.habit_tracker.repository;

import org.apache.ibatis.annotations.*;
import org.goros.habit_tracker.model.entity.HabitLog;
import org.goros.habit_tracker.mybatis.UUIDTypeHandler;

import java.time.LocalDate;
import java.util.UUID;

@Mapper
public interface HabitLogRepository {


    @Results(id = "habitLogMapper", value = {
            @Result(property = "habitLogId", column = "habit_log_id", typeHandler = UUIDTypeHandler.class),
            @Result(property = "logDate", column = "log_date"),
            @Result(property = "xpEarned", column = "xp_earned"),
            @Result(property = "habit", column = "{habitId=habit_id, currentAppUserId=app_user_id}",
                    one = @One(select = "org.goros.habit_tracker.repository.HabitRepository.getHabitById"))
    })
    @Select("""
        SELECT hl.*, h.app_user_id FROM habit_logs hl JOIN habits h ON hl.habit_id = h.habit_id WHERE hl.habit_id = #{habitId}
    """)
    HabitLog getHabitLogByHabitId(@Param("habitId") UUID habitId, @Param("userId") UUID appUserId);

    @ResultMap("habitLogMapper")
    @Select("""
    WITH inserted AS (
        INSERT INTO habit_logs(habit_log_id, log_date, status, xp_earned, habit_id)
        VALUES (#{habitLog.habitLogId}, #{habitLog.logDate}, #{habitLog.status}, #{habitLog.xpEarned}, #{habitLog.habit.habitId})
        RETURNING *
    )
    SELECT i.*, h.app_user_id
    FROM inserted i
    JOIN habits h ON i.habit_id = h.habit_id
    """)
    HabitLog saveHabitLog(@Param("habitLog") HabitLog habitLog, @Param("appUserId") UUID appUserId);

    @Select("""
    SELECT hl.habit_log_id, hl.log_date, hl.status, hl.xp_earned, hl.habit_id, h.app_user_id
    FROM habit_logs hl
    JOIN habits h ON hl.habit_id = h.habit_id
    WHERE hl.habit_id = #{habitId} AND hl.log_date = #{logDate}
    """)
    @ResultMap("habitLogMapper")
    HabitLog findByHabitIdAndDate(@Param("habitId") UUID habitId, @Param("logDate") LocalDate logDate, @Param("appUserId") UUID appUserId);

    @Update("""
    UPDATE habit_logs 
    SET status = #{habitLog.status}, xp_earned = #{habitLog.xpEarned} 
    WHERE habit_log_id = #{habitLog.habitLogId}
    """)
    @ResultMap("habitLogMapper")
    HabitLog updateHabitLog(@Param("habitLog") HabitLog habitLog);
}