CREATE TABLE expense_entries (
    id UUID PRIMARY KEY,
    profile_id UUID NOT NULL,
    expense_name VARCHAR(120) NOT NULL,
    amount NUMERIC(19, 2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    category VARCHAR(20) NOT NULL,
    frequency VARCHAR(20) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    essential BOOLEAN NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_expense_entries_profile
        FOREIGN KEY (profile_id)
        REFERENCES user_profiles (id)
        ON DELETE CASCADE
);

CREATE INDEX idx_expense_entries_profile_id ON expense_entries (profile_id);
