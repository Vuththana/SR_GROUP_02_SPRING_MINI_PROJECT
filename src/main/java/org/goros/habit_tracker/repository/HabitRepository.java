package org.goros.habit_tracker.repository;

import org.apache.ibatis.annotations.*;
import org.goros.habit_tracker.model.entity.Habit;
import org.goros.habit_tracker.model.request.HabitRequest;
import org.goros.habit_tracker.repository.typehandler.UUIDTypeHandler;

import java.util.List;
import java.util.UUID;

@Mapper
public interface HabitRepository {
    @Results(id = "HabitMapper" , value = {
            @Result(property = "habitId" , column = "habit_id" , typeHandler = UUIDTypeHandler.class),
            @Result(property = "isActive" , column = "is_active"),
            @Result(property = "appUserResponse" , column = "app_user_id" , one=@One(select = "org.goros.habit_tracker.repository.AppUserRepository.getUserById"))
    })
    @Select("""
        SELECT * FROM habits LIMIT #{size} OFFSET #{offset}
    """)
    List<Habit> getAllHabits(int offset, Integer size);

    @ResultMap("HabitMapper")
    @Select("""
    SELECT * FROM habits WHERE habit_id = #{habitId}
    """)
    Habit getHabitById(UUID habitId);

    @Select("""
    INSERT INTO habits(title, description, frequency is_active) VALUES (#{title}, #{description}, #{frequency}, #{isActive}) RETURNING *
    """)
    @ResultMap("HabitMapper")
    Habit saveHabit(HabitRequest request);
}
