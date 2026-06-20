CREATE TABLE budget_plans (
    id UUID PRIMARY KEY,
    profile_id UUID NOT NULL,
    month_start DATE NOT NULL,
    currency VARCHAR(3) NOT NULL,
    housing_target NUMERIC(19, 2) NOT NULL,
    essentials_target NUMERIC(19, 2) NOT NULL,
    lifestyle_target NUMERIC(19, 2) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_budget_plans_profile
        FOREIGN KEY (profile_id)
        REFERENCES user_profiles (id)
        ON DELETE CASCADE
);

CREATE INDEX idx_budget_plans_profile_id ON budget_plans (profile_id);
