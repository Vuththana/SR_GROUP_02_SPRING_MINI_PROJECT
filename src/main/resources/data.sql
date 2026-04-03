INSERT INTO achievements (title, description, badge, xp_required) VALUES
    ('First Step', 'Log your first habit', 'starter_badge.png', 10),
    ('Streak Master', 'Maintain a 7-day streak', 'fire_badge.png', 50),
    ('Early Bird', 'Complete a habit before 8 AM', 'sun_badge.png', 20),
    ('Level 10', 'Reach level 10', 'crown_badge.png', 500);

INSERT INTO app_users (username, email, password, level, xp, profile_image, is_verified) VALUES
    ('alice_jones', 'alice@example.com', 'hashed_pwd_abc123', 3, 150, 'alice_avatar.jpg', TRUE),
    ('bob_smith', 'bob@example.com', 'hashed_pwd_def456', 1, 40, NULL, FALSE),
    ('charlie_davis', 'charlie@example.com', 'hashed_pwd_ghi789', 12, 620, 'charlie_avatar.jpg', TRUE);

INSERT INTO app_user_achievements (app_user_id, achievement_id) VALUES
    (1, 1),
    (1, 3),
    (2, 1),
    (3, 1),
    (3, 2),
    (3, 3),
    (3, 4);

INSERT INTO habits (title, description, frequency, is_active, app_user_id) VALUES
    ('Morning Run', 'Run 3 miles', 'Daily', TRUE, 1),
    ('Read 20 Pages', 'Read non-fiction before bed', 'Daily', TRUE, 1),
    ('Drink Water', 'Drink 8 glasses of water', 'Daily', TRUE, 2),
    ('Meditate', '10 minutes of mindfulness', 'Daily', FALSE, 2),
    ('Gym Workout', 'Weight training', 'Weekly', TRUE, 3);

INSERT INTO habit_logs (log_date, status, xp_earned, habit_id) VALUES
    (CURRENT_DATE - INTERVAL '2 days', 'Completed', 10, 1),
    (CURRENT_DATE - INTERVAL '1 day', 'Completed', 10, 1),
    (CURRENT_DATE, 'Missed', 0, 1),
    (CURRENT_DATE - INTERVAL '1 day', 'Completed', 5, 2),
    (CURRENT_DATE, 'Completed', 5, 2),
    (CURRENT_DATE, 'Completed', 10, 3),
    (CURRENT_DATE - INTERVAL '5 days', 'Completed', 20, 5);