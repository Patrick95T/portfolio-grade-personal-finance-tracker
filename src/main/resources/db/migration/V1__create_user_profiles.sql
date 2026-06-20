CREATE TABLE user_profiles (
    id UUID PRIMARY KEY,
    full_name VARCHAR(150) NOT NULL,
    email_address VARCHAR(150) NOT NULL UNIQUE,
    employment_type VARCHAR(40) NOT NULL,
    preferred_currency VARCHAR(3) NOT NULL,
    country_code VARCHAR(2) NOT NULL,
    monthly_net_income NUMERIC(19, 2) NOT NULL,
    monthly_housing_cost NUMERIC(19, 2) NOT NULL,
    monthly_essential_costs NUMERIC(19, 2) NOT NULL,
    onboarding_date DATE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_user_profiles_email_address ON user_profiles (email_address);
