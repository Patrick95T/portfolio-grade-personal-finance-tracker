CREATE TABLE savings_goals (
    id UUID PRIMARY KEY,
    profile_id UUID NOT NULL,
    goal_name VARCHAR(120) NOT NULL,
    goal_type VARCHAR(30) NOT NULL,
    target_amount NUMERIC(19, 2) NOT NULL,
    current_saved NUMERIC(19, 2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    target_date DATE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_savings_goals_profile
        FOREIGN KEY (profile_id)
        REFERENCES user_profiles (id)
        ON DELETE CASCADE
);

CREATE INDEX idx_savings_goals_profile_id ON savings_goals (profile_id);
