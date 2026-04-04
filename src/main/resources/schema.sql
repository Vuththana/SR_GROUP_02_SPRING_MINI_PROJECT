CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS achievements (
    achievement_id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    badge VARCHAR(255),
    xp_required INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS app_users (
    app_user_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    level INTEGER DEFAULT 1,
    xp INTEGER DEFAULT 0,
    profile_image VARCHAR(255),
    is_verified BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS app_user_achievements (
    app_user_achievement_id SERIAL PRIMARY KEY,
    app_user_id UUID NOT NULL,
    achievement_id INTEGER NOT NULL,
    FOREIGN KEY (app_user_id) REFERENCES app_users(app_user_id) ON DELETE CASCADE,
    FOREIGN KEY (achievement_id) REFERENCES achievements(achievement_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS habits (
    habit_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title VARCHAR(255) NOT NULL,
    description TEXT,
    frequency VARCHAR(50),
    is_active BOOLEAN DEFAULT TRUE,
    app_user_id UUID NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (app_user_id) REFERENCES app_users(app_user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS habit_logs (
    habit_log_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    log_date DATE NOT NULL,
    status VARCHAR(50) NOT NULL,
    xp_earned INTEGER DEFAULT 0,
    habit_id UUID NOT NULL,
    FOREIGN KEY (habit_id) REFERENCES habits(habit_id) ON DELETE CASCADE
);