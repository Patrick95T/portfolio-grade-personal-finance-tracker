CREATE TABLE debt_accounts (
    id UUID PRIMARY KEY,
    profile_id UUID NOT NULL,
    lender_name VARCHAR(120) NOT NULL,
    debt_type VARCHAR(30) NOT NULL,
    current_balance NUMERIC(19, 2) NOT NULL,
    minimum_monthly_payment NUMERIC(19, 2) NOT NULL,
    annual_interest_rate NUMERIC(8, 4) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_debt_accounts_profile
        FOREIGN KEY (profile_id)
        REFERENCES user_profiles (id)
        ON DELETE CASCADE
);

CREATE INDEX idx_debt_accounts_profile_id ON debt_accounts (profile_id);
