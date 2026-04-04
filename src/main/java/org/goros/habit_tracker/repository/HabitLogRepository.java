package org.goros.habit_tracker.repository;

import org.apache.ibatis.annotations.*;
import org.goros.habit_tracker.model.entity.HabitLog;

import java.time.LocalDate;
import java.util.UUID;

@Mapper
public interface HabitLogRepository {


    @Results(id = "habitLogWithHabitMapper", value = {
            @Result(property = "habitLogId", column = "habit_log_id", id = true),
            @Result(property = "logDate", column = "log_date"),
            @Result(property = "xpEarned", column = "xp_earned"),
            @Result(property = "habit", column = "{habitId=habit_id, currentAppUserId=app_user_id}",
                    one = @One(select = "org.goros.habit_tracker.repository.HabitRepository.getHabitById"))
    })
    @Select("""
        SELECT hl.*, h.app_user_id FROM habit_logs hl JOIN habits h ON hl.habit_id = h.habit_id WHERE hl.habit_id = #{habitId}
    """)
    HabitLog getHabitLogByHabitId(@Param("habitId") UUID habitId, @Param("userId") UUID appUserId);

    @Results(id = "habitLogMapper", value = {
            @Result(property = "habitLogId", column = "habit_log_id", id = true),
            @Result(property = "logDate", column = "log_date"),
            @Result(property = "xpEarned", column = "xp_earned"),
            @Result(property = "habit.habitId", column = "habit_id")
    })
    @Select("""
        INSERT INTO habit_logs(habit_log_id, log_date, status, xp_earned, habit_id) VALUES (#{habitLogId}, #{logDate}, #{status}, #{xpEarned}, #{habit.habitId}) RETURNING *
    """)
    HabitLog saveHabitLog(HabitLog habitLog);

    @Select("""
        SELECT habit_log_id, log_date, status, xp_earned, habit_id FROM habit_logs WHERE habit_id = #{habitId} AND log_date = #{logDate}
    """)
    @ResultMap("habitLogMapper")
    HabitLog findByHabitIdAndDate(@Param("habitId") UUID habitId, @Param("logDate") LocalDate logDate);

    @Update("""
        UPDATE habit_logs SET status = #{status}, xp_earned = #{xpEarned} WHERE habit_log_id = #{habitLogId}
    """)
    HabitLog updateHabitLog(HabitLog habitLog);
}