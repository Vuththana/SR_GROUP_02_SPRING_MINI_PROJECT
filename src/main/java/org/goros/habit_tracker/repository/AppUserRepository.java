package org.goros.habit_tracker.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.goros.habit_tracker.model.entity.AppUser;

@Mapper
public interface AppUserRepository {

    @Results(id = "appUserMapper", value = {
            @Result(property = "userId", column = "app_user_id"),
    })
    @Select("""
    SELECT * FROM app_users WHERE email = #{identifier} OR username = #{identifier}
    """)
    AppUser getUserByEmailOrUsername(String identifier);
}
