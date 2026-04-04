INSERT INTO achievements (title, description, badge, xp_required)
VALUES
      ('First Step', 'Log your first habit', 'starter_badge.png', 10),
      ('Streak Master', 'Maintain a 7-day streak', 'fire_badge.png', 50),
      ('Early Bird', 'Complete a habit before 8 AM', 'sun_badge.png', 20),
      ('Level 10', 'Reach level 10', 'crown_badge.png', 500);


WITH inserted_users AS (
INSERT INTO app_users (app_user_id, username, email, password, level, xp, profile_image, is_verified) VALUES
    (gen_random_uuid(), 'alice_jones', 'alice@example.com', 'hashed_pwd_abc123', 3, 150, 'alice_avatar.jpg', TRUE),
    (gen_random_uuid(), 'bob_smith', 'bob@example.com', 'hashed_pwd_def456', 1, 40, NULL, FALSE),
    (gen_random_uuid(), 'charlie_davis', 'charlie@example.com', 'hashed_pwd_ghi789', 12, 620, 'charlie_avatar.jpg', TRUE)
    RETURNING app_user_id, username
    )
SELECT * FROM inserted_users;


WITH users AS (
    SELECT * FROM app_users
)
INSERT INTO app_user_achievements (app_user_id, achievement_id) VALUES
    ((SELECT app_user_id FROM users WHERE username='alice_jones'), 1),
    ((SELECT app_user_id FROM users WHERE username='alice_jones'), 3),
    ((SELECT app_user_id FROM users WHERE username='bob_smith'), 1),
    ((SELECT app_user_id FROM users WHERE username='charlie_davis'), 1),
    ((SELECT app_user_id FROM users WHERE username='charlie_davis'), 2),
    ((SELECT app_user_id FROM users WHERE username='charlie_davis'), 3),
    ((SELECT app_user_id FROM users WHERE username='charlie_davis'), 4);


WITH users AS (
    SELECT * FROM app_users
)
INSERT INTO habits (title, description, frequency, is_active, app_user_id) VALUES
    ('Morning Run', 'Run 3 miles', 'Daily', TRUE, (SELECT app_user_id FROM users WHERE username='alice_jones')),
    ('Read 20 Pages', 'Read non-fiction before bed', 'Daily', TRUE, (SELECT app_user_id FROM users WHERE username='alice_jones')),
    ('Drink Water', 'Drink 8 glasses of water', 'Daily', TRUE, (SELECT app_user_id FROM users WHERE username='bob_smith')),
    ('Meditate', '10 minutes of mindfulness', 'Daily', FALSE, (SELECT app_user_id FROM users WHERE username='bob_smith')),
    ('Gym Workout', 'Weight training', 'Weekly', TRUE, (SELECT app_user_id FROM users WHERE username='charlie_davis'))
RETURNING habit_id, title;


INSERT INTO habit_logs (log_date, status, xp_earned, habit_id)
VALUES (CURRENT_DATE, 'Missed', 0, 1),
       (CURRENT_DATE - INTERVAL '1 day', 'Completed', 5, 2),
       (CURRENT_DATE, 'Completed', 5, 2);