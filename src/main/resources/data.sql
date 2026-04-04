INSERT INTO achievements (achievement_id, title, description, badge, xp_required)
VALUES
    (gen_random_uuid(), 'First Step', 'Log your first habit', 'starter_badge.png', 10),
    (gen_random_uuid(), 'Streak Master', 'Maintain a 7-day streak', 'fire_badge.png', 50),
    (gen_random_uuid(), 'Early Bird', 'Complete a habit before 8 AM', 'sun_badge.png', 20),
    (gen_random_uuid(), 'Level 10', 'Reach level 10', 'crown_badge.png', 500)
ON CONFLICT (title) DO NOTHING
RETURNING achievement_id;


WITH inserted_user AS (
    INSERT INTO app_users (app_user_id, username, email, password, level, xp, profile_image, is_verified)
        VALUES (gen_random_uuid(), 'user', 'user@gmail.com',
                '$2a$12$ObwFvSlM2ehfX5nNksSIFeA412OxMrimB6qA7Yd3oVKh0qNRm6cxy',
                3, 150, 'alice_avatar.jpg', TRUE)
        ON CONFLICT (username) DO NOTHING
        RETURNING app_user_id
),
     selected_achievements AS (
         SELECT achievement_id
         FROM achievements
         ORDER BY title
         LIMIT 3
     )

INSERT INTO app_user_achievements (app_user_id, achievement_id)
SELECT iu.app_user_id, sa.achievement_id
FROM inserted_user iu
         JOIN selected_achievements sa ON TRUE
ON CONFLICT (app_user_id, achievement_id) DO NOTHING
RETURNING app_user_achievement_id;


WITH inserted_user AS (
    SELECT app_user_id FROM app_users WHERE username = 'user'
),
     inserted_habits AS (
         INSERT INTO habits (habit_id, title, description, frequency, is_active, app_user_id)
             VALUES
                 (gen_random_uuid(), 'Morning Run', 'Run 3 miles', 'DAILY', TRUE, (SELECT app_user_id FROM inserted_user)),
                 (gen_random_uuid(), 'Read 20 Pages', 'Read non-fiction before bed', 'DAILY', TRUE, (SELECT app_user_id FROM inserted_user)),
                 (gen_random_uuid(), 'Drink Water', 'Drink 8 glasses of water', 'DAILY', TRUE, (SELECT app_user_id FROM inserted_user)),
                 (gen_random_uuid(), 'Meditate', '10 minutes of mindfulness', 'DAILY', FALSE, (SELECT app_user_id FROM inserted_user)),
                 (gen_random_uuid(), 'Gym Workout', 'Weight training', 'WEEKLY', TRUE, (SELECT app_user_id FROM inserted_user))
             RETURNING habit_id, title
     )

INSERT INTO habit_logs (log_date, status, xp_earned, habit_id)
SELECT CURRENT_DATE, 'Missed', 0, ih.habit_id
FROM inserted_habits ih WHERE ih.title = 'Morning Run'
UNION ALL
SELECT CURRENT_DATE - INTERVAL '1 day', 'Completed', 5, ih.habit_id
FROM inserted_habits ih WHERE ih.title = 'Read 20 Pages'
UNION ALL
SELECT CURRENT_DATE, 'Completed', 5, ih.habit_id
FROM inserted_habits ih WHERE ih.title = 'Drink Water';