CREATE TABLE asset_records (
    id UUID PRIMARY KEY,
    profile_id UUID NOT NULL,
    asset_name VARCHAR(120) NOT NULL,
    asset_type VARCHAR(30) NOT NULL,
    current_value NUMERIC(19, 2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_asset_records_profile
        FOREIGN KEY (profile_id)
        REFERENCES user_profiles (id)
        ON DELETE CASCADE
);

CREATE INDEX idx_asset_records_profile_id ON asset_records (profile_id);
