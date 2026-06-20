create table audit_events (
    id uuid primary key,
    profile_id uuid not null,
    event_type varchar(50) not null,
    entity_type varchar(80) not null,
    entity_id uuid not null,
    summary varchar(255) not null,
    occurred_at timestamp with time zone not null
);

create index idx_audit_events_profile_occurred_at
    on audit_events (profile_id, occurred_at desc);
